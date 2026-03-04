package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;

public class Hammer extends MeleeClass {

    public Hammer() {
        super();
        setName("Hammer");

        setHp(250);
        setAtk(100);
        setDef(10);

        setTotalFrames(2);
        setFRAME_DURATION(400_000_000L);
        setAttackCooldown(2_300_000_000L); // ต้องมี L

        setupAttackFrame(getTotalFrames());
    }


    @Override
    public AttackData getAttackData() {
        return new AttackData(100, 120);
    }

    @Override
    public void setupAttackFrame(int totalFrame) {

        attackFrames = new Image[totalFrame];

        for (int i = 0; i < totalFrame; i++) {
            String path =
                    "/animations/hammer/attack/frame" + (i + 1) + ".gif";

            attackFrames[i] = new Image(
                    getClass().getResource(path).toExternalForm());
        }
    }
}
