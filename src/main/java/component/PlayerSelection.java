package component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PlayerSelection extends HBox {

    Text player1 = new Text("Player 1 : Selecting ");
    Text player2 = new Text("Player 2 : Selecting ");

    public PlayerSelection(){
        setPrefHeight(50);
        draw(Color.MOCCASIN);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10, 30, 10, 30));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        player1 = new Text();
        player1.setText("Player 1 : ");
        player1.setFont(Font.font(35));

        player2 = new Text();
        player2.setText("Player 2 : ");
        player2.setFont(Font.font(35));

        getChildren().addAll(
                player1,
                spacer,
                player2
        );
    }

    private void draw(Color backgroundColor) {
        BackgroundFill bgFill = new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};

        this.setBackground(new Background(bgFillA));
    }

    public void setPlayer1(String name){
        player1.setText("Player 1 : " + name);
    }

    public void setPlayer2(String name){
        player2.setText("Player 2 : " + name);
    }
}
