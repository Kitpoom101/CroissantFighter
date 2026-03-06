package component.scene1;

import application.SceneHandler;
import component.scene2.Scene2;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import logic.audio.AudioManager;
import logic.entity.Character;
import logic.gameLogic.SelectState;

import logic.entity.characters.meleeCharacters.*;
import logic.entity.characters.hybridCharacters.*;
import logic.entity.characters.rangedCharacters.*;

import java.net.URL;
import static logic.gameLogic.Selection.setPlayer_1_Character;
import static logic.gameLogic.Selection.setPlayer_2_Character;

/**
 * Character selection scene shown before the match starts.
 * <p>
 * This screen lets both players:
 * </p>
 * <ul>
 *   <li>Browse character cards.</li>
 *   <li>Preview selected characters.</li>
 *   <li>Confirm or cancel picks in a two-step flow.</li>
 * </ul>
 */
public class CharacterSelectScene extends StackPane {

    /** Currently highlighted character tile, or {@code null} if none selected. */
    private CharacterPane selectedPane;
    /** Top status bar that displays current player selections. */
    private final PlayerSelection playerSelection;
    /** Large image preview for player 1 selection. */
    private final ImageView player1PreviewImage;
    /** Large image preview for player 2 selection. */
    private final ImageView player2PreviewImage;
    /** Selection state machine: player 1 selection, player 2 selection, then done. */
    private SelectState selectState = SelectState.PLAYER1_SELECT;

    /**
     * Builds the full character selection UI, binds input handlers, and starts scene BGM.
     */
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
            pane.setOnMouseClicked(e -> {
                        selectCharacter(pane);
                AudioManager.playSFX("/audio/sfx/onMouseClicked/chaSelect.mp3");
            });
            characterRow.getChildren().add(pane);
        }

        // Action buttons for confirming/canceling current selection step.
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setOnAction(e -> {
            AudioManager.playSFX("/audio/sfx/onMouseClicked/clickSFX.mp3");
            confirmSelection();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e ->  {
            AudioManager.playSFX("/audio/sfx/onMouseClicked/cancelClick.mp3");
            cancelSelection();
        });

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

        AudioManager.playBGM("/audio/bgm/bgmSelectionSceneBalladDuParis.mp3");
    }

    /**
     * Applies the background image for this screen using cover behavior.
     */
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

    /**
     * Updates highlighted character card so only one card is selected at a time.
     *
     * @param pane character card clicked by the user
     */
    private void selectCharacter(CharacterPane pane){

        if(selectedPane != null){
            selectedPane.setSelected(false);
        }

        selectedPane = pane;
        selectedPane.setSelected(true);
    }

    /**
     * Processes current selection and advances the two-player selection flow.
     * <p>
     * Once both players are confirmed, transitions to {@link Scene2}.
     * </p>
     */
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

    /**
     * Cancels current step and rolls selection flow back by one stage.
     */
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

    /**
     * Creates a standardized preview image node for player preview panels.
     *
     * @return configured preview {@link ImageView}
     */
    private ImageView createPreviewImageView() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(230);
        imageView.setFitHeight(230);
        return imageView;
    }

    /**
     * Loads the portrait image for a character.
     *
     * @param characterName character identifier used in resource naming
     * @return character portrait image, or fallback image when missing
     */
    private Image loadCharacterImage(String characterName) {
        URL imageUrl = getClass().getResource("/" + characterName + ".png");
        if (imageUrl == null) {
            imageUrl = getClass().getResource("/Missing.png");
        }

        return new Image(imageUrl.toExternalForm());
    }

    /**
     * Creates a new character instance from selected character name.
     * <p>
     * Returned as base type {@link Character} to keep downstream logic polymorphic.
     * </p>
     *
     * @param name selected character name
     * @return newly created concrete character object
     */
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
