package logic.entity.characters.rangedCharacters;

import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;

public class Archer extends RangedClass {
    public Archer(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Archer");
    }

    public Archer() {
        super();
        setName("Archer");

    }

    @Override
    public void startAttack(Player self) {

    }

    @Override
    public void updateAttack(Player self) {

    }

    @Override
    public void setupAttackFrame(int totalFrame) {

    }

    @Override
    public boolean isAttackFinished() {
        return false;
    }
}
