package logic.entity.characters.hybridCharacters;

import logic.entity.characterClass.HybridClass;
import logic.gameLogic.Player;

public class Vampire extends HybridClass {
    public Vampire(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Vampire");
    }

    public Vampire(){
        super();
        setName("Vampire");
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
