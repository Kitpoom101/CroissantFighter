package logic.interfaces;

import component.scene2.Scene2;
import logic.entity.BaseProjectileAttack;

public interface SpawnAttack {
    default void spawnProjectile(BaseProjectileAttack projectile){
        Scene2.getInstance().addProjectile(projectile);
    };
}
