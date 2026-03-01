package component.scene2;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerLogic;

import static logic.gameLogic.Selection.getPlayer_1_Character;
import static logic.gameLogic.Selection.getPlayer_2_Character;

public class Scene2 extends Pane {

    private Player player;
    private PlayerLogic playerLogic;

    public Scene2(){
        super();

        player = new Player("/KatanaManEx.png");
        playerLogic = new PlayerLogic(player);

        getChildren().add(player.getSprite());

        draw(Color.GREEN);

        sceneProperty().addListener((obs, oldScene, newScene) -> {

            if (newScene != null) {

                newScene.setOnKeyPressed(e ->
                        playerLogic.handleKeyPressed(e)
                );
            }
        });

        draw(Color.GREEN);

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
}
