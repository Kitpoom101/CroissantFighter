package logic.entity.characterClass;

import logic.entity.AttackData;
import logic.entity.Character;
import logic.gameLogic.Player;
import logic.interfaces.Attackable;

public class RangedClass extends Character implements Attackable {
    public RangedClass() {
        super(100, 25, 5, 3, 2.0F);
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {

    }


    public RangedClass(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
    }

    @Override
    public void useSpecialSkill() {
    }

    @Override
    public AttackData getAttackData() {
        return null;
    }
}
