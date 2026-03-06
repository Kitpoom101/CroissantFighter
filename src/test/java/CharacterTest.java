import logic.entity.Character;
import logic.entity.characters.meleeCharacters.Katana;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {
    Character katana;

    @BeforeAll
    static void initJFX() {
        javafx.application.Platform.startup(() -> {});
    }

    @Test
    void testConstructor(){
        katana = new Katana();
        assertEquals("Katana", katana.getName());
        assertEquals(200, katana.getHp());
        assertEquals(50, katana.getAtk());
        assertEquals(7, katana.getDef());
    }
}


