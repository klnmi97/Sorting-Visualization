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
import javafx.scene.paint.Color;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class QuickSort {

    public static List<Animation> quickSort(ArrayList<BrickNode> list, Pane codePane){
        List<Animation> anim = new ArrayList<>();
        Pseudocode pc = new Pseudocode();
        addPseudocode(codePane, pc);
        sort(list, 0, list.size()-1, anim, pc);
        return anim;
    } 
     
    private static void sort(ArrayList<BrickNode> list, int low, int high, List<Animation> anim, Pseudocode code) 
    { 
        addAnimToList(anim, code.selectLine(1));
        if (low < high) 
        { 
            /* pi is partitioning index, arr[pi] is  
              now at right place */
            int partitionIndex = partition(list, low, high, anim, code);
            
            addAnimToList(anim, code.selectLine(3));
            sort(list, low, partitionIndex-1, anim, code); 
            addAnimToList(anim, code.selectLine(4));
            sort(list, partitionIndex+1, high, anim, code); 
        }
        else if(low == high){
            anim.add(AnimUtils.setColor(list.get(high), ViewController.DEFAULT, ViewController.SORTED));
        }
    } 
     
    private static int partition(ArrayList<BrickNode> list, int low, int high, 
            List<Animation> sq, Pseudocode code) 
    { 
        ParallelTransition parallelTransition = new ParallelTransition();
        for (int k = low; k <= high; k++) {
            parallelTransition.getChildren().add(AnimUtils.moveDownToX(list.get(k), k, k));            
        }
        sq.add(AnimUtils.makeParallel(
                parallelTransition, 
                code.selectLine(2)));
        BrickNode pivot = list.get(high);
        sq.add(AnimUtils.setColor(list.get(high), 
                ViewController.DEFAULT, Color.RED));
        
        int i = (low-1); // index of smaller element
        //sq.add(setColor(list.get(i+1), Color.LIGHTSKYBLUE, Color.YELLOW));
        
        for (int j=low; j<high; j++) 
        { 
            // If current element is smaller than or 
            // equal to pivot 
            sq.add(AnimUtils.setColor(list.get(j), 
                    ViewController.DEFAULT, ViewController.COMPARE));
            if (list.get(j).getValue() <= pivot.getValue()) 
            { 
                
                i++;
                //sq.add(setColor(list.get(i), Color.LIGHTSKYBLUE, Color.YELLOW));
                // swap arr[i] and arr[j] 
                if(i != j){
                    sq.add(AnimUtils.swap(list.get(i), list.get(j), i, j));
                }
                //sq.add(setColor(list.get(i), Color.YELLOW, Color.LIGHTSKYBLUE));
                BrickNode temp = list.get(i); 
                list.set(i, list.get(j)); 
                list.set(j, temp);
                sq.add(AnimUtils.setColor(list.get(i), 
                        ViewController.COMPARE, ViewController.DEFAULT));
                
            } else{
                sq.add(AnimUtils.setColor(list.get(j), 
                        ViewController.COMPARE, ViewController.DEFAULT));
            } 
            
        } 
  
        // swap arr[i+1] and arr[high] (or pivot)
        sq.add(AnimUtils.swap(list.get(i+1), list.get(high), i+1, high));
        BrickNode temp = list.get(i+1); 
        list.set(i+1, list.get(high)); 
        list.set(high, temp);
        sq.add(AnimUtils.setColor(pivot, Color.RED, ViewController.SORTED));
        
        parallelTransition = new ParallelTransition();
        for (int k = low; k <= high; k++) {
            parallelTransition.getChildren().add(AnimUtils.moveNodeUp(list.get(k)));            
        }
        sq.add(parallelTransition);
        return i+1; 
    }
    
    private static void addPseudocode(Pane codePane, Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(codePane, 
                "QuickSort(arr, low, high):",
                "  if low < high",
                "    mid = Partition(arr, low, high)",
                "    QuickSort(arr, low, mid - 1)",
                "    QuickSort(arr, mid + 1, high)",
                "",
                "Partition(arr, low, high):",
                "");
    }
    
    private static void addAnimToList(List<Animation> animList, Animation... anims){
        for(Animation anim : anims){
            if(anim != null){
                animList.add(anim);
            }
        }
    }
}
