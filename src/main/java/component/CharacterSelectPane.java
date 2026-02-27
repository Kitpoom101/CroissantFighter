package component;

import application.SceneHandler;
import component.scene2.Scene2;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.gameLogic.SelectState;

import static logic.gameLogic.Selection.setPlayer_1_Character;
import static logic.gameLogic.Selection.setPlayer_2_Character;

public class CharacterSelectPane extends VBox {
    private CharacterPane selectedPane;
    private final PlayerSelection playerSelection;
    private SelectState selectState = SelectState.PLAYER1_SELECT;

    public CharacterSelectPane(){
        playerSelection = new PlayerSelection();

        HBox characterRow = new HBox(5);
        characterRow.setAlignment(Pos.CENTER);

        characterRow.setPrefWidth(500);
        characterRow.setAlignment(Pos.CENTER);
        characterRow.setSpacing(5);

        for (int i = 0; i < 9; i++) {
            CharacterPane pane = new CharacterPane(i);
            pane.setOnMouseClicked(e ->
                selectCharacter(pane)
            );
            characterRow.getChildren().add(pane);
        }

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(e -> confirmSelection());

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> cancelSelection());

        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER);
        actionRow.getChildren().addAll(confirmBtn, cancelBtn);

        setSpacing(20);
        setAlignment(Pos.CENTER);

        getChildren().addAll(
                playerSelection,
                characterRow,
                actionRow
        );
    }

    private void selectCharacter(CharacterPane pane){

        if(selectedPane != null){
            selectedPane.setSelected(false);
        }

        selectedPane = pane;
        selectedPane.setSelected(true);
    }

    private void confirmSelection(){

        if (selectState == SelectState.DONE) {
            SceneHandler.switchRoot(new Scene2());
        }

        if(selectedPane == null) return;

        String chosenName = selectedPane.getName();

        if (selectState == SelectState.PLAYER1_SELECT) {
            playerSelection.setPlayer1(chosenName);
            System.out.println("Player 1 locked in: " + chosenName);
            setPlayer_1_Character(chosenName);
            selectState = SelectState.PLAYER2_SELECT;
        }
        else if (selectState == SelectState.PLAYER2_SELECT) {
            playerSelection.setPlayer2(chosenName);
            System.out.println("Player 2 locked in: " + chosenName);
            setPlayer_2_Character(chosenName);
            selectState = SelectState.DONE;
        }

        selectedPane.setSelected(false);
        selectedPane = null;
    }

    private void cancelSelection() {
        if (selectedPane != null) {
            selectedPane.setSelected(false);
            selectedPane = null;
        }

        if (selectState == SelectState.PLAYER2_SELECT) {
            playerSelection.clearPlayer1();
            selectState = SelectState.PLAYER1_SELECT;
            System.out.println("Player 1 selection canceled");
        } else if (selectState == SelectState.DONE) {
            playerSelection.clearPlayer2();
            selectState = SelectState.PLAYER2_SELECT;
            System.out.println("Player 2 selection canceled");
        }
    }
}
