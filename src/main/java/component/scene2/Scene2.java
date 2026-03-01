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

public class Scene2 extends Pane {

    private Player player1;
    private Player player2;
    private PlayerLogic playerLogic1;
    private PlayerLogic playerLogic2;

    public Scene2() {

        player1 = new Player(getPlayer_1_Character());
        playerLogic1 = new PlayerLogic(player1, 1);

        player2 = new Player(getPlayer_2_Character());
        playerLogic2 = new PlayerLogic(player2, 2);

        getChildren().addAll(
                player1.getSprite(),
                player2.getSprite()
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
            playerLogic1.handleKeyPressed(e);
            playerLogic2.handleKeyPressed(e);
        });

        scene.setOnKeyReleased(e -> {
            playerLogic1.handleKeyReleased(e);
            playerLogic2.handleKeyReleased(e);
        });
    }

    private void startGameLoop() {

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                playerLogic1.update(); // runs every frame
                playerLogic2.update(); // runs every frame
            }
        };

        gameLoop.start();
    }
}
