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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class CountingSort {
    
    public static List<Animation> countingSort(ArrayList<BrickNode> list, List<Label> counters, int maxValue, Pane codePane) { 
        List<Animation> anim = new ArrayList<>();
         // array of 0's at indices 0...maxValue
        int[] count = new int[maxValue];
        for(int i : count){
            count[i] = 0;
        }
        
        for (BrickNode num : list) {
            ++count[num.getValue()];
            //animation part
            //make parallel right
            int value = count[num.getValue()];
            String oldValue = Integer.toString(value-1);
            String newValue = Integer.toString(value);
            ParallelTransition pt = new ParallelTransition(
                    AnimUtils.moveDownToX(num, list.indexOf(num), num.getValue(), 
                    ViewController.LEFT_INDENT, ViewController.DEFAULT_LEFT_INDENT),
            AnimUtils.setText(counters.get(num.getValue()), oldValue, newValue));
            anim.add(pt);
        }
        System.out.println(count.length);
        for(int i = 1; i < count.length; i++){
            String oldVal = Integer.toString(count[i]);
            count[i] += count[i - 1]; 
            String newVal = Integer.toString(count[i]);
            anim.add(AnimUtils.setText(counters.get(i), oldVal, newVal));
        }
        
        
        BrickNode[] sorted = new BrickNode[list.size()];
        // for each num in numCounts
        for(int i = list.size() - 1; i >= 0; i--){
            sorted[count[list.get(i).getValue()] - 1] = list.get(i);
            anim.add(AnimUtils.moveUpToX(list.get(i), list.get(i).getValue(), count[list.get(i).getValue()] - 1, 
                    ViewController.DEFAULT_LEFT_INDENT, ViewController.LEFT_INDENT));
            count[list.get(i).getValue()]--;
            String oldVal = Integer.toString(count[list.get(i).getValue()] + 1);
            String newVal = Integer.toString(count[list.get(i).getValue()]);
            anim.add(AnimUtils.setText(counters.get(list.get(i).getValue()), oldVal, newVal));
            
        }
        return anim;
    }
}
