package logic.entity;

/**
 * Immutable-like container describing melee hitbox dimensions.
 */
public class AttackData {
    /** Hitbox width in pixels. */
    private double width;
    /** Hitbox height in pixels. */
    private double height;

    /**
     * Creates attack hitbox data.
     *
     * @param width hitbox width
     * @param height hitbox height
     */
    public AttackData(double width, double height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Returns hitbox width.
     *
     * @return width value
     */
    public double getWidth() { return width; }
    /**
     * Returns hitbox height.
     *
     * @return height value
     */
    public double getHeight() { return height; }
}
