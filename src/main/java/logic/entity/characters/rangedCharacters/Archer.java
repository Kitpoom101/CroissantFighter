package logic.entity.characters.rangedCharacters;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.audio.AudioManager;
import logic.entity.AttackData;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.RangedClass;
import logic.gameLogic.Player;
import logic.interfaces.HandleOwnWeapon;
import logic.interfaces.HaveWeapon;
import logic.interfaces.SpawnAttack;

public class Archer extends RangedClass implements SpawnAttack, HandleOwnWeapon {
    protected int originAttackRange;
    public Archer() {
        super(125, 35, 3, 3, 0.75f, 3);
        setName("Archer");
        setWeaponSprite("/animations/archer/attack/Bow.png");

        setOrigin(getAtk());
        setOriginAttackRange(getAttackRange());
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
        float randomY = (float)((Math.random() - 0.5) * 0.6);

        float spawnOffset = dirX > 0 ? 60 * dirX : 155 * dirX;

        BaseProjectileAttack arrow =
                new BaseProjectileAttack(
                        this.atk,
                        1.5f,
                        (int)(this.attackRange * 100),
                        startX + spawnOffset,
                        startY,
                        dirX,
                        randomY,
                        p,
                        new Image(
                                getClass()
                                        .getResource("/animations/archer/attack/arrow.png")
                                        .toExternalForm()
                        ),
                        3
                );

        AudioManager.playSFX("/audio/sfx/attack/arrowShoot.mp3");
        spawnProjectile(arrow);


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
        setAttackRange(getAttackRange() + getBuff());
        setAtk(getAtk() + getBuff());
    }

    @Override
    public void resetBuff(){
        setAttackRange(getOriginAttackRange());
        setAtk(getOrigin());
    }

    public int getOriginAttackRange() {
        return originAttackRange;
    }
    public void setOriginAttackRange(int originAttackRange) {
        this.originAttackRange = originAttackRange;
    }
}
