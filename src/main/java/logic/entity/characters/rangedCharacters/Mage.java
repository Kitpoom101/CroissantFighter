package logic.entity.characters.rangedCharacters;

import logic.entity.characterClass.RangedClass;

public class Mage extends RangedClass {
    public Mage(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
    }

    public Mage() {
        super();
    }
}
