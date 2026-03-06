package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.interfaces.AttackAnimation;

/**
 * Katana melee character with fast two-frame slash animation.
 */
public class Katana extends MeleeClass {


    /**
     * Creates Katana with custom stats.
     *
     * @param hp HP value
     * @param atk attack value
     * @param def defense value
     * @param attackRange range value
     * @param attackSpeed attack speed
     */
    public Katana(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Katana");
    }

    /**
     * Creates Katana with default tuned stats and animation configuration.
     */
    public Katana() {
        super();
        setName("Katana");
        setTotalFrames(2);
        setAtk(50);
        setAttackSpeed(1.2F);
        setFRAME_DURATION(100_000_000);// nano-sec
        setupAttackFrame(getTotalFrames());
    }

    /**
     * Returns Katana melee hitbox size.
     *
     * @return attack data
     */
    @Override
    public AttackData getAttackData() {
        return new AttackData(60, 100);
    }

    /**
     * Loads Katana attack animation frames.
     *
     * @param totalFrame total frame count to load
     */
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

    /**
     * Updates animation and triggers hit state on strike frame.
     *
     * @param self owning player
     */
    @Override
    public void updateAttack(Player self) {
        super.updateAttack(self);
        if(frameIndex == 1 && getAttackState() == AttackState.Attacking ){
            setAttackState(AttackState.WillAttack);
        }
    }
}
