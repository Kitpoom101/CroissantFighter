package logic.entity.characters.rangedCharacters;

import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;

public class Bubble extends RangedClass {
    public Bubble(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Bubble");
    }

    public Bubble() {
        super();
        setName("Bubble");
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
