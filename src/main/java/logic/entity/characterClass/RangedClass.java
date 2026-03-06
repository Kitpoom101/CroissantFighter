package logic.entity.characterClass;

import logic.entity.AttackData;
import logic.entity.Character;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.Attackable;
import logic.interfaces.UsesAmmo;

/**
 * Base class for ranged characters with ammo and reload mechanics.
 * <p>
 * Subclasses implement {@link #spawnRangedAttack(float, float, boolean, Player)}
 * to create projectile behavior while this class manages attack gating and ammo.
 * </p>
 */
public abstract class RangedClass extends Character
        implements Attackable, UsesAmmo {

    /** Maximum ammo capacity. */
    private int maxAmmo;
    /** Current available ammo. */
    private int currentAmmo;

    /** Reload state flag. */
    private boolean isReloading = false;
    /** Timestamp when reload started. */
    private long reloadStartTime = 0;
    /** Reload duration in nanoseconds. */
    private long reloadDuration = 1_500_000_000L; // 1.5 sec

    /** Timestamp of last successful shot. */
    private long lastShotTime = 0;
    /** Minimum delay between shots in nanoseconds. */
    private long shootDelay = 500_000_000L; // 0.5 sec

    /**
     * Creates ranged class with default ranged stats and ammo setup.
     */
    public RangedClass() {
        super(125, 25, 3, 3, 2.0F, 100);

        setOrigin(getAttackRange());

        this.maxAmmo = 3;
        this.currentAmmo = maxAmmo;
    }

    /**
     * Creates ranged class with custom combat stats and ammo capacity.
     *
     * @param hp HP value
     * @param atk attack value
     * @param def defense value
     * @param attackRange range value
     * @param attackSpeed attack speed
     * @param maxAmmo max ammo capacity
     */
    public RangedClass(int hp, int atk, int def,
                       int attackRange, float attackSpeed,
                       int maxAmmo) {
        super(hp, atk, def, attackRange, attackSpeed, 100);
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
        setBuff(10);
    }

    /**
     * Template attack entry that enforces ammo/cooldown rules before spawning projectile.
     *
     * @param self owning player
     * @param startX spawn X
     * @param startY spawn Y
     * @param facingRight facing direction
     */
    public final void tryAttack(Player self,
                                float startX,
                                float startY,
                                boolean facingRight) {

        updateAmmo();

        if (!canShoot()) return;

        consumeAmmo();
        startAttack(self);

        spawnRangedAttack(startX, startY, facingRight, self);
    }

    /**
     * Subclass hook to spawn concrete ranged projectile attack.
     *
     * @param startX spawn X
     * @param startY spawn Y
     * @param facingRight facing direction
     * @param p owning player
     */
    protected abstract void spawnRangedAttack(
            float startX,
            float startY,
            boolean facingRight,
            Player p
    );

    /**
     * Initializes ranged attack animation state.
     *
     * @param self owning player
     */
    @Override
    public void startAttack(Player self) {
        frameIndex = 0;
        finished = false;
        lastFrameTime = System.nanoTime();

        setAttackState(AttackState.Attacking);
    }

    /**
     * Updates ranged attack animation and resets state after frame duration.
     *
     * @param self owning player
     */
    @Override
    public void updateAttack(Player self) {

        long now = System.nanoTime();

        if (now - lastFrameTime >= FRAME_DURATION) {

            getWeaponSprite().setVisible(false);

            // 🔥 Reset state ONLY after finishing
            setAttackState(AttackState.NotAttacking);

            // return player to walk state
            self.setState(logic.gameLogic.PlayerState.WALK);
        }
    }

    /**
     * Indicates whether ranged attack animation has finished.
     *
     * @return finished state
     */
    public boolean isAttackFinished() {
        return finished;
    }

    /**
     * Returns whether a shot can currently be fired.
     *
     * @return {@code true} if not reloading, has ammo, and shoot delay elapsed
     */
    public boolean canShoot() {
        long now = System.nanoTime();
        return !isReloading &&
                currentAmmo > 0 &&
                (now - lastShotTime >= shootDelay);
    }

    /**
     * Consumes one ammo and starts shoot cooldown. Triggers reload when empty.
     */
    public void consumeAmmo() {
        currentAmmo--;
        lastShotTime = System.nanoTime();

        if (currentAmmo <= 0)
            reload();
    }

    /**
     * Starts reload process if not already reloading.
     */
    public void reload() {
        if (!isReloading) {
            isReloading = true;
            reloadStartTime = System.nanoTime();
        }
    }

    /**
     * Updates reload timer and restores ammo when reload duration is complete.
     */
    public void updateAmmo() {
        long now = System.nanoTime();

        if (isReloading &&
                now - reloadStartTime >= reloadDuration) {

            currentAmmo = maxAmmo;
            isReloading = false;
        }
    }

    /**
     * Returns current ammo.
     *
     * @return current ammo count
     */
    public int getCurrentAmmo() { return currentAmmo; }
    /**
     * Returns maximum ammo.
     *
     * @return max ammo count
     */
    public int getMaxAmmo() { return maxAmmo; }
    /**
     * Returns reload state.
     *
     * @return {@code true} when reloading
     */
    public boolean isReloading() { return isReloading; }

    /**
     * Sets current ammo.
     *
     * @param currentAmmo ammo count
     */
    public void setCurrentAmmo(int currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    /**
     * Sets maximum ammo capacity.
     *
     * @param maxAmmo max ammo count
     */
    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }
}
