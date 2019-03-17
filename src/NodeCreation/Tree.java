/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NodeCreation;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class Tree {
    
    private static final int RADIUS = 25;
    private static final int LEVEL_HEIGHT = 100;
    private static final Color PLACEHOLDER_BKGRND = Color.LIGHTGRAY;
    private static final Color SELECTION = Color.AQUAMARINE;
    private static final Color DEFAULT = Color.BLACK;
    
    
    private final int SPACING;
    private final double SPACING_COEF;
    
    private final List<BrickNode> treeNodes;
    private final List<Line> childConnections;
    private List<Circle> placeholders;
    
    public Tree(int[] inputArray){
        this.SPACING = countSecondLevelSpacing(inputArray);
        this.SPACING_COEF = countSpacingCoefficient(inputArray);
        this.treeNodes = createList(inputArray);
        this.childConnections = createConnections();
        this.placeholders = createPlaceholders();
    }

    public List<Circle> getPlaceholders() {
        return placeholders;
    }

    public List<Line> getChildConnections() {
        return childConnections;
    }
    
    /**
     *
     * @return List of created in constructor graphic tree nodes
     */
    public List<BrickNode> getNodesList(){
        return this.treeNodes;
    }
    
    /**
     * Computes the X-th coordinate position of node based on 
     * the position in the array of nodes and current array size
     * @param posInArray position in array
     * @return X coordinate position of node
     */
    public int getNodeX(int posInArray){
        double x = countSpacing(getLevel(posInArray)) 
                * getPositionInLevel(posInArray) 
                + getLevelIndent(getLevel(posInArray));
        return (int)x;
    }
    
    /**
     * Computes Y coordinate position of node based on 
     * the position in the array of nodes
     * @param posInArray position in array
     * @param arraySize size of array with nodes
     * @return Y coordinate position of node
     */
    public int getNodeY(int posInArray, int arraySize){
        int y = (getNuberOfLevels(arraySize) 
                - getLevel(posInArray) + 1) 
                * -LEVEL_HEIGHT;
        return y;
    }
    
    
    private List<BrickNode> createList(int[] inputArray){
        List<BrickNode> list = new ArrayList<>();
        for(int i = 0; i < inputArray.length; i++){
            BrickNode node = createNode(i, inputArray[i], Color.BLACK, 
                    getNodeX(i), getNodeY(i, inputArray.length));
            list.add(node);
        }
        return list;
    }
    
    private List<Line> createConnections(){
        if(treeNodes == null) return null;
        
        List<Line> connections = new ArrayList<>();
        int size = treeNodes.size();
        for(int i = 0; i < size; i++){
            int l = 2 * i + 1;
            int r = 2 * i + 2;
            int startX = getNodeX(i);
            int startY = getNodeY(i, size) + RADIUS;
            if(l < size){
                int endX = getNodeX(l);
                int endY = getNodeY(l, size) + RADIUS;
                Line left = new Line(startX, startY, endX, endY);
                left.setStrokeWidth(3);
                left.setTranslateX((startX + endX) / 2) ;
                left.setTranslateY((startY + endY) / 2);
                StackPane.setAlignment(left, Pos.BOTTOM_CENTER);
                connections.add(left);
            }
            if(r < size){
                int endX = getNodeX(r);
                int endY = getNodeY(r, size) + RADIUS;
                Line right = new Line(startX, startY, endX, endY);
                right.setStrokeWidth(3);
                right.setTranslateX((startX + endX) / 2) ;
                right.setTranslateY((startY + endY) / 2);
                StackPane.setAlignment(right, Pos.BOTTOM_CENTER);
                connections.add(right);
            }
        }
        return connections;
    }
    
    private List<Circle> createPlaceholders(){
        if(treeNodes == null) return null;
        
        List<Circle> placeholders = new ArrayList<>();
        int size = treeNodes.size();
        for (int i = 0; i < size; i++) {
            Circle holder = new Circle(RADIUS);
            holder.setFill(PLACEHOLDER_BKGRND);
            holder.setTranslateX(getNodeX(i));
            holder.setTranslateY(getNodeY(i, size));
            StackPane.setAlignment(holder, Pos.BOTTOM_CENTER);
            placeholders.add(holder);
        }
        
        return placeholders;
    }
    
    private double getLevelIndent(int level){
        return ((double)(getLevelWidth(level) - 1) / 2) * -countSpacing(level);
    }
    
    private int countSecondLevelSpacing(int[] array){
        int levels = getNuberOfLevels(array.length);
        return 65 * levels;
    }
    
    private double countSpacingCoefficient(int[] array){
        int levels = getNuberOfLevels(array.length);
        return (double)1 / levels;
    }
    
    private int countSpacing(int nodeLevel){
        int sp = SPACING - (int)((nodeLevel - 1) * (SPACING * SPACING_COEF));
        return  sp;
    }
    
    private int getLevel(int arrayPosition){
        int position = arrayPosition;
        int level = 1;
        
        while(position != 0){
            if(position % 2 == 0){
                position -= 2;
            } else {
                position -= 1;
            }
            position /= 2;
            level++;
        }
        return level;
    }
    
    private int getPositionInLevel(int i){
        int level = getLevel(i);
        return i - (int)Math.pow(2, level - 1) + 1;
    }
    
    private int getNuberOfLevels(int treeArraySize){
        return getLevel(treeArraySize - 1);
    }
    
    private int getLevelWidth(int level){
        return (int)Math.pow(2, level - 1);
    }
    
    private BrickNode createNode(int i, int value, Color color, double x, double y) {
        Circle body = new Circle(RADIUS);
        body.setFill(Color.WHITE);
        body.setStroke(color);
        body.setStrokeWidth(3);
        
        Text text = new Text(String.valueOf(value));
        text.setFont(ViewController.font);
        text.setTranslateY(-10);
        
        BrickNode node = new BrickNode(value);
        node.setPrefSize(body.getRadius() * 2, body.getRadius() * 2);
        //node.setId(String.valueOf(num));
        node.getChildren().addAll(body, text);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(x);
        node.setTranslateY(y);
        node.setShape(body);
        return node;
    }
    
    //Animations
    
    public Animation swap(int firstNode, int secondNode){
        int size = treeNodes.size();
        ParallelTransition swap = new ParallelTransition();
        
        TranslateTransition lr = new TranslateTransition();
        lr.setNode(treeNodes.get(firstNode));
        lr.setDuration(ViewController.SPEED);
        lr.setFromX(getNodeX(firstNode));
        lr.setFromY(getNodeY(firstNode, size));
        lr.setToX(getNodeX(secondNode));
        lr.setToY(getNodeY(secondNode, size));
        
        TranslateTransition rl = new TranslateTransition();
        rl.setNode(treeNodes.get(secondNode));
        rl.setDuration(ViewController.SPEED);
        rl.setFromX(getNodeX(secondNode));
        rl.setFromY(getNodeY(secondNode, size));
        rl.setToX(getNodeX(firstNode));
        rl.setToY(getNodeY(firstNode, size));
        
        swap.getChildren().addAll(lr, rl);
        return swap;
    }
    
    public Animation compare(int firstNode, int secondNode) {
        int size = treeNodes.size();
        SequentialTransition compare = new SequentialTransition();
        ParallelTransition select = new ParallelTransition();
        ParallelTransition unselect = new ParallelTransition();
        
        Animation selectFirst = repaint(treeNodes.get(firstNode).getShape(), DEFAULT, SELECTION);
        Animation selectSecond = repaint(treeNodes.get(secondNode).getShape(), DEFAULT, SELECTION);
        select.getChildren().addAll(selectFirst, selectSecond);
        
        Animation unselectFirst = repaint(treeNodes.get(firstNode).getShape(), SELECTION, DEFAULT);
        Animation unselectSecond = repaint(treeNodes.get(secondNode).getShape(), SELECTION, DEFAULT);
        unselect.getChildren().addAll(unselectFirst, unselectSecond);
        
        compare.getChildren().addAll(select, unselect);
        return compare;
    }
    
    private Animation repaint(Shape shape, Color fromValue, Color toValue) {
        StrokeTransition repainting = new StrokeTransition();
        repainting.setShape(shape);
        repainting.setDuration(ViewController.SPEED);
        repainting.setFromValue(fromValue);
        repainting.setToValue(toValue);
        return repainting;
    }
    
    public Animation hide(int nodePos){
        FadeTransition hide = new FadeTransition();
        hide.setNode(treeNodes.get(nodePos));
        hide.setDuration(ViewController.SPEED);
        hide.setFromValue(1);
        hide.setToValue(0);
        
        return hide;
    }
}
