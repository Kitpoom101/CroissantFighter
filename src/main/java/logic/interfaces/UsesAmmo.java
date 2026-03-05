package logic.interfaces;

public interface UsesAmmo {
    void reload();
    void updateAmmo();
    boolean canShoot();
    void consumeAmmo();
    int getCurrentAmmo();
    int getMaxAmmo();
    boolean isReloading();
}