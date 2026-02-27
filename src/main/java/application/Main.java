package application;

import component.CharacterSelectPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        root.setPrefHeight(600);
        root.setPrefWidth(1000);

        CharacterSelectPane characterSelectPane = new CharacterSelectPane();

        root.getChildren().addAll(
                characterSelectPane
        );

        SceneHandler.init(primaryStage, root);
        primaryStage.setTitle("CroissantFighter");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
