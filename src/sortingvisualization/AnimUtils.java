/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 *
 * @author mihae
 */
public class AnimUtils {
    
    public static Animation moveDownToX(BrickNode sp, int X, int i) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(sp);
        transition.setDuration(ViewController.SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(i * ViewController.SPACING 
                + ViewController.LEFT_INDENT);
        transition.setFromY(ViewController.TOP_INDENT);
        transition.setToX(X * ViewController.SPACING 
                + ViewController.LEFT_INDENT);
        transition.setToY(ViewController.SORT_GROUP_MOVE_DELTA);
        return transition;

    }
    
    
    public static TranslateTransition moveTo(BrickNode sp, int X, int i) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(sp);
        transition.setDuration(ViewController.SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(i * ViewController.SPACING + ViewController.LEFT_INDENT);
        transition.setToX(X * ViewController.SPACING + ViewController.LEFT_INDENT);
        return transition;
    }

    public static TranslateTransition moveNodeUp(BrickNode sp){
        TranslateTransition moveNodeUp = new TranslateTransition();
        moveNodeUp.setNode(sp);
        moveNodeUp.setDuration(ViewController.SPEED);
        moveNodeUp.setFromY(ViewController.SORT_GROUP_MOVE_DELTA);
        moveNodeUp.setToY(ViewController.TOP_INDENT);
        return moveNodeUp;
    }
    
    public static Animation swap(BrickNode firstNode, BrickNode secondNode, int poss1, int poss2){
        double pos1 = poss1 * ViewController.SPACING + ViewController.LEFT_INDENT;
        double pos2 = poss2 * ViewController.SPACING + ViewController.LEFT_INDENT;
        TranslateTransition transition1 = new TranslateTransition();
        transition1.setNode(firstNode);
        transition1.setDuration(ViewController.SPEED);
        transition1.setFromX(pos1);
        transition1.setToX(pos2);
        
        TranslateTransition transition2 = new TranslateTransition();
        transition2.setNode(secondNode);
        transition2.setDuration(ViewController.SPEED);
        transition2.setFromX(pos2);
        transition2.setToX(pos1);
        
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(transition1, transition2);
        return parallelTransition;
    }
    
    public static Animation setColor(BrickNode sp, Color fromColor, Color toColor){
        return new FillTransition(ViewController.SPEED, sp.getShape(), fromColor, toColor);
    }
    
    public static Animation highlightLine(List<Label> code, int lineNumber){
        Animation animation = new Transition() {

            {
                setCycleDuration(ViewController.SPEED);
                setInterpolator(Interpolator.EASE_IN);
            }

            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(1, 0.6, 0.43, 0 + frac);
                code.get(lineNumber).setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        return animation;
    }
    
    public static Animation selectNodes(BrickNode node1, BrickNode node2){
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                setColor(node1, ViewController.DEFAULT, ViewController.COMPARE), 
                setColor(node2, ViewController.DEFAULT, ViewController.COMPARE));
        return parallelTransition;
    }
    
    public static Animation unselectNodes(BrickNode node1, BrickNode node2){
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                setColor(node1, ViewController.COMPARE, ViewController.DEFAULT), 
                setColor(node2, ViewController.COMPARE, ViewController.DEFAULT));
        return parallelTransition;
    }
}