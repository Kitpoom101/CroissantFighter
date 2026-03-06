package logic.interfaces;

import logic.gameLogic.Player;

/**
 * Contract for characters that support frame-based attack animations.
 * <p>
 * Implementations are expected to initialize animation state when an attack starts,
 * update animation frames each tick, and report completion status.
 * </p>
 */
public interface AttackAnimation {

    /**
     * Initializes attack animation state for a new attack cycle.
     *
     * @param self owning player instance
     */
    void startAttack(Player self);
    /**
     * Updates animation progression during game loop ticks.
     *
     * @param self owning player instance
     */
    void updateAttack(Player self);
    /**
     * Prepares attack frame resources.
     *
     * @param totalFrame number of frames to configure
     */
    void setupAttackFrame(int totalFrame);
    /**
     * Reports whether current attack animation has completed.
     *
     * @return {@code true} when animation is finished
     */
    boolean isAttackFinished();
    /**
     * Sets frame duration in nanoseconds.
     *
     * @param FRAME_DURATION frame duration value
     */
    void setFRAME_DURATION(long FRAME_DURATION);
    /**
     * Sets total frame count used by animation.
     *
     * @param totalFrames frame count
     */
    void setTotalFrames(int totalFrames);
}
