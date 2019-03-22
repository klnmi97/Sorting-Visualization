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
import sortingvisualization.Controllers.ViewController;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 *
 * @author Mykhailo Klunko
 */
public class BubbleSort extends Sorting implements AbstractAlgorithm{

    List<BrickNode> list;
    Pseudocode pc;
    VariablesInfo vars;
    
    public BubbleSort(List<BrickNode> list, VariablesInfo vars, Pane infoPane){
        this.list = list;
        this.pc = new Pseudocode();
        this.vars = vars;
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    @Override
    public List<Animation> sort() { 
        List<Animation> anim = new ArrayList<>();
        ParallelTransition parallelTransition;
        int n = list.size();  
        BrickNode temp;
        
        addAnimations(anim, pc.selectLine(1),
                            vars.setText("Iterate from 0 to " + (n - 1) ));
        
        for(int i=0; i < n; i++) {
            
            parallelTransition = new ParallelTransition();
            addAnimations(anim, pc.selectLine(2),
                                vars.setText("Iterate from 1 to " + (n - i - 1)));
            
            for(int j=1; j < (n-i); j++){ 
                
                //select elements to compare (anim)
                if(j == 1) {
                    
                    addAnimations(anim, pc.selectLine(3),
                            vars.setText("Checking if " + list.get(j-1).getValue() 
                                    + " > " + list.get(j).getValue()),
                            AnimUtils.selectNodes(list.get(j-1), list.get(j)));
                    
                } else {
                    
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j), Constants.DEFAULT, 
                            Constants.COMPARE));
                    addAnimations(anim, parallelTransition, 
                                        pc.selectLine(3),
                                        vars.setText("Checking if " + list.get(j-1).getValue() 
                                    + " > " + list.get(j).getValue()));
                    
                }
                
                if(list.get(j-1).getValue() > list.get(j).getValue()) {  
                    
                    addAnimations(anim, AnimUtils.swap(list.get(j), list.get(j-1), j, j - 1),
                                        pc.selectLine(4),
                                        vars.setText("Swapping " + list.get(j-1).getValue() 
                                    + " and " + list.get(j).getValue()));
                    //swap elements
                    temp = list.get(j-1);  
                    list.set(j-1, list.get(j));  
                    list.set(j, temp);
                }  
                //unselect (anim)
                if(j == n - i - 1){
                    addAnimations(anim, AnimUtils.setColor(list.get(j-1), 
                                Constants.COMPARE, Constants.DEFAULT),
                                        AnimUtils.setColor(list.get(n-i-1), 
                                Constants.COMPARE, Constants.SORTED),
                                        vars.setText(list.get(n-i-1).getValue() + " is sorted"));
                } else {
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j-1), Constants.COMPARE, 
                            Constants.DEFAULT));
                }
            }
        }
        
        addAnimations(anim, AnimUtils.setColor(list.get(0), 
                                Constants.DEFAULT, Constants.SORTED),
                            pc.unselectAll(),
                            vars.setText("Array is sorted"));
        return anim;
    } 
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines("BubbleSort(A):",
                "  for i = 0 to size(A) - 1",
                "    for j = 1 to (size(A) - i) - 1",
                "      if array[i] > array[j]",
                "        swap(array[i], array[j])");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
