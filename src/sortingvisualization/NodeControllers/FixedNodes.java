/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.NodeControllers;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.Scaling;

/**
 *
 * @author mihae
 */
public class FixedNodes {
    
    private static final double scalingFactor = Scaling.computeDPIScale();
    
    private static final double NODE_HEIGHT = 30 * scalingFactor;
    private static final double NODE_WIDTH = 50 * scalingFactor;
    
    private static final int LEVEL1 = (int)(-350 * scalingFactor);
    private static final int LEVEL2 = (int)(250 * scalingFactor) + LEVEL1;
    private static final int SPACING = initSpacing();
    
    private static final Duration SPEED = Duration.millis(1000);
    
    private List<BrickNode> list;
    
    /**
     * Instantiate fixed nodes manager class
     * @param inputArray input values. Cannot be null
     * @param max maximal possible value
     */
    public FixedNodes(int[] inputArray, int max) {
        this.list = createList(inputArray, max);
    }
    
    /**
     * Get created visual nodes
     * @return list of nodes
     */
    public List<BrickNode> getNodes() {
        return list;
    }
    
    private List<BrickNode> createList(int[] inputArray, int currentMax){
        list = new ArrayList<>();
        int leftIndent = countIndent(inputArray.length);
        for(int i = 0; i < inputArray.length; i++){
            BrickNode node = createFixedNode(i, inputArray[i], leftIndent, LEVEL1);
            
            list.add(node);
        }
        return list;
    }
    
    /**
     * Creates visualization of buckets. They will be located at the lower line
     * (Level 2)
     * @param count count of buckets to create
     * @param startLabelCounter starting value for buckets
     * @param diff increment for each next bucket
     * @return list of bucket visual representation
     */
    public List<FlowPane> createBucketList(int count, int startLabelCounter, int diff){
        List<FlowPane> buckets = new ArrayList<>();
        int y = LEVEL2 + (int)(10 * scalingFactor);
        int currentLabel = startLabelCounter;
        int leftIndent = countIndent(count); 
        for(int i = 0; i < count; i++){
            int x = SPACING * i + leftIndent;
            FlowPane bucket = createBucket(x, y, Integer.toString(currentLabel));
            buckets.add(bucket);
            currentLabel += diff;
        }
        return buckets;
    }
    
    private FlowPane createBucket(int x, int y, String labelText){
        FlowPane base = new FlowPane(Orientation.VERTICAL);
        base.setColumnHalignment(HPos.CENTER);
        base.setAlignment(Pos.BOTTOM_CENTER);
        Line baseLine = new Line(0, 0, 50 * scalingFactor, 0);
        baseLine.setStrokeWidth(5 * scalingFactor);
        baseLine.setStroke(Constants.BUCKET);
        Text label = new Text(labelText);
        label.setFont(Constants.MAIN_FONT);
        base.getChildren().addAll(baseLine, label);
        base.setTranslateX(x);
        base.setTranslateY(y + 24 * scalingFactor); //TODO: smarter Y position. Problem with height of children because of FlowPane alignmen
        return base;
    }
    
    private BrickNode createFixedNode(int i, int value, double leftIndent, double topIndent){
        Rectangle rectangle = new Rectangle(NODE_WIDTH, NODE_HEIGHT);
        rectangle.setStroke(Constants.STROKE);
        rectangle.setFill(Constants.F_DEFAULT);
        
        Text text = new Text(String.valueOf(value));
        text.setFont(Constants.MAIN_FONT);
        BrickNode node = new BrickNode(value);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        HBox numberBox = new HBox();
        String valueToSet = String.valueOf(value);
        int index = valueToSet.toCharArray().length - 1;
        for(char ch: valueToSet.toCharArray()){
            Text digit = new Text(Character.toString(ch));
            digit.setUserData(String.valueOf(index));
            digit.setFont(Constants.MAIN_FONT);
            numberBox.getChildren().add(digit);
            numberBox.setAlignment(Pos.BOTTOM_CENTER);
            node.getDigits().add(digit);
            index--;
        }
        node.getChildren().addAll(rectangle, numberBox);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(SPACING * i + leftIndent);
        node.setTranslateY(topIndent);
        node.setShape(rectangle);
        return node;
    }
    
    // Node animation
    
