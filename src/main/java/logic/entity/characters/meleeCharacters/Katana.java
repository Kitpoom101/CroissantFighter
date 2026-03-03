package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;
import logic.gameLogic.Player;
import logic.gameLogic.PlayerState;
import logic.interfaces.AttackAnimation;

import java.util.Objects;

public class Katana extends MeleeClass implements AttackAnimation {

    private Image[] attackFrames;
    private final int totalFrames = 2;

    private int frameIndex = 0;
    private int frameTimer = 0;

    private long lastFrameTime;
    private final long FRAME_DURATION = 320_000_000;

    private final int FRAME_DELAY = 5; // speed
    private boolean finished = false;

    public Katana(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Katana");
    }

    public Katana() {
        super();
        setName("Katana");
        setupAttackFrame(totalFrames);
    }

    @Override
    public AttackData getAttackData() {
        return new AttackData(60, 100);
    }

    @Override
    public void setupAttackFrame(int totalFrame) {
        attackFrames = new Image[totalFrame];

        for (int i = 0; i < totalFrame; i++) {
            String path =
                    "/animations/katana/attack/frame" + (i + 1) + ".png";
            attackFrames[i] = new Image(
                    getClass().getResource(path).toExternalForm());
        }
    }

    @Override
    public void startAttack(Player self) {
        frameIndex = 0;
        finished = false;
        lastFrameTime = System.nanoTime();

        self.getWeaponSprite().setImage(attackFrames[0]);
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
