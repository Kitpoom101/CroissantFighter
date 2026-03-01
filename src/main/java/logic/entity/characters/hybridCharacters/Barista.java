package logic.entity.characters.hybridCharacters;

import logic.entity.characterClass.HybridClass;

public class Barista extends HybridClass {
    public Barista(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Barista");
    }

    public Barista(){
        super();
        setName("Barista");
    }
}
