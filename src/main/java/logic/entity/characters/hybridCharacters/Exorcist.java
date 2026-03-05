package logic.entity.characters.hybridCharacters;

import javafx.scene.image.Image;
import logic.entity.BaseProjectileAttack;
import logic.entity.characterClass.HybridClass;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.HaveWeapon;
import logic.interfaces.SpawnAttack;

public class Exorcist extends HybridClass implements SpawnAttack {

    // ระบบกระสุน
    private final int MAX_AMMO = 3;
    private int currentAmmo = MAX_AMMO;

    // ระบบรีโหลด (1.5 วินาที)
    private boolean isReloading = false;
    private long reloadStartTime = 0;
    private final long RELOAD_DURATION = 1_500_000_000L; // นาโนวินาที

    // ระบบดีเลย์การยิง (0.5 วินาที)
    private long lastShotTime = 0;
    private final long SHOOT_DELAY = 500_000_000L; // นาโนวินาที

    public Exorcist(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        init();
    }

    public Exorcist() {
        super();
        init();
    }

    private void init() {
        setName("Exorcist");
        setAtk(30);

        try {
            getWeaponSprite().setImage(new Image(getClass().getResource("/Missing.png").toExternalForm()));
        } catch (Exception e) {}

        getWeaponSprite().setVisible(false);
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player p) {
        long now = System.nanoTime();

        if (isReloading || currentAmmo <= 0 || (now - lastShotTime < SHOOT_DELAY)) {
            return;
        }

        currentAmmo--;
        lastShotTime = now;

        float dirX = facingRight ? 1 : -1;

        Image bulletImage = new Image(getClass().getResource("/projectilebaseattack.png").toExternalForm());

        // 🔽 ปรับความเร็วกระสุน
        BaseProjectileAttack bullet = new BaseProjectileAttack(
                this.atk,
                2f,
                this.attackRange * 150,
                startX,
                startY,
                dirX,
                0,
                p,
                bulletImage,
                0.5f
        );

        spawnProjectile(bullet);
        getWeaponSprite().setVisible(true);

        p.setState(PlayerState.WALK);

    }

    public void reload() {
        if (!isReloading && currentAmmo < MAX_AMMO) {
            isReloading = true;
            reloadStartTime = System.nanoTime();
        }
    }

    public void updateExorcistLogic() {
        long now = System.nanoTime();

        if (isReloading) {
            if (now - reloadStartTime >= RELOAD_DURATION) {
                currentAmmo = MAX_AMMO;
                isReloading = false;
            }
        }

        if (getWeaponSprite().isVisible() && (now - lastShotTime > 200_000_000L)) {
            getWeaponSprite().setVisible(false);
        }
    }

    // 🔽 เพิ่ม Getter เพื่อเอาข้อมูลไปแสดงบน UI
    public int getCurrentAmmo() { return currentAmmo; }
    public int getMaxAmmo() { return MAX_AMMO; }
    public boolean isReloading() { return isReloading; }

    @Override
    public void startAttack(Player self) { }

    @Override
    public void updateAttack(Player self) {
        self.setState(PlayerState.WALK);
    }

    @Override
    public void setupAttackFrame(int totalFrame) { }

    @Override
    public boolean isAttackFinished() { return true; }



}