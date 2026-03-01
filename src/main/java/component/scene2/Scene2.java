package component.scene2;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerLogic;

import static logic.gameLogic.Selection.getPlayer_1_Character;
import static logic.gameLogic.Selection.getPlayer_2_Character;
import javafx.scene.layout.*;

public class Scene2 extends Pane {

    private Player player;
    private PlayerLogic playerLogic;

    public Scene2() {

        player = new Player("/KatanaManEx.png");
        playerLogic = new PlayerLogic(player);

        getChildren().add(player.getSprite());

        draw(Color.GREEN);

        /* ✅ WAIT UNTIL SCENE EXISTS */
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setupControls(newScene);
                startGameLoop();
            }
        });

        System.out.println(getPlayer_1_Character());
        System.out.println(getPlayer_2_Character());
    }

    private void draw(Color backgroundColor) {
        BackgroundFill bgFill = new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};

        this.setBackground(new Background(bgFillA));
    }

    private void spawnPlayer1() {

        Image image = new Image(
                getClass().getResource("/" + getPlayer_1_Character() + ".png").toExternalForm()
        );

        ImageView sprite = new ImageView(image);

        sprite.setFitWidth(100);
        sprite.setPreserveRatio(true);

        this.getChildren().add(sprite);
    }

    private void spawnPlayer2() {

        Image image = new Image(
                getClass().getResource("/" + getPlayer_2_Character() + ".png").toExternalForm()
        );

        ImageView sprite = new ImageView(image);

        sprite.setFitWidth(100);
        sprite.setPreserveRatio(true);

        this.getChildren().add(sprite);
    }

    private void setupControls(Scene scene) {

        scene.setOnKeyPressed(playerLogic::handleKeyPressed);
        scene.setOnKeyReleased(playerLogic::handleKeyReleased);
    }

    private void startGameLoop() {

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                playerLogic.update(); // runs every frame
            }
        };

        gameLoop.start();
    }
}
