package logic.entity.characterClass;

import logic.entity.Character;

public class RangedClass extends Character {
    public RangedClass() {
        super(100, 25, 5, 3, 2.0F);
    }

    public RangedClass(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
    }
    @Override
    public void attack() {
    }

    @Override
    public void useSpecialSkill() {
    }
}
