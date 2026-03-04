package component;

import application.SceneHandler;
import component.scene2.Scene2;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import logic.entity.Character;
import logic.gameLogic.SelectState;

import logic.entity.characters.meleeCharacters.*;
import logic.entity.characters.hybridCharacters.*;
import logic.entity.characters.rangedCharacters.*;

import java.net.URL;
import static logic.gameLogic.Selection.setPlayer_1_Character;
import static logic.gameLogic.Selection.setPlayer_2_Character;

public class CharacterSelectScene extends StackPane {

    // The currently highlighted character tile in the row.
    private CharacterPane selectedPane;
    // Top banner that shows each player's selected character name/state.
    private final PlayerSelection playerSelection;
    // Large previews for player 1 and player 2.
    private final ImageView player1PreviewImage;
    private final ImageView player2PreviewImage;
    // Selection flow state: P1 picks -> P2 picks -> DONE.
    private SelectState selectState = SelectState.PLAYER1_SELECT;

    public CharacterSelectScene(){
        // Root vertical layout for all UI controls in this scene.
        VBox ui = new VBox();

        // Build reusable UI elements.
        playerSelection = new PlayerSelection();
        player1PreviewImage = createPreviewImageView();
        player2PreviewImage = createPreviewImageView();

        // Horizontal list of selectable character cards.
        HBox characterRow = new HBox(5);
        characterRow.setAlignment(Pos.CENTER);

        characterRow.setPrefWidth(600);
        characterRow.setAlignment(Pos.CENTER);
        characterRow.setSpacing(10);

        // Create 9 character cards and wire click-to-select behavior.
        for (int i = 0; i < 9; i++) {
            CharacterPane pane = new CharacterPane(i);
            pane.setOnMouseClicked(e ->
                selectCharacter(pane)
            );
            characterRow.getChildren().add(pane);
        }

        // Action buttons for confirming/canceling current selection step.
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(e -> confirmSelection());

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> cancelSelection());

        // Container for action buttons.
        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER);
        actionRow.getChildren().addAll(confirmBtn, cancelBtn);

        // Preview panes keep player images centered and responsive.
        StackPane player1PreviewPane = new StackPane(player1PreviewImage);
        player1PreviewPane.setAlignment(Pos.CENTER);
        player1PreviewPane.setMinWidth(0);
        HBox.setHgrow(player1PreviewPane, Priority.ALWAYS);

        StackPane player2PreviewPane = new StackPane(player2PreviewImage);
        player2PreviewPane.setAlignment(Pos.CENTER);
        player2PreviewPane.setMinWidth(0);
        HBox.setHgrow(player2PreviewPane, Priority.ALWAYS);

        // Row that displays both player preview panes.
        HBox previewRow = new HBox(20);
        previewRow.setAlignment(Pos.CENTER);
        previewRow.setFillHeight(true);
        previewRow.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(previewRow, Priority.ALWAYS);
        previewRow.getChildren().addAll(player1PreviewPane, player2PreviewPane);


        // General spacing/sizing for the page layout.
        ui.setSpacing(14);
        ui.setAlignment(Pos.TOP_CENTER);
        ui.setPadding(new Insets(12, 16, 12, 16));
        ui.setFillWidth(true);
        ui.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        playerSelection.setMaxWidth(Double.MAX_VALUE);

        ui.getChildren().addAll(
                playerSelection,
                characterRow,
                actionRow,
                previewRow
        );


        // Attach UI to this scene root and paint background image.
        getChildren().add(ui);

        setImageBackground();
    }

    // Applies a full-scene background image with "cover" behavior.
    private void setImageBackground(){
        Image image = new Image(getClass().getResource("/CroissantShop.png").toExternalForm());
        BackgroundSize size = new BackgroundSize(
                100, 100,
                true, true,
                false, true   // COVER = true
        );

        BackgroundImage bg = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                size
        );

        this.setBackground(new Background(bg));
    }

    // Keeps exactly one card selected at a time.
    private void selectCharacter(CharacterPane pane){

        if(selectedPane != null){
            selectedPane.setSelected(false);
        }

        selectedPane = pane;
        selectedPane.setSelected(true);
    }

    // Handles the confirm button according to the current select state.
    private void confirmSelection(){

        // When both players are done, move to gameplay scene.
        if (selectState == SelectState.DONE) {
            SceneHandler.switchRoot(new Scene2());
        }

        // Ignore confirm if no character is currently selected.
        if(selectedPane == null) return;

        // Build chosen character instance from selected card name.
        String chosenName = selectedPane.getName();
        Character chosenChar = createNewCharacter(selectedPane.getName());

        // First confirm locks Player 1 and advances to Player 2.
        if (selectState == SelectState.PLAYER1_SELECT) {
            playerSelection.setPlayer1(chosenName);
            player1PreviewImage.setImage(loadCharacterImage(chosenName));
            System.out.println("Player 1 locked in: " + chosenName);
            setPlayer_1_Character(chosenChar);
            selectState = SelectState.PLAYER2_SELECT;
        }
        // Second confirm locks Player 2 and marks setup complete.
        else if (selectState == SelectState.PLAYER2_SELECT) {
            playerSelection.setPlayer2(chosenName);
            player2PreviewImage.setImage(loadCharacterImage(chosenName));
            System.out.println("Player 2 locked in: " + chosenName);
            setPlayer_2_Character(chosenChar);
            selectState = SelectState.DONE;
        }

        // Clear current highlight after lock-in.
        selectedPane.setSelected(false);
        selectedPane = null;
    }

    // Handles cancel button: rollback one selection step.
    private void cancelSelection() {
        // Remove current temporary highlight first.
        if (selectedPane != null) {
            selectedPane.setSelected(false);
            selectedPane = null;
        }

        // If P1 already locked and we are on P2 step, undo P1.
        if (selectState == SelectState.PLAYER2_SELECT) {
            playerSelection.clearPlayer1();
            player1PreviewImage.setImage(null);
            selectState = SelectState.PLAYER1_SELECT;
            System.out.println("Player 1 selection canceled");
        // If both were locked, undo P2 and return to P2 step.
        } else if (selectState == SelectState.DONE) {
            playerSelection.clearPlayer2();
            player2PreviewImage.setImage(null);
            selectState = SelectState.PLAYER2_SELECT;
            System.out.println("Player 2 selection canceled");
        }
    }

    // Creates a standardized preview image holder for both players.
    private ImageView createPreviewImageView() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(230);
        imageView.setFitHeight(230);
        return imageView;
    }

    // Loads a character portrait by name, with fallback when missing.
    private Image loadCharacterImage(String characterName) {
        URL imageUrl = getClass().getResource("/" + characterName + ".png");
        if (imageUrl == null) {
            imageUrl = getClass().getResource("/Missing.png");
        }

        return new Image(imageUrl.toExternalForm());
    }

    // Factory mapping from selected UI name to concrete Character subclass.
    // Returned as base type so game logic can use polymorphism.
    private Character createNewCharacter(String name) {
        switch (name) {
            case "Katana": return new Katana();
            case "Hammer": return new Hammer();
            case "Pyro": return new Pyro();
            case "Barista": return new Barista();
            case "Exorcist": return new Exorcist();
            case "Vampire": return new Vampire();
            case "Archer": return new Archer();
            case "Bubble": return new Bubble();
            case "Mage": return new Mage();
            default: return new Katana();
        }
    }
}
