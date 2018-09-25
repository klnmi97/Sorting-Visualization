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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class InsertionSort {

    public static List<Animation> insertionSort(ArrayList<BrickNode> list, Pane codePane)
    {
        List<Animation> sq = new ArrayList<>();
        int n = list.size();
        List<StackPane> codeLines = new ArrayList<>();
        
        addPseudocode(codePane, codeLines);
        for (int i=1; i<n; ++i)
        {
            BrickNode key = list.get(i);
            if(i == 1){
                sq.add(AnimUtils.setColor(list.get(i-1), ViewController.DEFAULT, ViewController.SORTED));
            }
            sq.add(new SequentialTransition(
                    AnimUtils.setColor(key, ViewController.DEFAULT, Color.RED),
                    AnimUtils.moveDownToX(key, i, i)));
            int j = i-1;
            while (j>=0 && list.get(j).compareTo(key) == 1) 
            {
                sq.add(AnimUtils.setColor(list.get(j), ViewController.SORTED, 
                        ViewController.COMPARE));
                sq.add(new SequentialTransition(
                        AnimUtils.swap(key, list.get(j), j+1, j),
                        AnimUtils.setColor(list.get(j), 
                                ViewController.COMPARE, ViewController.SORTED)));
                list.set(j+1, list.get(j));
                j = j-1;
            }
            if(j >= 0){
                sq.add(AnimUtils.setColor(list.get(j), ViewController.SORTED, 
                        ViewController.COMPARE));
                sq.add(new ParallelTransition(
                        AnimUtils.setColor(list.get(j), ViewController.COMPARE, 
                                    ViewController.SORTED),
                        new SequentialTransition(AnimUtils.moveNodeUp(key),
                        AnimUtils.setColor(key, Color.RED, ViewController.SORTED))));
            } else{
                sq.add(new SequentialTransition(AnimUtils.moveNodeUp(key),
                    AnimUtils.setColor(key, Color.RED, ViewController.SORTED)));
            }
            list.set(j+1, key);
        }
        return sq;
    }
    
    private static void addPseudocode(Pane pane, List<StackPane> code){
        //TODO: improve pseudocode
        code.add(AnimUtils.createLine("set first element as sorted"));
        code.add(AnimUtils.createLine("for each unsorted element"));
        code.add(AnimUtils.createLine("  select the element i"));
        code.add(AnimUtils.createLine("    for j = sizeOfArray to 0"));
        code.add(AnimUtils.createLine("      if selectedElement > i-th element"));
        code.add(AnimUtils.createLine("        move sorted element right by 1"));
        code.add(AnimUtils.createLine("      else insert element i here"));
        
        pane.getChildren().addAll(code);
    }
}
