/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NodeCreation;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sortingvisualization.BrickNode;
import sortingvisualization.Scaling;
import sortingvisualization.ViewController;

/**
 *
 * @author mihae
 */
public class DynamicNodes {
    
    private List<BrickNode> list;
    private static double scalingFactor = Scaling.computeDPIScale();
    
    public DynamicNodes(){
        list = new ArrayList<>();
    }
    
    public List<BrickNode> createList(int[] inputArray, int currentMax){
        list = new ArrayList<>();
        for(int i = 0; i < inputArray.length; i++){
            BrickNode node = createValueNode(i, inputArray[i], currentMax, ViewController.countIndent(inputArray.length));
            
            list.add(node);
        }
        return list;
    }
    
    private BrickNode createValueNode(int i, int value, int currentMax, int leftIndent) {
        return createCustomNode(i, value, currentMax, ViewController.DEFAULT, leftIndent, ViewController.LEVEL1);
    }
    
    private BrickNode createCustomNode(int i, int value, int currentMax, 
            Color color, double leftIndent, double topIndent) {
        int num = value;
        double percent = (double)num / currentMax;
        Rectangle rectangle = new Rectangle(50 * scalingFactor, (percent * 10 * 20 * scalingFactor) + 5);
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
}
