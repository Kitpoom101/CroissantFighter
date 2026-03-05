package application;

import component.scene1.CharacterSelectScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CharacterSelectScene characterSelectScene =
                new CharacterSelectScene();

        SceneHandler.init(primaryStage, characterSelectScene);

        primaryStage.setTitle("CroissantFighter");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
