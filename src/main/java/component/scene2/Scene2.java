package component.scene2;

import application.SceneHandler;
import component.scene1.CharacterSelectScene;
import javafx.animation.*;
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
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import logic.entity.BaseProjectileAttack;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerLogic;
import logic.gameLogic.PlayerState;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static logic.gameLogic.Selection.getPlayer_1_Character;
import static logic.gameLogic.Selection.getPlayer_2_Character;

public class Scene2 extends Pane {
    private static final double HEALTH_BAR_TOP_MARGIN = 20;
    private static final double HEALTH_BAR_SIDE_MARGIN = 20;
    private static final double SKILL_BAR_VERTICAL_GAP = 10;
    private static final long MATCH_DURATION_SECONDS = 180;
    private static final long MATCH_DURATION_NANOS = MATCH_DURATION_SECONDS * 1_000_000_000L;

    // Player models for both sides.
    public Player player1;
    public Player player2;
    private PlayerLogic playerLogic1;
    private PlayerLogic playerLogic2;
    private HealthBar player1HealthBar;
    private HealthBar player2HealthBar;
    private SkillBar player1SkillBar;
    private SkillBar player2SkillBar;
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

        getChildren().addAll(
                playerLogic1.getAmmoText(),
                playerLogic2.getAmmoText()
        );
        
        // Initialize health bars with each character's starting HP.
        // ใช้ HealthBar ใหม่พร้อมชื่อตัวละคร
        player1HealthBar = new HealthBar(player1.getCharacter().getHp(), player1.getCharacter().getName(), true);
        player2HealthBar = new HealthBar(player2.getCharacter().getHp(), player2.getCharacter().getName(), false);
        player1SkillBar = new SkillBar();
        player2SkillBar = new SkillBar();
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

        // Place player skill bars directly below each health bar.
        player1SkillBar.setLayoutX(HEALTH_BAR_SIDE_MARGIN);
        player1SkillBar.setLayoutY(
                HEALTH_BAR_TOP_MARGIN + player1HealthBar.prefHeight(-1) + SKILL_BAR_VERTICAL_GAP
        );
        player2SkillBar.layoutXProperty().bind(
                widthProperty().subtract(
                        player2SkillBar.prefWidth(-1) + HEALTH_BAR_SIDE_MARGIN
                )
        );
        player2SkillBar.setLayoutY(
                HEALTH_BAR_TOP_MARGIN + player2HealthBar.prefHeight(-1) + SKILL_BAR_VERTICAL_GAP
        );

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
                player1SkillBar,
                player2SkillBar,
                countdownLabel,
                player1.getPlayerRoot(),
                player2.getPlayerRoot(),
                playerLogic1.getAttackHitbox(),
                playerLogic1.getBuffText(),
                player1.getWeaponSprite(),
                player2.getWeaponSprite(),
                playerLogic2.getAttackHitbox(),
                playerLogic2.getBuffText()
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
                player1SkillBar.setCooldownProgress(playerLogic1.getSkillCooldownProgress());
                player2SkillBar.setCooldownProgress(playerLogic2.getSkillCooldownProgress());

                // Compute remaining time from elapsed nanoseconds.
                long elapsedNanos = now - matchStartNanos;
                long remainingNanos = Math.max(0L, MATCH_DURATION_NANOS - elapsedNanos);
                long remainingSeconds = remainingNanos / 1_000_000_000L;
                // Update timer text in MM:SS format.
                countdownLabel.setText(formatTime(remainingSeconds));

