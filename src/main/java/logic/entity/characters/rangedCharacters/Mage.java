package logic.entity.characters.rangedCharacters;

import component.scene2.Scene2;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.entity.AttackData;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;
import logic.gameLogic.TarotType;
import logic.interfaces.SpawnAttack;

public class Mage extends RangedClass implements SpawnAttack {
    public Mage() {
        super(100, 30, 2, 4, 0.5f, 5);
        setName("Mage");
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {

    }

    @Override
    public AttackData getAttackData() {
        return null;
    }

    @Override
    protected void spawnRangedAttack(
            float startX,
            float startY,
            boolean facingRight,
            Player p) {

        float dirX = facingRight ? 1 : -1;
        TarotType card =
                TarotType.values()[(int)
                        (Math.random() *
                                TarotType.values().length)];

        int damage = this.atk;

        switch (card) {
            case FOOL:
                damage = Math.random() < 0.5 ? (int)(damage * 1.5) : 0;
                break;
            case MAGICIAN:
                damage *= 1.1;
                break;
            case DEATH:
                damage *= 1.5;
                this.takeDamage((int)(this.getHp() * 0.25f));
                break;
            case EMPRESS:
                this.setHp(
                        Math.min(
                                getMaxHp(),
                                getHp() + (int)(getMaxHp() * 0.2f)
                        )
                );
                damage *= 0.8;
                break;
        }

        BaseProjectileAttack cardProjectile =
                new BaseProjectileAttack(
                        damage,
                        1.2f,
                        (int)(this.attackRange * 100),
                        startX + 100,
                        startY,
                        dirX,
                        -0.15f,
                        p,
                        new Image(
                                getClass()
                                        .getResource(
                                                "/animations/mage/"
                                                        + card.name().toLowerCase()
                                                        + ".png")
                                        .toExternalForm()
                        ),
                        1
                );

        spawnProjectile(cardProjectile);
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

    @Override
    public void resetBuff() {

    }
}
