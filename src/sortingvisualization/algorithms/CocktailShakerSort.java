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
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;


/**
 *
 * @author Mykhailo Klunko
 */
public class CocktailShakerSort {

    public static List<Animation> cocktailShakerSort(ArrayList<BrickNode> list, List<Animation> sq) {  
        
        ParallelTransition parallelTransition;
        boolean swapped = true;
        int i = 0;
        int j = list.size() - 1;
        
        int firstSelected = i;
        int secondSelected = i;
        int lastStart = i;
        int lastFinish = j;
        parallelTransition = new ParallelTransition();
        
        while(i < j && swapped) 
        {
            swapped = false;
            for(int k = i; k < j; k++) 
            {
                if(k == 0){
                    sq.add(AnimUtils.selectNodes(list.get(k), list.get(k+1)));
                } else {
                    parallelTransition.getChildren().add(
                            AnimUtils.setColor(list.get(k+1), 
                                    ViewController.DEFAULT, ViewController.COMPARE));
                    sq.add(parallelTransition);
                }
                if(list.get(k).compareTo(list.get(k+1)) == 1) 
                {
                    sq.add(AnimUtils.swap(list.get(k), list.get(k + 1), k, k + 1));
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
            
            if(swapped) 
            {
                swapped = false;
                for(int k = j; k > i; k--) 
                {
                    
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(k-1), ViewController.DEFAULT, 
                            ViewController.COMPARE));
                    sq.add(parallelTransition);
                    
                    if(list.get(k).compareTo(list.get(k - 1)) == -1) 
                    {
                        sq.add(AnimUtils.swap(list.get(k), list.get(k - 1), k, k - 1));
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

        }
        
        parallelTransition = new ParallelTransition();
        for (int k = lastStart; k < lastFinish; k++) {
            parallelTransition.getChildren().add(AnimUtils
                    .setColor(list.get(k), ViewController.DEFAULT, ViewController.SORTED));
        }
        sq.add(new SequentialTransition(
                AnimUtils.unselectNodes(list.get(firstSelected), list.get(secondSelected)), 
                parallelTransition));
        
        return sq;
    } 
    
}
