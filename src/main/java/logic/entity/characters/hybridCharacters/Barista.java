package logic.entity.characters.hybridCharacters;

import component.scene2.Scene2;
import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.HybridClass;
import logic.gameLogic.Player;
import logic.interfaces.HandleOwnWeapon;
import logic.interfaces.SpawnAttack;

/**
 * Barista hybrid character that throws coffee projectiles with slight arc and knockback.
 */
public class Barista extends HybridClass implements SpawnAttack, HandleOwnWeapon {
    /**
     * Creates Barista with custom stats.
     *
     * @param hp HP value
     * @param atk attack value
     * @param def defense value
     * @param attackRange range value
     * @param attackSpeed attack speed
     */
    public Barista(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Barista");
    }

    /**
     * Creates Barista with default hybrid stats.
     */
    public Barista(){
        super();
        setName("Barista");
    }


    /**
     * Spawns a coffee projectile attack.
     *
     * @param startX spawn X
     * @param startY spawn Y
     * @param facingRight facing direction
     * @param p owning player
     */
    @Override
    public void attack(float startX, float startY, boolean facingRight, Player p) {

        float dirX = facingRight ? 1 : -1;
        float speed = 5.5f + (float)Math.random();

        BaseProjectileAttack coffeeShot =
                new BaseProjectileAttack(
                        this.getAtk(),
                        speed,
                        this.attackRange * 200,
                        startX,
                        startY,
                        dirX,
                        -0.15f, // slight arc
                        p,
                        new Image(
                                getClass().getResource(
                                        "/animations/barista/attack/coffee.png"
                                ).toExternalForm()
                        ),
                        1.2f // slightly bigger
                );
        coffeeShot.setHasKnockback(true);

        spawnProjectile(coffeeShot);
    }

    /**
     * No explicit attack animation start behavior for this character.
     *
     * @param self owning player
     */
    @Override
    public void startAttack(Player self) {

    }

    /**
     * No explicit per-frame attack animation update for this character.
     *
     * @param self owning player
     */
    @Override
    public void updateAttack(Player self) {

    }

    /**
     * Barista currently does not use frame-based weapon animation.
     *
     * @param totalFrame requested frame count
     */
    @Override
    public void setupAttackFrame(int totalFrame) {

    }

    /**
     * Returns fixed value because Barista uses projectile attacks.
     *
     * @return always {@code false}
     */
    @Override
    public boolean isAttackFinished() {
        return false;
    }

    /*@Override
    public AttackData getAttackData() {
        return new AttackData(20, 20);
    }*/


}
