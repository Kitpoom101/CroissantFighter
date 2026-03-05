package component.scene2;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SkillBar extends Pane {

    private static final double BAR_WIDTH = 220;
    private static final double BAR_HEIGHT = 16;

    private final Rectangle cooldownBackground;
    private final Rectangle chargeForeground;

    public SkillBar() {
        setPrefSize(BAR_WIDTH, BAR_HEIGHT);

        cooldownBackground = new Rectangle(BAR_WIDTH, BAR_HEIGHT);
        cooldownBackground.setArcWidth(6);
        cooldownBackground.setArcHeight(6);
        cooldownBackground.setFill(Color.rgb(160, 30, 30)); // red cooldown bar
        cooldownBackground.setStroke(Color.BLACK);
        cooldownBackground.setStrokeWidth(1.5);

        chargeForeground = new Rectangle(0, BAR_HEIGHT);
        chargeForeground.setArcWidth(6);
        chargeForeground.setArcHeight(6);
        chargeForeground.setFill(Color.GOLD); // yellow fill as cooldown progresses

        getChildren().addAll(cooldownBackground, chargeForeground);
    }

    public void setCooldownProgress(double progress) {
        double clamped = Math.max(0.0, Math.min(progress, 1.0));
        chargeForeground.setWidth(BAR_WIDTH * clamped);
    }
}
