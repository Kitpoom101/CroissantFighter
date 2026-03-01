package logic.entity.characterClass;

import logic.entity.Character;

public class RangeClass extends Character {
    public RangeClass(int hp, int atk, int def, int attackRange) {
        super(hp, atk, def, attackRange);
    }

    @Override
    public void attack() {
    }

    @Override
    public void useSpecialSkill() {
    }
}
