/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;
import sortingvisualization.ViewController;

/**
 *
 * @author mihae
 */
public class RadixSort {
    
    List<BrickNode> list;
    Pseudocode pc;
    
    public RadixSort(List<BrickNode> list, Pane infoPane){
        this.list = list;
        pc = new Pseudocode();
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    public List<Animation> sort(){
        List<Animation> anim = new ArrayList<>();
        
        //TODO: move to parent class
        // Determine minimum and maximum values
        int minValue = list.get(0).getValue();
        int maxValue = list.get(0).getValue();
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getValue() < minValue) {
                minValue = list.get(i).getValue();
            } else if (list.get(i).getValue() > maxValue) {
                maxValue = list.get(i).getValue();
            }
        }
        
        for (int exp = 1, i = 0; maxValue/exp > 0; exp *= 10, i++){ 
            anim.add(AnimUtils.makeParallel(
                    AnimUtils.setDigitsColor(list, i, Color.BLACK, Color.RED),
                    pc.selectLine(1)));
            anim.add(pc.selectLine(2));
            countSort(list, list.size(), exp, anim);
            anim.add(AnimUtils.setDigitsColor(list, i, Color.RED, Color.BLACK));
        }
        anim.add(pc.unselectAll());
        return anim;
    }
    
    
    static void countSort(List<BrickNode> list, int n, int exp, List<Animation> anim){ 
        //int output[] = new int[n]; // output array 
        BrickNode output[] = new BrickNode[n];
        int i; 
        int count[] = new int[10];
        int helperCount[] = new int[10];
        Arrays.fill(count,0); 
  
        // Store count of occurrences in count[] 
        for (i = 0; i < n; i++){ 
            int bucket = (list.get(i).getValue()/exp)%10;
            count[bucket]++; 
            anim.add(AnimUtils.moveTo(list.get(i), i, bucket, count[bucket] - 1, 
                    ViewController.LEFT_INDENT, ViewController.countIndent(10)));
        }
  
        //fill helper array for animation
        for(i = 0; i < 10; i++){
            helperCount[i] = count[i];
        }
        
        // Change count[i] so that count[i] now contains 
        // actual position of this digit in output[] 
        for (i = 1; i < 10; i++) {
            count[i] += count[i - 1]; 
        }
            
  
        // Build the output array 
        for (i = n - 1; i >= 0; i--) 
        { 
           
            output[count[ (list.get(i).getValue()/exp)%10 ] - 1] = list.get(i);
            anim.add(AnimUtils.moveFrom(list.get(i), count[ (list.get(i).getValue()/exp)%10 ] - 1, 
                    (list.get(i).getValue()/exp)%10, 
                    helperCount[(list.get(i).getValue()/exp)%10] - 1, 
                    ViewController.countIndent(10), ViewController.LEFT_INDENT));
            count[ (list.get(i).getValue()/exp)%10 ]--; 
            helperCount[(list.get(i).getValue()/exp)%10]--;
        } 
  
        // Copy the output array to arr[], so that arr[] now 
        // contains sorted numbers according to curent digit 
        for (i = 0; i < n; i++) 
            list.set(i, output[i]); 
    }
    
    private static void addPseudocode(Pseudocode code) {
        code.addLines(
                "d is max number of digits",
                "for i = 1 to d:",
                "  do Counting(Stable) Sort for i-th digit");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
