package application;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneHandler {
    private static Stage stage;
    private static Scene scene;

    // create scene once
    public static void init(Stage primaryStage, Parent firstRoot) {
        stage = primaryStage;
        scene = new Scene(firstRoot, 1000, 600);
        stage.setScene(scene);
    }

    // swap screen
    public static void switchRoot(Parent newRoot) {
        scene.setRoot(newRoot);
    }

    public static Scene getScene() {
        return scene;
    }
}
