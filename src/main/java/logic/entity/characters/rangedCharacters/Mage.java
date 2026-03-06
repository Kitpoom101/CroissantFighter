package logic.entity.characters.rangedCharacters;

import component.scene2.Scene2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import logic.audio.AudioManager;
import logic.entity.AttackData;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;
import logic.gameLogic.TarotType;
import logic.interfaces.HandleOwnWeapon;
import logic.interfaces.HaveWeapon;
import logic.interfaces.SpawnAttack;

public class Mage extends RangedClass implements SpawnAttack, HandleOwnWeapon {
    public Mage() {
        super(125, 30, 2, 4, 0.5f, 5);
        setName("Mage");
        setWeaponSprite("/animations/mage/staff.png");
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

        float spawnOffset = dirX > 0 ? 60 * dirX : 145 * dirX;

        BaseProjectileAttack cardProjectile =
                new BaseProjectileAttack(
                        damage,
                        1.2f,
                        (int)(this.attackRange * 100),
                        startX + spawnOffset,
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
        AudioManager.playSFX("/audio/sfx/attack/mage/cardThrow.mp3");
    }

    @Override
    public void startAttack(Player self) {
        super.startAttack(self);
    }

    @Override
    public void setupAttackFrame(int totalFrame) {

    }

    @Override
    public boolean isAttackFinished() {
        return false;
    }

    @Override
    public void useSpecialSkill() {
        setHp(getHp() + getBuff());
    }

    @Override
    public void resetBuff(){

    }

    @Override
    public void reload() {
        super.reload();
        AudioManager.playSFX("/audio/sfx/attack/mage/cardReload.mp3");
    }
}
