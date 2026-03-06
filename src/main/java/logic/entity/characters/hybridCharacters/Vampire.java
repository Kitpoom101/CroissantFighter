package logic.entity.characters.hybridCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.HybridClass;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.interfaces.HaveWeapon;
import logic.interfaces.OwnWeaponPos;

/**
 * Vampire hybrid melee character with life-steal mechanics.
 */
public class Vampire extends HybridClass implements HaveWeapon, OwnWeaponPos {
    /** Cached max HP value used by lifesteal clamp logic. */
    protected int maxHp;
    /** Current life-steal multiplier. */
    protected double lifeStealMultiplier;
    /** Original life-steal multiplier used to reset buffs. */
    protected double originLifeStealMultiplier;
    /**
     * Creates Vampire with custom stats.
     *
     * @param hp HP value
     * @param atk attack value
     * @param def defense value
     * @param attackRange range value
     * @param attackSpeed attack speed
     */
    public Vampire(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Vampire");
    }

    /**
     * Creates Vampire with default tuned stats and animation setup.
     */
    public Vampire(){
        super();
        setMaxHp(getHp());

        setTotalFrames(2);
        setupAttackFrame(getTotalFrames());
        setAtk(20);

        setLifeStealMultiplier(0.35);
        setOriginLifeStealMultiplier(getLifeStealMultiplier());

        setAttackSpeed(2.0F);
        setFRAME_DURATION(300_000_000);// nano-sec

        setName("Vampire");
    }

    /**
     * Returns Vampire melee hitbox data.
     *
     * @return attack data
     */
    @Override
    public AttackData getAttackData() {
        return new AttackData(200, 100);
    }

    /**
     * Loads Vampire attack animation frames.
     *
     * @param totalFrame frame count
     */
    @Override
    public void setupAttackFrame(int totalFrame) {
        attackFrames = new Image[totalFrame];

        for (int i = 0; i < totalFrame; i++) {
            String path =
                    "/animations/vampire/attack/frame" + (i + 1) + ".png";
            attackFrames[i] = new Image(
                    getClass().getResource(path).toExternalForm());
        }
    }

    /**
     * Updates animation and marks strike frame for combat handling.
     *
     * @param self owning player
     */
    @Override
    public void updateAttack(Player self) {
        super.updateAttack(self);
        if(frameIndex == 1 && getAttackState() == AttackState.Attacking ){
            setAttackState(AttackState.WillAttack);
        }
    }

    /**
     * Increases attack and life-steal multiplier while buff is active.
     */
    @Override
    public void useSpecialSkill() {
        setAtk(getAtk() + getBuff());
        setLifeStealMultiplier(getLifeStealMultiplier() + 0.5);
    }

    /**
     * Restores attack and life-steal multiplier to origin values.
     */
    @Override
    public void resetBuff(){
        setAtk(getOrigin());
        setLifeStealMultiplier(getOriginLifeStealMultiplier());
    }


    /**
     * Returns cached max HP.
     *
     * @return max HP
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Sets cached max HP.
     *
     * @param maxHp max HP value
     */
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    /**
     * Returns life-steal multiplier.
     *
     * @return multiplier value
     */
    public double getLifeStealMultiplier() {
        return lifeStealMultiplier;
    }

    /**
     * Sets life-steal multiplier.
     *
     * @param lifeStealMultiplier multiplier value
     */
    public void setLifeStealMultiplier(double lifeStealMultiplier) {
        this.lifeStealMultiplier = lifeStealMultiplier;
    }

    /**
     * Returns baseline life-steal multiplier used for reset.
     *
     * @return origin multiplier
     */
    public double getOriginLifeStealMultiplier() {
        return originLifeStealMultiplier;
    }

    /**
     * Sets baseline life-steal multiplier used for reset.
     *
     * @param originLifeStealMultiplier origin multiplier
     */
    public void setOriginLifeStealMultiplier(double originLifeStealMultiplier) {
        this.originLifeStealMultiplier = originLifeStealMultiplier;
    }
}
