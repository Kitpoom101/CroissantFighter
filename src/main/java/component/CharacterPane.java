package component;

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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterPane extends StackPane {

    private final String[] allSprite = {"/Katana.png"};
    private final String[] names = {"Katana"};
    private Character character;
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

    private String name;

    public CharacterPane(int charNum){
        setName(charNum);
        setCharacter(charNum);

        setPrefSize(100, 100);
        draw(Color.GREEN);

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

    private void draw(Color backgroundColor) {
        BackgroundFill bgFill = new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};

        this.setBackground(new Background(bgFillA));
    }

    private Image spriteLoader(int i){
        String path = i < allSprite.length
                ? allSprite[i] : "/Missing.png";

        return new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(path)
                )
        );
    }

    public void setSelected(boolean value){
        if(value){
            setStyle("-fx-border-color: gold; -fx-border-width: 3;");
        } else {
            setStyle("");
        }
    }

    // setter & getter

    public void setCharacter(int i){
        this.character = characterList.get(i);
    }

    public Character getCharacter(){
        return character;
    }

    public String getName() {
        return name;
    }

    public void setName(int i) {
        this.name = characterList.get(i).getName();
    }
}
