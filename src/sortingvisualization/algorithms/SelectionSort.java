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
public class SelectionSort{

    public static List<Animation> selectionSort(ArrayList<BrickNode> list, List<Animation> anim){
        
        int arrayLength = list.size();
        ParallelTransition parallelTransition;
        ParallelTransition compareTransition;
        for (int i = 0; i < arrayLength - 1; i++){  
            int index = i;
            anim.add(AnimUtils.setColor(list.get(i), ViewController.DEFAULT, Color.RED));
            compareTransition = new ParallelTransition();
            
            for (int j = i + 1; j < arrayLength; j++) {
                compareTransition.getChildren().add(AnimUtils.setColor(list.get(j), ViewController.DEFAULT, 
                        ViewController.COMPARE));
                anim.add(compareTransition);
                if (list.get(j).getValue() < list.get(index).getValue()){ 
                    
                    anim.add(new ParallelTransition(
                            AnimUtils.setColor(list.get(index), Color.RED, 
                                    ViewController.DEFAULT),
                            AnimUtils.setColor(list.get(j), 
                                    ViewController.COMPARE, Color.RED)));
                    index = j;  //searching for lowest index 
                    compareTransition = new ParallelTransition();
                } else {
                    compareTransition = new ParallelTransition();
                    compareTransition.getChildren().add(AnimUtils.setColor(list.get(j), 
                            ViewController.COMPARE, ViewController.DEFAULT));
                }
            }
            
            anim.add(compareTransition);
            parallelTransition = new ParallelTransition();
            if(index != i){
                anim.add(new SequentialTransition(
                        AnimUtils.setColor(list.get(i), ViewController.DEFAULT, Color.RED), 
                        AnimUtils.swap(list.get(index), list.get(i), index, i)));
                parallelTransition.getChildren().add(AnimUtils.setColor(list.get(i), Color.RED, 
                        ViewController.DEFAULT));
            }
            parallelTransition.getChildren().add(AnimUtils.setColor(list.get(index), Color.RED, 
                    ViewController.SORTED));
            anim.add(parallelTransition);
            
            BrickNode smallerNumber = list.get(index);   
            list.set(index, list.get(i));
            list.set(i, smallerNumber);
            
            
        }
        anim.add(AnimUtils.setColor(list.get(arrayLength - 1), ViewController.DEFAULT, 
                        ViewController.SORTED));
        return anim;
    }
    
}
