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
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.AnimUtils;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 * Class for creation animation flow(code, sorting, variables) for 
 * the Bubble sorting algorithm.
 * @author Mykhailo Klunko
 */
public class BubbleSort extends Sorting implements AbstractAlgorithm{

    private final List<BrickNode> list;
    private final Pseudocode code;
    private final VariablesInfo vars;
    
    /**
     * Creates a new instance of the Bubble Sort algorithm animation flow 
     * creator class
     * @param list list of nodes to animate
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public BubbleSort(List<BrickNode> list, VariablesInfo vars, Pane infoPane){
        this.list = list;
        this.code = new Pseudocode();
        this.vars = vars;
        addPseudocode(code);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates animation flow for the Bubble sorting algorithm
     * @return list of animation steps
     */
    @Override
    public List<Animation> sort() { 
        List<Animation> anim = new ArrayList<>();
        ParallelTransition parallelTransition;
        int n = list.size();  
        BrickNode temp;
        
        addAnimations(anim, code.selectLine(1),
                            vars.setText("Iterate from %d to %d", 0, (n - 1)));
        
        for(int i=0; i < n; i++) {
            
            parallelTransition = new ParallelTransition();
            if((n - i - 1) != 0){
                addAnimations(anim, code.selectLine(2),
                                    vars.setText("Iterate from %d to %d, i = %d", 
                                            1, (n - i - 1), i));
            }
            for(int j = 1; j < (n - i); j++){ 
                
                //select elements to compare (anim)
                if(j == 1) {
                    
                    addAnimations(anim, code.selectLine(3),
                            vars.setText("Checking if %s > %s, j = %d", 
                                    list.get(j - 1), list.get(j), j),
                            AnimUtils.selectNodes(list.get(j - 1), list.get(j)));
                    
                } else {
                    
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j), Constants.DEFAULT, 
                            Constants.COMPARE));
                    addAnimations(anim, parallelTransition, 
                                        code.selectLine(3),
                                        vars.setText("Checking if %s > %s, j = %d", 
                                                list.get(j - 1), list.get(j), j)); 
                }
                
                if(list.get(j-1).getValue() > list.get(j).getValue()) {
                    addAnimations(anim, AnimUtils.swap(list.get(j), list.get(j-1), j, j - 1),
                                        code.selectLine(4),
                                        vars.setText("Swapping %s and %s, j = %d", 
                                                list.get(j - 1), list.get(j), j));
                    //swap elements
                    temp = list.get(j-1);  
                    list.set(j-1, list.get(j));  
                    list.set(j, temp);
                }
                if(j == n - i - 1){
                    addAnimations(anim, AnimUtils.setColor(list.get(j-1), 
                                Constants.COMPARE, Constants.DEFAULT),
                                        AnimUtils.setColor(list.get(n-i-1), 
                                Constants.COMPARE, Constants.SORTED),
                                        vars.setText("%s is sorted", list.get(n-i-1)));
                } else {
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j - 1), Constants.COMPARE, 
                            Constants.DEFAULT));
                }
            }
        }
        
        addAnimations(anim, AnimUtils.setColor(list.get(0), 
                                Constants.DEFAULT, Constants.SORTED),
                            code.unselectAll(),
                            vars.setText("Array is sorted"));
        return anim;
    } 
    
    private void addPseudocode(Pseudocode code) {
        code.addLines("BubbleSort(A):",
                "  for i = 0 to size(A) - 1",
                "    for j = 1 to (size(A) - i) - 1",
                "      if array[j - 1] > array[j]",
                "        swap(array[j - 1], array[j])");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(code.getCode());
        });
    }
}
