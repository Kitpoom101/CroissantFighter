import logic.entity.Character;
import logic.entity.characterClass.RangedClass;
import logic.entity.characters.hybridCharacters.Barista;
import logic.entity.characters.hybridCharacters.Exorcist;
import logic.entity.characters.hybridCharacters.Vampire;
import logic.entity.characters.meleeCharacters.Hammer;
import logic.entity.characters.meleeCharacters.Katana;

import logic.entity.characters.meleeCharacters.Pyro;
import logic.entity.characters.rangedCharacters.Archer;
import logic.entity.characters.rangedCharacters.Bubble;
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
        assertEquals(200, character.getMaxHp());
        assertEquals(30, character.getAtk());
        assertEquals(7, character.getDef());
        assertEquals(1, character.getAttackRange());
        assertEquals(1.2F, character.getAttackSpeed());
        assertEquals(15, character.getBuff());
        assertEquals(character.getOrigin(), character.getDef());
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
        assertEquals(40, character.getAtk());
        assertEquals(7, character.getDef());
        assertEquals(1, character.getAttackRange());
        assertEquals(0.6F, character.getAttackSpeed());
        assertEquals(15, character.getBuff());
        assertEquals(character.getOrigin(), character.getDef());
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
        assertEquals(15, character.getBuff());
        assertEquals(character.getOrigin(), character.getDef());
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
        assertEquals(20, character.getAtk());
        assertEquals(5, character.getDef());
        assertEquals(2, character.getAttackRange());
        assertEquals(10, character.getBuff());
        assertEquals(character.getOrigin(), character.getAtk());
        assertEquals(1.0F, character.getAttackSpeed());
        assertEquals(null, character.getAttackData());
    }

    @Test
    void testBaristaSetMaxHp() {
        character = new Barista();
        character.setHp(300);
        assertEquals(150,character.getHp());
    }

    @Test
    void testBaristaSkill() {
        character = new Barista();
        assertEquals(20, character.getAtk());
        assertEquals(10, character.getBuff());
        character.useSpecialSkill();
        assertEquals(30, character.getAtk());
        character.resetBuff();
        assertEquals(20, character.getAtk());
    }

    // BARISTA

    // EXORCIST

    @Test
    void testExorcistConstructor(){
        character = new Exorcist();
        assertEquals("Exorcist", character.getName());
        assertEquals(150, character.getHp());
        assertEquals(150, character.getMaxHp());
        assertEquals(20, character.getAtk());
        assertEquals(5, character.getDef());
        assertEquals(2, character.getAttackRange());
        assertEquals(10, character.getBuff());
        assertEquals(character.getOrigin(), character.getAtk());
        assertEquals(1.0F, character.getAttackSpeed());
        assertEquals(null, character.getAttackData());
    }

    @Test
    void testExorcistSetMaxHp() {
        character = new Exorcist();
        character.setHp(300);
        assertEquals(150,character.getHp());
    }

    @Test
    void testExorcistSkill() {
        character = new Exorcist();
        assertEquals(20, character.getAtk());
        assertEquals(10, character.getBuff());
        character.useSpecialSkill();
        assertEquals(30, character.getAtk());
        character.resetBuff();
        assertEquals(20, character.getAtk());
    }

    // EXORCIST

    // VAMPIRE

    @Test
    void testVampireConstructor(){
        character = new Vampire();
        assertEquals("Vampire", character.getName());
        assertEquals(150, character.getHp());
        assertEquals(150, character.getMaxHp());
        assertEquals(20, character.getAtk());
        assertEquals(5, character.getDef());
        assertEquals(2, character.getAttackRange());
        assertEquals(10, character.getBuff());
        assertEquals(character.getOrigin(), character.getAtk());
        assertEquals(0.35, ((Vampire) character).getLifeStealMultiplier());
        assertEquals(0.35, ((Vampire) character).getOriginLifeStealMultiplier());
        assertEquals(2.0F, character.getAttackSpeed());
        assertEquals(200, character.getAttackData().getWidth());
        assertEquals(100, character.getAttackData().getHeight());
    }

    @Test
    void testVampireSetMaxHp() {
        character = new Vampire();
        character.setHp(300);
        assertEquals(150,character.getHp());
    }

    @Test
    void testVampireSkill() {
        character = new Vampire();
        assertEquals(20, character.getAtk());
        assertEquals(0.35, ((Vampire) character).getLifeStealMultiplier());
        assertEquals(10, character.getBuff());
        character.useSpecialSkill();
        assertEquals(30, character.getAtk());
        assertEquals(0.85, ((Vampire) character).getLifeStealMultiplier());
        character.resetBuff();
        assertEquals(20, character.getAtk());
        assertEquals(0.35, ((Vampire) character).getLifeStealMultiplier());
    }

    // VAMPIRE

    // ==== HYBRID ==== //
    // ==== RANGED ==== //

    // ARCHER

    @Test
    void testArcherConstructor(){
        character = new Archer();
        assertEquals("Archer", character.getName());
        assertEquals(125, character.getHp());
        assertEquals(125, character.getMaxHp());
        assertEquals(35, character.getAtk());
        assertEquals(3, character.getDef());
        assertEquals(3, character.getAttackRange());
        assertEquals(10, character.getBuff());
        assertEquals(character.getOrigin(), character.getAtk());
        assertEquals(((Archer) character).getOriginAttackRange(), character.getAttackRange());
        assertEquals(0.75F, character.getAttackSpeed());
        assertEquals(3, ((RangedClass) character).getMaxAmmo());
        assertEquals(null, character.getAttackData());
    }

    @Test
    void testArcherSetMaxHp() {
        character = new Archer();
        character.setHp(300);
        assertEquals(125,character.getHp());
    }

    @Test
    void testArcherSkill() {
        character = new Archer();
        assertEquals(35, character.getAtk());
        assertEquals(10, character.getBuff());
        assertEquals(3, character.getAttackRange());
        character.useSpecialSkill();
        assertEquals(45, character.getAtk());
        assertEquals(13, character.getAttackRange());
        character.resetBuff();
        assertEquals(35, character.getAtk());
        assertEquals(3, character.getAttackRange());
    }

    //ARCHER

    // BUBBLE

    @Test
    void testBubbleConstructor(){
        character = new Bubble();
        assertEquals("Bubble", character.getName());
        assertEquals(125, character.getHp());
        assertEquals(125, character.getMaxHp());
        assertEquals(20, character.getAtk());
        assertEquals(4, character.getDef());
        assertEquals(3, character.getAttackRange());
        assertEquals(10, character.getBuff());
        assertEquals(character.getOrigin(), character.getAtk());
        assertEquals(0.5F, character.getAttackSpeed());
        assertEquals(1, ((RangedClass) character).getMaxAmmo());
        assertEquals(null, character.getAttackData());
    }

    @Test
    void testBubbleSetMaxHp() {
        character = new Bubble();
        character.setHp(300);
        assertEquals(125,character.getHp());
    }

    @Test
    void testBubbleSkill() {
        character = new Bubble();
        assertEquals(35, character.getAtk());
        assertEquals(10, character.getBuff());
        assertEquals(3, character.getAttackRange());
        character.useSpecialSkill();
        assertEquals(45, character.getAtk());
        assertEquals(13, character.getAttackRange());
        character.resetBuff();
        assertEquals(35, character.getAtk());
        assertEquals(3, character.getAttackRange());
    }

    // BUBBLE

    // ==== RANGED ==== //

//    150, 15, 5, 2, 1.0F, 150
}


