package component;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Objects;

public class CharacterPane extends StackPane {

    private final String[] allSprite = {"/KatanaManEx.png"};
    private final String[] names = {"KatanaManEx"};
    private String name;

    public CharacterPane(int charNum){
        setName(charNum);

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


    public String getName() {
        return name;
    }

    public void setName(int i) {
        this.name = i < names.length ? names[i] : "Missing";
    }
}
