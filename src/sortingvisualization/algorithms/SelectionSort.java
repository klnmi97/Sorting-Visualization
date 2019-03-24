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
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.AnimUtils;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 * Class for creation animation flow(code, sorting, variables) for 
 * Selection sorting algorithm.
 * @author Mykhailo Klunko
 */
public class SelectionSort extends Sorting implements AbstractAlgorithm {

    private final List<BrickNode> list;
    private final Pseudocode pc;
    private final VariablesInfo vars;
    
    /**
     * Creates a new instance of the Insertion Sort algorithm animation flow 
     * creator class
     * @param list list of nodes to animate
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public SelectionSort(List<BrickNode> list, VariablesInfo vars, Pane infoPane){
        this.list = list;
        this.pc = new Pseudocode();
        this.vars = vars;
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates animation flow for the Insertion sorting algorithm
     * @return list of animation steps
     */
    @Override
    public List<Animation> sort(){
        List<Animation> anim = new ArrayList<>();
        int arrayLength = list.size();
        ParallelTransition parallelTransition;
        ParallelTransition compareTransition;
        
        for (int i = 0; i < arrayLength - 1; i++){  
            int index = i;
            
            addAnimations(anim, pc.selectLines(2, 3),
                    vars.setText("Set %s at position %d as current minimum", list.get(i), i),
                    AnimUtils.setColor(list.get(i), Constants.DEFAULT, Constants.SELECTED));
            compareTransition = new ParallelTransition();
            
            for (int j = i + 1; j < arrayLength; j++) {
                
                compareTransition.getChildren().add(AnimUtils.setColor(list.get(j), Constants.DEFAULT, 
                        Constants.COMPARE));
                addAnimations(anim, pc.selectLine(4),
                        vars.setText("Check if %s < %s", list.get(j), list.get(index)),
                        compareTransition);
                
                if (list.get(j).getValue() < list.get(index).getValue()){ 
                    
                    addAnimations(anim, pc.selectLine(5),
                            vars.setText("Set %s at position %d as current minimum", list.get(j), j),
                            AnimUtils.setColor(list.get(index), Constants.SELECTED, 
                                    Constants.DEFAULT),
                            AnimUtils.setColor(list.get(j), 
                                    Constants.COMPARE, Constants.SELECTED));
                    
                    index = j;  //searching for lowest index 
                    
                    compareTransition = new ParallelTransition();
                } else {
                    
                    compareTransition = new ParallelTransition();
                    compareTransition.getChildren().add(AnimUtils.setColor(list.get(j), 
                            Constants.COMPARE, Constants.DEFAULT));
                }
            }
            
            addAnimations(anim, compareTransition);
            parallelTransition = new ParallelTransition();
            
            if(index != i){
                addAnimations(anim, pc.selectLine(6), 
                        vars.setText("Swap %s and %s", list.get(index), list.get(i)),
                        new SequentialTransition(
                        AnimUtils.setColor(list.get(i), Constants.DEFAULT, Constants.SELECTED), 
                        AnimUtils.swap(list.get(index), list.get(i), index, i)));
                
                parallelTransition.getChildren().add(AnimUtils.setColor(list.get(i), Constants.SELECTED, 
                        Constants.DEFAULT));
            }
            parallelTransition.getChildren().add(AnimUtils.setColor(list.get(index), Constants.SELECTED, 
                    Constants.SORTED));
            addAnimations(anim, pc.selectLine(6),
                    vars.setText("%s is now sorted", list.get(index)),
                    parallelTransition);
            
            BrickNode smallerNumber = list.get(index);   
            list.set(index, list.get(i));
            list.set(i, smallerNumber);
            
            
        }
        addAnimations(anim, pc.unselectAll(),
                vars.setText("Array is sorted"),
                AnimUtils.setColor(list.get(arrayLength - 1), Constants.DEFAULT, 
                        Constants.SORTED));
        return anim;
    }
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(
                "SelectionSort(A):",
                " for i = 0 to size(A) - 1",
                "   set the first unsorted element as the minimum",
                "     for i = 1 to size(A) - 1",
                "       if currentElement < currentMin",
                "         set currentElement as currentMin",
                "       swap currentMin with first unsorted element");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
