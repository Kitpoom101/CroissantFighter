package logic.entity.characters.rangedCharacters;

import component.scene2.Scene2;
import logic.entity.AttackData;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.HybridClass;
import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;
import logic.interfaces.SpawnAttack;

public class Bubble extends RangedClass implements SpawnAttack {
    public Bubble(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Bubble");
    }

    public Bubble() {
        super();
        setName("Bubble");
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player p) {

        float dirX = facingRight ? 1 : -1;

        BaseProjectileAttack bubble =
                new BaseProjectileAttack(
                        this.atk,
                        6f,
                        this.attackRange * 100,
                        startX,
                        startY,
                        dirX,
                        0,
                        p
                );

        spawnProjectile(bubble);
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
