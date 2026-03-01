package logic.entity.characters.hybridCharacters;

import logic.entity.characterClass.HybridClass;

public class Vampire extends HybridClass {
    public Vampire(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
    }

    public Vampire(){
        super();
    }
}
