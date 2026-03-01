package logic.gameLogic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {

    private Character character;
    private ImageView sprite;

    public Player(String spritePath) {

        Image image = new Image(
                getClass().getResource(spritePath).toExternalForm()
        );

        sprite = new ImageView(image);

        sprite.setFitWidth(120);
        sprite.setPreserveRatio(true);

        sprite.setLayoutX(300);
        sprite.setLayoutY(400);
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void translate(double dx, double dy) {
        sprite.setLayoutX(sprite.getLayoutX() + dx);
        sprite.setLayoutY(sprite.getLayoutY() + dy);
    }

    public void faceLeft() {
        sprite.setScaleX(-1);
    }

    public void faceRight() {
        sprite.setScaleX(1);
    }
}