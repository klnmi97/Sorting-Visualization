/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.NodeControllers;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.Scaling;
import sortingvisualization.Controllers.ViewController;

/**
 * Class represents nodes that have height dependent on value
 * @author Mykhailo Klunko
 */
public final class DynamicNodes {
    
    private static double scalingFactor = Scaling.computeDPIScale();
    
    private static final double NODE_HEIGHT = 200 * scalingFactor;
    private static final double NODE_WIDTH = 50 * scalingFactor;
    
    private static final int LEVEL1 = (int)(-350 * scalingFactor);
    private static final int LEVEL2 = (int)(250 * scalingFactor) + LEVEL1;
    private static final int SPACING = initSpacing();

    private List<BrickNode> list;
    private final int currentMax;
    
    /**
     * Create dynamic nodes manager class
     * @param inputArray array with input values
     * @param max maximal defined value
     */
    public DynamicNodes(int[] inputArray, int max){
        this.currentMax = max;
        this.list = createList(inputArray, max);
    }
    
    /**
     * Get graphic nodes
     * @return list of nodes
     */
    public List<BrickNode> getNodes(){
        return list;
    }
    
    private static int countIndent(int number){
        return (int)(((double)number / 2) * -SPACING);
    }
    
    private List<BrickNode> createList(int[] inputArray, int currentMax){
        list = new ArrayList<>();
        for(int i = 0; i < inputArray.length; i++){
            BrickNode node = createValueNode(i, inputArray[i], currentMax, ViewController.countIndent(inputArray.length));
            
            list.add(node);
        }
        return list;
    }
    
    private BrickNode createValueNode(int i, int value, int currentMax, int leftIndent) {
        return createCustomNode(i, value, currentMax, Constants.DEFAULT, leftIndent, ViewController.LEVEL1);
    }
    
    /**
     * Create labels with position marks
     * @return list of text labels
     */
    public List<Text> createLabels(){
        return createLabelsList(list.size(), 1, LEVEL1 + 30);
    }
    
    /**
     * Create labels with number values for placeholders
     * @return list of text labels
     */
    public List<Text> createPlaceholderLabels(){
        return createLabelsList(currentMax, 0, LEVEL2 + 30);
    }
    
    private List<Text> createLabelsList(int count, int step, int y){
        List<Text> labels = new ArrayList<>();
        int currentValue = 0;
        int leftIndent = countIndent(count);
        for(int i = 0; i < count; i++){
            Text label = new Text(String.valueOf(currentValue));
            StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
            label.setTranslateX(SPACING * i + leftIndent);
            label.setTranslateY(y);
            label.fontProperty().set(Constants.font);
            labels.add(label);
            currentValue += step;
        }
        return labels;
    }
    
    /**
     * Create placeholders for dynamic nodes. Values starting from zero with
     * step of size one
     * @param count number of placeholders
     * @return nodes that represent placeholders
     */
    public List<BrickNode> createPlaceHolders(int count){
        List<BrickNode> subList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BrickNode stackPane = createCustomNode(i, i, count, Constants.PLACEHOLDER, 
                    countIndent(count), LEVEL2);
            subList.add(stackPane);
        }
        return subList;
    }
    
    private BrickNode createCustomNode(int i, int value, int currentMax, 
            Color color, double leftIndent, double topIndent) {
        int num = value;
        double percent = (double)num / currentMax;
        if(percent < 0.07){
            percent = 0;
        }
        Rectangle rectangle = new Rectangle(NODE_WIDTH, (percent * NODE_HEIGHT) + 5);
        rectangle.setFill(color);
        
        Text text = new Text(String.valueOf(num));
        text.setFont(ViewController.font);
        BrickNode node = new BrickNode(num);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        //node.setId(String.valueOf(num));
        //stackPane.setValue(num);
        node.getChildren().addAll(rectangle, text);
        BrickNode.setAlignment(text, Pos.BOTTOM_CENTER);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(ViewController.SPACING * i + leftIndent);
        node.setTranslateY(topIndent);
        node.setShape(rectangle);
        return node;
    }
    
    /**
     * Get absolute minimal height of window needed for nodes to be in viewport
     * @return height in pixels
     */
    public double getViewportMinHeight() {
        return LEVEL1 * -1 + NODE_HEIGHT;
    }
    
    private static int initSpacing() {
        return (int)(60 * scalingFactor);
    }
}
