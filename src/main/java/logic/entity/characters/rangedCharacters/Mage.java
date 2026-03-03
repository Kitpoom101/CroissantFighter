package logic.entity.characters.rangedCharacters;

import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;

public class Mage extends RangedClass {
    public Mage(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Mage");
    }

    public Mage() {
        super();
        setName("Mage");
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
