package component.scene2;

import application.SceneHandler;
import component.CharacterSelectScene;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.entity.BaseProjectileAttack;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerLogic;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static logic.gameLogic.Selection.getPlayer_1_Character;
import static logic.gameLogic.Selection.getPlayer_2_Character;

public class Scene2 extends Pane {
    private static final double HEALTH_BAR_TOP_MARGIN = 20;
    private static final double HEALTH_BAR_SIDE_MARGIN = 20;
    private static final long MATCH_DURATION_SECONDS = 180;
    private static final long MATCH_DURATION_NANOS = MATCH_DURATION_SECONDS * 1_000_000_000L;

    // Player models for both sides.
    public Player player1;
    public Player player2;
    private PlayerLogic playerLogic1;
    private PlayerLogic playerLogic2;
    private HealthBar player1HealthBar;
    private HealthBar player2HealthBar;
    // Center timer label (MM:SS).
    private Label countdownLabel;
    // Frame loop that drives all runtime updates.
    private AnimationTimer gameLoop;
    // Stops gameplay input/update once someone wins.
    private boolean gameOver;
    // First frame timestamp; used to compute remaining match time.
    private long matchStartNanos = -1L;
    // Active projectiles currently in the arena.
    private List<BaseProjectileAttack> projectileList = new ArrayList<>();

    // Singleton-like access for systems that need current combat scene instance.
    private static Scene2 instance;

    // Returns the current active scene2 instance.
    public static Scene2 getInstance() {
        return instance;
    }

