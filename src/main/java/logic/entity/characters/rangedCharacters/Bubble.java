package logic.entity.characters.rangedCharacters;

import component.scene2.Scene2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.entity.AttackData;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.HybridClass;
import logic.entity.characterClass.RangedClass;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.interfaces.HandleOwnWeapon;
import logic.interfaces.HaveWeapon;
import logic.interfaces.SpawnAttack;

public class Bubble extends RangedClass implements SpawnAttack, HandleOwnWeapon {

    private static final int MIN_AMMO = 3;
    private static final int MAX_AMMO = 8;

    private long blowerStartTime;
    private static final long BLOWER_DURATION = 1_000_000_000L;

    public Bubble() {
        super(150, 20, 4, 3, 0.5f, 2);
        setName("Bubble");
        setWeaponSprite("/animations/bubble/attack/atkprop/bubbleBlower.png");
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

        float spawnOffset = 100 * dirX;

        for (float i = 1.0F; i > 0; i -= 0.1) {

            float randomY =
                    (float)((Math.random() - 0.5) * 0.6);

            BaseProjectileAttack bubble =
                    new BaseProjectileAttack(
                            (int)(this.atk * i * 0.67),
                            6f * i * 0.1f,
                            (int)(this.attackRange * 100 * i),
                            startX + spawnOffset,
                            startY,
                            dirX,
                            randomY,
                            p,
                            new Image(
                                    getClass()
                                            .getResource(
                                                    "/animations/bubble/attack/atkprop/emojiBubble.png")
                                            .toExternalForm()
                            ),
                            i
                    );

            spawnProjectile(bubble);
        }
    }

    @Override
    public void startAttack(Player self) {

        setAttackState(AttackState.WillAttack);

        ImageView blower = getWeaponSprite();

        blowerStartTime = System.nanoTime();
    }

    @Override
    public void updateAttack(Player self) {

        if (!getWeaponSprite().isVisible()) return;

        long now = System.nanoTime();

        if (now - blowerStartTime >= BLOWER_DURATION) {

            getWeaponSprite().setVisible(false);

            // 🔥 Reset state ONLY after finishing
            setAttackState(AttackState.NotAttacking);

            // return player to walk state
            self.setState(logic.gameLogic.PlayerState.WALK);
        }
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
