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
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class BubbleSort extends Sorting implements AbstractAlgorithm{

    List<BrickNode> list;
    Pseudocode pc;
    
    public BubbleSort(List<BrickNode> list, Pane infoPane){
        this.list = list;
        pc = new Pseudocode();
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    @Override
    public List<Animation> sort() { 
        List<Animation> anim = new ArrayList<>();
        ParallelTransition parallelTransition;
        int n = list.size();  
        BrickNode temp;
        
        addAnimToList(anim, pc.selectLine(0));
        for(int i=0; i < n; i++){  
            parallelTransition = new ParallelTransition();
            addAnimToList(anim, pc.selectLine(1));
            for(int j=1; j < (n-i); j++){ 
                
                //select elements to compare (anim)
                if(j == 1){
                    anim.add(AnimUtils.makeParallel(pc.selectLine(2),
                            AnimUtils.selectNodes(list.get(j-1), list.get(j))));
                } else {
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j), ViewController.DEFAULT, 
                            ViewController.COMPARE));
                    anim.add(AnimUtils.makeParallel(parallelTransition, 
                            pc.selectLine(2)));
                }
                if(list.get(j-1).getValue() > list.get(j).getValue()){  
                    //swap elements  
                    anim.add(AnimUtils.makeParallel(AnimUtils.swap(list.get(j), list.get(j-1), j, j - 1),
                            pc.selectLine(3)));
                    temp = list.get(j-1);  
                    list.set(j-1, list.get(j));  
                    list.set(j, temp);
                }  
                //unselect (anim)
                if(j == n - i - 1){
                    anim.add(new ParallelTransition(
                            AnimUtils.setColor(list.get(j-1), 
                                ViewController.COMPARE, ViewController.DEFAULT),
                            AnimUtils.setColor(list.get(n-i-1), 
                                ViewController.COMPARE, ViewController.SORTED)));
                } else {
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(j-1), ViewController.COMPARE, 
                            ViewController.DEFAULT));
                }
            }
        }
        anim.add(AnimUtils.makeParallel(AnimUtils.setColor(list.get(0), 
                ViewController.DEFAULT, ViewController.SORTED),
                pc.unselectAll()));
        return anim;
    } 
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines("for i = 0 to sizeOfArray-1",
                "  for j = 1 to lastUnsortedElement-1",
                "    if leftElement > rightElement",
                "      swap(leftElement, rightElement)");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
