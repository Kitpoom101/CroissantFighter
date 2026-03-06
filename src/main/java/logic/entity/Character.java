package logic.entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.gameLogic.SkillState;
import logic.interfaces.AttackAnimation;
import logic.interfaces.Attackable;
import logic.interfaces.Damageable;

/**
 * Base abstract character model shared by all playable classes.
 * <p>
 * Provides stats, combat state, weapon sprite handling, animation state, and skill state.
 * Concrete subclasses define attack style, animation frames, and buff reset behavior.
 * </p>
 */
public abstract class Character implements Attackable, Damageable, AttackAnimation {

    /** Display name of the character. */
    protected String name;
    /** Current hit points. */
    protected int hp;

    /** Maximum hit points. */
    protected int maxHp;
    /** Base attack damage. */
    protected int atk;
    /** Defense used in incoming damage reduction. */
    protected int def;
    /** Effective attack range unit for class-specific attacks. */
    protected int attackRange;
    /** Attack speed factor used by player logic cooldown handling. */
    protected float attackSpeed;
    /** Buff magnitude used by special skills. */
    protected int buff;
    /** Original stat snapshot used for resetting buff effects. */
    protected int origin;

    /** Weapon sprite used for attack animation rendering. */
    private ImageView weaponSprite;

    /** Current skill availability state. */
    protected SkillState skillState = SkillState.CanUseSkill;

    /** Ordered attack animation frames. */
    protected Image[] attackFrames;
    /** Number of expected attack frames. */
    protected int totalFrames;

    /** Current animation frame index. */
    protected int frameIndex = 0;

    /** Last timestamp at which animation frame changed. */
    protected long lastFrameTime;
    /** Frame duration in nanoseconds. */
    protected long FRAME_DURATION;

    /** Whether current attack animation has finished. */
    protected boolean finished = false;
    /** Current attack state used by combat logic. */
    protected AttackState attackState = AttackState.NotAttacking;

    /**
     * Creates a character with explicit base stats.
     *
     * @param hp initial HP
     * @param atk initial attack
     * @param def initial defense
     * @param attackRange initial attack range
     * @param attackSpeed initial attack speed
     * @param maxHp maximum HP cap
     */
    public Character(int hp, int atk, int def, int attackRange, float attackSpeed, int maxHp) {
        this.setMaxHp(maxHp);
        this.setHp(hp);
        this.setAtk(atk);
        this.setDef(def);
        this.setAttackRange(attackRange);
        this.setAttackSpeed(attackSpeed);

        weaponSprite = new ImageView();
        weaponSprite.setVisible(false);

        weaponSprite.setImage(
                new Image(
                        getClass()
                                .getResource("/Missing.png")
                                .toExternalForm()
                )
        );
    }

    /**
     * Creates a character with fallback default stats.
     */
    public Character() {
        this.setHp(100);
        this.setAtk(15);
        this.setDef(5);
        this.setAttackRange(1);
        this.setAttackSpeed(1.0F);
    }


    public void moveLeft() {}
    public void moveRight() {}
    public void jump() {}

    /**
     * Performs character attack action.
     *
     * @param startX world X spawn/start position
     * @param startY world Y spawn/start position
     * @param facingRight character facing direction
     * @param player owning player context
     */
    public abstract void attack(float startX, float startY, boolean facingRight, Player player);
    /**
     * Applies class-specific special skill effect.
     */
    public void useSpecialSkill() {};

    /**
     * Applies incoming damage after defense mitigation.
     *
     * @param damage incoming raw damage
     */
    public void takeDamage(int damage) {

        int finalDamage = Math.max(1, damage - def);

        hp -= finalDamage;

        if (hp < 0)
            hp = 0;
    }

    /**
     * Returns attack range.
     *
     * @return attack range value
     */
    public int getAttackRange() {
        return attackRange;
    }

    /**
     * Sets attack range with non-negative clamp.
     *
     * @param attackRange new range value
     */
    public void setAttackRange(int attackRange) {
        this.attackRange = Math.max(0, attackRange);
    }

    /**
     * Returns defense value.
     *
     * @return defense
     */
    public int getDef() {
        return def;
    }

