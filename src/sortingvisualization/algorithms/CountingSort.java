/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class CountingSort extends Sorting implements AbstractAlgorithm {
    
    private List<BrickNode> list;
    private Pseudocode pc;
    private List<Text> counters;
    
    /**
     * Counting Sort animation manager
     * @param list list with graphic elements to sort
     * @param counters label counters for each value of item range
     * @param infoPane pane to add code graphic representation to
     */
    public CountingSort(List<BrickNode> list, List<Text> counters, Pane infoPane){
        this.list = list;
        this.pc = new Pseudocode();
        this.counters = counters;
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates a full list of sorting process animation
     * @return list with animations
     */
    @Override
    public List<Animation> sort() { 
        List<Animation> anim = new ArrayList<>();
        
         // array of 0's at indices 0...maxValue
        int[] count = new int[ViewController.CNT_MAX];
        for(int i : count) {
            count[i] = 0;
        }
        
        for (BrickNode num : list) {
            ++count[num.getValue()];
            //animation part
            int value = count[num.getValue()];
            
            String oldValue = Integer.toString(value-1);
            String newValue = Integer.toString(value);
            addAnimations(anim, 
                    AnimUtils.moveDownToX(num, list.indexOf(num), num.getValue(),
                            ViewController.LEFT_INDENT, ViewController.TEN_LEFT_INDENT),
                    AnimUtils.setText(counters.get(num.getValue()), oldValue, newValue),
                    pc.selectLine(2));
        }
        
        for(int i = 1; i < count.length; i++) {
            
            String oldVal = Integer.toString(count[i]);
            
            count[i] += count[i - 1]; 
            
            String newVal = Integer.toString(count[i]);
            addAnimations(anim, 
                    AnimUtils.setText(counters.get(i), oldVal, newVal),
                    pc.selectLine(4));
        }
        
        
        BrickNode[] sorted = new BrickNode[list.size()];
        // for each num in numCounts
        for(int i = list.size() - 1; i >= 0; i--){
            count[list.get(i).getValue()]--;
            
            String oldVal = Integer.toString(count[list.get(i).getValue()] + 1);
            String newVal = Integer.toString(count[list.get(i).getValue()]);
            addAnimations(anim, 
                    AnimUtils.setText(counters.get(list.get(i).getValue()), oldVal, newVal),
                    pc.selectLine(6));
            
            sorted[count[list.get(i).getValue()]] = list.get(i);
            
            addAnimations(anim, 
                    AnimUtils.moveUpToX(list.get(i), list.get(i).getValue(), count[list.get(i).getValue()], 
                    ViewController.TEN_LEFT_INDENT, ViewController.LEFT_INDENT),
                    pc.selectLine(7));
        }
        addAnimations(anim, pc.unselectAll());
        return anim;
    }

    private void addPseudocode(Pseudocode code) {
        code.addLines(
                "create a counting array size of max value - 1",
                "for each element in the initial array",
                "  increase the corresponding counter by 1",
                "for each counter in counting array",
                "  currentCounter += prevoiusCounter",
                "for each element in the initial array",
                "  decrease the corresponding counter by 1",
                "  move currentElement to result[current counter]");
    }

    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
