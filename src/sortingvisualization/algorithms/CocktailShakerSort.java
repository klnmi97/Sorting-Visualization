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
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;
import sortingvisualization.ViewController;


/**
 *
 * @author Mykhailo Klunko
 */
public class CocktailShakerSort {

    public static List<Animation> cocktailShakerSort(ArrayList<BrickNode> list, Pane codePane) {  
        List<Animation> sq = new ArrayList<>();
        ParallelTransition parallelTransition;
        boolean swapped = true;
        int i = 0;
        int j = list.size() - 1;
        
        int firstSelected = i;
        int secondSelected = i;
        int lastStart = i;
        int lastFinish = j;
        parallelTransition = new ParallelTransition();
        
        Pseudocode pc = new Pseudocode();
        addPseudocode(codePane, pc);
        addAnimToList(sq, pc.selectLines(0, 1));
        while(i < j && swapped) 
        {
            addAnimToList(sq, pc.selectLines(2, 3));
            swapped = false;
            for(int k = i; k < j; k++) 
            {
                if(k == 0){
                    sq.add(AnimUtils.makeParallel(
                            pc.selectLine(4),
                            AnimUtils.selectNodes(list.get(k), list.get(k+1))));
                } else {
                    parallelTransition.getChildren().add(
                            AnimUtils.setColor(list.get(k+1), 
                                    ViewController.DEFAULT, ViewController.COMPARE));
                    sq.add(AnimUtils.makeParallel(
                                    pc.selectLine(4),
                                    parallelTransition));
                }
                if(list.get(k).compareTo(list.get(k+1)) == 1) 
                {
                    sq.add(AnimUtils.makeParallel(
                            pc.selectLines(5, 6), 
                            AnimUtils.swap(list.get(k), list.get(k + 1), k, k + 1)));
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
                            list.get(k+1), ViewController.COMPARE, 
                            ViewController.SORTED));
                } else {
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(k), ViewController.COMPARE, 
                            ViewController.DEFAULT));
                }
            }
            
            j--;
            lastStart = i;
            lastFinish = j + 2;
            addAnimToList(sq, pc.selectLine(7));
            if(swapped) 
            {
                addAnimToList(sq, pc.selectLines(8,9));
                swapped = false;
                for(int k = j; k > i; k--) 
                {
                    
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(k-1), ViewController.DEFAULT, 
                                ViewController.COMPARE));
                    sq.add(AnimUtils.makeParallel(
                            pc.selectLine(10),
                            parallelTransition));
                    
                    if(list.get(k).compareTo(list.get(k - 1)) == -1) 
                    {
                        sq.add(AnimUtils.makeParallel(
                                AnimUtils.swap(list.get(k), list.get(k - 1), k, k - 1),
                                pc.selectLines(11, 12)));
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
                                list.get(k-1), ViewController.COMPARE, 
                                ViewController.SORTED));
                    } else {
                        parallelTransition = new ParallelTransition();
                        parallelTransition.getChildren().add(AnimUtils.setColor(
                                list.get(k), ViewController.COMPARE, 
                                ViewController.DEFAULT));
                    }
                }
                lastStart = i;
                lastFinish = j + 1;
            }
            i++;
            addAnimToList(sq, pc.selectLine(13));
        }
        
        parallelTransition = new ParallelTransition();
        for (int k = lastStart; k < lastFinish; k++) {
            parallelTransition.getChildren().add(AnimUtils
                    .setColor(list.get(k), ViewController.DEFAULT, ViewController.SORTED));
        }
        
        sq.add(AnimUtils.makeParallel(new SequentialTransition(
                AnimUtils.unselectNodes(list.get(firstSelected), list.get(secondSelected)), 
                parallelTransition),
                pc.unselectAll()));
        
        
        return sq;
    } 
    
    private static void addPseudocode(Pane codePane, Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(codePane, 
                "swapped = true",
                "do",
                "  swapped = false",
                "  for i = 0 to lastUnsortedRight-1",
                "    if leftElement > rightElement",
                "      swap(leftElement, rightElement)",
                "      swapped = true",
                "  if swapped",
                "    swapped = false",
                "    for j = lastUnsortedRight-1 to lastUnsortedLeft-1",
                "     if leftElement > rightElement",
                "       swap(leftElement, rightElement)",
                "       swapped = true",
                "while swapped");
    }
    
    private static void addAnimToList(List<Animation> animList, Animation... anims){
        for(Animation anim : anims){
            if(anim != null){
                animList.add(anim);
            }
        }
    }
}
