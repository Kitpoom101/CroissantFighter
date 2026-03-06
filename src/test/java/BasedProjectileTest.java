import javafx.application.Platform;
import javafx.scene.image.Image;
import logic.entity.BaseProjectileAttack;
import logic.entity.characters.meleeCharacters.Katana;
import logic.entity.characters.rangedCharacters.Archer;
import logic.gameLogic.Player;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BasedProjectileTest {
    BaseProjectileAttack baseProjectileAttack;
    Player player1;
    Player player2;

    @BeforeAll
    static void initJFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUpBasedProjectile(){
        player1 = new Player(new Archer(), 1);
        player2 = new Player(new Katana(), 2);
        baseProjectileAttack = new BaseProjectileAttack(
                10, 3,
                3, 0, 0,
                0, 0, player2,
                new Image(getClass()
                .getResource("/animations/archer/attack/arrow.png")
                .toExternalForm()), 1);
    }

    @Test
    void testTakeDamageFromProjectile(){
        assertEquals(200, player2.getCharacter().getHp());
        int dmg = baseProjectileAttack.getDamage();
        player2.getCharacter().takeDamage(dmg);
        // katana def = 7
        // 10 - 7 = 3
        assertEquals(197, player2.getCharacter().getHp());
    }
}
