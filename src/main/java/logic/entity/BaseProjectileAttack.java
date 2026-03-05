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

    private boolean knockback;


    private ImageView sprite;

    public BaseProjectileAttack(int damage, float speed, int range,
                                float startX, float startY,
                                float directionX, float directionY, Player owner, Image image,
                                float sizeMultiplier) {

        this.damage = damage;
        this.speed = speed;
        this.range = range;

        this.x = startX;
        this.y = startY;
        this.directionX = directionX;
        this.directionY = directionY;

        this.owner = owner;

        sprite = new ImageView(image);

        double size = 40 * sizeMultiplier;

        sprite.setFitWidth(size);
        sprite.setPreserveRatio(true);

        sprite.setScaleX(directionX < 0 ? -1 : 1);

        sprite.setLayoutX(x);
        sprite.setLayoutY(y - size / 2);

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

    public int getDamage() {
        return damage;
    }

    public boolean hasKnockback() {
        return knockback;
    }

    public void setHasKnockback(boolean hasKnockback) {
        this.knockback = hasKnockback;
    }

    public float getDirectionX() {
        return directionX;
    }

    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }

    public float getDirectionY() {
        return directionY;
    }

    public void setDirectionY(float directionY) {
        this.directionY = directionY;
    }
}