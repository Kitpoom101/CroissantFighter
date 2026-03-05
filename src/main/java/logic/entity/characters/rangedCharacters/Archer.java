package logic.entity.characters.rangedCharacters;

import javafx.scene.image.Image;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;
import logic.interfaces.SpawnAttack;

public class Archer extends RangedClass implements SpawnAttack {
    public Archer(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Archer");
    }

    public Archer() {
        super();
        setName("Archer");
        setAttackSpeed(0.75f);
    }


    @Override
    public void attack(float startX, float startY, boolean facingRight, Player p) {

        float dirX = facingRight ? 1 : -1;


            float randomY = (float)((Math.random() - 0.5) * 0.6);

            BaseProjectileAttack arrow  =
                    new BaseProjectileAttack(
                            (int) (this.atk),
                            1.5f,
                            (int) (this.attackRange * 100),
                            startX + 100,
                            startY,
                            dirX,
                            randomY,
                            p,
                            new Image(
                                    getClass().getResource("/animations/archer/attack/arrow.png").toExternalForm()
                            ),
                            3
                    );

            spawnProjectile(arrow);

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
