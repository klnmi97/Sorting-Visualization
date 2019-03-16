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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class Tree {
    
    private static final int RADIUS = 25;
    private static final int LEVEL_HEIGHT = 100;
    private List<BrickNode> treeNodes;
    private int SPACING;
    private double SPACING_COEF;
    
    public List<BrickNode> createList(int[] inputArray, int currentMax){
        this.SPACING = countSecondLevelSpacing(inputArray);
        this.SPACING_COEF = countSpacingCoefficient(inputArray);
        
        this.treeNodes = new ArrayList<>();
        for(int i = 0; i < inputArray.length; i++){
            BrickNode node = createCustomNode(i, inputArray[i], currentMax, Color.BLACK, getNodeX(i), getNodeY(i, inputArray.length));
            treeNodes.add(node);
        }
        return treeNodes;
    }
    
    public int getNodeX(int posInArray){
        double x = countSpacing(getLevel(posInArray)) 
                * getPositionInLevel(posInArray) 
                + getLevelIndent(getLevel(posInArray));
        return (int)x;
    }
    
    public int getNodeY(int posInArray, int arraySize){
        int y = (getNuberOfLevels(arraySize) 
                - getLevel(posInArray) + 1) 
                * -LEVEL_HEIGHT;
        return y;
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
    
    private BrickNode createCustomNode(int i, int value, int currentMax, 
            Color color, double leftIndent, double bottomIndent) {
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
        node.setTranslateX(leftIndent);
        node.setTranslateY(bottomIndent);
        node.setShape(body);
        return node;
    }
}