    public Scene2() {
        // Build players from selections made in character select scene.
        player1 = new Player(getPlayer_1_Character(), 1);
        player2 = new Player(getPlayer_2_Character(), 2);

        // Create gameplay logic controllers (each knows self + enemy).
        playerLogic1 = new PlayerLogic(player1, player2, 1);
        playerLogic2 = new PlayerLogic(player2, player1, 2);
        
        // Initialize health bars with each character's starting HP.
        player1HealthBar = new HealthBar(player1.getCharacter().getHp());
        player2HealthBar = new HealthBar(player2.getCharacter().getHp());
        // Initialize timer label with full match duration.
        countdownLabel = new Label(formatTime(MATCH_DURATION_SECONDS));
        countdownLabel.setFont(Font.font("Monospaced", 28));
        countdownLabel.setTextFill(Color.WHITE);
        countdownLabel.setStyle("-fx-font-weight: bold;");

        // Pin player 1 health bar to top-left.
        player1HealthBar.setLayoutX(HEALTH_BAR_SIDE_MARGIN);
        player1HealthBar.setLayoutY(HEALTH_BAR_TOP_MARGIN);

        // Bind player 2 health bar to top-right even when scene width changes.
        player2HealthBar.layoutXProperty().bind(
                widthProperty().subtract(
                        player2HealthBar.prefWidth(-1) + HEALTH_BAR_SIDE_MARGIN
                )
        );
        player2HealthBar.setLayoutY(HEALTH_BAR_TOP_MARGIN);

        // Keep countdown centered horizontally based on scene width and label width.
        countdownLabel.layoutXProperty().bind(
                Bindings.createDoubleBinding(
                        () -> (getWidth() - countdownLabel.getLayoutBounds().getWidth()) / 2.0,
                        widthProperty(),
                        countdownLabel.layoutBoundsProperty()
                )
        );
        countdownLabel.setLayoutY(HEALTH_BAR_TOP_MARGIN + 6);

        // Add all UI and game nodes to scene graph.
        // Order matters for z-layering (later nodes appear on top of earlier ones).
        getChildren().addAll(
                player1HealthBar,
                player2HealthBar,
                countdownLabel,
                player1.getSprite(),
                player2.getSprite(),
                playerLogic1.getAttackHitbox(),
                player1.getWeaponSprite(),
                player2.getWeaponSprite(),
                playerLogic2.getAttackHitbox()
        );

        // Apply arena background image.
        setImageBackground();

        // Wait until this root is attached to an actual Scene before wiring key handlers.
        // This avoids null scene issues during constructor execution.
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setupControls(newScene);
                startGameLoop();
            }
        });

        // Register current instance for static access (projectile spawning path).
        instance = this;

    }

    // Applies scene background image (background2.png) with cover behavior.
    private void setImageBackground() {
        URL imageUrl = getClass().getResource("/background2.png");
        if (imageUrl == null) {
            // Fallback to existing background if background2.png is not present.
            imageUrl = getClass().getResource("/background.png");
        }
        if (imageUrl == null) {
            // Last fallback: keep a plain color background instead of crashing.
            setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            return;
        }

        Image image = new Image(imageUrl.toExternalForm());
        BackgroundSize size = new BackgroundSize(
                100, 100,
                true, true,
                false, true
        );
        BackgroundImage bg = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                size
        );
        setBackground(new Background(bg));
    }


    // Register key press/release callbacks and forward inputs to both player controllers.
    private void setupControls(Scene scene) {

        // Key pressed event: start movement/attacks.
        scene.setOnKeyPressed(e -> {
            // Ignore all input once the match has ended.
            if (gameOver) return;
            playerLogic1.handleKeyPressed(e);
            playerLogic2.handleKeyPressed(e);
        });

        // Key released event: stop movement state as needed.
        scene.setOnKeyReleased(e -> {
            // Ignore all input once the match has ended.
            if (gameOver) return;
            playerLogic1.handleKeyReleased(e);
            playerLogic2.handleKeyReleased(e);
        });
    }

    // Starts the per-frame update loop for gameplay simulation and UI refresh.
    private void startGameLoop() {

        // AnimationTimer gives a monotonic nanosecond timestamp each frame.
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Initialize match start time on first frame only.
                if (matchStartNanos < 0) {
                    matchStartNanos = now;
                }

                // Update players every frame (movement, attack state, hitboxes).
                playerLogic1.update(); // runs every frame
                playerLogic2.update(); // runs every frame

                // Sync health bar visuals with current HP values.
                player1HealthBar.setCurrentHp(player1.getCharacter().getHp());
                player2HealthBar.setCurrentHp(player2.getCharacter().getHp());

                // Compute remaining time from elapsed nanoseconds.
                long elapsedNanos = now - matchStartNanos;
                long remainingNanos = Math.max(0L, MATCH_DURATION_NANOS - elapsedNanos);
                long remainingSeconds = remainingNanos / 1_000_000_000L;
                // Update timer text in MM:SS format.
                countdownLabel.setText(formatTime(remainingSeconds));

                // End match immediately when timer reaches zero.
                if (remainingSeconds <= 0) {
                    endGameAndShowPopup("Time's up, you too slow");
                    return;
                }

                // Check if someone has reached 0 HP.
                checkGameOver();

                // Use iterator so we can safely remove projectiles during traversal.
                Iterator<BaseProjectileAttack> iterator = projectileList.iterator();

                while (iterator.hasNext()) {
                    BaseProjectileAttack p = iterator.next();
                    // Advance projectile position/state.
                    p.update();

                    // Target is the opposite player from projectile owner.
                    Player target = (p.getOwner() == player1) ? player2 : player1;

                    // If projectile collides with target sprite, apply damage and remove projectile.
                    if (p.getSprite().getBoundsInParent()
                            .intersects(target.getSprite().getBoundsInParent())) {

                        target.getCharacter().takeDamage(p.getOwner().getCharacter().getAtk());

                        // Remove projectile visual from scene and list from simulation.
                        getChildren().remove(p.getSprite());
                        iterator.remove();
                        continue;
                    }

                    // Remove projectile after it has traveled beyond its allowed range.
                    if (p.isOutOfRange()) {
                        getChildren().remove(p.getSprite());
                        iterator.remove();
                    }
                }
            }
        };

        // Begin frame callbacks.
        gameLoop.start();
    }

    // Formats seconds to MM:SS for display.
    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Ends match when one player's HP is zero.
    private void checkGameOver() {
        // Avoid repeated end-game handling.
        if (gameOver) return;

        // Read current HP values.
        int player1Hp = player1.getCharacter().getHp();
        int player2Hp = player2.getCharacter().getHp();

        // Declare the opponent as winner when a player's HP reaches zero.
        if (player1Hp <= 0) {
            endGameAndShowPopup(2);
        } else if (player2Hp <= 0) {
            endGameAndShowPopup(1);
        }
    }

    // Stops gameplay and shows a centered winner popup with restart action.
    private void endGameAndShowPopup(int winnerPlayerNumber) {
        endGameAndShowPopup("Player " + winnerPlayerNumber + " win!");
    }

    // Stops gameplay and shows a centered popup with custom message.
    private void endGameAndShowPopup(String message) {
        // Flip game state and stop update loop.
        gameOver = true;
        if (gameLoop != null) {
            gameLoop.stop();
        }

        // Popup message text.
        Label winnerLabel = new Label(message);
        winnerLabel.setTextFill(Color.BLACK);
        winnerLabel.setFont(Font.font("Monospaced", 26));

        // Restart button returns to character selection scene.
        Button restartButton = new Button("(Restart)");
        restartButton.setOnAction(e -> SceneHandler.switchRoot(new CharacterSelectScene()));

        // Popup container styling and layout.
        VBox popup = new VBox(20, winnerLabel, restartButton);
        popup.setAlignment(Pos.CENTER);
        popup.setPrefWidth(420);
        popup.setPrefHeight(220);
        popup.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");

        // Keep popup centered horizontally/vertically while window resizes.
        popup.layoutXProperty().bind(
                Bindings.createDoubleBinding(
                        () -> (getWidth() - popup.getPrefWidth()) / 2.0,
                        widthProperty()
                )
        );
        popup.layoutYProperty().bind(
                Bindings.createDoubleBinding(
                        () -> (getHeight() - popup.getPrefHeight()) / 2.0,
                        heightProperty()
                )
        );

        // Attach popup above gameplay content.
        getChildren().add(popup);
        popup.toFront();
    }

    // Registers a new projectile to both simulation list and scene graph.
    public void addProjectile(BaseProjectileAttack p) {
        projectileList.add(p);
        getChildren().add(p.getSprite());
        // Ensure projectile appears above most scene nodes.
        p.getSprite().toFront();
        System.out.println("Projectile added");
    }

    // Removes projectile from scene and simulation list.
    public void removeProjectile(BaseProjectileAttack p) {
        getChildren().remove(p.getSprite());
        projectileList.remove(p);
    }
}
