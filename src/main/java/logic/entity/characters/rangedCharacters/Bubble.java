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

/**
 * Bubble ranged character that emits multiple bubble projectiles per shot.
 */
public class Bubble extends RangedClass implements SpawnAttack, HandleOwnWeapon {

    /** Minimum randomized ammo size after reload. */
    private static final int MIN_AMMO = 1;
    /** Maximum randomized ammo size after reload. */
    private static final int MAX_AMMO = 5;

    /** Timestamp when blower visual was shown. */
    private long blowerStartTime;
    /** Duration blower visual remains visible. */
    private static final long BLOWER_DURATION = 1_000_000_000L;

    /**
     * Creates Bubble with tuned stats and weapon sprite.
     */
    public Bubble() {
        super(150, 20, 4, 3, 0.5f, 1);
        setName("Bubble");
        setWeaponSprite("/animations/bubble/attack/atkprop/bubbleBlower.png");

        setOrigin(getAtk());
    }

    /**
     * Not used directly; ranged attacks are spawned via {@code tryAttack}.
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
     * Bubble uses projectile attack so hitbox attack data is unused.
     *
     * @return {@code null}
     */
    @Override
    public AttackData getAttackData() {
        return null;
    }

    /**
     * Spawns a burst of bubbles with varied scale/speed/range values.
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

    /**
     * Starts Bubble attack by setting strike state and blower timer.
     *
     * @param self owning player
     */
    @Override
    public void startAttack(Player self) {

        setAttackState(AttackState.WillAttack);

        ImageView blower = getWeaponSprite();

        blowerStartTime = System.nanoTime();
    }

    /**
     * Hides blower sprite after configured duration and resets attack state.
     *
     * @param self owning player
     */
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

    /**
     * Not used for Bubble projectile attacks.
     *
     * @param totalFrame frame count
     */
    @Override
    public void setupAttackFrame(int totalFrame) {

    }

    /**
     * Completion is controlled by state and visual timer.
     *
     * @return always {@code false}
     */
    @Override
    public boolean isAttackFinished() {
        return false;
    }

    /**
     * Bubble skill increases attack value.
     */
    @Override
    public void useSpecialSkill() {
        setAtk(getAtk() + getBuff());
    }

    /**
     * Restores attack to origin value.
     */
    @Override
    public void resetBuff() {
        setAtk(getOrigin());
    }

    /**
     * Reloads and randomizes ammo size between configured min/max bounds.
     */
    @Override
    public void reload() {
        super.reload();

        int randomMax = MIN_AMMO + (int)(Math.random() * (MAX_AMMO - MIN_AMMO + 1));

        setMaxAmmo(randomMax);
        setCurrentAmmo(randomMax);
    }
}
