package logic.entity.characters.hybridCharacters;

import component.scene2.Scene2;
import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.HybridClass;
import logic.gameLogic.Player;
import logic.interfaces.SpawnAttack;

public class Barista extends HybridClass implements SpawnAttack {
    public Barista(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Barista");
    }

    public Barista(){
        super();
        setName("Barista");
    }


    @Override
    public void attack(float startX, float startY, boolean facingRight, Player p) {

        float dirX = facingRight ? 1 : -1;
        float speed = 5.5f + (float)Math.random();
        int damage = this.getAtk();

        if (Math.random() < 0.25) { // 25% crit chance
            damage *= 2; // 200% damage
        }

        BaseProjectileAttack coffeeShot =
                new BaseProjectileAttack(
                        damage,
                        speed,
                        this.attackRange * 200,
                        startX,
                        startY,
                        dirX,
                        -0.15f, // slight arc
                        p,
                        new Image(
                                getClass().getResource(
                                        "/animations/barista/attack/coffee.png"
                                ).toExternalForm()
                        ),
                        1.2f // slightly bigger
                );
        coffeeShot.setHasKnockback(true);

        spawnProjectile(coffeeShot);
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

    /*@Override
    public AttackData getAttackData() {
        return new AttackData(20, 20);
    }*/


}
