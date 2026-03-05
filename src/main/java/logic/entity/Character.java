package logic.entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.gameLogic.SkillState;
import logic.interfaces.AttackAnimation;
import logic.interfaces.Attackable;
import logic.interfaces.Damageable;

public abstract class Character implements Attackable, Damageable, AttackAnimation {

    protected String name;
    protected int hp;
    protected int maxHp;
    protected int atk;
    protected int def;
    protected int attackRange;
    protected float attackSpeed;
    protected int buff;
    protected int origin;

    // weapon
    private ImageView weaponSprite;

    // skill
    protected SkillState skillState = SkillState.CanUseSkill;

    // animation
    protected Image[] attackFrames;
    protected int totalFrames;

    protected int frameIndex = 0;

    protected long lastFrameTime;
    protected long FRAME_DURATION;

    protected boolean finished = false;
    protected AttackState attackState = AttackState.NotAttacking;

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

    public abstract void attack(float startX, float startY, boolean facingRight, Player player);
    public void useSpecialSkill() {};

    public void takeDamage(int damage) {

        int finalDamage = Math.max(0, damage - def);

        hp -= finalDamage;

        if (hp < 0)
            hp = 0;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = Math.max(0, attackRange);
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = Math.max(0, def);
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = Math.max(0, atk);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.min(Math.max(0, hp), this.getMaxHp());
    }

    public int getMaxHp() {
        return maxHp;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = Math.max(0, attackSpeed);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getWeaponSprite(){
        return weaponSprite;
    }

    public abstract AttackData getAttackData();

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public void setFRAME_DURATION(long FRAME_DURATION) {
        this.FRAME_DURATION = FRAME_DURATION;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    @Override
    public boolean isAttackFinished() {
        return finished;
    }

    public AttackState getAttackState() {
        return attackState;
    }

    public void setAttackState(AttackState attackState) {
        this.attackState = attackState;
    }

    public int getBuff() {
        return buff;
    }

    public void setBuff(int buff) {
        this.buff = buff;
    }

    public SkillState getSkillState() {
        return skillState;
    }

    public void setSkillState(SkillState skillState) {
        this.skillState = skillState;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public abstract void resetBuff();

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }
}
