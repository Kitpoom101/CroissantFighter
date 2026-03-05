package PlayerTest;

import logic.entity.Character;
import logic.entity.characters.meleeCharacters.Hammer;
import logic.entity.characters.meleeCharacters.Katana;
import logic.gameLogic.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterCreationTest {
    Character katana;
    Player player;

    @BeforeEach
    protected void setupPlayer(){
        katana = new Katana();
    }

    @Test
    void testPlayerConstructor(){
        assertEquals("Katana", katana.getName());
        assertEquals(200, katana.getHp());
        assertEquals(50, katana.getAtk());
        assertEquals(7, katana.getDef());
    }
}

