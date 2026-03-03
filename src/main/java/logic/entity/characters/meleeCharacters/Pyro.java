package logic.entity.characters.meleeCharacters;

import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;
import logic.gameLogic.Player;

public class Pyro extends MeleeClass {
    public Pyro(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Pyro");
    }

    public Pyro() {
        super();
        setName("Pyro");
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
