/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.ViewController;

/**
 *
 * @author mihae
 */
public class SelectionSort{

    public static List<Animation> selectionSort(ArrayList<BrickNode> list, List<Animation> sq){
        int arrayLength = list.size();
        for (int i = 0; i < arrayLength - 1; i++)  
        {  
            int index = i;
            sq.add(AnimUtils.setColor(list.get(i), ViewController.DEFAULT, Color.RED));
            
            for (int j = i + 1; j < arrayLength; j++){  
                sq.add(AnimUtils.setColor(list.get(j), ViewController.DEFAULT, 
                        ViewController.COMPARE));
                
                if (list.get(j).getValue() < list.get(index).getValue()){ 
                    
                    sq.add(new SequentialTransition(
                            AnimUtils.setColor(list.get(index), Color.RED, 
                                    ViewController.DEFAULT),
                            AnimUtils.setColor(list.get(j), 
                                    ViewController.COMPARE, Color.RED)));
                    index = j;//searching for lowest index  
                } else {
                    sq.add(AnimUtils.setColor(list.get(j), 
                            ViewController.COMPARE, ViewController.DEFAULT));
                }
            }
            if(index != i){
                sq.add(AnimUtils.setColor(list.get(i), ViewController.DEFAULT, 
                        Color.RED));
                sq.add(AnimUtils.swap(list.get(index), list.get(i), index, i));
                sq.add(AnimUtils.setColor(list.get(i), Color.RED, 
                        ViewController.DEFAULT));
            }
            
            sq.add(AnimUtils.setColor(list.get(index), Color.RED, 
                    ViewController.DEFAULT));
            
            BrickNode smallerNumber = list.get(index);   
            list.set(index, list.get(i));
            list.set(i, smallerNumber);
        }
        return sq;
    }
    
}
