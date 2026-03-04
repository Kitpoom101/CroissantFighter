package logic.entity.characterClass;

import logic.entity.AttackData;
import logic.entity.Character;
import logic.gameLogic.Player;
import logic.interfaces.Attackable;

public abstract class HybridClass extends Character implements Attackable {

    public HybridClass() {
        super(150, 15, 5, 2, 1.0F);
        setBuff(10);
        setOrigin(getAtk());
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {

    }


    public HybridClass(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);

    }


    @Override
    public void useSpecialSkill() {
        setAtk(getAtk() + getBuff());
    }

    @Override
    public void resetBuff(){
        setAtk(getOrigin());
    }

    @Override
    public AttackData getAttackData() {
        return null;
    }

}
