package logic.entity.characters.rangedCharacters;

import component.scene2.Scene2;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;
import logic.gameLogic.TarotType;
import logic.interfaces.SpawnAttack;

public class Mage extends RangedClass implements SpawnAttack {
    public Mage(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Mage");
    }

    public Mage() {
        super();
        setName("Mage");
        setAttackSpeed(0.5f);
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player p) {

        float dirX = facingRight ? 1 : -1;
        // 🎴 Pick random tarot card
        TarotType card = TarotType.values()[(int)(Math.random() * TarotType.values().length)];

        int damage = this.atk;
        switch (card) {

            case FOOL:
                if (Math.random() < 0.5) {
                    damage *= 1.5;   // big hit
                } else {
                    damage = 0;    // complete miss
                }
                break;

            case MAGICIAN:
                damage *= 1.1;
                break;

            case DEATH:
                damage *= 1.5;
                int sacrifice = (int) (this.getHp() * 0.25F);
                this.takeDamage(sacrifice);

                if (sacrifice > 0) {
                    Scene2.getInstance().showFloatingText(p, sacrifice, Color.DARKRED, "-");
                }

                break;

            case EMPRESS:
                int healAmount = (int) (this.getMaxHp() * 0.2f);
                int actualHeal = Math.min(healAmount, this.getMaxHp() - this.getHp());
                this.setHp(this.getHp() + actualHeal);

                if (actualHeal > 0) {
                    Scene2.getInstance().showFloatingText(
                            p,
                            actualHeal,
                            Color.LIMEGREEN,
                            "+"
                    );
                }

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
                        0,
                        p,
                        new Image(
                                getClass().getResource("/animations/mage/" + card.name().toLowerCase() + ".png")
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
}
