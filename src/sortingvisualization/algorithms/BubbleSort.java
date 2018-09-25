/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class BubbleSort {

    
    public static List<Animation> bubbleSort(ArrayList<BrickNode> list, Pane codePane) { 
        List<Animation> anim = new ArrayList<>();
        ParallelTransition parallelTransition;
        int n = list.size();  
        BrickNode temp;
        List<StackPane> codeLines = new ArrayList<>();
        
        addPseudocode(codePane, codeLines);
        for(int i=0; i < n; i++){  
            parallelTransition = new ParallelTransition();
            for(int j=1; j < (n-i); j++){ 
                //select elements to compare (anim)
                if(j == 1){
                    anim.add(AnimUtils.selectNodes(list.get(j-1), list.get(j)));
                } else {
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j), ViewController.DEFAULT, 
                            ViewController.COMPARE));
                    anim.add(parallelTransition);
                }
                if(list.get(j-1).getValue() > list.get(j).getValue()){  
                    //swap elements  
                    anim.add(AnimUtils.swap(list.get(j), list.get(j-1), j, j - 1));
                    temp = list.get(j-1);  
                    list.set(j-1, list.get(j));  
                    list.set(j, temp);
                }  
                //unselect (anim)
                if(j == n - i - 1){
                    anim.add(new ParallelTransition(
                            AnimUtils.setColor(list.get(j-1), 
                                ViewController.COMPARE, ViewController.DEFAULT),
                            AnimUtils.setColor(list.get(n-i-1), 
                                ViewController.COMPARE, ViewController.SORTED)));
                } else {
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j-1), ViewController.COMPARE, 
                            ViewController.DEFAULT));
                }
            }
        }
        anim.add(AnimUtils.setColor(list.get(0), 
                ViewController.DEFAULT, ViewController.SORTED));
        return anim;
    } 
    
    private static void addPseudocode(Pane pane, List<StackPane> code){
        //TODO: improve pseudocode
        code.add(AnimUtils.createLine("for i = 0 to sizeOfArray-1"));
        code.add(AnimUtils.createLine("  for j = 1 to lastUnsortedElement-1"));
        code.add(AnimUtils.createLine("    if leftElement > rightElement"));
        code.add(AnimUtils.createLine("      swap(leftElement, rightElement)"));
        pane.getChildren().addAll(code);
    }
    
}
