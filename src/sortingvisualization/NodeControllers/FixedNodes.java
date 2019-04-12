/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.NodeControllers;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.Scaling;

/**
 *
 * @author mihae
 */
public class FixedNodes {
    
    private static final double scalingFactor = Scaling.computeDPIScale();
    public static final double NODE_HEIGHT = 30 * scalingFactor;
    public static final double NODE_WIDTH = 50 * scalingFactor;
    
    private static final int LEVEL1 = (int)(-350 * scalingFactor);
    private static final int LEVEL2 = (int)(250 * scalingFactor) + LEVEL1;
    private static final int SPACING = initSpacing();
    
    private List<BrickNode> list;
    
    public FixedNodes(int[] inputArray, int max) {
        this.list = createList(inputArray, max);
    }
    
    public List<BrickNode> getNodes() {
        return list;
    }
    
    public List<BrickNode> createList(int[] inputArray, int currentMax){
        list = new ArrayList<>();
        int leftIndent = countIndent(inputArray.length);
        for(int i = 0; i < inputArray.length; i++){
            BrickNode node = createFixedNode(i, inputArray[i], leftIndent, LEVEL1);
            
            list.add(node);
        }
        return list;
    }
    
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
        Text label = new Text(labelText);
        label.setFont(Constants.font);
        base.getChildren().addAll(baseLine, label);
        base.setTranslateX(x);
        base.setTranslateY(y + 24 * scalingFactor); //TODO: smarter Y position. Problem with height of children because of FlowPane alignmen
        return base;
    }
    
    private BrickNode createFixedNode(int i, int value, double leftIndent, double topIndent){
        Rectangle rectangle = new Rectangle(NODE_WIDTH, NODE_HEIGHT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.WHITE);
        
        Text text = new Text(String.valueOf(value));
        text.setFont(Constants.font);
        BrickNode node = new BrickNode(value);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        HBox numberBox = new HBox();
        String valueToSet = String.valueOf(value);
        int index = valueToSet.toCharArray().length - 1;
        for(char ch: valueToSet.toCharArray()){
            Text digit = new Text(Character.toString(ch));
            digit.setUserData(String.valueOf(index));
            digit.setFont(Constants.font);
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
