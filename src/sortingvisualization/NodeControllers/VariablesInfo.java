/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.NodeControllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import sortingvisualization.Controllers.ViewController;
import sortingvisualization.Utilities.Scaling;

/**
 * 
 * @author Mykhailo Klunko
 */
public class VariablesInfo {
    
    private static final double scaling = Scaling.computeDPIScale();
    private static final Font font = Font.font("Courier New", FontWeight.MEDIUM, 14 * scaling); //TODO: maybe better font
    
    private Label infoField;
    private String previousValue;
    
    /**
     * Creates new instance of variable state information handler class
     * @param preferredWidth width of side panel for correct text wrapping
     */
    public VariablesInfo(double preferredWidth){
        this.previousValue = "";
        this.infoField = new Label("");
        infoField.setFont(font);
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
            new KeyFrame(ViewController.SPEED.multiply(0.1),
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
