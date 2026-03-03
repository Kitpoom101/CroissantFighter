package logic.interfaces;

import logic.gameLogic.Player;

public interface AttackAnimation {

    void startAttack(Player self);
    void updateAttack(Player self);
    void setupAttackFrame(int totalFrame);
}
