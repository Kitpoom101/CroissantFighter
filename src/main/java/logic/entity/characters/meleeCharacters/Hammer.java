package logic.entity.characters.meleeCharacters;

import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;

public class Hammer extends MeleeClass {
    public Hammer(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Hammer");
    }

    public Hammer() {
        super();
        setName("Hammer");
    }

    @Override
    public AttackData getAttackData() {
        return new AttackData(90, 120);
    }
}
