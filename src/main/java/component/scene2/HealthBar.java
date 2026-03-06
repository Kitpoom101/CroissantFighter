package component.scene2;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Displays a compact HealthBar
 */
public class HealthBar extends StackPane {
    private static final double BAR_WIDTH = 400;
    private static final double BAR_HEIGHT = 30; // ปรับให้เพรียวลง
    private final Rectangle foreground;
    private final Label nameLabel;
    private final int maxHp;


    /**
     *  Creates a new HealthBar
     */
    public HealthBar(int maxHp, String characterName, boolean isPlayer1) {
        this.maxHp = maxHp;

        Rectangle background = new Rectangle(BAR_WIDTH, BAR_HEIGHT, Color.rgb(40, 40, 40, 0.8));
        background.setArcWidth(15);
        background.setArcHeight(15);
        background.setStroke(Color.WHITE);
        background.setStrokeWidth(2);

        // 2. แถบเลือด (Foreground)
        foreground = new Rectangle(BAR_WIDTH, BAR_HEIGHT);
        foreground.setArcWidth(15);
        foreground.setArcHeight(15);
        updateColor(1.0);


        nameLabel = new Label(characterName);
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        nameLabel.setEffect(new DropShadow(5, Color.BLACK));

        this.getChildren().addAll(background, foreground, nameLabel);
        this.setAlignment(isPlayer1 ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);

        this.setEffect(new DropShadow(10, Color.BLACK));
    }

    /**
     * Connect the player's HP to the HealthBar.
     */
    public void setCurrentHp(int currentHp) {
        double ratio = Math.max(0, (double) currentHp / maxHp);
        foreground.setWidth(BAR_WIDTH * ratio);
        updateColor(ratio);
    }


    /**
     * health bar that reduces
     */
    private void updateColor(double ratio) {
        Color color = ratio > 0.5 ? Color.LIMEGREEN : (ratio > 0.2 ? Color.GOLD : Color.RED);
        Stop[] stops = new Stop[] { new Stop(0, color), new Stop(1, color.darker()) };
        foreground.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops));
    }

}