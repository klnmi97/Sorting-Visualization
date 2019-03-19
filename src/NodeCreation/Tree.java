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
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import sortingvisualization.BrickNode;
import sortingvisualization.Scaling;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class Tree {
    private static final double scalingFactor = Scaling.computeDPIScale();
    
    private static final int NODE_SIZE = initNodeSize();
    private static final int ARRAY_ITEM_SIZE = initArrayItemSize();
    private static final int LEVEL_HEIGHT = initLevelHeight();
    
    private static final Color FILL = Color.WHITE;
    private static final Color PLACEHOLDER_BKGRND = Color.LIGHTGRAY;
    private static final Color SELECTION = Color.AQUAMARINE;
    private static final Color LIGHT_SELECTION = Color.GRAY;
    private static final Color DEFAULT = Color.BLACK;
    private static final Color ARRAY_SORTED = Color.GAINSBORO;
    
    private static final int ARRAY_SPACING = initArraySpacing();
    
    private final int TREE_SPACING;
    private final double SPACING_COEF;
    
    private final List<BrickNode> treeNodes;
    private final List<BrickNode> arrayNodes;
    private final List<Line> childConnections;
    private List<Circle> placeholders;
    
    public Tree(int[] inputArray){
        this.TREE_SPACING = countSecondLevelSpacing(inputArray);
        this.SPACING_COEF = countSpacingCoefficient(inputArray);
        this.treeNodes = createList(inputArray);
        this.childConnections = createConnections();
        this.placeholders = createPlaceholders();
        this.arrayNodes = createGraphicArray(inputArray);
    }

    public List<BrickNode> getArrayNodes() {
        return arrayNodes;
    }
    
    public List<Circle> getPlaceholders() {
        return placeholders;
    }

    public List<Line> getChildConnections() {
        return childConnections;
    }
    
    //Graphic tree logic
    
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
            BrickNode node = createNode(inputArray[i], Color.BLACK, 
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
            int startY = getNodeY(i, size) + NODE_SIZE;
            if(l < size){
                int endX = getNodeX(l);
                int endY = getNodeY(l, size) + NODE_SIZE;
                Line left = new Line(startX, startY, endX, endY);
                left.setStrokeWidth(3);
                left.setTranslateX((startX + endX) / 2) ;
                left.setTranslateY((startY + endY) / 2);
                StackPane.setAlignment(left, Pos.BOTTOM_CENTER);
                connections.add(left);
            }
            if(r < size){
                int endX = getNodeX(r);
                int endY = getNodeY(r, size) + NODE_SIZE;
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
            Circle holder = new Circle(NODE_SIZE);
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
        return (int)(65 * levels * scalingFactor);
    }
    
    private double countSpacingCoefficient(int[] array){
        int levels = getNuberOfLevels(array.length);
        return (double)1 / levels;
    }
    
    private int countSpacing(int nodeLevel){
        int sp = TREE_SPACING - (int)((nodeLevel - 1) * (TREE_SPACING * SPACING_COEF));
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
    
    private BrickNode createNode(int value, Color color, double x, double y) {
        Circle body = new Circle(NODE_SIZE);
        body.setFill(FILL);
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
    
    private void swapInternalArray(int firstNode, int secondNode){
        BrickNode swap = arrayNodes.get(firstNode); 
        arrayNodes.set(firstNode, arrayNodes.get(secondNode)); 
        arrayNodes.set(secondNode, swap);
    }
    
    //Graphic array logic
    
    private List<BrickNode> createGraphicArray(int[] inputArray){
        List<BrickNode> list = new ArrayList<>();
        
        int size = inputArray.length;
        for(int i = 0; i < size; i++){
            BrickNode node = createArrayNode(inputArray[i], getArrayNodeX(i, size), 
                    getArrayNodeY());
            list.add(node);
        }
        return list;
    }
    
    private static int initArraySpacing() {
        return ARRAY_ITEM_SIZE;
    }
    
    private int getArrayIndent(int size) {
        return (int)(((double)size / 2) * -ARRAY_SPACING);
    }
    
    private int getArrayNodeX(int position, int arraySize){
        int x = position * ARRAY_SPACING + getArrayIndent(arraySize);
        return x;
    }
    
    private int getArrayNodeY(){
        return 10;
    }
    
    private BrickNode createArrayNode(int value, double x, double y){
        Rectangle cell = new Rectangle(ARRAY_ITEM_SIZE, ARRAY_ITEM_SIZE);
        cell.setFill(FILL);
        cell.setStroke(DEFAULT);
        
        Text text = new Text(String.valueOf(value));
        text.setFont(ViewController.font);
        
        BrickNode node = new BrickNode(value);
        node.setPrefSize(cell.getWidth(), cell.getHeight());
        //node.setId(String.valueOf(num));
        node.getChildren().addAll(cell, text);
        node.setAlignment(Pos.TOP_CENTER);
        node.setTranslateX(x);
        node.setTranslateY(y);
        node.setShape(cell);
        return node;
    }
    
    //Animations

    /**
     * Creates graphic animation of swapping two nodes in the graphic array
     * @param firstNode position of first node in array
     * @param secondNode position of second node in array
     * @return animation of swapping two nodes
     */
    public Animation swapInArray(int firstNode, int secondNode){
        int size = arrayNodes.size();
        ParallelTransition swap = new ParallelTransition();
        
        TranslateTransition lr = new TranslateTransition();
        lr.setNode(arrayNodes.get(firstNode));
        lr.setDuration(ViewController.SPEED);
        lr.setFromX(getArrayNodeX(firstNode, size));
        lr.setToX(getArrayNodeX(secondNode, size));
        
        TranslateTransition rl = new TranslateTransition();
        rl.setNode(arrayNodes.get(secondNode));
        rl.setDuration(ViewController.SPEED);
        rl.setFromX(getArrayNodeX(secondNode, size));
        rl.setToX(getArrayNodeX(firstNode, size));
        
        swap.getChildren().addAll(lr, rl);
        //swap items during swap in treeNodes array to have right order of nodes 
        //in the arrayNodes array
        swapInternalArray(firstNode, secondNode);
        return swap;
    }
    
    /**
     * Creates graphic animation of swapping two nodes in the created tree
     * @param firstNode position of first node in array
     * @param secondNode position of second node in array
     * @return animation of swapping two nodes
     */
    public Animation swapInTree(int firstNode, int secondNode){
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
    
    /**
     * Creates parallel animation of swapping nodes in the graphic tree and in
     * the graphic array
     * @param firstNode position of first node in array
     * @param secondNode position of second node in array
     * @return parallel animation of swapping two nodes(tree and array)
     */
    public Animation swap(int firstNode, int secondNode){
        ParallelTransition swap = new ParallelTransition();
        
        Animation arraySwap = swapInArray(firstNode, secondNode);
        Animation treeSwap = swapInTree(firstNode, secondNode);
        swap.getChildren().addAll(arraySwap, treeSwap);
        return swap;
    }
    
    /**
     * Creates animation of highlighting two nodes from the virtually 
     * created tree. Its purpose is to show comparing or selection of two
     * nodes.
     * @param firstNode position of the first selected node in the initial array
     * @param secondNode position of the second selected node in the initial array
     * @return animation that temporary changes color of two nodes
     */
    public Animation compareInTree(int firstNode, int secondNode) {
        int size = treeNodes.size();
        SequentialTransition compare = new SequentialTransition();
        ParallelTransition select = new ParallelTransition();
        ParallelTransition unselect = new ParallelTransition();
        
        Animation changeStrokeFirst = repaintStroke(treeNodes.get(firstNode).getShape(), DEFAULT, LIGHT_SELECTION);
        Animation repaintFirst = repaint(treeNodes.get(firstNode).getShape(), FILL, SELECTION);
        Animation changeStrokeSecond = repaintStroke(treeNodes.get(secondNode).getShape(), DEFAULT, LIGHT_SELECTION);
        Animation repaintSecond = repaint(treeNodes.get(secondNode).getShape(), FILL, SELECTION);
        select.getChildren().addAll(changeStrokeFirst, repaintFirst, changeStrokeSecond, repaintSecond);
        
        Animation backStrokeFirst = repaintStroke(treeNodes.get(firstNode).getShape(), LIGHT_SELECTION, DEFAULT);
        Animation paintBackFirst = repaint(treeNodes.get(firstNode).getShape(), SELECTION, FILL);
        Animation backStrokeSecond = repaintStroke(treeNodes.get(secondNode).getShape(), LIGHT_SELECTION, DEFAULT);
        Animation paintBackSecond = repaint(treeNodes.get(secondNode).getShape(), SELECTION, FILL);
        unselect.getChildren().addAll(backStrokeFirst, paintBackFirst, backStrokeSecond, paintBackSecond);
        
        compare.getChildren().addAll(select, unselect);
        return compare;
    }
    
    /**
     * Creates animation of highlighting two nodes from the
     * created graphic array. Its purpose is to show comparing or selection of 
     * two nodes.
     * @param firstNode position of the first selected node in the initial array
     * @param secondNode position of the second selected node in the initial array
     * @return animation that temporary changes color of two nodes in the array 
     * in the tree
     */
    public Animation compareInArray(int firstNode, int secondNode) {
        int size = treeNodes.size();
        SequentialTransition compare = new SequentialTransition();
        ParallelTransition select = new ParallelTransition();
        ParallelTransition unselect = new ParallelTransition();
        
        Animation selectFirst = repaint(arrayNodes.get(firstNode).getShape(), FILL, SELECTION);
        Animation selectSecond = repaint(arrayNodes.get(secondNode).getShape(), FILL, SELECTION);
        select.getChildren().addAll(selectFirst, selectSecond);
        
        Animation unselectFirst = repaint(arrayNodes.get(firstNode).getShape(), SELECTION, FILL);
        Animation unselectSecond = repaint(arrayNodes.get(secondNode).getShape(), SELECTION, FILL);
        unselect.getChildren().addAll(unselectFirst, unselectSecond);
        
        compare.getChildren().addAll(select, unselect);
        return compare;
    }
    
    public Animation compare(int firstNode, int secondNode){
        ParallelTransition compare = new ParallelTransition();
        
        Animation arrayHighlight = compareInArray(firstNode, secondNode);
        Animation treeHighlight = compareInTree(firstNode, secondNode);
        compare.getChildren().addAll(arrayHighlight, treeHighlight);
        return compare;
    }
    
    private Animation repaintStroke(Shape shape, Color fromValue, Color toValue) {
        StrokeTransition repainting = new StrokeTransition();
        repainting.setShape(shape);
        repainting.setDuration(ViewController.SPEED);
        repainting.setFromValue(fromValue);
        repainting.setToValue(toValue);
        return repainting;
    }
    
    private Animation repaint(Shape shape, Color fromValue, Color toValue) {
        FillTransition repaint = new FillTransition();
        repaint.setShape(shape);
        repaint.setDuration(ViewController.SPEED);
        repaint.setFromValue(fromValue);
        repaint.setToValue(toValue);
        return repaint;
    }
    
    public Animation setSorted(int nodePosition) {
        ParallelTransition setSorted = new ParallelTransition();
        
        Animation array = repaint(arrayNodes.get(nodePosition).getShape(), FILL, ARRAY_SORTED);
        Animation tree = hide(nodePosition);
        setSorted.getChildren().addAll(array, tree);
        return setSorted;
    }
    
    public Animation hide(int nodePos) {
        FadeTransition hide = new FadeTransition();
        hide.setNode(treeNodes.get(nodePos));
        hide.setDuration(ViewController.SPEED);
        hide.setFromValue(1);
        hide.setToValue(0);
        
        return hide;
    }
    
    //Utils
    
    private static int initNodeSize() {
        return (int)(25 * scalingFactor);
    }

    private static int initArrayItemSize() {
        return (int)(40 * scalingFactor);
    }

    private static int initLevelHeight() {
        return (int)(100 * scalingFactor);
    }
}
