package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;

/**
 * Hammer melee character with slower but larger hitbox attack.
 */
public class Hammer extends MeleeClass {
    /**
     * Creates Hammer with custom stats.
     *
     * @param hp HP value
     * @param atk attack value
     * @param def defense value
     * @param attackRange range value
     * @param attackSpeed attack speed
     */
    public Hammer(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Hammer");
    }

    /**
     * Creates Hammer with default tuned stats and animation setup.
     */
    public Hammer() {
        super();
        setName("Hammer");
        setAtk(40);
        setTotalFrames(2);
        setAttackSpeed(0.6F);
        setFRAME_DURATION(400_000_000);
        setupAttackFrame(getTotalFrames());
    }

    /**
     * Returns Hammer melee hitbox size.
     *
     * @return attack data
     */
    @Override
    public AttackData getAttackData() {
        return new AttackData(100, 120);
    }


    /**
     * Loads Hammer attack animation frames.
     *
     * @param totalFrame total frame count to load
     */
    @Override
    public void setupAttackFrame(int totalFrame) {
        attackFrames = new Image[totalFrame];

        for (int i = 0; i < totalFrame; i++) {
            String path =
                    "/animations/hammer/attack/frame" + (i + 1) + ".png";
            attackFrames[i] = new Image(
                    getClass().getResource(path).toExternalForm());
        }
    }

    /**
     * Updates animation and flags strike frame for hit processing.
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
