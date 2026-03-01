package logic.entity.characterClass;

import logic.entity.Character;

public class MeleeClass extends Character {
    public MeleeClass() {
        super(250, 20, 10, 1, 1.0F);
    }

    public MeleeClass(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
    }
    @Override
    public void attack() {
    }

    @Override
    public void useSpecialSkill() {
    }
}
