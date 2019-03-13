/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author Mykhailo Klunko
 */
public class Tree {
    
    List<BrickNode> treeNodes;
    
    private int countSpacing(int nodeLevel){
        return  (int) Math.pow(2, nodeLevel);
    }
    
    private int getLevel(int arrayPosition){
        int position = arrayPosition;
        int level = 0;
        
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
    
    private BrickNode createCustomNode(int i, int value, int currentMax, 
            Color color, double leftIndent, double topIndent) {
        int num = value;
        double percent = (double)num / currentMax;
        Rectangle rectangle = new Rectangle(50, (percent * 10 * 20) + 5);
        rectangle.setFill(color);
        
        Text text = new Text(String.valueOf(num));
        //text.setFont(font);
        BrickNode node = new BrickNode(num);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        //node.setId(String.valueOf(num));
        //stackPane.setValue(num);
        node.getChildren().addAll(rectangle, text);
        BrickNode.setAlignment(text, Pos.BOTTOM_CENTER);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(countSpacing(getLevel(i)) * i + leftIndent);
        node.setTranslateY(topIndent);
        node.setShape(rectangle);
        return node;
    }
}
