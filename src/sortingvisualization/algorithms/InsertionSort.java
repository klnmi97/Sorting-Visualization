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
import sortingvisualization.Controllers.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class InsertionSort extends Sorting implements AbstractAlgorithm {

    List<BrickNode> list;
    Pseudocode pc;
    
    public InsertionSort(List<BrickNode> list, Pane infoPane){
        this.list = list;
        pc = new Pseudocode();
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    @Override
    public List<Animation> sort()
    {
        List<Animation> sq = new ArrayList<>();
        int n = list.size();
        
        for (int i = 1; i < n; ++i)
        {
            BrickNode key = list.get(i);
            if(i == 1){
                sq.add(AnimUtils.makeParallel(
                        AnimUtils.setColor(list.get(i-1), Constants.DEFAULT, Constants.SORTED),
                        pc.selectLines(0, 1)));
            }
            sq.add(AnimUtils.makeParallel(new SequentialTransition(
                    AnimUtils.setColor(key, Constants.DEFAULT, Color.RED),
                    AnimUtils.moveDownToX(key, i, i)),
                    pc.selectLine(2)));
            int j = i-1;
            while (j>=0 && list.get(j).compareTo(key) == 1) 
            {
                sq.add(AnimUtils.makeParallel(AnimUtils.setColor(list.get(j), Constants.SORTED, 
                        Constants.COMPARE),
                        pc.selectLines(4)));
                sq.add(AnimUtils.makeParallel(new SequentialTransition(
                        AnimUtils.swap(key, list.get(j), j+1, j),
                        AnimUtils.setColor(list.get(j), 
                                Constants.COMPARE, Constants.SORTED)),
                        pc.selectLine(5)));
                list.set(j+1, list.get(j));
                j = j-1;
            }
            if(j >= 0){
                sq.add(AnimUtils.makeParallel(AnimUtils.setColor(list.get(j), Constants.SORTED, 
                        Constants.COMPARE),
                        pc.selectLines(4)));
                sq.add(AnimUtils.makeParallel(new ParallelTransition(
                        AnimUtils.setColor(list.get(j), Constants.COMPARE, 
                                    Constants.SORTED),
                        new SequentialTransition(
                                AnimUtils.moveNodeUp(key),
                                AnimUtils.setColor(key, Color.RED, Constants.SORTED))),
                        pc.selectLine(6)));
            } else{
                sq.add(AnimUtils.makeParallel(new SequentialTransition(AnimUtils.moveNodeUp(key),
                    AnimUtils.setColor(key, Color.RED, Constants.SORTED)),
                        pc.selectLines(4, 6)));
            }
            list.set(j+1, key);
        }
        addAnimToList(sq, pc.unselectAll());
        return sq;
    }
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines( 
                "set first element as sorted",
                "for each unsorted element",
                "  select the element i",
                "    for j = sizeOfArray to 0",
                "      if selectedElement > i-th element",
                "        move sorted element right by 1",
                "      else insert element i here");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