    /**
     * Sets defense with non-negative clamp.
     *
     * @param def defense value
     */
    public void setDef(int def) {
        this.def = Math.max(0, def);
    }

    /**
     * Returns attack value.
     *
     * @return attack damage
     */
    public int getAtk() {
        return atk;
    }

    /**
     * Sets attack with non-negative clamp.
     *
     * @param atk attack value
     */
    public void setAtk(int atk) {
        this.atk = Math.max(0, atk);
    }

    /**
     * Returns current HP.
     *
     * @return HP value
     */
    public int getHp() {
        return hp;
    }

    /**
     * Sets current HP clamped to range {@code [0, maxHp]}.
     *
     * @param hp new HP
     */
    public void setHp(int hp) {
        this.hp = Math.min(Math.max(0, hp), this.getMaxHp());
    }

    /**
     * Returns maximum HP.
     *
     * @return max HP
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Returns attack speed.
     *
     * @return attack speed
     */
    public float getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Sets attack speed with non-negative clamp.
     *
     * @param attackSpeed speed value
     */
    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = Math.max(0, attackSpeed);
    }

    /**
     * Returns character name.
     *
     * @return display name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets character name.
     *
     * @param name display name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns weapon sprite node.
     *
     * @return weapon sprite
     */
    public ImageView getWeaponSprite(){
        return weaponSprite;
    }

    /**
     * Sets weapon sprite image by resource path.
     *
     * @param path classpath resource path
     */
    public void setWeaponSprite(String path) {
        this.weaponSprite.setImage(new Image(
                getClass()
                        .getResource(path)
                        .toExternalForm()
        ));
    }

    /**
     * Returns attack hitbox data for melee-like attacks.
     *
     * @return attack data or {@code null} for non-hitbox attacks
     */
    public abstract AttackData getAttackData();

    /**
     * Sets total number of animation frames.
     *
     * @param totalFrames frame count
     */
    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    /**
     * Sets animation frame duration in nanoseconds.
     *
     * @param FRAME_DURATION frame duration
     */
    public void setFRAME_DURATION(long FRAME_DURATION) {
        this.FRAME_DURATION = FRAME_DURATION;
    }

    /**
     * Returns total configured animation frames.
     *
     * @return frame count
     */
    public int getTotalFrames() {
        return totalFrames;
    }

    /**
     * Indicates whether current attack animation has finished.
     *
     * @return finished state
     */
    @Override
    public boolean isAttackFinished() {
        return finished;
    }

    /**
     * Returns attack state.
     *
     * @return attack state
     */
    public AttackState getAttackState() {
        return attackState;
    }

    /**
     * Sets attack state.
     *
     * @param attackState new state
     */
    public void setAttackState(AttackState attackState) {
        this.attackState = attackState;
    }

    /**
     * Returns skill buff amount.
     *
     * @return buff value
     */
    public int getBuff() {
        return buff;
    }

    /**
     * Sets skill buff amount.
     *
     * @param buff buff value
     */
    public void setBuff(int buff) {
        this.buff = buff;
    }

    /**
     * Returns skill state.
     *
     * @return skill state
     */
    public SkillState getSkillState() {
        return skillState;
    }

    /**
     * Sets skill state.
     *
     * @param skillState new skill state
     */
    public void setSkillState(SkillState skillState) {
        this.skillState = skillState;
    }

    /**
     * Returns original stat snapshot used by buff reset.
     *
     * @return origin stat
     */
    public int getOrigin() {
        return origin;
    }

    /**
     * Sets original stat snapshot used by buff reset.
     *
     * @param origin base stat value
     */
    public void setOrigin(int origin) {
        this.origin = origin;
    }

    /**
     * Resets class-specific buff-modified stats back to origin values.
     */
    public abstract void resetBuff();

    /**
     * Sets maximum HP cap.
     *
     * @param maxHp max HP
     */
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }


    /**
     * Returns current animation frame index.
     *
     * @return frame index
     */
    public int getFrameIndex() {
        return frameIndex;
    }


    /**
     * Sets current animation frame index.
     *
     * @param frameIndex frame index
     */
    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }
}