    /**
     * Moves node from the main line (Level 1) to the lower line (Level 2). The x
     * value of each level  is counted from the initial number of elements in level.
     * Indents of the main (first) level are computed implicitly.
     * The y value is the position of the node counted starting from the current 
     * level y coordinate in the upper direction.
     * @param node node to move
     * @param fromX node's x position in the virtual array of Level 1
     * @param toX new node's x position at Level 2
     * @param fromY node's y position at Level 1
     * @param toY new node's y position at Level 2
     * @param secondLevelSize number of items in Level 2. Is used for centering
     * @return animation of moving node to the lower line
     */
     public Animation moveDownTo(BrickNode node, int fromX, int toX, int fromY, int toY, 
            int secondLevelSize){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(node);
        transition.setDuration(SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(fromX * SPACING + countIndent(list.size()));
        transition.setFromY(LEVEL1 - fromY * (SPACING - NODE_HEIGHT));
        transition.setToX(toX * SPACING + countIndent(secondLevelSize));
        transition.setToY(LEVEL2 - toY * (SPACING - NODE_HEIGHT));
        return transition;
    }
    
    /**
     * Moves node from the lower line (Level 2) to the upper main line (Level 1). The x
     * value of each level  is counted from the initial number of elements in level.
     * Indents of the main (first) level are computed implicitly.
     * The y value is the position of the node counted starting from the current 
     * level y coordinate in the upper direction. The default Y is zero (exactly 
     * at the current line)
     * @param node node to move
     * @param toX node's new x position in the virtual array of Level 1 (and is usually 
     * actual position in the list of nodes)
     * @param fromX original node's x position at Level 2
     * @param fromY original node's y position at Level 2
     * @param toY new node's y position at Level 1. Set it to zero by default
     * @param secondLevelSize number of elements at the second line (Level 2)
     * @return animation of moving node up from lower line
     */
    public Animation moveUpTo(BrickNode node, int toX, int fromX, int fromY, int toY, 
            int secondLevelSize){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(node);
        transition.setDuration(SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(fromX * SPACING + countIndent(secondLevelSize));
        transition.setFromY(LEVEL2 - fromY * (SPACING - NODE_HEIGHT));
        transition.setToX(toX * SPACING + countIndent(list.size()));
        transition.setToY(LEVEL1 - toY * (SPACING - NODE_HEIGHT));
        return transition;
    }
    
    /**
     * Creates animation of changing Y position of the node at lower line. Y position
     * is not equal to the actual coordinate.
     * @param node node that needs changing Y position 
     * @param fromY initial Y position
     * @param toY new Y position
     * @return animation of changing node's vertical position in relation to the 
     * lower line
     */
    public Animation changeLowY(BrickNode node, int fromY, int toY){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(node);
        transition.setDuration(SPEED);
        transition.setFromY(LEVEL2 - fromY * (SPACING - NODE_HEIGHT));
        transition.setToY(LEVEL2 - toY * (SPACING - NODE_HEIGHT));
        return transition;
    }
    
    /**
     * Creates animation of changing digit color of the fixed size node
     * @param fixedNode node to change digit color
     * @param digitPlace digit place. Is counted from zero in right-to-left direction
     * @param formerColor former color of the digit
     * @param newColor new color of the digit
     * @return animation of changing digit color
     */
    public Animation setNodeDigitColor(BrickNode fixedNode, int digitPlace, Color formerColor, Color newColor){
        String digitPosition = String.valueOf(digitPlace);
        for (Text digit : fixedNode.getDigits()) {
            if (digitPosition.equals(digit.getUserData())) {
                return new FillTransition(SPEED, digit, formerColor, newColor);
            }
        }
        return null;
    }
    
    /**
     * Changes color of exact digit for each fixed size node from list. If such position of
     * digit does not exist, this node will be ignored.
     * @param nodes list of nodes to change
     * @param digitPlace place of digit. Counted from zero in the direction from
     * right to left.
     * @param formerColor former color of the digit at the position
     * @param newColor new color of digit
     * @return animation of changing exact digit color for all nodes from list 
     */
    public Animation setDigitsColor(List<BrickNode> nodes, int digitPlace, Color formerColor, Color newColor){
        ParallelTransition pt = new ParallelTransition();
        for(int i = 0; i < nodes.size(); i++){
            Animation setColor = setNodeDigitColor(nodes.get(i), digitPlace, formerColor, newColor);
            if(setColor != null){
                pt.getChildren().add(setColor);
            }
        }
        return pt;
    }
    
    private static int countIndent(int number){
        return (int)(((double)number / 2) * -SPACING);
    }
    
    private static int initSpacing() {
        return (int)(60 * scalingFactor);
    }
    
    /**
     * Get absolute minimal height of window needed for nodes to be in viewport
     * @return height in pixels
     */
    public double getViewportMinHeight() {
        return LEVEL1 * -1 + NODE_HEIGHT;
    }
}
