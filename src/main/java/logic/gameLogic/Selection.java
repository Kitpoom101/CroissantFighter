package logic.gameLogic;

import logic.entity.Character;

public class Selection {
    public static Character Player_1_Character;
    public static Character Player_2_Character;

    public static Character getPlayer_1_Character() {
        return Player_1_Character;
    }

    public static void setPlayer_1_Character(Character player_1_Character) {
        Player_1_Character = player_1_Character;
    }

    public static Character getPlayer_2_Character() {
        return Player_2_Character;
    }

    public static void setPlayer_2_Character(Character player_2_Character) {
        Player_2_Character = player_2_Character;
    }
}
