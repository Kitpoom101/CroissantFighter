package logic.entity.characterClass;

import logic.entity.Character;

public class HybridClass extends Character {
    public HybridClass() {
        super(150, 15, 5, 2, 1.0F);
    }

    public HybridClass(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
    }

    @Override
    public void attack() {

    }

    @Override
    public void useSpecialSkill() {

    }
}
