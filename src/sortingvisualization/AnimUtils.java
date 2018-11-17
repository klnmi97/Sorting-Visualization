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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
        transition.setFromY(ViewController.TOP_INDENT);
        transition.setToX(toX * ViewController.SPACING 
                + withLeftIndent);
        transition.setToY(ViewController.SORT_GROUP_MOVE_DELTA - toY * ViewController.SPACING / 2);
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
        transition.setFromY(ViewController.SORT_GROUP_MOVE_DELTA - fromY * ViewController.SPACING / 2);
        transition.setToX(toX * ViewController.SPACING 
                + withLeftIndent);
        transition.setToY(ViewController.TOP_INDENT);
        return transition;
    }
    
    public static Animation moveY(BrickNode sp, int fromY, int toY){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(sp);
        transition.setDuration(ViewController.SPEED);
        transition.setFromY(ViewController.SORT_GROUP_MOVE_DELTA - fromY * ViewController.SPACING / 2);
        transition.setToY(ViewController.SORT_GROUP_MOVE_DELTA - toY * ViewController.SPACING / 2);
        return transition;
    }
    
    public static Animation moveDownToX(BrickNode sp, int fromX, int toX, double startLeftIndent, double withLeftIndent) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(sp);
        transition.setDuration(ViewController.SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(fromX * ViewController.SPACING 
                + startLeftIndent);
        transition.setFromY(ViewController.TOP_INDENT);
        transition.setToX(toX * ViewController.SPACING 
                + withLeftIndent);
        transition.setToY(ViewController.SORT_GROUP_MOVE_DELTA);
        return transition;
    }
    
    public static Animation moveDownToX(BrickNode sp, int toX, int fromX) {
        return moveDownToX(sp, fromX, toX, ViewController.LEFT_INDENT , ViewController.LEFT_INDENT);
    }
    
    public static TranslateTransition moveToX(BrickNode sp, int X, int i) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(sp);
        transition.setDuration(ViewController.SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(i * ViewController.SPACING + ViewController.LEFT_INDENT);
        transition.setToX(X * ViewController.SPACING + ViewController.LEFT_INDENT);
        return transition;
    }

    public static Animation moveUpToX(BrickNode sp, int fromX, int toX, double startLeftIndent, double withLeftIndent){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(sp);
        transition.setDuration(ViewController.SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(fromX * ViewController.SPACING 
                + startLeftIndent);
        transition.setFromY(ViewController.SORT_GROUP_MOVE_DELTA);
        transition.setToX(toX * ViewController.SPACING 
                + withLeftIndent);
        transition.setToY(ViewController.TOP_INDENT);
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
    
    public static Animation setColor(StackPane sp, Color fromColor, Color toColor){
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
    
    public static Animation setText(Label lbl, String fromVal, String descImp) {
        String content = descImp;
        String oldVal = fromVal;
        return new Timeline(
            new KeyFrame(Duration.millis(0),
                new KeyValue(lbl.textProperty(), oldVal)),
            new KeyFrame(Duration.millis(1),
                new KeyValue(lbl.backgroundProperty(), new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))),
            new KeyFrame(Duration.millis(100),
                new KeyValue(lbl.textProperty(), content)),
            new KeyFrame(Duration.millis(101),
                new KeyValue(lbl.fontProperty(), Font.font("Helvetica", 20))),
            new KeyFrame(ViewController.SPEED,
                new KeyValue(lbl.fontProperty(), Font.font("Helvetica", 20))),
            new KeyFrame(ViewController.SPEED,
                new KeyValue(lbl.backgroundProperty(), new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)))));
    }
    
    public static Animation makeParallel(Animation... anims){
        ParallelTransition pt = new ParallelTransition();
        for(Animation anim : anims){
            if(anim != null){
                pt.getChildren().add(anim);
            }
        }
        return pt;
    }
    
}
