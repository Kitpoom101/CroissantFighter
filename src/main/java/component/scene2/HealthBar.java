package component.scene2;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar extends Pane {

    private static final double BAR_WIDTH = 400;
    private static final double BAR_HEIGHT = 50;

    private final Rectangle background;
    private final Rectangle foreground;
    private final int maxHp;

    public HealthBar(int maxHp) {
        this.maxHp = Math.max(maxHp, 1);

        setPrefSize(BAR_WIDTH, BAR_HEIGHT);

        background = new Rectangle(BAR_WIDTH, BAR_HEIGHT);
        background.setArcWidth(8);
        background.setArcHeight(8);
        background.setFill(Color.rgb(50, 50, 50));
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(2);

        foreground = new Rectangle(BAR_WIDTH, BAR_HEIGHT);
        foreground.setArcWidth(8);
        foreground.setArcHeight(8);
        foreground.setFill(Color.LIMEGREEN);

        getChildren().addAll(background, foreground);
    }

    public void setCurrentHp(int currentHp) {
        int clampedHp = Math.max(0, Math.min(currentHp, maxHp));
        double ratio = (double) clampedHp / maxHp;

        foreground.setWidth(BAR_WIDTH * ratio);
        foreground.setFill(Color.color(1.0 - ratio, ratio, 0));
    }
}
