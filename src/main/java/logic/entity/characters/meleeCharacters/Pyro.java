package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;

public class Pyro extends MeleeClass {

    public Pyro() {
        super();

        setName("Pyro");

        setHp(180);
        setAtk(40);
        setDef(5);

        setTotalFrames(1);
        setFRAME_DURATION(200_000_000L);   // อย่าตั้งเร็วเกิน
        setAttackCooldown(900_000_000L);   // ใส่คูลดาวน์ด้วย

        setupAttackFrame(getTotalFrames());
    }

    @Override
    public AttackData getAttackData() {
        return new AttackData(120, 120);
    }

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
}
