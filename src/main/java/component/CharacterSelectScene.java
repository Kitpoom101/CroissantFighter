package component;

import application.SceneHandler;
import component.scene2.Scene2;
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
import java.util.List;

import static logic.gameLogic.Selection.setPlayer_1_Character;
import static logic.gameLogic.Selection.setPlayer_2_Character;

public class CharacterSelectScene extends VBox {
    private CharacterPane selectedPane;
    private final PlayerSelection playerSelection;
    private final ImageView player1PreviewImage;
    private final ImageView player2PreviewImage;
    private SelectState selectState = SelectState.PLAYER1_SELECT;

    private final Image scene1Bg = new Image(ClassLoader.getSystemResource("CroissantShop.png").toString());


    public CharacterSelectScene(){
        playerSelection = new PlayerSelection();
        player1PreviewImage = createPreviewImageView();
        player2PreviewImage = createPreviewImageView();

        HBox characterRow = new HBox(5);
        characterRow.setAlignment(Pos.CENTER);

        characterRow.setPrefWidth(600);
        characterRow.setAlignment(Pos.CENTER);
        characterRow.setSpacing(10);

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

        StackPane player1PreviewPane = new StackPane(player1PreviewImage);
        player1PreviewPane.setAlignment(Pos.CENTER);
        player1PreviewPane.setMinWidth(0);
        HBox.setHgrow(player1PreviewPane, Priority.ALWAYS);

        StackPane player2PreviewPane = new StackPane(player2PreviewImage);
        player2PreviewPane.setAlignment(Pos.CENTER);
        player2PreviewPane.setMinWidth(0);
        HBox.setHgrow(player2PreviewPane, Priority.ALWAYS);

        HBox previewRow = new HBox(20);
        previewRow.setAlignment(Pos.CENTER);
        previewRow.setFillHeight(true);
        previewRow.setPrefWidth(900);
        previewRow.getChildren().addAll(player1PreviewPane, player2PreviewPane);

        setSpacing(20);
        setAlignment(Pos.CENTER);

        getChildren().addAll(
                playerSelection,
                characterRow,
                actionRow,
                previewRow
        );

        setImageBackground(scene1Bg);
    }

    private void setImageBackground(Image image){
        this.setBackground(new Background(new BackgroundImage(image, null ,null ,null, new BackgroundSize(this.getPrefWidth(), this.getPrefHeight(), false, false, false, false))));
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
        Character chosenChar = createNewCharacter(selectedPane.getName());

        if (selectState == SelectState.PLAYER1_SELECT) {
            playerSelection.setPlayer1(chosenName);
            player1PreviewImage.setImage(loadCharacterImage(chosenName));
            System.out.println("Player 1 locked in: " + chosenName);
            setPlayer_1_Character(chosenChar);
            selectState = SelectState.PLAYER2_SELECT;
        }
        else if (selectState == SelectState.PLAYER2_SELECT) {
            playerSelection.setPlayer2(chosenName);
            player2PreviewImage.setImage(loadCharacterImage(chosenName));
            System.out.println("Player 2 locked in: " + chosenName);
            setPlayer_2_Character(chosenChar);
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
            player1PreviewImage.setImage(null);
            selectState = SelectState.PLAYER1_SELECT;
            System.out.println("Player 1 selection canceled");
        } else if (selectState == SelectState.DONE) {
            playerSelection.clearPlayer2();
            player2PreviewImage.setImage(null);
            selectState = SelectState.PLAYER2_SELECT;
            System.out.println("Player 2 selection canceled");
        }
    }

    private ImageView createPreviewImageView() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(320);
        imageView.setFitHeight(320);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private Image loadCharacterImage(String characterName) {
        URL imageUrl = getClass().getResource("/" + characterName + ".png");
        if (imageUrl == null) {
            imageUrl = getClass().getResource("/Missing.png");
        }

        return new Image(imageUrl.toExternalForm());
    }

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
