package logic.entity.characterClass;

import logic.entity.AttackData;
import logic.entity.Character;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.Attackable;
import logic.interfaces.HaveWeapon;

/**
 * Base class for melee characters.
 * <p>
 * Provides close-range attack animation flow and defense-based skill buff behavior.
 * </p>
 */
public abstract class MeleeClass extends Character implements Attackable, HaveWeapon {

    /**
     * Creates a melee character with default melee archetype stats.
     */
    public MeleeClass() {
        super(200, 20, 7, 1, 1.0F, 200);
        setBuff(15);
        setOrigin(getDef());
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {

    }


    /**
     * Creates a melee character with custom stats.
     *
     * @param hp HP value
     * @param atk attack value
     * @param def defense value
     * @param attackRange range value
     * @param attackSpeed attack speed
     */
    public MeleeClass(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed, 250);

    }

    /**
     * Temporarily increases defense by buff amount.
     */
    @Override
    public void useSpecialSkill() {
        setDef(getDef() + getBuff());
    }

    /**
     * Restores defense to pre-buff origin value.
     */
    @Override
    public void resetBuff(){
        setDef(getOrigin());
    }

    /**
     * Returns melee hitbox data.
     *
     * @return attack data, or {@code null} when overridden subtype not configured
     */
    @Override
    public AttackData getAttackData() {
        return null;
    }

    /**
     * Initializes melee attack animation state.
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
     * Updates melee animation frame progression.
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
     * Indicates whether melee animation sequence has finished.
     *
     * @return finished state
     */
    public boolean isAttackFinished(){
        return this.finished;
    }
}
