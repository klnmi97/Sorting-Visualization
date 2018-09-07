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
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class BubbleSort {

    
    public static List<Animation> bubbleSort(ArrayList<BrickNode> list, List<Animation> sq) {  
        ParallelTransition parallelTransition;
        int n = list.size();  
        BrickNode temp;
        for(int i=0; i < n; i++){  
            parallelTransition = new ParallelTransition();
            for(int j=1; j < (n-i); j++){ 
                //select elements to compare (anim)
                if(j == 1){
                    sq.add(AnimUtils.selectNodes(list.get(j-1), list.get(j)));
                } else {
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j), ViewController.DEFAULT, 
                            ViewController.COMPARE));
                    sq.add(parallelTransition);
                }
                if(list.get(j-1).getValue() > list.get(j).getValue()){  
                    //swap elements  
                    sq.add(AnimUtils.swap(list.get(j), list.get(j-1), j, j - 1));
                    temp = list.get(j-1);  
                    list.set(j-1, list.get(j));  
                    list.set(j, temp);
                }  
                //unselect (anim)
                if(j == n - i - 1){
                    sq.add(AnimUtils.unselectNodes(list.get(j-1), list.get(j)));
                } else {
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j-1), ViewController.COMPARE, 
                            ViewController.DEFAULT));
                }
            }
            sq.add(AnimUtils.setColor(list.get(n-i-1), 
                    ViewController.DEFAULT, ViewController.SORTED));
        }
        return sq;
    } 
    
}
