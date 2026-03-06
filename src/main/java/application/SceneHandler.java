package application;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utility class that owns a single JavaFX {@link Scene} and handles root swapping.
 * <p>
 * Keeps scene initialization centralized so different screens can transition
 * without creating new stage/scene instances.
 * </p>
 * <p>
 * Usage pattern:
 * </p>
 * <ol>
 *   <li>Call {@link #init(Stage, Parent)} once during app startup.</li>
 *   <li>Switch views by calling {@link #switchRoot(Parent)} with new roots.</li>
 *   <li>Access the shared scene via {@link #getScene()} if needed.</li>
 * </ol>
 */
public class SceneHandler {
    /**
     * Default scene width in pixels.
     */
    private static final int DEFAULT_WIDTH = 1000;
    /**
     * Default scene height in pixels.
     */
    private static final int DEFAULT_HEIGHT = 600;

    /** Shared primary stage instance set during initialization. */
    private static Stage stage;
    /** Shared scene instance whose root is swapped during navigation. */
    private static Scene scene;

    /**
     * Prevents instantiation of this utility class.
     */
    private SceneHandler() {
        throw new UnsupportedOperationException("SceneHandler is a utility class");
    }

    /**
     * Initializes the scene graph once and binds it to the primary stage.
     * <p>
     * This method should be called exactly once at application startup before
     * any call to {@link #switchRoot(Parent)}.
     * </p>
     *
     * @param primaryStage JavaFX primary stage
     * @param firstRoot initial root node shown in the scene
     */
    public static void init(Stage primaryStage, Parent firstRoot) {
        stage = primaryStage;
        scene = new Scene(firstRoot, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        stage.setScene(scene);
    }

    /**
     * Replaces current scene root with a new root and forces immediate layout pass.
     * <p>
     * Calling {@code applyCss()} and {@code layout()} ensures the new root has
     * styles and layout computed before the next render pass.
     * </p>
     *
     * @param newRoot new root node to display
     */
    public static void switchRoot(Parent newRoot) {
        scene.setRoot(newRoot);

        newRoot.applyCss();
        newRoot.layout();
    }

    /**
     * Returns the shared scene instance.
     *
     * @return active scene
     */
    public static Scene getScene() {
        return scene;
    }
}
