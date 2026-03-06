package application;

import component.scene1.CharacterSelectScene;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX application entry point for CroissantFighter.
 * <p>
 * Responsible for bootstrapping the first scene and showing the primary stage.
 * </p>
 */
public class Main extends Application {

    /**
     * JavaFX lifecycle method invoked after application launch.
     * Initializes the character selection root and registers it with {@link SceneHandler}.
     *
     * @param primaryStage primary window provided by JavaFX runtime
     * @throws Exception if startup initialization fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        CharacterSelectScene characterSelectScene =
                new CharacterSelectScene();

        SceneHandler.init(primaryStage, characterSelectScene);

        primaryStage.setTitle("CroissantFighter");
        primaryStage.show();
    }

    /**
     * Program entry point.
     *
     * @param args command-line arguments passed to JavaFX launcher
     */
    public static void main(String[] args) {
        launch(args);
    }

}
