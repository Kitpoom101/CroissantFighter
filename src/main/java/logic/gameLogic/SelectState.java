package logic.gameLogic;

/**
 * Selection flow states for character pick scene.
 */
public enum SelectState {
    /** Waiting for player 1 to pick a character. */
    PLAYER1_SELECT,
    /** Waiting for player 2 to pick a character. */
    PLAYER2_SELECT,
    /** Both players locked in; selection is complete. */
    DONE
}
