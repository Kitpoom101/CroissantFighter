package logic.entity.characterClass;

import logic.entity.AttackData;
import logic.entity.Character;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.Attackable;

public abstract class HybridClass extends Character implements Attackable {

    public HybridClass() {
        super(150, 15, 5, 2, 1.0F);
        setBuff(10);
        setOrigin(getAtk());
    }

    @Override
    public void attack(float startX, float startY, boolean facingRight, Player player) {

    }


    public HybridClass(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);

    }


    @Override
    public void useSpecialSkill() {
        setAtk(getAtk() + getBuff());
    }

    @Override
    public void resetBuff(){
        setAtk(getOrigin());
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
