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
import sortingvisualization.BrickNode;

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
            count[list.get(i).getValue()]--;
        }
        for(BrickNode i : sorted){
            System.out.println(i.getValue());
        }
        return anim;
    }
}
