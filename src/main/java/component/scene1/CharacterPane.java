package component.scene1;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logic.entity.Character;
import logic.entity.characters.hybridCharacters.Barista;
import logic.entity.characters.hybridCharacters.Exorcist;
import logic.entity.characters.hybridCharacters.Vampire;
import logic.entity.characters.meleeCharacters.Hammer;
import logic.entity.characters.meleeCharacters.Katana;
import logic.entity.characters.meleeCharacters.Pyro;
import logic.entity.characters.rangedCharacters.Archer;
import logic.entity.characters.rangedCharacters.Bubble;
import logic.entity.characters.rangedCharacters.Mage;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Visual card for one selectable character in scene 1.
 * <p>
 * Each pane shows a portrait sprite, tracks selected state, and exposes
 * the chosen character metadata used by the selection scene.
 * </p>
 */
public class CharacterPane extends StackPane {

    /** Sprite resource paths in the same order as character list indices. */
    private final String[] allSprite = {
            "/Katana.png", "/Hammer.png", "/Pyro.png",
            "/Barista.png", "/Exorcist.png", "/Vampire.png",
            "/Archer.png", "/Bubble.png", "/Mage.png"
    };
    /** Reserved name list (currently unused). */
    private final String[] names = {"Katana"};
    /** Character model represented by this pane. */
    private Character character;
    /** Normal (unselected) pane background. */
    private final Image charBG = new Image(ClassLoader.getSystemResource("CharPaneBg.png").toString());
    /** Selected pane background. */
    private final Image charBGSelect = new Image(ClassLoader.getSystemResource("CharPaneSelect.png").toString());

    /** Available character instances indexed to UI card positions. */
    private ArrayList<Character> characterList = new ArrayList<>();
    {
        characterList.add(new Katana());
        characterList.add(new Hammer());
        characterList.add(new Pyro());
        characterList.add(new Barista());
        characterList.add(new Exorcist());
        characterList.add(new Vampire());
        characterList.add(new Archer());
        characterList.add(new Bubble());
        characterList.add(new Mage());
    }

    /** Display name of the represented character. */
    private String name;

    /**
     * Creates a character card for the character at the given index.
     *
     * @param charNum character index in the selection list
     */
    public CharacterPane(int charNum){
        setName(charNum);
        setCharacter(charNum);

        setPrefSize(100, 100);
        setImageBackground(charBG);

        ImageView view = new ImageView(spriteLoader(charNum));
        view.setFitWidth(80);   // smaller than 100
        view.setFitHeight(80);
        view.setPreserveRatio(true);

        getChildren().add(view);

        setOnMouseEntered(e -> {
            setOpacity(0.8);
        });
        setOnMouseExited(e -> {
            setOpacity(1.0);
        });

        System.out.println(getName());
    }

    /**
     * Paints this pane with a solid background color.
     *
     * @param backgroundColor color to apply
     */
    private void draw(Color backgroundColor) {
        BackgroundFill bgFill = new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};

        this.setBackground(new Background(bgFillA));
    }

    /**
     * Applies an image background sized to this pane's preferred size.
     *
     * @param image background image
     */
    private void setImageBackground(Image image){
        this.setBackground(new Background(new BackgroundImage(image, null ,null ,null, new BackgroundSize(this.getPrefWidth(), this.getPrefHeight(), false, false, false, false))));
    }

    /**
     * Loads portrait sprite by index with fallback support.
     *
     * @param i sprite index
     * @return loaded sprite image
     */
    private Image spriteLoader(int i){
        String path = i < allSprite.length
                ? allSprite[i] : "/Missing.png";

        return new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(path)
                )
        );
    }

    /**
     * Toggles selected visual state for this card.
     *
     * @param value {@code true} for selected style, {@code false} for normal style
     */
    public void setSelected(boolean value){
        if(value){
            setImageBackground(charBGSelect);
        } else {
            setImageBackground(charBG);
        }
    }

    // setter & getter

    /**
     * Assigns represented character by index.
     *
     * @param i character index
     */
    public void setCharacter(int i){
        this.character = characterList.get(i);
    }

    /**
     * Returns represented character model.
     *
     * @return character represented by this pane
     */
    public Character getCharacter(){
        return character;
    }

    /**
     * Returns represented character name.
     *
     * @return character name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets represented character name from character list index.
     *
     * @param i character index
     */
    public void setName(int i) {
        this.name = characterList.get(i).getName();
    }
}
