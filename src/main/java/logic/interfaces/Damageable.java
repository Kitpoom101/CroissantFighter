package logic.interfaces;

/**
 * Contract for entities that can receive and process incoming damage.
 */
public interface Damageable {
    /**
     * Applies incoming damage to the implementer.
     *
     * @param damage raw damage value before internal mitigation rules
     */
    void takeDamage(int damage);
}
