/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.scene.layout.Pane;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class CountingSort {
    
    public static List<Animation> countingSort(ArrayList<BrickNode> list, int maxValue, Pane codePane) { 
        List<Animation> anim = new ArrayList<>();
         // array of 0's at indices 0...maxValue
        int[] count = new int[maxValue + 1];
        for(int i : count){
            count[i] = 0;
        }
        
        // populate numCounts
        for(int i = 0; i < list.size(); i++){
            anim.add(AnimUtils.moveDownToX(list.get(i), i, list.get(i).getValue(), 
                    ViewController.LEFT_INDENT, ViewController.DEFAULT_LEFT_INDENT));
        }
        for (BrickNode num : list) {
            ++count[num.getValue()];
        }

        for(int i = 1; i < count.length; i++){
            count[i] += count[i - 1]; 
        }
        
        
        // populate the final sorted array
        //int[] sortedArray = new int[theArray.length];
        
        
        BrickNode[] sorted = new BrickNode[list.size()];
        // for each num in numCounts
        for(int i = list.size() - 1; i >= 0; i--){
            sorted[count[list.get(i).getValue()] - 1] = list.get(i);
            anim.add(AnimUtils.moveUpToX(list.get(i), list.get(i).getValue(), count[list.get(i).getValue()] - 1, 
                    ViewController.DEFAULT_LEFT_INDENT, ViewController.LEFT_INDENT));
            count[list.get(i).getValue()]--;
        }
        return anim;
    }
}
