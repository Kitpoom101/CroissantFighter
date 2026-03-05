package logic.entity.characterClass;

import logic.entity.AttackData;
import logic.entity.Character;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.Attackable;
import logic.interfaces.HaveWeapon;

public abstract class MeleeClass extends Character implements Attackable, HaveWeapon {

    public MeleeClass() {
        super(250, 20, 10, 1, 1.0F, 250);
        setBuff(15);
        setOrigin(getDef());
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {

    }


    public MeleeClass(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed, 250);

    }

    @Override
    public void useSpecialSkill() {
        setDef(getDef() + getBuff());
    }

    @Override
    public void resetBuff(){
        setDef(getOrigin());
    }

    @Override
    public AttackData getAttackData() {
        return null;
    }

    @Override
    public void startAttack(Player self) {
        frameIndex = 0;
        finished = false;
        lastFrameTime = System.nanoTime();
        if (attackFrames.length > 0) self.getWeaponSprite().setImage(attackFrames[0]);
    }

    @Override
    public void updateAttack(Player self) {

        long now = System.nanoTime();

        // change frame every few ticks
        if(now - lastFrameTime >= FRAME_DURATION){

            lastFrameTime = now;
            frameIndex++;

            if(frameIndex >= attackFrames.length){
                finished = true;
                self.setState(PlayerState.WALK);
                return;
            }

            self.getWeaponSprite()
                    .setImage(attackFrames[frameIndex]);
        }

        // ⭐ HIT FRAME (middle frame)
//        if(frameIndex == 1){
//            dealDamage(self, enemy);
//        }
    }

    public boolean isAttackFinished(){
        return this.finished;
    }
}