package logic.interfaces;


/**
 * Contract for characters that use an ammo/reload shooting model.
 */
public interface UsesAmmo {
    /**
     * Starts reload behavior.
     */
    void reload();
    /**
     * Updates reload/shooting timers each frame.
     */
    void updateAmmo();
    /**
     * Checks whether a shot can be fired.
     *
     * @return {@code true} when shooting is currently allowed
     */
    boolean canShoot();
    /**
     * Consumes one ammo unit for a successful shot.
     */
    void consumeAmmo();
    /**
     * Returns current ammo count.
     *
     * @return current ammo
     */
    int getCurrentAmmo();
    /**
     * Returns maximum ammo capacity.
     *
     * @return max ammo
     */
    int getMaxAmmo();
    /**
     * Returns whether reload is in progress.
     *
     * @return {@code true} while reloading
     */
    boolean isReloading();
}