                // End match immediately when timer reaches zero.
                if (remainingSeconds <= 0) {
                    handleGameOver(null, null); // Draw or Time Out logic
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
                            .intersects(target.getHitbox()
                                    .localToScene(target.getHitbox().getBoundsInLocal())
                            )) {

                        if (p.hasKnockback()) {

                            double randomX = 67 + Math.random() * 8;
                            double randomY = 67 + Math.random() * 30;

                            target.translate(p.getDirectionX() * randomX, -randomY);
                        }
                        int damage = p.getDamage();
                        target.getCharacter().takeDamage(damage);

                        int finalDamage = damage - target.getCharacter().getDef();
                        if (finalDamage > 0) {
                            showFloatingText(target, finalDamage, Color.DARKRED, "-");
                        }
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
        if (gameOver) return;
        if (player1.getCharacter().getHp() <= 0) {
            handleGameOver(player2, player1);
        } else if (player2.getCharacter().getHp() <= 0) {
            handleGameOver(player1, player2);
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
        System.out.println(p.getDamage());
    }

    // Removes projectile from scene and simulation list.
    public void removeProjectile(BaseProjectileAttack p) {
        getChildren().remove(p.getSprite());
        projectileList.remove(p);
    }

    public void showFloatingText(Player target, int amount, Color color, String prefix) {

        Label label = new Label(prefix + amount);
        label.setTextFill(color);
        label.setFont(Font.font("Monospaced", 20));
        label.setStyle("-fx-font-weight: bold;");

        double x = target.getHitbox().localToScene(
                target.getHitbox().getBoundsInLocal()
        ).getMinX();

        double y = target.getHitbox().localToScene(
                target.getHitbox().getBoundsInLocal()
        ).getMinY();

        label.setLayoutX(x + 20);
        label.setLayoutY(y - 10);

        getChildren().add(label);
        label.toFront();

        javafx.animation.FadeTransition fade =
                new javafx.animation.FadeTransition(
                        javafx.util.Duration.seconds(0.8),
                        label
                );
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        javafx.animation.TranslateTransition move =
                new javafx.animation.TranslateTransition(
                        javafx.util.Duration.seconds(0.8),
                        label
                );
        move.setByY(-30);

        javafx.animation.ParallelTransition animation =
                new javafx.animation.ParallelTransition(fade, move);

        animation.setOnFinished(e -> getChildren().remove(label));
        animation.play();
    }

    private void handleGameOver(Player winner, Player loser) {
        gameOver = true;
        gameLoop.stop();

        if (loser != null && winner != null) {
            loser.setState(PlayerState.DEAD);

            // 1. Death Animation: ล้มลง 90 องศา
            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), loser.getPlayerRoot());
            rotate.setToAngle(loser.isFacingRight() ? 90 : -90);

            // 2. Fade Out: จางหายไป
            FadeTransition fade = new FadeTransition(Duration.seconds(1), loser.getPlayerRoot());
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setDelay(Duration.seconds(0.5));

            SequentialTransition deathAnim = new SequentialTransition(rotate, fade);
            deathAnim.setOnFinished(e -> showVictoryOverlay(winner));
            deathAnim.play();
        } else {
            showVictoryOverlay(null);
        }
    }

    private void showVictoryOverlay(Player winner) {

        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER);
        overlay.setBackground(new Background(
                new BackgroundFill(Color.rgb(0, 0, 0, 0.7), null, null)
        ));
        overlay.setPrefSize(getWidth(), getHeight());

        Label titleLabel;
        Label subtitleLabel = new Label();

        if (winner == null) {
            titleLabel = new Label("TIME UP!");
            subtitleLabel.setText("DRAW");
        } else {
            titleLabel = new Label("VICTORY");

            int playerNumber = (winner == player1) ? 1 : 2;
            String characterName = winner.getCharacter().getName();

            subtitleLabel.setText(
                    "PLAYER " + playerNumber + " (" + characterName + ") WINS!"
            );
        }

        titleLabel.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 80));
        titleLabel.setTextFill(Color.GOLD);

        subtitleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 36));
        subtitleLabel.setTextFill(Color.WHITE);

        Button restartBtn = new Button("REMATCH");
        restartBtn.setStyle(
                "-fx-background-color: #ffcc00; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 24px;"
        );
        restartBtn.setOnAction(e ->
                SceneHandler.switchRoot(new CharacterSelectScene())
        );

        overlay.getChildren().addAll(titleLabel, subtitleLabel, restartBtn);
        overlay.setOpacity(0);
        getChildren().add(overlay);

        FadeTransition fade = new FadeTransition(Duration.seconds(1), overlay);
        fade.setToValue(1.0);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), titleLabel);
        scale.setFromX(0);
        scale.setFromY(0);
        scale.setToX(1);
        scale.setToY(1);

        new ParallelTransition(fade, scale).play();
    }

}
