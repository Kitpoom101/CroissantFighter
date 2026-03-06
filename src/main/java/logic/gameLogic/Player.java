package logic.gameLogic;

import javafx.scene.Group;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.entity.Character;

/**
 * Runtime player container that binds character data with scene nodes.
 * <p>
 * Holds sprite visuals, collision hitbox, facing direction, and current player state.
 * </p>
 */
public class Player {

    /** Selected character model for this player. */
    private Character character;
    /** Main body sprite displayed in scene. */
    private ImageView sprite;
    /** Weapon sprite node attached to character. */
    private ImageView weaponSprite;

    /** Current player action state. */
    private PlayerState state = PlayerState.WALK;

    /** Root node grouping sprite and hitbox for movement transforms. */
    private Group playerRoot;
    /** Collision hitbox used for combat intersection checks. */
    private Rectangle hitbox;

    /**
     * Creates a player model and scene nodes.
     *
     * @param character selected character instance
     * @param i player index used to determine spawn side and glow color
     */
    public Player(Character character, int i) {

        Image image = new Image(
                getClass().getResource("/animations/BaseIdleGif/Idle.GIF").toExternalForm()
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


    /**
     * Returns current player state.
     *
     * @return player state
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Sets current player state.
     *
     * @param state new state
     */
    public void setState(PlayerState state) {
        this.state = state;
    }


    /**
     * Returns body sprite.
     *
     * @return sprite node
     */
    public ImageView getSprite() {
        return sprite;
    }

    /**
     * Moves player root by delta values.
     *
     * @param dx horizontal delta
     * @param dy vertical delta
     */
    public void translate(double dx, double dy) {
        playerRoot.setLayoutX(playerRoot.getLayoutX() + dx);
        playerRoot.setLayoutY(playerRoot.getLayoutY() + dy);
    }

    /**
     * Flips sprite to face left.
     */
    public void faceLeft() {
        sprite.setScaleX(-1);
    }

    /**
     * Flips sprite to face right.
     */
    public void faceRight() {
        sprite.setScaleX(1);
    }

    /**
     * Returns current facing direction.
     *
     * @return {@code true} when facing right
     */
    public boolean isFacingRight() {
        return sprite.getScaleX() == 1;
    }

    /**
     * Returns character model.
     *
     * @return character instance
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Sets character model.
     *
     * @param character character instance
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Returns weapon sprite node.
     *
     * @return weapon sprite
     */
    public ImageView getWeaponSprite() {
        return weaponSprite;
    }

    /**
     * Returns root group containing player visuals.
     *
     * @return player root node
     */
    public Group getPlayerRoot() {
        return playerRoot;
    }

    /**
     * Returns collision hitbox rectangle.
     *
     * @return hitbox node
     */
    public Rectangle getHitbox(){
        return hitbox;
    }
}
