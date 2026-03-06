package logic.gameLogic;


/**
 * Tarot card variants used by Mage projectile random effect system.
 */
public enum TarotType {
    /** Unstable card: high damage chance or complete miss. */
    FOOL,
    /** Balanced offensive card with moderate damage boost. */
    MAGICIAN,
    /** Utility card with self-heal and lower outgoing damage. */
    EMPRESS,
    /** High-risk high-reward card with heavy damage and self-damage. */
    DEATH
}
