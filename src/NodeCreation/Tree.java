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
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class Tree {
    
    private static final int RADIUS = 25;
    private static final int LEVEL_HEIGHT = 100;
    
    private final int SPACING;
    private final double SPACING_COEF;
    
    private final List<BrickNode> treeNodes;
    
    public Tree(int[] inputArray){
        this.SPACING = countSecondLevelSpacing(inputArray);
        this.SPACING_COEF = countSpacingCoefficient(inputArray);
        this.treeNodes = createList(inputArray);
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
}
