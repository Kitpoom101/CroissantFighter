package logic.entity.characters.meleeCharacters;

import javafx.scene.image.Image;
import logic.entity.characterClass.MeleeClass;

import java.util.Objects;

public class Katana extends MeleeClass {
    public Katana(int hp, int atk, int def, int attackRange, float attackSpeed) {
        super(hp, atk, def, attackRange, attackSpeed);
        setName("Katana");
    }

    public Katana() {
        super();
        setName("Katana");

    }

    public Image loadSprite() {
        return new Image(
                Objects.requireNonNull(
                        getClass()
                                .getResourceAsStream("/Katana.png")
                )
        );
    }
}
