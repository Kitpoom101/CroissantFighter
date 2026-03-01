package logic.entity;

abstract class BaseProjectileAttack {
    protected int damage;
    protected float speed;
    protected int range;

    public abstract void move();
    public abstract void onHit(Character target);
}
