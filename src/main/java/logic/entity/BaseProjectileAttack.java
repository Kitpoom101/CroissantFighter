package logic.entity;

import javafx.scene.image.ImageView;

import javafx.scene.image.Image;
import logic.gameLogic.Player;

public class BaseProjectileAttack {

    protected int damage;
    protected float speed;
    protected int range;

    protected float currentDistance = 0;

    protected float x;
    protected float y;
    protected float directionX;
    protected float directionY;
    private Player owner;


    private ImageView sprite;

    public BaseProjectileAttack(int damage, float speed, int range,
                                float startX, float startY,
                                float directionX, float directionY, Player owner) {

        this.damage = damage;
        this.speed = speed;
        this.range = range;

        this.x = startX;
        this.y = startY;
        this.directionX = directionX;
        this.directionY = directionY;

        this.owner = owner;

        Image image = new Image(
                getClass().getResource("/projectilebaseattack.png").toExternalForm()
        );
        sprite = new ImageView(image);

        sprite.setLayoutX(x);
        sprite.setLayoutY(y);

        sprite.setFitWidth(40);
        sprite.setPreserveRatio(true);
    }

    public Player getOwner() {
        return owner;
    }

    public void update() {

        float dx = directionX * speed;
        float dy = directionY * speed;

        x += dx;
        y += dy;

        sprite.setLayoutX(x);
        sprite.setLayoutY(y);

        currentDistance += Math.sqrt(dx * dx + dy * dy);
    }

    public boolean isOutOfRange() {
        return currentDistance >= range;
    }

    public ImageView getSprite() {
        return sprite;
    }
}