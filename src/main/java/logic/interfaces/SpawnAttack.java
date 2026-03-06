package logic.interfaces;

import component.scene2.Scene2;
import logic.entity.BaseProjectileAttack;

/**
 * Helper contract for classes that spawn projectile attacks into the active battle scene.
 */
public interface SpawnAttack {
    /**
     * Adds projectile to the active {@link Scene2} instance.
     *
     * @param projectile projectile to register and render
     */
    default void spawnProjectile(BaseProjectileAttack projectile){
        Scene2.getInstance().addProjectile(projectile);
    }
}
