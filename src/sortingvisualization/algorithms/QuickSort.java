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
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.AnimUtils;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.Controllers.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class QuickSort extends Sorting implements AbstractAlgorithm {

    List<BrickNode> list;
    Pseudocode pc;
    
    public QuickSort(List<BrickNode> list, Pane infoPane){
        this.list = list;
        pc = new Pseudocode();
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    @Override
    public List<Animation> sort(){
        List<Animation> anim = new ArrayList<>();
        
        quickSort(list, 0, list.size()-1, anim, pc);
        addAnimToList(anim, pc.unselectAll());
        return anim;
    } 
     
    private void quickSort(List<BrickNode> list, int low, int high, List<Animation> anim, Pseudocode code) 
    { 
        addAnimToList(anim, code.selectLine(1));
        if (low < high) 
        { 
            /* pi is partitioning index, arr[pi] is  
              now at right place */
            int partitionIndex = partition(list, low, high, anim, code);
            
            addAnimToList(anim, code.selectLine(3));
            quickSort(list, low, partitionIndex-1, anim, code); 
            addAnimToList(anim, code.selectLine(4));
            quickSort(list, partitionIndex+1, high, anim, code); 
        }
        else if(low == high){
            anim.add(AnimUtils.setColor(list.get(high), Constants.DEFAULT, Constants.SORTED));
        }
    } 
     
    private static int partition(List<BrickNode> list, int low, int high, 
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
        
        sq.add(AnimUtils.makeParallel(
                AnimUtils.setColor(list.get(high), Constants.DEFAULT, Color.RED),
                code.selectLines(7,8,9)));
        
        int i = (low-1); // index of smaller element
        //sq.add(setColor(list.get(i+1), Color.LIGHTSKYBLUE, Color.YELLOW));
        
        for (int j=low; j<high; j++) 
        { 
            // If current element is smaller than or 
            // equal to pivot 
            sq.add(AnimUtils.makeParallel(
                    AnimUtils.setColor(list.get(j), Constants.DEFAULT, Constants.COMPARE),
                    code.selectLine(10)));
            if (list.get(j).getValue() <= pivot.getValue()) 
            { 
                
                i++;
                //if(i != j){
                    sq.add(AnimUtils.makeParallel(AnimUtils.swap(list.get(i), list.get(j), i, j),
                            code.selectLine(11)));
                //} else{
                //    addAnimToList(sq, code.selectLine(11));
                //}
                //sq.add(setColor(list.get(i), Color.YELLOW, Color.LIGHTSKYBLUE));
                BrickNode temp = list.get(i); 
                list.set(i, list.get(j)); 
                list.set(j, temp);
                sq.add(AnimUtils.makeParallel(
                        AnimUtils.setColor(list.get(i), Constants.COMPARE, Constants.DEFAULT),
                        code.selectLine(12)));
                
            } else{
                sq.add(AnimUtils.setColor(list.get(j), Constants.COMPARE, Constants.DEFAULT));
            } 
            
        } 
  
        // swap arr[i+1] and arr[high] (or pivot)
        sq.add(AnimUtils.makeParallel(
                AnimUtils.swap(list.get(i+1), list.get(high), i+1, high),
                code.selectLine(13)));
        BrickNode temp = list.get(i+1); 
        list.set(i+1, list.get(high)); 
        list.set(high, temp);
        sq.add(AnimUtils.setColor(pivot, Color.RED, Constants.SORTED));
        
        parallelTransition = new ParallelTransition();
        for (int k = low; k <= high; k++) {
            parallelTransition.getChildren().add(AnimUtils.moveNodeUp(list.get(k)));            
        }
        sq.add(AnimUtils.makeParallel(
                parallelTransition,
                code.selectLine(14)));
        return i+1; 
    }
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(
                "QuickSort(arr, low, high):",
                "  if low < high",
                "    mid = Partition(arr, low, high)",
                "    QuickSort(arr, low, mid - 1)",
                "    QuickSort(arr, mid + 1, high)",
                "",
                "Partition(arr, low, high):",
                "  pivot = arr[high]",
                "  index = (low - 1)",
                "  for j = low to high - 1",
                "    if arr[j] ≤ pivot",
                "      swap(arr[j], arr[index])",
                "      index++",
                "  swap(arr[index + 1], pivot)",
                "  return index + 1");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
