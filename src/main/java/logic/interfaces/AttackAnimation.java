package logic.interfaces;

import javafx.scene.image.ImageView;
import logic.gameLogic.Player;

public interface AttackAnimation {

    void startAttack(Player self);
    void updateAttack(Player self);
    void setupAttackFrame(int totalFrame);
    boolean isAttackFinished();
}
