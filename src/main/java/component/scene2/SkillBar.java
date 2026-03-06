package component.scene2;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Displays a compact skill cooldown bar.
 */
public class SkillBar extends Pane {

    /** Fixed width for the skill bar in pixels. */
    private static final double BAR_WIDTH = 220;
    /** Fixed height for the skill bar in pixels. */
    private static final double BAR_HEIGHT = 16;

    /** Static red layer that indicates the full cooldown area. */
    private final Rectangle cooldownBackground;
    /** Dynamic yellow layer that grows as cooldown progress increases. */
    private final Rectangle chargeForeground;

    /**
     * Creates a new skill bar with red base and empty yellow charge.
     */
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

    /**
     * Updates the cooldown progress shown by the yellow fill.
     *
     * @param progress cooldown progress in range {@code [0.0, 1.0]}.
     *                 Values outside the range are clamped.
     */
    public void setCooldownProgress(double progress) {
        double clamped = Math.max(0.0, Math.min(progress, 1.0));
        chargeForeground.setWidth(BAR_WIDTH * clamped);
    }
}
