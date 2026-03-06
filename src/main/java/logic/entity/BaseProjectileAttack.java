package logic.entity;

import javafx.scene.image.ImageView;

import javafx.scene.image.Image;
import logic.gameLogic.Player;

/**
 * Generic projectile model used by ranged/hybrid attacks.
 * <p>
 * Stores damage, movement vectors, travel range, owning player, and a JavaFX sprite.
 * </p>
 */
public class BaseProjectileAttack {

    /** Raw damage dealt on collision before defense reduction. */
    protected int damage;
    /** Projectile speed per update tick. */
    protected float speed;
    /** Maximum travel distance before auto-removal. */
    protected int range;

    /** Distance already traveled since spawn. */
    protected float currentDistance = 0;

    /** Current X position. */
    protected float x;
    /** Current Y position. */
    protected float y;
    /** Normalized or signed X direction. */
    protected float directionX;
    /** Normalized or signed Y direction. */
    protected float directionY;
    /** Player who spawned this projectile. */
    private Player owner;

    /** Whether this projectile applies knockback on hit. */
    private boolean knockback;


    /** Render node attached to scene graph. */
    private ImageView sprite;

    /**
     * Creates a projectile instance with sprite and movement properties.
     *
     * @param damage projectile damage
     * @param speed projectile speed
     * @param range maximum travel distance
     * @param startX spawn X coordinate
     * @param startY spawn Y coordinate
     * @param directionX horizontal direction
     * @param directionY vertical direction
     * @param owner player who owns the projectile
     * @param image projectile sprite image
     * @param sizeMultiplier sprite size scale factor
     */
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

    /**
     * Returns projectile owner.
     *
     * @return owner player
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Advances projectile position and accumulates traveled distance.
     */
    public void update() {

        float dx = directionX * speed;
        float dy = directionY * speed;

        x += dx;
        y += dy;

        sprite.setLayoutX(x);
        sprite.setLayoutY(y);

        currentDistance += Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Checks whether projectile has exceeded configured range.
     *
     * @return {@code true} if out of range
     */
    public boolean isOutOfRange() {
        return currentDistance >= range;
    }

    /**
     * Returns sprite node used for rendering and collision.
     *
     * @return projectile sprite
     */
    public ImageView getSprite() {
        return sprite;
    }

    /**
     * Returns raw projectile damage.
     *
     * @return damage value
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns knockback flag.
     *
     * @return {@code true} if knockback should be applied
     */
    public boolean hasKnockback() {
        return knockback;
    }

    /**
     * Sets knockback behavior.
     *
     * @param hasKnockback knockback enabled state
     */
    public void setHasKnockback(boolean hasKnockback) {
        this.knockback = hasKnockback;
    }

    /**
     * Returns X direction component.
     *
     * @return direction X
     */
    public float getDirectionX() {
        return directionX;
    }

    /**
     * Sets X direction component.
     *
     * @param directionX new direction X
     */
    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }

    /**
     * Returns Y direction component.
     *
     * @return direction Y
     */
    public float getDirectionY() {
        return directionY;
    }

    /**
     * Sets Y direction component.
     *
     * @param directionY new direction Y
     */
    public void setDirectionY(float directionY) {
        this.directionY = directionY;
    }
}
