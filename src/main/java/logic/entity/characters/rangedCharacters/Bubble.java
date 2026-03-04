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
import logic.interfaces.SpawnAttack;

public class Bubble extends RangedClass implements SpawnAttack {

    private long blowerStartTime;
    private static final long BLOWER_DURATION = 1_000_000_000L;

    public Bubble(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Bubble");
    }

    public Bubble() {
        super();
        setName("Bubble");

        setTotalFrames(1);
        setFRAME_DURATION(300_000_000); // 0.3 sec
        setupAttackFrame(getTotalFrames());

        getWeaponSprite().setImage(
                new Image(
                        getClass()
                                .getResource("/animations/bubble/attack/atkprop/bubbleBlower.png")
                                .toExternalForm()
                )
        );

        getWeaponSprite().setVisible(false);
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player p) {

        float dirX = facingRight ? 1 : -1;

        for (float i = 1.0F; i > 0; i -= 0.1) {

            float randomY = (float)((Math.random() - 0.5) * 0.6);

            BaseProjectileAttack bubble =
                    new BaseProjectileAttack(
                            (int) (this.atk * 0.2 * i),
                            6f * i * 0.1f,
                            (int) (this.attackRange * 100 * i),
                            startX + 100,
                            startY,
                            dirX,
                            randomY,
                            p,
                            new Image(
                                    getClass().getResource("/animations/bubble/attack/atkprop/emojiBubble.png").toExternalForm()
                            ),
                            i
                    );

            spawnProjectile(bubble);
        }
    }

    @Override
    public void startAttack(Player self) {

        ImageView blower = getWeaponSprite();

        blowerStartTime = System.nanoTime();

        // position in front
        float offsetX = self.isFacingRight() ? 40 : -40;

        blower.setLayoutX(self.getSprite().getLayoutX() + offsetX);
        blower.setLayoutY(self.getSprite().getLayoutY());

        blower.setScaleX(self.isFacingRight() ? 1 : -1); // flip if facing left

        blower.setVisible(true);

        getWeaponSprite().setFitWidth(40);
        getWeaponSprite().setFitHeight(40);
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
}
