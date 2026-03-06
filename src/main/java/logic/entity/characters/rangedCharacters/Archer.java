package logic.entity.characters.rangedCharacters;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.audio.AudioManager;
import logic.entity.AttackData;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;
import logic.interfaces.HandleOwnWeapon;
import logic.interfaces.HaveWeapon;
import logic.interfaces.SpawnAttack;


/**
 * Archer ranged character that fires arrow projectiles.
 */
public class Archer extends RangedClass implements SpawnAttack {
    /** Original attack range used for skill reset. */
    protected int originAttackRange;
    /**
     * Creates Archer with tuned base stats, weapon sprite, and origin snapshots.
     */
    public Archer() {
        super(125, 35, 3, 6, 0.75f, 3);
        setName("Archer");
        setWeaponSprite("/animations/archer/attack/Bow.png");

        setOrigin(getAtk());
        setOriginAttackRange(getAttackRange());
    }


    /**
     * Not used directly; ranged attacks should go through {@code tryAttack}.
     *
     * @param startX spawn X
     * @param startY spawn Y
     * @param facingRight facing direction
     * @param player owning player
     */
    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {

    }

    /**
     * Archer uses projectile spawning, not melee hitbox data.
     *
     * @return {@code null}
     */
    @Override
    public AttackData getAttackData() {
        return null;
    }

    /**
     * Spawns arrow projectile with slight vertical random spread.
     *
     * @param startX spawn X
     * @param startY spawn Y
     * @param facingRight facing direction
     * @param p owning player
     */
    @Override
    protected void spawnRangedAttack(
            float startX,
            float startY,
            boolean facingRight,
            Player p) {

        float dirX = facingRight ? 1 : -1;
        float randomY = (float)((Math.random() - 0.5) * 0.6);

        float spawnOffset = dirX > 0 ? 60 * dirX : 155 * dirX;

        BaseProjectileAttack arrow =
                new BaseProjectileAttack(
                        this.atk,
                        1.5f,
                        (int)(this.attackRange * 100),
                        startX + spawnOffset,
                        startY,
                        dirX,
                        randomY,
                        p,
                        new Image(
                                getClass()
                                        .getResource("/animations/archer/attack/arrow.png")
                                        .toExternalForm()
                        ),
                        3
                );

        AudioManager.playSFX("/audio/sfx/attack/arrowShoot.mp3");
        spawnProjectile(arrow);


    }

    /**
     * Delegates to ranged base start behavior.
     *
     * @param self owning player
     */
    @Override
    public void startAttack(Player self) {
        super.startAttack(self);
    }

    /**
     * Not used for Archer projectile attacks.
     *
     * @param totalFrame frame count
     */
    @Override
    public void setupAttackFrame(int totalFrame) {

    }

    /**
     * Archer attack completion is controlled by ranged base logic.
     *
     * @return always {@code false}
     */
    @Override
    public boolean isAttackFinished() {
        return false;
    }


    /**
     * Archer skill increases both range and attack.
     */
    @Override
    public void useSpecialSkill() {
        setAttackRange(getAttackRange() + getBuff());
        setAtk(getAtk() + getBuff());
    }


    /**
     * Restores attack/range to origin values.
     */
    @Override
    public void resetBuff(){
        setAttackRange(getOriginAttackRange());
        setAtk(getOrigin());
    }

    /**
     * Returns original attack range.
     *
     * @return origin range
     */
    public int getOriginAttackRange() {
        return originAttackRange;
    }
    /**
     * Sets original attack range.
     *
     * @param originAttackRange origin range
     */
    public void setOriginAttackRange(int originAttackRange) {
        this.originAttackRange = originAttackRange;
    }
}
