package logic.entity.characters.hybridCharacters;

import javafx.scene.image.Image;
import logic.audio.AudioManager;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.HybridClass;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.SpawnAttack;
import logic.interfaces.UsesAmmo;

/**
 * Exorcist hybrid character using projectile bullets with ammo and reload mechanics.
 */
public class Exorcist extends HybridClass implements SpawnAttack, UsesAmmo {

    /** Maximum bullet count per magazine. */
    private final int MAX_AMMO = 3;
    /** Current remaining bullets. */
    private int currentAmmo = MAX_AMMO;

    /** Reload state flag. */
    private boolean isReloading = false;
    /** Timestamp when reload began. */
    private long reloadStartTime = 0;
    /** Reload duration in nanoseconds. */
    private final long RELOAD_DURATION = 1_500_000_000L; // นาโนวินาที

    /** Timestamp of last shot. */
    private long lastShotTime = 0;
    /** Minimum delay between shots in nanoseconds. */
    private final long SHOOT_DELAY = 500_000_000L; // นาโนวินาที

    /**
     * Creates Exorcist with custom stats.
     *
     * @param hp HP value
     * @param atk attack value
     * @param def defense value
     * @param attackRange range value
     * @param attackSpeed attack speed
     */
    public Exorcist(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Exorcist");
        //init();
    }

    /**
     * Creates Exorcist with default hybrid stats.
     */
    public Exorcist() {
        super();
        setName("Exorcist");
        //init();

    }

    /**
     * Optional initializer for weapon sprite defaults.
     */
    private void init() {
        setName("Exorcist");
        setAtk(30);

        try {
            getWeaponSprite().setImage(new Image(getClass().getResource("/Missing.png").toExternalForm()));
        } catch (Exception e) {}

        getWeaponSprite().setVisible(false);
    }

    /**
     * Fires a bullet projectile if shooting constraints pass.
     *
     * @param startX spawn X
     * @param startY spawn Y
     * @param facingRight facing direction
     * @param p owning player
     */
    @Override
    public void attack(float startX, float startY, boolean facingRight, Player p) {

        if (!canShoot()) return;
        consumeAmmo();

        float dirX = facingRight ? 1 : -1;

        Image bulletImage = new Image(
                getClass().getResource("/animations/exorcist/bullet.png")
                        .toExternalForm()
        );

        BaseProjectileAttack bullet = new BaseProjectileAttack(
                this.atk,
                3f,
                this.attackRange * 150,
                startX,
                startY,
                dirX,
                0,
                p,
                bulletImage,
                1f
        );

        AudioManager.playSFX("/audio/sfx/attack/pew.wav");
        spawnProjectile(bullet);
    }

    /**
     * Checks whether Exorcist can shoot now.
     *
     * @return {@code true} if not reloading, has ammo, and shoot delay elapsed
     */
    public boolean canShoot() {
        long now = System.nanoTime();
        return !isReloading &&
                currentAmmo > 0 &&
                (now - lastShotTime >= SHOOT_DELAY);
    }

    /**
     * Consumes one ammo and updates shot timestamp.
     */
    public void consumeAmmo() {
        currentAmmo--;
        lastShotTime = System.nanoTime();
    }


    /**
     * Starts reload when magazine is not full.
     */
    public void reload() {
        if (!isReloading && currentAmmo < MAX_AMMO) {
            isReloading = true;
            reloadStartTime = System.nanoTime();
        }
    }

    /**
     * Completes reload when duration has elapsed and hides temporary weapon sprite.
     */
    @Override
    public void updateAmmo() {
        long now = System.nanoTime();

        if (isReloading && now - reloadStartTime >= RELOAD_DURATION) {
            currentAmmo = MAX_AMMO;
            isReloading = false;
        }

        if (getWeaponSprite().isVisible() &&
                now - lastShotTime > 200_000_000L) {

            getWeaponSprite().setVisible(false);
        }
    }


    /**
     * Returns current ammo.
     *
     * @return current ammo count
     */
    public int getCurrentAmmo() { return currentAmmo; }
    /**
     * Returns max ammo capacity.
     *
     * @return max ammo count
     */
    public int getMaxAmmo() { return MAX_AMMO; }
    /**
     * Returns reload state.
     *
     * @return {@code true} when reloading
     */
    public boolean isReloading() { return isReloading; }

    /**
     * Exorcist does not use frame-based start animation here.
     *
     * @param self owning player
     */
    @Override
    public void startAttack(Player self) { }

    /**
     * Immediately returns player to walk state.
     *
     * @param self owning player
     */
    @Override
    public void updateAttack(Player self) {
        self.setState(PlayerState.WALK);
    }

    /**
     * Not used for Exorcist projectile attacks.
     *
     * @param totalFrame requested frame count
     */
    @Override
    public void setupAttackFrame(int totalFrame) { }

    /**
     * Exorcist projectile attacks are treated as instant-complete.
     *
     * @return always {@code true}
     */
    @Override
    public boolean isAttackFinished() { return true; }




}
