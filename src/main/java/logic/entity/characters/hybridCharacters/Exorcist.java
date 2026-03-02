package logic.entity.characters.hybridCharacters;

import logic.entity.characterClass.HybridClass;

public class Exorcist extends HybridClass {
    public Exorcist(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Exorcist");
    }

    public Exorcist() {
        super();
        setName("Exorcist");
    }
}
