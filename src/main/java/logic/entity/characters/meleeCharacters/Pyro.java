package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;

/**
 * Pyro melee character with very fast single-frame strike timing.
 */
public class Pyro extends MeleeClass {
    /**
     * Creates Pyro with custom stats.
     *
     * @param hp HP value
     * @param atk attack value
     * @param def defense value
     * @param attackRange range value
     * @param attackSpeed attack speed
     */
    public Pyro(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Pyro");
    }

    /**
     * Creates Pyro with default tuned stats and animation setup.
     */
    public Pyro() {
        super();
        setTotalFrames(1);
        setFRAME_DURATION(100_000_000);
        setName("Pyro");
        setAttackSpeed(2.0F);
        setupAttackFrame(getTotalFrames());
    }

    /**
     * Returns Pyro melee hitbox size.
     *
     * @return attack data
     */
    @Override
    public AttackData getAttackData() {
        return new AttackData(150, 120);
    }


    /**
     * Loads Pyro attack frame resources.
     *
     * @param totalFrame total frame count to load
     */
    @Override
    public void setupAttackFrame(int totalFrame) {
        attackFrames = new Image[totalFrame];

        for (int i = 0; i < totalFrame; i++) {
            String path =
                    "/animations/pyro/attack/frame" + (i + 1) + ".png";
            attackFrames[i] = new Image(
                    getClass().getResource(path).toExternalForm());
        }
    }

    /**
     * Updates animation and immediately flags strike frame.
     *
     * @param self owning player
     */
    @Override
    public void updateAttack(Player self) {
        super.updateAttack(self);
        if(frameIndex == 0 && getAttackState() == AttackState.Attacking ){
            setAttackState(AttackState.WillAttack);
        }
    }
}
