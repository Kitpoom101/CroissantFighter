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
import logic.entity.characters.rangedCharacters.Mage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterAttackTest {

    Character katana;
    Character hammer;
    Character pyro;
    Character barista;
    Character exorcist;
    Character vampire;
    Character archer;
    Character bubble;
    Character mage;

    Character character;

    @BeforeAll
    static void initJFX() {
        javafx.application.Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        character = new Katana();

        katana = new Katana();
        hammer = new Hammer();
        pyro = new Pyro();
        barista = new Barista();
        exorcist = new Exorcist();
        vampire = new Vampire();
        archer = new Archer();
        bubble = new Bubble();
        mage = new Mage();
    }

    @Test
    void hammer_takeDamage_shouldReduceHpByDamageMinusDefense() {
        // Katana default defense = 7
        hammer.takeDamage(20); // effective damage = 13
        assertEquals(187, hammer.getHp());
    }

    @Test
    void hammer_takeDamage_shouldAlwaysDealAtLeastOneDamage() {
        // damage < defense still deals minimum 1
        hammer.takeDamage(3);
        assertEquals(199, hammer.getHp());
    }

    @Test
    void hammer_takeDamage_shouldNotDropHpBelowZero() {
        hammer.takeDamage(10_000);
        assertEquals(0, hammer.getHp());
    }

    @Test
    void pyro_takeDamage_shouldReduceHpByDamageMinusDefense() {
        // Katana default defense = 7
        pyro.takeDamage(20); // effective damage = 13
        assertEquals(187, pyro.getHp());
    }

    @Test
    void pyro_takeDamage_shouldAlwaysDealAtLeastOneDamage() {
        // damage < defense still deals minimum 1
        pyro.takeDamage(3);
        assertEquals(199, pyro.getHp());
    }

    @Test
    void pyro_takeDamage_shouldNotDropHpBelowZero() {
        pyro.takeDamage(10_000);
        assertEquals(0, pyro.getHp());
    }

    @Test
    void barista_takeDamage_shouldReduceHpByDamageMinusDefense() {
        barista.takeDamage(20);
        assertEquals(135, barista.getHp());
    }

    @Test
    void barista_takeDamage_shouldAlwaysDealAtLeastOneDamage() {
        // damage < defense still deals minimum 1
        barista.takeDamage(3);
        assertEquals(149, barista.getHp());
    }

    @Test
    void barista_takeDamage_shouldNotDropHpBelowZero() {
        barista.takeDamage(10_000);
        assertEquals(0, barista.getHp());
    }

    @Test
    void exorcist_takeDamage_shouldReduceHpByDamageMinusDefense() {
        // exorcist default defense = 5
        exorcist.takeDamage(20); // effective damage = 15
        assertEquals(135, exorcist.getHp());
    }

    @Test
    void exorcist_takeDamage_shouldAlwaysDealAtLeastOneDamage() {
        // damage < defense still deals minimum 1
        exorcist.takeDamage(3);
        assertEquals(149, exorcist.getHp());
    }

    @Test
    void exorcist_takeDamage_shouldNotDropHpBelowZero() {
        exorcist.takeDamage(10_000);
        assertEquals(0, exorcist.getHp());
    }

    @Test
    void vampire_takeDamage_shouldReduceHpByDamageMinusDefense() {
        // vampire default defense = 5
        vampire.takeDamage(20); // effective damage = 15
        assertEquals(135, vampire.getHp());
    }

    @Test
    void vampire_takeDamage_shouldAlwaysDealAtLeastOneDamage() {
        // damage < defense still deals minimum 1
        vampire.takeDamage(3);
        assertEquals(149, vampire.getHp());
    }

    @Test
    void vampire_takeDamage_shouldNotDropHpBelowZero() {
        vampire.takeDamage(10_000);
        assertEquals(0, vampire.getHp());
    }

    @Test
    void archer_takeDamage_shouldReduceHpByDamageMinusDefense() {
        // archer default defense = 3
        archer.takeDamage(20); // effective damage = 17
        assertEquals(108, archer.getHp());
    }

    @Test
    void archer_takeDamage_shouldAlwaysDealAtLeastOneDamage() {
        // damage < defense still deals minimum 1
        archer.takeDamage(3);
        assertEquals(124, archer.getHp());
    }

    @Test
    void archer_takeDamage_shouldNotDropHpBelowZero() {
        archer.takeDamage(10_000);
        assertEquals(0, archer.getHp());
    }

    @Test
    void bubble_takeDamage_shouldReduceHpByDamageMinusDefense() {
        // bubble default defense = 4
        bubble.takeDamage(20); // effective damage = 16
        assertEquals(109, bubble.getHp());
    }

    @Test
    void bubble_takeDamage_shouldAlwaysDealAtLeastOneDamage() {
        // damage < defense still deals minimum 1
        bubble.takeDamage(3);
        assertEquals(124, bubble.getHp());
    }

    @Test
    void bubble_takeDamage_shouldNotDropHpBelowZero() {
        bubble.takeDamage(10_000);
        assertEquals(0, bubble.getHp());
    }

    @Test
    void mage_takeDamage_shouldReduceHpByDamageMinusDefense() {
        // mage default defense = 2
        mage.takeDamage(20); // effective damage = 13
        assertEquals(107, mage.getHp());
    }

    @Test
    void mage_takeDamage_shouldAlwaysDealAtLeastOneDamage() {
        // damage < defense still deals minimum 1
        mage.takeDamage(3);
        assertEquals(124, mage.getHp());
    }

    @Test
    void mage_takeDamage_shouldNotDropHpBelowZero() {
        mage.takeDamage(10_000);
        assertEquals(0, mage.getHp());
    }

    @Test
    void katana_takeDamage_shouldReduceHpByDamageMinusDefense() {
        // Katana default defense = 7
        katana.takeDamage(20); // effective damage = 13
        assertEquals(187, katana.getHp());
    }

    @Test
    void katana_takeDamage_shouldAlwaysDealAtLeastOneDamage() {
        // damage < defense still deals minimum 1
        katana.takeDamage(3);
        assertEquals(199, katana.getHp());
    }

    @Test
    void katana_takeDamage_shouldNotDropHpBelowZero() {
        katana.takeDamage(10_000);
        assertEquals(0, katana.getHp());
    }
}
