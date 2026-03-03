package logic.entity.characters.meleeCharacters;

import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;
import logic.gameLogic.Player;

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
