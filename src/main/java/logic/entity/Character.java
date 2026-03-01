package logic.entity;

import javafx.scene.image.Image;
import logic.interfaces.Attackable;

import java.util.Objects;

public abstract class Character implements Attackable {

    private String name;
    private int hp;
    private int atk;
    private int def;
    private int attackRange;
    private float attackSpeed;

    public Character(int hp, int atk, int def, int attackRange, float attackSpeed) {
        this.setHp(hp);
        this.setAtk(atk);
        this.setDef(def);
        this.setAttackRange(attackRange);
        this.setAttackSpeed(attackSpeed);
    }

    public Character() {
        this.setHp(100);
        this.setAtk(15);
        this.setDef(5);
        this.setAttackRange(1);
        this.setAttackSpeed(1.0F);
    }


    public void moveLeft() {}
    public void moveRight() {}
    public void jump() {}

    public void attack() {};
    public void useSpecialSkill() {};

    public void takeDamage(int damage) {

        int finalDamage = Math.max(0, damage - def);

        hp -= finalDamage;

        if (hp < 0)
            hp = 0;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
