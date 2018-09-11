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
        /*
        TODO: add 'sorted' identification
            sq.add(AnimUtils.setColor(list.get(n-i-1), 
                    ViewController.DEFAULT, ViewController.SORTED));
        }*/
        boolean swapped = true;
        int i = 0;
        int j = list.size() - 1;
        int firstSelected = i, secondSelected = i;
        parallelTransition = new ParallelTransition();
        while(i < j && swapped) 
        {
            swapped = false;
            for(int k = i; k < j; k++) 
            {
                if(k == 0){
                    sq.add(AnimUtils.selectNodes(list.get(k), list.get(k+1)));
                } else {
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(k+1), ViewController.DEFAULT, 
                            ViewController.COMPARE));
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
                    //sq.add(AnimUtils.unselectNodes(list.get(k), list.get(k+1)));
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(k+1), ViewController.COMPARE, 
                            ViewController.DEFAULT));
                } else {
                    parallelTransition = new ParallelTransition();
                    parallelTransition.getChildren().add(AnimUtils.setColor(
                            list.get(k), ViewController.COMPARE, 
                            ViewController.DEFAULT));
                }
            }
            
            j--;

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
                        //
                        parallelTransition = new ParallelTransition();
                        parallelTransition.getChildren().add(AnimUtils.setColor(
                                list.get(k-1), ViewController.COMPARE, 
                                ViewController.DEFAULT));
                    } else {
                        parallelTransition = new ParallelTransition();
                        parallelTransition.getChildren().add(AnimUtils.setColor(
                                list.get(k), ViewController.COMPARE, 
                                ViewController.DEFAULT));
                    }
                }
            }
            i++;

        }
            
        sq.add(AnimUtils.unselectNodes(list.get(firstSelected), list.get(secondSelected)));
        return sq;
    } 
    
}
