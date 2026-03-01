package logic.entity.characters.rangedCharacters;

import logic.entity.characterClass.RangedClass;

public class Archer extends RangedClass {
    public Archer(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
    }

    public Archer() {
        super();
    }
}
