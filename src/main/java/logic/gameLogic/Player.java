package logic.gameLogic;

import javafx.scene.Group;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.entity.Character;

public class Player {

    // character
    private Character character;
    private ImageView sprite;
    // weapon
    private ImageView weaponSprite;

    private PlayerState state = PlayerState.WALK;

    private Group playerRoot;
    private Rectangle hitbox;

    public Player(Character character, int i) {

        Image image = new Image(
                getClass().getResource("/animations/BaseIdleGif/Idle.gif").toExternalForm()
        );

        setCharacter(character);

        playerRoot = new Group();

        sprite = new ImageView(image);
        sprite.setFitWidth(150);
        sprite.setFitHeight(150);
        sprite.setPreserveRatio(true);

        // REAL collision box
        hitbox = new Rectangle(100,120);
        hitbox.setFill(Color.color(1, 0, 0, 0.3));
        hitbox.setStroke(Color.RED);

        hitbox.setLayoutX(25);
        hitbox.setLayoutY(20);
        hitbox.setVisible(false);

        playerRoot.getChildren().addAll(sprite, hitbox, character.getWeaponSprite());

        DropShadow glow = new DropShadow();
        glow.setColor(Color.BLUE);
        glow.setRadius(15);

        sprite.setEffect(glow);

        if(i == 1){
            playerRoot.setLayoutX(300);
            playerRoot.setLayoutY(400);
            glow.setColor(Color.BLUE);
        }else{
            playerRoot.setLayoutX(600);
            playerRoot.setLayoutY(400);
            glow.setColor(Color.RED);
        }

        // for weapon //
        weaponSprite = character.getWeaponSprite();
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
        playerRoot.setLayoutX(playerRoot.getLayoutX() + dx);
        playerRoot.setLayoutY(playerRoot.getLayoutY() + dy);
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

    public ImageView getWeaponSprite() {
        return weaponSprite;
    }

    public Group getPlayerRoot() {
        return playerRoot;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }
}