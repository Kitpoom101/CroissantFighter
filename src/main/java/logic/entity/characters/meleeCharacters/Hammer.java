package logic.entity.characters.meleeCharacters;

import logic.entity.characterClass.MeleeClass;

public class Hammer extends MeleeClass {
    public Hammer(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
    }

    public Hammer() {
        super();
    }
}
