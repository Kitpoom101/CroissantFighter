package component.scene2;

import application.SceneHandler;
import component.CharacterSelectScene;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
    private static final long MATCH_DURATION_SECONDS = 180;
    private static final long MATCH_DURATION_NANOS = MATCH_DURATION_SECONDS * 1_000_000_000L;

    public Player player1, player2;
    private PlayerLogic playerLogic1, playerLogic2;
    private HealthBar player1HealthBar, player2HealthBar;
    private Label countdownLabel;
    private AnimationTimer gameLoop;
    private boolean gameOver = false;
    private long matchStartNanos = -1L;
    private List<BaseProjectileAttack> projectileList = new ArrayList<>();
    private static Scene2 instance;

    public static Scene2 getInstance() { return instance; }

    public Scene2() {
        instance = this;
        player1 = new Player(getPlayer_1_Character(), 1);
        player2 = new Player(getPlayer_2_Character(), 2);

        playerLogic1 = new PlayerLogic(player1, player2, 1);
        playerLogic2 = new PlayerLogic(player2, player1, 2);

        // ใช้ HealthBar ใหม่พร้อมชื่อตัวละคร
        player1HealthBar = new HealthBar(player1.getCharacter().getHp(), player1.getCharacter().getName(), true);
        player2HealthBar = new HealthBar(player2.getCharacter().getHp(), player2.getCharacter().getName(), false);

        countdownLabel = new Label(formatTime(MATCH_DURATION_SECONDS));
        countdownLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 28));
        countdownLabel.setTextFill(Color.WHITE);

        player1HealthBar.setLayoutX(HEALTH_BAR_SIDE_MARGIN);
        player1HealthBar.setLayoutY(HEALTH_BAR_TOP_MARGIN);

        player2HealthBar.layoutXProperty().bind(widthProperty().subtract(400 + HEALTH_BAR_SIDE_MARGIN));
        player2HealthBar.setLayoutY(HEALTH_BAR_TOP_MARGIN);

        countdownLabel.layoutXProperty().bind(Bindings.createDoubleBinding(
                () -> (getWidth() - countdownLabel.getLayoutBounds().getWidth()) / 2.0, widthProperty(), countdownLabel.layoutBoundsProperty()));
        countdownLabel.setLayoutY(HEALTH_BAR_TOP_MARGIN + 6);

        getChildren().addAll(player1HealthBar, player2HealthBar, countdownLabel,
                player1.getPlayerRoot(), player2.getPlayerRoot(),
                playerLogic1.getBuffText(), playerLogic2.getBuffText());

        setImageBackground();

        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setupControls(newScene);
                startGameLoop();
            }
        });
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameOver) return;
                if (matchStartNanos < 0) matchStartNanos = now;

                playerLogic1.update();
                playerLogic2.update();

                player1HealthBar.setCurrentHp(player1.getCharacter().getHp());
                player2HealthBar.setCurrentHp(player2.getCharacter().getHp());

                long elapsedNanos = now - matchStartNanos;
                long remainingSeconds = Math.max(0, (MATCH_DURATION_NANOS - elapsedNanos) / 1_000_000_000L);
                countdownLabel.setText(formatTime(remainingSeconds));

                if (remainingSeconds <= 0) {
                    handleGameOver(null, null); // Draw or Time Out logic
                    return;
                }

                checkGameOver();
                updateProjectiles();
            }
        };
        gameLoop.start();
    }

    private void checkGameOver() {
        if (gameOver) return;
        if (player1.getCharacter().getHp() <= 0) {
            handleGameOver(player2, player1);
        } else if (player2.getCharacter().getHp() <= 0) {
            handleGameOver(player1, player2);
        }
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
            deathAnim.setOnFinished(e -> showVictoryOverlay(winner.getCharacter().getName()));
            deathAnim.play();
        } else {
            showVictoryOverlay("TIME UP!");
        }
    }

    private void showVictoryOverlay(String resultText) {
        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER);
        overlay.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), null, null)));
        overlay.setPrefSize(getWidth(), getHeight());

        Label winLabel = new Label(resultText.contains("TIME") ? resultText : "VICTORY");
        winLabel.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 80));
        winLabel.setTextFill(Color.GOLD);

        Label nameLabel = new Label(!resultText.contains("TIME") ? resultText + " WINS!" : "");
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        nameLabel.setTextFill(Color.WHITE);

        Button restartBtn = new Button("REMATCH");
        restartBtn.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 24px; -fx-cursor: hand;");
        restartBtn.setOnAction(e -> SceneHandler.switchRoot(new CharacterSelectScene()));

        overlay.getChildren().addAll(winLabel, nameLabel, restartBtn);
        overlay.setOpacity(0);
        getChildren().add(overlay);

        // Animation: ค่อยๆ แสดงหน้าจอผู้ชนะ
        FadeTransition ft = new FadeTransition(Duration.seconds(1), overlay);
        ft.setToValue(1.0);

        ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), winLabel);
        st.setFromX(0); st.setFromY(0); st.setToX(1); st.setToY(1);

        new ParallelTransition(ft, st).play();
    }

    private void updateProjectiles() {
        Iterator<BaseProjectileAttack> iterator = projectileList.iterator();
        while (iterator.hasNext()) {
            BaseProjectileAttack p = iterator.next();
            p.update();
            Player target = (p.getOwner() == player1) ? player2 : player1;

            if (p.getSprite().getBoundsInParent().intersects(target.getHitbox().localToScene(target.getHitbox().getBoundsInLocal()))) {
                int damage = p.getDamage();
                target.getCharacter().takeDamage(damage);
                if (damage - target.getCharacter().getDef() > 0) {
                    showDamageText(target, damage - target.getCharacter().getDef());
                }
                getChildren().remove(p.getSprite());
                iterator.remove();
            } else if (p.isOutOfRange()) {
                getChildren().remove(p.getSprite());
                iterator.remove();
            }
        }
    }

    private void setupControls(Scene scene) {
        scene.setOnKeyPressed(e -> { if (!gameOver) { playerLogic1.handleKeyPressed(e); playerLogic2.handleKeyPressed(e); }});
        scene.setOnKeyReleased(e -> { if (!gameOver) { playerLogic1.handleKeyReleased(e); playerLogic2.handleKeyReleased(e); }});
    }

    private String formatTime(long totalSeconds) {
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }

    private void setImageBackground() {
        try {
            Image image = new Image(getClass().getResource("/background2.png").toExternalForm());
            setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, false, true))));
        } catch (Exception e) {
            setBackground(new Background(new BackgroundFill(Color.DARKSLATEGRAY, null, null)));
        }
    }

    public void addProjectile(BaseProjectileAttack p) {
        projectileList.add(p);
        getChildren().add(p.getSprite());
        p.getSprite().toFront();
    }

    private void showDamageText(Player target, int damage) {
        Label damageLabel = new Label("-" + damage);
        damageLabel.setTextFill(Color.ORANGERED);
        damageLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 24));

        double x = target.getHitbox().localToScene(target.getHitbox().getBoundsInLocal()).getMinX();
        double y = target.getHitbox().localToScene(target.getHitbox().getBoundsInLocal()).getMinY();

        damageLabel.setLayoutX(x + 20);
        damageLabel.setLayoutY(y - 20);

        getChildren().add(damageLabel);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.8), damageLabel);
        fade.setFromValue(1.0); fade.setToValue(0.0);
        TranslateTransition move = new TranslateTransition(Duration.seconds(0.8), damageLabel);
        move.setByY(-50);

        ParallelTransition anim = new ParallelTransition(fade, move);
        anim.setOnFinished(e -> getChildren().remove(damageLabel));
        anim.play();
    }
}