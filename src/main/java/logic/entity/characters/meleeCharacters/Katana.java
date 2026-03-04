package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;

public class Katana extends MeleeClass {

    public Katana() {
        super();
        setName("Katana");

        setHp(200);
        setAtk(35);              // เบากว่า Hammer
        setDef(8);

        setTotalFrames(2);
        setFRAME_DURATION(250_000_000L);   // เร็วกว่า
        setAttackCooldown(500_000_000L);   // คูลดาวน์สั้นกว่า

        setupAttackFrame(getTotalFrames());
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
}
