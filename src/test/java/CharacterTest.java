import logic.entity.Character;
import logic.entity.characters.hybridCharacters.Barista;
import logic.entity.characters.meleeCharacters.Hammer;
import logic.entity.characters.meleeCharacters.Katana;

import logic.entity.characters.meleeCharacters.Pyro;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {
    Character katana;
    Character character;

    @BeforeAll
    static void initJFX() {
        javafx.application.Platform.startup(() -> {});
    }

    @BeforeEach
    void setUpKatana() {
        katana = new Katana();
    }

    // ==== MELEE ==== //

    // KATANA

    @Test
    void testKatanaConstructor(){
        character = new Katana();
        assertEquals("Katana", character.getName());
        assertEquals(200, character.getHp());
        assertEquals(50, character.getAtk());
        assertEquals(7, character.getDef());
        assertEquals(1, character.getAttackRange());
        assertEquals(1.2F, character.getAttackSpeed());
        assertEquals(200, character.getMaxHp());
        assertEquals(60, character.getAttackData().getWidth());
        assertEquals(100, character.getAttackData().getHeight());
    }

    @Test
    void testKatanaSetMaxHp() {
        katana.setHp(300);
        assertEquals(200,katana.getHp());
    }

    @Test
    void testKatanaSkill() {
        assertEquals(7, katana.getDef());
        assertEquals(15, katana.getBuff());
        katana.useSpecialSkill();
        assertEquals(22, katana.getDef());
        katana.resetBuff();
        assertEquals(7, katana.getDef());
    }

    // KATANA

    // HAMMER

    @Test
    void testHammerConstructor(){
        character = new Hammer();
        assertEquals("Hammer", character.getName());
        assertEquals(200, character.getHp());
        assertEquals(200, character.getMaxHp());
        assertEquals(60, character.getAtk());
        assertEquals(7, character.getDef());
        assertEquals(1, character.getAttackRange());
        assertEquals(0.6F, character.getAttackSpeed());
        assertEquals(100, character.getAttackData().getWidth());
        assertEquals(120, character.getAttackData().getHeight());
    }

    @Test
    void testHammerSetMaxHp() {
        character = new Hammer();
        character.setHp(300);
        assertEquals(200,character.getHp());
    }

    @Test
    void testHammerSkill() {
        character = new Hammer();
        assertEquals(7, character.getDef());
        assertEquals(15, character.getBuff());
        character.useSpecialSkill();
        assertEquals(22, character.getDef());
        character.resetBuff();
        assertEquals(7, character.getDef());
    }

    // HAMMER

    // PYRO

    @Test
    void testPyroConstructor(){
        character = new Pyro();
        assertEquals("Pyro", character.getName());
        assertEquals(200, character.getHp());
        assertEquals(200, character.getMaxHp());
        assertEquals(20, character.getAtk());
        assertEquals(7, character.getDef());
        assertEquals(1, character.getAttackRange());
        assertEquals(2.0F, character.getAttackSpeed());
        assertEquals(150, character.getAttackData().getWidth());
        assertEquals(120, character.getAttackData().getHeight());
    }

    @Test
    void testPyroSetMaxHp() {
        character = new Pyro();
        character.setHp(300);
        assertEquals(200,character.getHp());
    }

    @Test
    void testPyroSkill() {
        character = new Pyro();
        assertEquals(7, character.getDef());
        assertEquals(15, character.getBuff());
        character.useSpecialSkill();
        assertEquals(22, character.getDef());
        character.resetBuff();
        assertEquals(7, character.getDef());
    }

    // PYRO

    // ==== MELEE ==== //
    // ==== HYBRID ==== //

    // BARISTA

    @Test
    void testBaristaConstructor(){
        character = new Barista();
        assertEquals("Barista", character.getName());
        assertEquals(150, character.getHp());
        assertEquals(150, character.getMaxHp());
        assertEquals(15, character.getAtk());
        assertEquals(5, character.getDef());
        assertEquals(2, character.getAttackRange());
        assertEquals(2.0F, character.getAttackSpeed());
        assertEquals(150, character.getAttackData().getWidth());
        assertEquals(120, character.getAttackData().getHeight());
    }

    @Test
    void testBaristaSetMaxHp() {
        character = new Barista();
        character.setHp(300);
        assertEquals(200,character.getHp());
    }

    @Test
    void testBaristaSkill() {
        character = new Barista();
        assertEquals(7, character.getDef());
        assertEquals(15, character.getBuff());
        character.useSpecialSkill();
        assertEquals(22, character.getDef());
        character.resetBuff();
        assertEquals(7, character.getDef());
    }

//    150, 15, 5, 2, 1.0F, 150
}


