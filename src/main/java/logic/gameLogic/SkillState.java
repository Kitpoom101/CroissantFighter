package logic.gameLogic;

/**
 * Skill availability states for character special abilities.
 */
public enum SkillState {
    /** Skill is recharging and cannot be used. */
    CooldownSkill,
    /** Skill is ready to use. */
    CanUseSkill,
    /** Skill has just been activated. */
    UsedSkill
}
