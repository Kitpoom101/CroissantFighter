package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import logic.entity.AttackData;
import logic.entity.characterClass.MeleeClass;
import logic.gameLogic.AttackState;
import logic.gameLogic.Player;
import logic.interfaces.AttackAnimation;

public class Katana extends MeleeClass implements AttackAnimation {


    public Katana(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Katana");
    }

    public Katana() {
        super();
        setName("Katana");
        setTotalFrames(2);
        setAtk(50);
        setAttackSpeed(2.0F);
        setFRAME_DURATION(100_000_000);// nano-sec
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

    @Override
    public void updateAttack(Player self) {
        super.updateAttack(self);
        if(frameIndex == 1 && getAttackState() == AttackState.AllowAttack ){
            setAttackState(AttackState.WillAttack);
        }
    }
}
