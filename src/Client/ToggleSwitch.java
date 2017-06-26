package Client;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Created by parsahejabi on 6/25/17.
 */
public class ToggleSwitch extends Parent{
    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);

    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    public ToggleSwitch() {
        Rectangle background = new Rectangle(75, 35);
        background.setArcWidth(35);
        background.setArcHeight(35);
        background.setFill(Color.rgb(144,144,144));
        background.setStroke(Color.LIGHTGRAY);

        Circle trigger = new Circle(8.5);
        trigger.setCenterX(17.5);
        trigger.setCenterY(17.5);
        trigger.setFill(Color.WHITE);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, trigger);

        switchedOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState.booleanValue();
            translateAnimation.setToX(isOn ? 75 - 35 : 0);
            fillAnimation.setFromValue(isOn ? Color.rgb(144,144,144) : Color.rgb(56,151,240));
            fillAnimation.setToValue(isOn ? Color.rgb(56,151,240) : Color.rgb(144,144,144));

            animation.play();
        });

        setOnMouseClicked(event -> {
            switchedOn.set(!switchedOn.get());
        });
    }
}