/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Utilities;

import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sortingvisualization.Constants.Constants;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.Controllers.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class AnimUtils {
    
    //three first functions need clever refactoring
    public static Animation moveTo(BrickNode sp, int fromX, int toX, int toY, 
            double startLeftIndent, double withLeftIndent){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(sp);
        transition.setDuration(ViewController.SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(fromX * ViewController.SPACING 
                + startLeftIndent);
        transition.setFromY(ViewController.LEVEL1);
        transition.setToX(toX * ViewController.SPACING 
                + withLeftIndent);
        transition.setToY(ViewController.LEVEL2 - toY * ViewController.SPACING / 2);
        return transition;
    }
    
    public static Animation moveFrom(BrickNode sp, int toX, int fromX, int fromY, 
            double startLeftIndent, double withLeftIndent){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(sp);
        transition.setDuration(ViewController.SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(fromX * ViewController.SPACING 
                + startLeftIndent);
        transition.setFromY(ViewController.LEVEL2 - fromY * ViewController.SPACING / 2);
        transition.setToX(toX * ViewController.SPACING 
                + withLeftIndent);
        transition.setToY(ViewController.LEVEL1);
        return transition;
    }
    
    public static Animation moveY(BrickNode sp, int fromY, int toY){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(sp);
        transition.setDuration(ViewController.SPEED);
        transition.setFromY(ViewController.LEVEL2 - fromY * ViewController.SPACING / 2);
        transition.setToY(ViewController.LEVEL2 - toY * ViewController.SPACING / 2);
        return transition;
    }
    
    public static Animation setNodeDigitColor(BrickNode fixedNode, int digitPlace, Color formerColor, Color newColor){
        String digitPosition = String.valueOf(digitPlace);
        for (Text digit : fixedNode.getDigits()) {
            if (digitPosition.equals(digit.getUserData())) {
                return new FillTransition(ViewController.SPEED, digit, formerColor, newColor);
            }
        }
        return null;
    }
    
    //TODO: check if list is not null/create notnull list for animation
    public static Animation setDigitsColor(List<BrickNode> nodes, int digitPlace, Color formerColor, Color newColor){
        ParallelTransition pt = new ParallelTransition();
        for(int i = 0; i < nodes.size(); i++){
            Animation setColor = setNodeDigitColor(nodes.get(i), digitPlace, formerColor, newColor);
            if(setColor != null){
                pt.getChildren().add(setColor);
            }
        }
        return pt;
    }
}
