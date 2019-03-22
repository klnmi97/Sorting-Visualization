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
 * Cocktail - Shaker sorting algorithm.
 * @author Mykhailo Klunko
 */
public class CocktailShakerSort extends Sorting implements AbstractAlgorithm {

    private final List<BrickNode> list;
    private final Pseudocode pc;
    private final VariablesInfo vars;
    
    /**
     * Creates a new instance of Cocktail-Shaker animation flow creator class
     * @param list list of nodes to animate
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public CocktailShakerSort(List<BrickNode> list, VariablesInfo vars, Pane infoPane){
        this.list = list;
        this.pc = new Pseudocode();
        this.vars = vars;
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates animation flow for Cocktail - Shaker sorting algorithm
     * @return list of animation steps
     */
    @Override
    public List<Animation> sort() {  
        List<Animation> anim = new ArrayList<>();
        ParallelTransition parallelTransition;
        boolean swapped = true;
        int i = 0;
        int j = list.size() - 1;
        
        int firstSelected = i;
        int secondSelected = i;
        int lastStart = i;
        int lastFinish = j;
        parallelTransition = new ParallelTransition();
        
        //addAnimToList(anim, pc.selectLines(0, 1));
        
        while(i < j && swapped) 
        {
            swapped = false;
            addAnimations(anim, pc.selectLines(3, 4),
                    vars.setText("Set swapped to " + swapped + "\n" 
                            + "Iterate from " + i + " to " + j));
            for(int k = i; k < j; k++) 
            {
                if(k == 0){
                    addAnimations(anim, pc.selectLine(5),
                            vars.setText("Check if " + list.get(k).getValue() 
                                    + " > " + list.get(k + 1).getValue()),
                            AnimUtils.selectNodes(list.get(k), list.get(k+1)));
                    
                } else {
                    parallelTransition.getChildren().add(
                            AnimUtils.setColor(list.get(k+1), 
                                    Constants.DEFAULT, Constants.COMPARE));
                    addAnimations(anim, pc.selectLine(5),
                            vars.setText("Check if " + list.get(k).getValue() 
                                    + " > " + list.get(k + 1).getValue()),
                                    parallelTransition);
                }
                if(list.get(k).compareTo(list.get(k+1)) == 1) 
                {
                    addAnimations(anim, pc.selectLines(6, 7),
                            vars.setText("Swap " + list.get(k).getValue() 
                                    + " and " + list.get(k + 1).getValue() 
                                    + ", set swapped to true"),
                            AnimUtils.swap(list.get(k), list.get(k + 1), k, k + 1));
                    //swap
                    BrickNode temp = list.get(k);
                    list.set(k, list.get(k + 1));
                    list.set(k + 1, temp);
                    swapped = true;
                    
                }
                firstSelected = k; 
                secondSelected = k + 1;
                if(k == j - 1){
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(k+1), Constants.COMPARE, 
                            Constants.SORTED));
                } else {
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(k), Constants.COMPARE, 
                            Constants.DEFAULT));
                }
            }
            
            j--;
            lastStart = i;
            lastFinish = j + 2;
            
            addAnimations(anim, pc.selectLine(8),
                    vars.setText("Check if there was at least one swap, swapped is " + swapped));
            
            if(swapped) 
            {
                swapped = false;
                
                addAnimations(anim, pc.selectLines(9, 10),
                        vars.setText("Set swapped to " + swapped + "\n" 
                            + "Iterate from " + j + " downto " + i));
                
                for(int k = j; k > i; k--) 
                {
                    
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(k-1), Constants.DEFAULT, 
                                Constants.COMPARE));
                    addAnimations(anim, pc.selectLine(11),
                            vars.setText("Check if " + list.get(k - 1).getValue() 
                                    + " > " + list.get(k).getValue()),
                            parallelTransition);
                    
                    if(list.get(k).compareTo(list.get(k - 1)) == -1) 
                    {
                        addAnimations(anim, pc.selectLines(12, 13),
                                vars.setText("Swap " + list.get(k - 1).getValue() + " and " + list.get(k).getValue()),
                                AnimUtils.swap(list.get(k), list.get(k - 1), k, k - 1));
                        
                        BrickNode temp = list.get(k);
                        list.set(k, list.get(k - 1));
                        list.set(k - 1, temp);
                        swapped = true;
                        
                    }
                    firstSelected = k - 1; 
                    secondSelected = k;
                    if(k == i + 1){
                        parallelTransition = new ParallelTransition();
                        parallelTransition.getChildren().add(AnimUtils.setColor(
                                list.get(k-1), Constants.COMPARE, 
                                Constants.SORTED));
                    } else {
                        parallelTransition = new ParallelTransition();
                        parallelTransition.getChildren().add(AnimUtils.setColor(
                                list.get(k), Constants.COMPARE, 
                                Constants.DEFAULT));
                    }
                }
                lastStart = i;
                lastFinish = j + 1;
            }
            i++;
            
            addAnimations(anim, pc.selectLine(14), 
                    vars.setText("Variable swapped is " + swapped));
        }
        
        parallelTransition = new ParallelTransition();
        for (int k = lastStart; k < lastFinish; k++) {
            parallelTransition.getChildren().add(AnimUtils
                    .setColor(list.get(k), Constants.DEFAULT, Constants.SORTED));
        }
        
        anim.add(AnimUtils.makeParallel(new SequentialTransition(
                AnimUtils.unselectNodes(list.get(firstSelected), list.get(secondSelected)), 
                parallelTransition),
                pc.unselectAll(),
                vars.setText("Array is sorted!")));
        
        return anim;
    } 
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(
                "ShakerSort(A):",
                "  swapped = true",
                "  do",
                "    swapped = false",
                "    for i = 0 to lastUnsortedRight - 1",
                "      if leftElement > rightElement",
                "        swap(leftElement, rightElement)",
                "        swapped = true",
                "    if swapped",
                "      swapped = false",
                "      for j = lastUnsortedR - 1 to lastUnsortedL - 1",
                "        if leftElement > rightElement",
                "          swap(leftElement, rightElement)",
                "          swapped = true",
                "  while swapped");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
