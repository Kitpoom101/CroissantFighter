package component.scene2;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static logic.gameLogic.Selection.getPlayer_1_Character;
import static logic.gameLogic.Selection.getPlayer_2_Character;

public class Scene2 extends VBox {

    public Scene2(){
        super();
        draw(Color.GREEN);
        System.out.println(getPlayer_1_Character());
        System.out.println(getPlayer_2_Character());
    }

    private void draw(Color backgroundColor) {
        BackgroundFill bgFill = new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};

        this.setBackground(new Background(bgFillA));
    }
}
