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
    
    private List<BrickNode> treeNodes;
    private int SPACING;
    private double SPACING_COEF;
    
    public List<BrickNode> createList(int[] inputArray, int currentMax){
        this.SPACING = countSecondLevelSpacing(inputArray);
        this.SPACING_COEF = countSpacingCoefficient(inputArray);
        
        this.treeNodes = new ArrayList<>();
        int levels = getNuberOfLevels(inputArray);
        for(int i = 0; i < inputArray.length; i++){
            System.out.print(inputArray[i] + ",");
            int currentLevel = getLevel(i);
            int currentLvlReversed = levels - currentLevel;
            int currentBottomIndent = (currentLvlReversed + 1) * -100;
            BrickNode node = createCustomNode(i, inputArray[i], currentMax, Color.WHITE, getLevelIndent(currentLevel), currentBottomIndent);
            
            treeNodes.add(node);
        }
        return treeNodes;
    }
    
    private double getLevelIndent(int level){
        return ((double)(getLevelWidth(level) - 1) / 2) * -countSpacing(level);
    }
    
    private int countSecondLevelSpacing(int[] array){
        int levels = getNuberOfLevels(array);
        return 65 * levels;
    }
    
    private double countSpacingCoefficient(int[] array){
        int levels = getNuberOfLevels(array);
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
    
    private int getNuberOfLevels(int[] treeArray){
        return getLevel(treeArray.length - 1);
    }
    
    private int getLevelWidth(int level){
        return (int)Math.pow(2, level - 1);
    }
    
    private BrickNode createCustomNode(int i, int value, int currentMax, 
            Color color, double leftIndent, double bottomIndent) {
        int num = value;
        double percent = (double)num / currentMax;
        Circle body = new Circle(25);
        body.setFill(color);
        body.setStroke(Color.BLACK);
        body.setStrokeWidth(3);
        Text text = new Text(String.valueOf(num));
        text.setFont(ViewController.font);
        text.setTranslateY(-10);
        BrickNode node = new BrickNode(num);
        node.setPrefSize(body.getRadius() * 2, body.getRadius() * 2);
        //node.setId(String.valueOf(num));
        //stackPane.setValue(num);
        node.getChildren().addAll(body, text);
        //BrickNode.setAlignment(text, Pos.BOTTOM_CENTER);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(countSpacing(getLevel(i)) * getPositionInLevel(i) + leftIndent);
        node.setTranslateY(bottomIndent);
        node.setShape(body);
        return node;
    }
}
