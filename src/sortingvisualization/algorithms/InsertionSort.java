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
import javafx.scene.paint.Color;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.AnimUtils;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 * Class for creation animation flow(code, sorting, variables) for 
 * Insertion sorting algorithm.
 * @author Mykhailo Klunko
 */
public class InsertionSort extends Sorting implements AbstractAlgorithm {

    private final List<BrickNode> list;
    private final Pseudocode code;
    private final VariablesInfo vars;
    
    /**
     * Creates a new instance of Insertion Sort algorithm animation flow 
     * creator class
     * @param list list of nodes to animate
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public InsertionSort(List<BrickNode> list,VariablesInfo vars, Pane infoPane){
        this.list = list;
        this.code = new Pseudocode();
        this.vars = vars;
        addPseudocode(code);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates animation flow for the Insertion sorting algorithm
     * @return list of animation steps
     */
    @Override
    public List<Animation> sort()
    {
        List<Animation> anim = new ArrayList<>();
        int n = list.size();
        
        for (int i = 1; i < n; ++i)
        {
            BrickNode key = list.get(i);
            if(i == 1){
                addAnimations(anim, code.selectLines(1, 2),
                        vars.setText(list.get(i - 1).getValue() + " is sorted"),
                        AnimUtils.setColor(list.get(i-1), Constants.DEFAULT, Constants.SORTED));
            }
            addAnimations(anim, code.selectLine(3), 
                    vars.setText("Selecting " + list.get(i).getValue()),
                    new SequentialTransition(
                    AnimUtils.setColor(key, Constants.DEFAULT, Color.RED),
                    AnimUtils.moveDownToX(key, i, i)));
            
            int j = i-1;
            while (j>=0 && list.get(j).compareTo(key) == 1) 
            {
                addAnimations(anim, code.selectLines(5),
                        vars.setText("Check if " + list.get(j).getValue() + " > " + key.getValue()),
                        AnimUtils.setColor(list.get(j), Constants.SORTED, Constants.COMPARE));
                addAnimations(anim, code.selectLine(6),
                        vars.setText("Moving " + list.get(j).getValue() + " one position right"),
                        new SequentialTransition(
                        AnimUtils.swap(key, list.get(j), j+1, j),
                        AnimUtils.setColor(list.get(j), 
                                Constants.COMPARE, Constants.SORTED)));
                
                list.set(j+1, list.get(j));
                j = j-1;
            }
            if(j >= 0){
                addAnimations(anim, code.selectLines(5),
                        vars.setText("Check if " + list.get(j).getValue() + " > " + key.getValue()),
                        AnimUtils.setColor(list.get(j), Constants.SORTED, Constants.COMPARE));
                addAnimations(anim, code.selectLine(7),
                        vars.setText("Insert " + key.getValue() + " at the " + (j + 1) + ". position"),
                        new ParallelTransition(
                        AnimUtils.setColor(list.get(j), Constants.COMPARE, 
                                    Constants.SORTED),
                        new SequentialTransition(
                                AnimUtils.moveNodeUp(key),
                                AnimUtils.setColor(key, Color.RED, Constants.SORTED))));
            } else{
                addAnimations(anim, code.selectLines(5, 7),
                        vars.setText("No elements left to compare with. Insert " 
                                + key.getValue() + " at the " + (j + 1) + ". position"),
                        new SequentialTransition(AnimUtils.moveNodeUp(key),
                    AnimUtils.setColor(key, Color.RED, Constants.SORTED)));
            }
            list.set(j+1, key);
        }
        addAnimations(anim, code.unselectAll(),
                vars.setText("Array is sorted"));
        return anim;
    }
    
    private void addPseudocode(Pseudocode code){
        code.addLines(
                "InsertionSort(A):",
                "  set first element as sorted",
                "  for each unsorted element",
                "    select the element i",
                "      for j = lastSortedIndex downto 0",
                "        if selectedElement > i-th element",
                "          move sorted element right by 1",
                "        else insert element i here");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(code.getCode());
        });
    }
}
