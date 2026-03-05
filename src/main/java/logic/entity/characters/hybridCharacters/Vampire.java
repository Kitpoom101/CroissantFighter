package logic.entity.characters.hybridCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.HybridClass;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.interfaces.HaveWeapon;
import logic.interfaces.OwnWeaponPos;

public class Vampire extends HybridClass implements HaveWeapon, OwnWeaponPos {
    protected int maxHp;
    protected double lifeStealMultiplier;
    protected double originLifeStealMultiplier;
    public Vampire(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Vampire");
    }

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

    @Override
    public AttackData getAttackData() {
        return new AttackData(200, 100);
    }

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

    @Override
    public void updateAttack(Player self) {
        super.updateAttack(self);
        if(frameIndex == 1 && getAttackState() == AttackState.Attacking ){
            setAttackState(AttackState.WillAttack);
        }
    }

    @Override
    public void useSpecialSkill() {
        setAtk(getAtk() + getBuff());
        setLifeStealMultiplier(getLifeStealMultiplier() + 0.5);
    }

    @Override
    public void resetBuff(){
        setAtk(getOrigin());
        setLifeStealMultiplier(getOriginLifeStealMultiplier());
    }


    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public double getLifeStealMultiplier() {
        return lifeStealMultiplier;
    }

    public void setLifeStealMultiplier(double lifeStealMultiplier) {
        this.lifeStealMultiplier = lifeStealMultiplier;
    }

    public double getOriginLifeStealMultiplier() {
        return originLifeStealMultiplier;
    }

    public void setOriginLifeStealMultiplier(double originLifeStealMultiplier) {
        this.originLifeStealMultiplier = originLifeStealMultiplier;
    }
}
