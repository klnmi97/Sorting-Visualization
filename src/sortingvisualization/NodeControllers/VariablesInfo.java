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
    private static final Font font = Font.font("Courier New", FontWeight.MEDIUM, 12 * scaling); //TODO: maybe better font
    private static final int DEFAULT_WIDTH = 400;
    
    private Label infoField;
    private String previousValue;
    
    public VariablesInfo(){
        this.previousValue = "";
        this.infoField = new Label("");
        infoField.setFont(font);
        infoField.setWrapText(true);
        infoField.setPrefWidth(DEFAULT_WIDTH);
    }
    
    /**
     * Gets Label node for panel with information about state of variables
     * @return label
     */
    public Label getInfoField() {
        return infoField;
    }
    
    public Animation setText(String value) {
        String oldVal = previousValue;
        previousValue = value;
        return new Timeline(
            new KeyFrame(Duration.millis(0),
                new KeyValue(infoField.textProperty(), oldVal)),
            new KeyFrame(ViewController.SPEED.multiply(0.1),
                new KeyValue(infoField.textProperty(), value)));
    }
    
}
