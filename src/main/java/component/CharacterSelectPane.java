package component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CharacterSelectPane extends VBox {
    private CharacterPane selectedPane;
    private final PlayerSelection playerSelection;
    private int player = 1;

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

        setSpacing(20);
        setAlignment(Pos.CENTER);

        getChildren().addAll(
                playerSelection,
                characterRow,
                confirmBtn
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

        if(selectedPane == null) return;

        String chosenName = selectedPane.getName();

        if (player == 1) {
            playerSelection.setPlayer1(chosenName);
            System.out.println("Player 1 locked in: " + chosenName);
        }
        if (player == 2) {
            playerSelection.setPlayer2(chosenName);
            System.out.println("Player 2 locked in: " + chosenName);
        }

        selectedPane.setSelected(false);
        selectedPane = null;

        player = (player == 1) ? 2 : 1;
    }

}
