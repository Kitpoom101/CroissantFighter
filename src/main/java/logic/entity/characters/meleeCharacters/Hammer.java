package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;
import logic.gameLogic.Player;

public class Hammer extends MeleeClass {
    public Hammer(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Hammer");
    }

    public Hammer() {
        super();
        setName("Hammer");
        setTotalFrames(2);
        setFRAME_DURATION(400_000_000);
        setupAttackFrame(getTotalFrames());
    }

    @Override
    public AttackData getAttackData() {
        return new AttackData(90, 120);
    }


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
}
