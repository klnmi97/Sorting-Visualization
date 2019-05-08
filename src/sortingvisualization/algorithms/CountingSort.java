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
import sortingvisualization.Constants.Constants;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.NodeControllers.DynamicNodes;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 * Class for creation animation flow(code, sorting, variables) for 
 * Counting sorting algorithm.
 * @author Mykhailo Klunko
 */
public class CountingSort extends Sorting implements AbstractAlgorithm {
    
    private final DynamicNodes mngr;
    private final List<BrickNode> list;
    private final Pseudocode code;
    private final List<Text> counters;
    private final VariablesInfo vars;
    
    /**
     * Counting Sort animation manager
     * @param manager node manager
     * @param counters label counters for each value of item range
     * @param vars instance of variables information class
     * @param infoPane pane to add code graphic representation to
     */
    public CountingSort(DynamicNodes manager, List<Text> counters, VariablesInfo vars, Pane infoPane) {
        this.mngr = manager;
        this.list = manager.getNodes();
        this.code = new Pseudocode();
        this.counters = counters;
        this.vars = vars;
        addPseudocode(code);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates a full list of sorting process animation
     * @return list with animations
     */
    @Override
    public List<Animation> sort() { 
        List<Animation> anim = new ArrayList<>();
        
        int max = Constants.CNT_MAX + 1;
        // array of 0's at indices 0...maxValue
        int[] count = new int[max];
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
                    mngr.moveDownToX(num, list.indexOf(num), num.getValue(),
                            mngr.getUpperLevelIndent(), mngr.countIndent(max)),
                    mngr.setText(counters.get(num.getValue()), oldValue, newValue),
                    code.selectLine(3),
                    vars.setText("Increase counter of " + num + " by one from " 
                            + oldValue + " to " + newValue));
        }
        
        for(int i = 1; i < count.length; i++) {
            
            String oldVal = Integer.toString(count[i]);
            
            count[i] += count[i - 1]; 
            
            String newVal = Integer.toString(count[i]);
            addAnimations(anim, 
                    mngr.setText(counters.get(i), oldVal, newVal),
                    code.selectLine(5),
                    vars.setText("Add counter of " + (i - 1) + " to the counter of " + i 
                            + "\nCounter of " + i + " is " + oldVal + " + " + count[i - 1]));
        }
        
        
        BrickNode[] sorted = new BrickNode[list.size()];
        // for each num in numCounts
        for(int i = list.size() - 1; i >= 0; i--){
            count[list.get(i).getValue()]--;
            
            String oldVal = Integer.toString(count[list.get(i).getValue()] + 1);
            String newVal = Integer.toString(count[list.get(i).getValue()]);
            addAnimations(anim, 
                    mngr.setText(counters.get(list.get(i).getValue()), oldVal, newVal),
                    code.selectLine(7),
                    vars.setText("Iterating through the initial array, index = " + i
                            + "\nCounter of " + list.get(i) + " was decreased by one and now is " + newVal));
            
            sorted[count[list.get(i).getValue()]] = list.get(i);
            
            addAnimations(anim, 
                    mngr.moveUpToX(list.get(i), list.get(i).getValue(), count[list.get(i).getValue()], 
                    mngr.countIndent(max), mngr.getUpperLevelIndent()),
                    code.selectLine(8),
                    vars.setText("Move " + list.get(i) + " to the " + count[list.get(i).getValue()] 
                            + ". position"));
        }
        addAnimations(anim, code.unselectAll(),
                vars.setText("Array is sorted"));
        return anim;
    }

    private void addPseudocode(Pseudocode code) {
        code.addLines(
                "CountingSort(A):",
                " create a counting array size of max value - 1",
                " for each element in the initial array",
                "   increase the corresponding counter by 1",
                " for each counter in counting array",
                "   currentCounter += prevoiusCounter",
                " for each element in the initial array",
                "   decrease the corresponding counter by 1",
                "   move currentElement to result[current counter]");
    }

    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(code.getCode());
        });
    }
}
