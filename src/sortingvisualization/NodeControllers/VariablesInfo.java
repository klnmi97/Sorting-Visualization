
package sortingvisualization.NodeControllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import sortingvisualization.Constants.Constants;

/**
 * Information panel that show the current state of variables
 * @author Mykhailo Klunko
 */
public class VariablesInfo {
    
    private final Label infoField;
    private String previousValue;
    
    /**
     * Creates new instance of variable state information handler class
     * @param preferredWidth width of side panel for correct text wrapping
     */
    public VariablesInfo(double preferredWidth){
        this.previousValue = "";
        this.infoField = new Label("");
        infoField.setFont(Constants.INFO_FONT);
        infoField.setWrapText(true);
        infoField.setPrefWidth(preferredWidth);
    }
    
    /**
     * Gets Label node for panel with information about the state of variables
     * @return label
     */
    public Label getInfoField() {
        return infoField;
    }
    
    /**
     * Creates animation of changing text at the information node
     * @param value text to set
     * @return animation of changing text
     */
    public Animation setText(String value) {
        String oldVal = previousValue;
        previousValue = value;
        return new Timeline(
            new KeyFrame(Duration.millis(0),
                new KeyValue(infoField.textProperty(), oldVal)),
            new KeyFrame(Duration.millis(100),
                new KeyValue(infoField.textProperty(), value)));
    }
    
    /**
     * Creates animation of changing text at the information node
     * @param formatString string with parameters
     * @param args string parameters
     * @return animation of changing text
     */
    public Animation setText(String formatString, Object... args){
        String resultStr = String.format(formatString, args);
        return setText(resultStr);
    }
    
    /**
     * Set default text which will be shown on the start
     * @param text new default text
     */
    public void setDefaultText(String text){
        this.previousValue = text;
        this.infoField.setText(text);
    }
    
}
