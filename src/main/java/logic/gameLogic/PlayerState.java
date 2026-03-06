package logic.gameLogic;

/**
 * High-level player state used by movement/combat animation logic.
 */
public enum PlayerState {
    /** Default locomotion state. */
    WALK,
    /** Active attack state. */
    ATTACK,
    /** Temporary hit-reaction state. */
    HIT,
    /** Defeated/dead state. */
    DEAD,
    /** Active skill usage state. */
    SKILL
}
