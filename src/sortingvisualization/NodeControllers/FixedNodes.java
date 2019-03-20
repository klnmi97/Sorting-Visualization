/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.NodeControllers;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sortingvisualization.Utilities.Scaling;
import sortingvisualization.Controllers.ViewController;

/**
 *
 * @author mihae
 */
public class FixedNodes {
    
    private static double scalingFactor = Scaling.computeDPIScale();
    private List<BrickNode> list;
    
    public FixedNodes(){
        list = new ArrayList<>();
    }
    
    public List<BrickNode> createList(int[] inputArray, int currentMax){
        list = new ArrayList<>();
        int leftIndent = ViewController.countIndent(inputArray.length);
        for(int i = 0; i < inputArray.length; i++){
            BrickNode node = createFixedNode(i, inputArray[i], leftIndent, ViewController.LEVEL1);
            
            list.add(node);
        }
        return list;
    }
    
    private BrickNode createFixedNode(int i, int value, double leftIndent, double topIndent){
        Rectangle rectangle = new Rectangle(50 * scalingFactor, 30 * scalingFactor);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.WHITE);
        
        Text text = new Text(String.valueOf(value));
        text.setFont(ViewController.font);
        BrickNode node = new BrickNode(value);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        HBox numberBox = new HBox();
        String valueToSet = String.valueOf(value);
        int index = valueToSet.toCharArray().length - 1;
        for(char ch: valueToSet.toCharArray()){
            Text digit = new Text(Character.toString(ch));
            digit.setUserData(String.valueOf(index));
            digit.setFont(ViewController.font);
            numberBox.getChildren().add(digit);
            numberBox.setAlignment(Pos.BOTTOM_CENTER);
            node.getDigits().add(digit);
            index--;
        }
        node.getChildren().addAll(rectangle, numberBox);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(ViewController.SPACING * i + leftIndent);
        node.setTranslateY(topIndent);
        node.setShape(rectangle);
        return node;
    }
}
