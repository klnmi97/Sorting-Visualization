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
import javafx.scene.paint.Color;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class InsertionSort {

    public static List<Animation> insertionSort(ArrayList<BrickNode> list, List<Animation> sq)
    {
        SequentialTransition extractKey;
        int n = list.size();
        for (int i=1; i<n; ++i)
        {
            BrickNode key = list.get(i);
            
            sq.add(new SequentialTransition(
                    AnimUtils.setColor(key, ViewController.DEFAULT, Color.RED),
                    AnimUtils.moveDownToX(key, i, i)));
            
            int j = i-1;
            
            while (j>=0 && list.get(j).compareTo(key) == 1) 
            {
                sq.add(AnimUtils.setColor(list.get(j), ViewController.DEFAULT, 
                        ViewController.COMPARE));
                
                sq.add(new SequentialTransition(
                        AnimUtils.swap(key, list.get(j), j+1, j),
                        AnimUtils.setColor(list.get(j), 
                                ViewController.COMPARE, ViewController.DEFAULT)));
                
                list.set(j+1, list.get(j));
                j = j-1;
            }
            if(j >= 0){
                sq.add(AnimUtils.setColor(list.get(j), ViewController.DEFAULT, 
                        ViewController.COMPARE));
                sq.add(new ParallelTransition(
                        AnimUtils.setColor(list.get(j), ViewController.COMPARE, 
                                    ViewController.DEFAULT),
                        new SequentialTransition(AnimUtils.moveNodeUp(key),
                        AnimUtils.setColor(key, Color.RED, ViewController.DEFAULT))));
            } else{
                sq.add(new SequentialTransition(AnimUtils.moveNodeUp(key),
                    AnimUtils.setColor(key, Color.RED, ViewController.DEFAULT)));
            }
            
            list.set(j+1, key);
            
        }
        return sq;
    }
    
}
