package logic.gameLogic;

import logic.entity.Character;

/**
 * Shared static holder for both players' selected characters between scenes.
 */
public class Selection {
    /** Selected character for player 1. */
    public static Character Player_1_Character;
    /** Selected character for player 2. */
    public static Character Player_2_Character;

    /**
     * Returns player 1 selected character.
     *
     * @return player 1 character
     */
    public static Character getPlayer_1_Character() {
        return Player_1_Character;
    }

    /**
     * Sets player 1 selected character.
     *
     * @param player_1_Character selected character
     */
    public static void setPlayer_1_Character(Character player_1_Character) {
        Player_1_Character = player_1_Character;
    }

    /**
     * Returns player 2 selected character.
     *
     * @return player 2 character
     */
    public static Character getPlayer_2_Character() {
        return Player_2_Character;
    }

    /**
     * Sets player 2 selected character.
     *
     * @param player_2_Character selected character
     */
    public static void setPlayer_2_Character(Character player_2_Character) {
        Player_2_Character = player_2_Character;
    }
}
