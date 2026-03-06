package logic.entity.characterClass;

import logic.entity.AttackData;
import logic.entity.Character;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.Attackable;

/**
 * Base class for hybrid characters that combine melee/ranged traits.
 * <p>
 * Uses attack buff as special skill and shared weapon animation progression.
 * </p>
 */
public abstract class HybridClass extends Character implements Attackable {

    /**
     * Creates a hybrid character with default hybrid stats.
     */
    public HybridClass() {
        super(150, 20, 5, 2, 1.0F, 150);
        setBuff(10);
        setOrigin(getAtk());
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {

    }


    /**
     * Creates a hybrid character with custom stats.
     *
     * @param hp HP value
     * @param atk attack value
     * @param def defense value
     * @param attackRange range value
     * @param attackSpeed attack speed
     */
    public HybridClass(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed, 150);

    }


    /**
     * Temporarily increases attack by buff amount.
     */
    @Override
    public void useSpecialSkill() {
        setAtk(getAtk() + getBuff());
    }

    /**
     * Restores attack to origin value.
     */
    @Override
    public void resetBuff(){
        setAtk(getOrigin());
    }

    /**
     * Returns hybrid attack data.
     *
     * @return attack data or {@code null}
     */
    @Override
    public AttackData getAttackData() {
        return null;
    }

    /**
     * Initializes hybrid attack animation state.
     *
     * @param self owning player
     */
    @Override
    public void startAttack(Player self) {
        frameIndex = 0;
        finished = false;
        lastFrameTime = System.nanoTime();
        if (attackFrames.length > 0) self.getWeaponSprite().setImage(attackFrames[0]);
    }

    /**
     * Updates hybrid attack animation progression.
     *
     * @param self owning player
     */
    @Override
    public void updateAttack(Player self) {

        long now = System.nanoTime();

        // change frame every few ticks
        if(now - lastFrameTime >= FRAME_DURATION){

            lastFrameTime = now;
            frameIndex++;

            if(frameIndex >= attackFrames.length){
                finished = true;
                self.setState(PlayerState.WALK);
                return;
            }

            self.getWeaponSprite()
                    .setImage(attackFrames[frameIndex]);
        }

        // ⭐ HIT FRAME (middle frame)
//        if(frameIndex == 1){
//            dealDamage(self, enemy);
//        }
    }

    /**
     * Indicates whether attack animation finished.
     *
     * @return finished state
     */
    public boolean isAttackFinished(){
        return this.finished;
    }

}
