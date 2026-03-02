package logic.gameLogic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.entity.Character;

public class Player {

    private Character character;
    private ImageView sprite;

    private PlayerState state = PlayerState.WALK;

    public Player(Character character, int i) {

        Image image = new Image(
                getClass().getResource("/" + character.getName() + ".png").toExternalForm()
        );

        setCharacter(character);

        sprite = new ImageView(image);

        sprite.setFitWidth(120);
        sprite.setPreserveRatio(true);

        if(i == 1){
            sprite.setLayoutX(300);
            sprite.setLayoutY(400);
        }else{
            sprite.setLayoutX(600);
            sprite.setLayoutY(400);
        }
    }


    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
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

    public boolean isFacingRight() {
        return sprite.getScaleX() == 1;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}