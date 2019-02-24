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
import javafx.scene.paint.Color;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class InsertionSort {

    public static List<Animation> insertionSort(List<BrickNode> list, Pane codePane)
    {
        List<Animation> sq = new ArrayList<>();
        int n = list.size();
        
        Pseudocode pc = new Pseudocode();
        addPseudocode(codePane, pc);
        for (int i = 1; i < n; ++i)
        {
            BrickNode key = list.get(i);
            if(i == 1){
                sq.add(AnimUtils.makeParallel(
                        AnimUtils.setColor(list.get(i-1), ViewController.DEFAULT, ViewController.SORTED),
                        pc.selectLines(0, 1)));
            }
            sq.add(AnimUtils.makeParallel(new SequentialTransition(
                    AnimUtils.setColor(key, ViewController.DEFAULT, Color.RED),
                    AnimUtils.moveDownToX(key, i, i)),
                    pc.selectLine(2)));
            int j = i-1;
            while (j>=0 && list.get(j).compareTo(key) == 1) 
            {
                sq.add(AnimUtils.makeParallel(AnimUtils.setColor(list.get(j), ViewController.SORTED, 
                        ViewController.COMPARE),
                        pc.selectLines(4)));
                sq.add(AnimUtils.makeParallel(new SequentialTransition(
                        AnimUtils.swap(key, list.get(j), j+1, j),
                        AnimUtils.setColor(list.get(j), 
                                ViewController.COMPARE, ViewController.SORTED)),
                        pc.selectLine(5)));
                list.set(j+1, list.get(j));
                j = j-1;
            }
            if(j >= 0){
                sq.add(AnimUtils.makeParallel(AnimUtils.setColor(list.get(j), ViewController.SORTED, 
                        ViewController.COMPARE),
                        pc.selectLines(4)));
                sq.add(AnimUtils.makeParallel(new ParallelTransition(
                        AnimUtils.setColor(list.get(j), ViewController.COMPARE, 
                                    ViewController.SORTED),
                        new SequentialTransition(
                                AnimUtils.moveNodeUp(key),
                                AnimUtils.setColor(key, Color.RED, ViewController.SORTED))),
                        pc.selectLine(6)));
            } else{
                sq.add(AnimUtils.makeParallel(new SequentialTransition(AnimUtils.moveNodeUp(key),
                    AnimUtils.setColor(key, Color.RED, ViewController.SORTED)),
                        pc.selectLines(4, 6)));
            }
            list.set(j+1, key);
        }
        addAnimToList(sq, pc.unselectAll());
        return sq;
    }
    
    private static void addPseudocode(Pane codePane, Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(codePane, 
                "set first element as sorted",
                "for each unsorted element",
                "  select the element i",
                "    for j = sizeOfArray to 0",
                "      if selectedElement > i-th element",
                "        move sorted element right by 1",
                "      else insert element i here");
    }
    
    private static void addAnimToList(List<Animation> animList, Animation... anims){
        for(Animation anim : anims){
            if(anim != null){
                animList.add(anim);
            }
        }
    }
}
