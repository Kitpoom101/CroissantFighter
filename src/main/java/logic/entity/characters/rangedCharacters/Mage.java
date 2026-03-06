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

/**
 * Mage ranged character that throws tarot-card projectiles with random effects.
 */
public class Mage extends RangedClass implements SpawnAttack, HandleOwnWeapon {
    /**
     * Creates Mage with tuned stats and staff sprite.
     */
    public Mage() {
        super(100, 30, 2, 4, 0.5f, 5);
        setName("Mage");
        setWeaponSprite("/animations/mage/staff.png");
    }

    /**
     * Not used directly; ranged attacks should be routed via {@code tryAttack}.
     *
     * @param startX spawn X
     * @param startY spawn Y
     * @param facingRight facing direction
     * @param player owning player
     */
    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {

    }

    /**
     * Mage uses projectile attack model, so melee attack data is unused.
     *
     * @return {@code null}
     */
    @Override
    public AttackData getAttackData() {
        return null;
    }

    /**
     * Spawns tarot projectile and applies card-specific effects before firing.
     *
     * @param startX spawn X
     * @param startY spawn Y
     * @param facingRight facing direction
     * @param p owning player
     */
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

    /**
     * Delegates start behavior to ranged base class.
     *
     * @param self owning player
     */
    @Override
    public void startAttack(Player self) {
        super.startAttack(self);
    }

    /**
     * Not used for Mage projectile attacks.
     *
     * @param totalFrame frame count
     */
    @Override
    public void setupAttackFrame(int totalFrame) {

    }

    /**
     * Completion is controlled by ranged base state.
     *
     * @return always {@code false}
     */
    @Override
    public boolean isAttackFinished() {
        return false;
    }

    /**
     * Mage skill heals by buff amount.
     */
    @Override
    public void useSpecialSkill() {
        setHp(getHp() + getBuff());
    }

    /**
     * Mage heal skill has no additional reset behavior.
     */
    @Override
    public void resetBuff(){

    }

    /**
     * Performs reload and plays Mage-specific reload SFX.
     */
    @Override
    public void reload() {
        super.reload();
        AudioManager.playSFX("/audio/sfx/attack/mage/cardReload.mp3");
    }
}
