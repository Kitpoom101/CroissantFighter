package logic.entity.characterClass;

import logic.entity.AttackData;
import logic.entity.Character;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.Attackable;

public abstract class MeleeClass extends Character implements Attackable {

    protected long FRAME_DURATION = 300_000_000; // 0.3 วิ
    protected long lastFrameTime;
    protected int frameIndex = 0;
    protected boolean finished = true;

    protected long attackCooldown = 600_000_000;
    protected long lastAttackTime = 0;

    public void setAttackCooldown(long cooldown) {
        this.attackCooldown = cooldown;
    }


    public MeleeClass() {
        super(250, 20, 10, 1, 1.0F);
    }

    public boolean canAttack() {
        return System.nanoTime() - lastAttackTime >= attackCooldown;
    }

    @Override
    public void startAttack(Player self) {

        if (self.getState() != PlayerState.WALK) return;
        if (!canAttack()) return;

        self.setState(PlayerState.ATTACK);

        setAttackState(AttackState.AllowAttack);

        lastAttackTime = System.nanoTime();

        frameIndex = 0;
        finished = false;
        lastFrameTime = System.nanoTime();

        if (attackFrames != null && attackFrames.length > 0) {
            self.getWeaponSprite().setImage(attackFrames[0]);
        }
    }

    @Override
    public void updateAttack(Player self) {

        if (self.getState() != PlayerState.ATTACK) return;

        long now = System.nanoTime();

        if (now - lastFrameTime >= FRAME_DURATION) {

            lastFrameTime = now;
            frameIndex++;

            // 🔥 เฟรมที่ 1 ปล่อยดาเมจ
            if (frameIndex == 1 && getAttackState() == AttackState.AllowAttack) {
                setAttackState(AttackState.WillAttack);
            }

            if (frameIndex >= attackFrames.length) {

                finished = true;
                self.setState(PlayerState.WALK);

                setAttackState(AttackState.NotAttacking);
                return;
            }

            self.getWeaponSprite().setImage(attackFrames[frameIndex]);
        }
    }

    @Override
    public abstract AttackData getAttackData();

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {}

    @Override
    public void useSpecialSkill() {}

    public boolean isAttackFinished() {
        return finished;
    }
}
