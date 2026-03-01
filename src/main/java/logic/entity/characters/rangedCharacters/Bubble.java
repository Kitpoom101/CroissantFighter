package logic.entity.characters.rangedCharacters;

import logic.entity.characterClass.RangedClass;

public class Bubble extends RangedClass {
    public Bubble(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
    }

    public Bubble() {
        super();
    }
}
