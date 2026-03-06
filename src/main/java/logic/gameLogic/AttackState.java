package logic.gameLogic;

/**
 * Attack state machine values used by player and character attack logic.
 */
public enum AttackState {
    /** Character is idle and can start a new attack. */
    NotAttacking,
    /** Character is currently in attack animation or startup. */
    Attacking,
    /** Character reached strike frame and should apply hit logic. */
    WillAttack,
    /** Character is waiting for attack-speed cooldown before attacking again. */
    AttackCooldown
}
