package component.scene1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Top status bar used in scene 1 to display each player's current selection state.
 */
public class PlayerSelection extends HBox {

    /** Default text shown while player 1 has not locked a character. */
    private static final String PLAYER_1_SELECTING_TEXT = "Player 1 : Selecting";
    /** Default text shown while player 2 has not locked a character. */
    private static final String PLAYER_2_SELECTING_TEXT = "Player 2 : Selecting";

    /** Text node displaying player 1 selection status. */
    Text player1 = new Text("Player 1 : Selecting ");
    /** Text node displaying player 2 selection status. */
    Text player2 = new Text("Player 2 : Selecting ");

    /**
     * Creates the player selection status bar and configures layout/styling.
     */
    public PlayerSelection(){
        setPrefHeight(50);
        draw(Color.MOCCASIN);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10, 30, 10, 30));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        player1 = new Text();
        player1.setText(PLAYER_1_SELECTING_TEXT);
        player1.setFont(Font.font(35));

        player2 = new Text();
        player2.setText(PLAYER_2_SELECTING_TEXT);
        player2.setFont(Font.font(35));

        getChildren().addAll(
                player1,
                spacer,
                player2
        );
    }

    /**
     * Applies a solid background color to the status bar.
     *
     * @param backgroundColor color to apply
     */
    private void draw(Color backgroundColor) {
        BackgroundFill bgFill = new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};

        this.setBackground(new Background(bgFillA));
    }

    /**
     * Sets player 1 selected name in the status text.
     *
     * @param name selected character name
     */
    public void setPlayer1(String name){
        player1.setText("Player 1 : " + name);
    }

    /**
     * Sets player 2 selected name in the status text.
     *
     * @param name selected character name
     */
    public void setPlayer2(String name){
        player2.setText("Player 2 : " + name);
    }

    /**
     * Resets player 1 status text to default selecting message.
     */
    public void clearPlayer1() {
        player1.setText(PLAYER_1_SELECTING_TEXT);
    }

    /**
     * Resets player 2 status text to default selecting message.
     */
    public void clearPlayer2() {
        player2.setText(PLAYER_2_SELECTING_TEXT);
    }
}
