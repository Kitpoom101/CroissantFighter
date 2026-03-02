package component.scene2;

import application.SceneHandler;
import component.CharacterSelectScene;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerLogic;

import static logic.gameLogic.Selection.getPlayer_1_Character;
import static logic.gameLogic.Selection.getPlayer_2_Character;

public class Scene2 extends Pane {
    
    private static final double HEALTH_BAR_TOP_MARGIN = 20;
    private static final double HEALTH_BAR_SIDE_MARGIN = 20;
    private static final long MATCH_DURATION_SECONDS = 180;
    private static final long MATCH_DURATION_NANOS = MATCH_DURATION_SECONDS * 1_000_000_000L;

    private Player player1;
    private Player player2;
    private PlayerLogic playerLogic1;
    private PlayerLogic playerLogic2;
    private HealthBar player1HealthBar;
    private HealthBar player2HealthBar;
    private Label countdownLabel;
    private AnimationTimer gameLoop;
    private boolean gameOver;
    private long matchStartNanos = -1L;


    public Scene2() {

        player1 = new Player(getPlayer_1_Character(), 1);
        player2 = new Player(getPlayer_2_Character(), 2);

        playerLogic1 = new PlayerLogic(player1, player2, 1);
        playerLogic2 = new PlayerLogic(player2, player1, 2);
        
        player1HealthBar = new HealthBar(player1.getCharacter().getHp());
        player2HealthBar = new HealthBar(player2.getCharacter().getHp());
        countdownLabel = new Label(formatTime(MATCH_DURATION_SECONDS));
        countdownLabel.setFont(Font.font("Monospaced", 28));
        countdownLabel.setTextFill(Color.WHITE);
        countdownLabel.setStyle("-fx-font-weight: bold;");

        player1HealthBar.setLayoutX(HEALTH_BAR_SIDE_MARGIN);
        player1HealthBar.setLayoutY(HEALTH_BAR_TOP_MARGIN);

        player2HealthBar.layoutXProperty().bind(
                widthProperty().subtract(
                        player2HealthBar.prefWidth(-1) + HEALTH_BAR_SIDE_MARGIN
                )
        );
        player2HealthBar.setLayoutY(HEALTH_BAR_TOP_MARGIN);

        countdownLabel.layoutXProperty().bind(
                Bindings.createDoubleBinding(
                        () -> (getWidth() - countdownLabel.getLayoutBounds().getWidth()) / 2.0,
                        widthProperty(),
                        countdownLabel.layoutBoundsProperty()
                )
        );
        countdownLabel.setLayoutY(HEALTH_BAR_TOP_MARGIN + 6);

        getChildren().addAll(
                player1HealthBar,
                player2HealthBar,
                countdownLabel,
                player1.getSprite(),
                player2.getSprite(),
                playerLogic1.getAttackHitbox(),
                playerLogic2.getAttackHitbox()
        );

        draw(Color.GREEN);

        /* ✅ WAIT UNTIL SCENE EXISTS */
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setupControls(newScene);
                startGameLoop();
            }
        });

    }

    private void draw(Color backgroundColor) {
        BackgroundFill bgFill = new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};

        this.setBackground(new Background(bgFillA));
    }


    private void setupControls(Scene scene) {

        scene.setOnKeyPressed(e -> {
            if (gameOver) return;
            playerLogic1.handleKeyPressed(e);
            playerLogic2.handleKeyPressed(e);
        });

        scene.setOnKeyReleased(e -> {
            if (gameOver) return;
            playerLogic1.handleKeyReleased(e);
            playerLogic2.handleKeyReleased(e);
        });
    }

    private void startGameLoop() {

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (matchStartNanos < 0) {
                    matchStartNanos = now;
                }

                playerLogic1.update(); // runs every frame
                playerLogic2.update(); // runs every frame

                player1HealthBar.setCurrentHp(player1.getCharacter().getHp());
                player2HealthBar.setCurrentHp(player2.getCharacter().getHp());

                long elapsedNanos = now - matchStartNanos;
                long remainingNanos = Math.max(0L, MATCH_DURATION_NANOS - elapsedNanos);
                long remainingSeconds = remainingNanos / 1_000_000_000L;
                countdownLabel.setText(formatTime(remainingSeconds));

                checkGameOver();
            }
        };

        gameLoop.start();
    }

    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void checkGameOver() {
        if (gameOver) return;

        int player1Hp = player1.getCharacter().getHp();
        int player2Hp = player2.getCharacter().getHp();

        if (player1Hp <= 0) {
            endGameAndShowPopup(2);
        } else if (player2Hp <= 0) {
            endGameAndShowPopup(1);
        }
    }

    private void endGameAndShowPopup(int winnerPlayerNumber) {
        gameOver = true;
        if (gameLoop != null) {
            gameLoop.stop();
        }

        Label winnerLabel = new Label("Player " + winnerPlayerNumber + " win!");
        winnerLabel.setTextFill(Color.BLACK);
        winnerLabel.setFont(Font.font("Monospaced", 26));

        Button restartButton = new Button("(Restart)");
        restartButton.setOnAction(e -> SceneHandler.switchRoot(new CharacterSelectScene()));

        VBox popup = new VBox(20, winnerLabel, restartButton);
        popup.setAlignment(Pos.CENTER);
        popup.setPrefWidth(420);
        popup.setPrefHeight(220);
        popup.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");

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

        getChildren().add(popup);
        popup.toFront();
    }
}
