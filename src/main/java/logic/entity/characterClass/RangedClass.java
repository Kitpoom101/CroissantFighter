package logic.entity.characterClass;

import logic.entity.AttackData;
import logic.entity.Character;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.Attackable;
import logic.interfaces.UsesAmmo;

public abstract class RangedClass extends Character
        implements Attackable, UsesAmmo {

    // ===== Ammo System =====
    private int maxAmmo;
    private int currentAmmo;

    private boolean isReloading = false;
    private long reloadStartTime = 0;
    private long reloadDuration = 1_500_000_000L; // 1.5 sec

    private long lastShotTime = 0;
    private long shootDelay = 500_000_000L; // 0.5 sec

    // ===== Constructor =====
    public RangedClass() {
        super(125, 25, 3, 3, 2.0F, 125);

        setOrigin(getAttackRange());

        this.maxAmmo = 3;
        this.currentAmmo = maxAmmo;
    }

    public RangedClass(int hp, int atk, int def,
                       int attackRange, float attackSpeed,
                       int maxAmmo) {
        super(hp, atk, def, attackRange, attackSpeed, 125);
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
        setBuff(10);
    }

    // ===== Template Attack Entry =====
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

    protected abstract void spawnRangedAttack(
            float startX,
            float startY,
            boolean facingRight,
            Player p
    );

    // ===== Animation =====
    @Override
    public void startAttack(Player self) {
    }

    @Override
    public void updateAttack(Player self) {
    }

    public boolean isAttackFinished() {
        return finished;
    }

    // ===== Ammo Logic =====
    public boolean canShoot() {
        long now = System.nanoTime();
        return !isReloading &&
                currentAmmo > 0 &&
                (now - lastShotTime >= shootDelay);
    }

    public void consumeAmmo() {
        currentAmmo--;
        lastShotTime = System.nanoTime();

        if (currentAmmo <= 0)
            reload();
    }

    public void reload() {
        if (!isReloading) {
            isReloading = true;
            reloadStartTime = System.nanoTime();
        }
    }

    public void updateAmmo() {
        long now = System.nanoTime();

        if (isReloading &&
                now - reloadStartTime >= reloadDuration) {

            currentAmmo = maxAmmo;
            isReloading = false;
        }
    }

    // ===== Getters =====
    public int getCurrentAmmo() { return currentAmmo; }
    public int getMaxAmmo() { return maxAmmo; }
    public boolean isReloading() { return isReloading; }

    public void setCurrentAmmo(int currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }
}