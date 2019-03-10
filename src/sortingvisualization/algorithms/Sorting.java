/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.List;
import javafx.animation.Animation;
import sortingvisualization.BrickNode;

/**
 *
 * @author Mykhailo Klunko
 */
public class Sorting {

    protected void addAnimToList(List<Animation> animList, Animation... anims){
        for(Animation anim : anims){
            if(anim != null){
                animList.add(anim);
            }
        }
    }
    
    /**
     * Gets maximum value from list of BrickNode elements
     * @param list
     * @return maximum integer value
     */
    public static int getMaxValue(List<BrickNode> list){
        int maximum = list.get(0).getValue();
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getValue() > maximum){
                maximum = list.get(i).getValue();
            }
        }
        return maximum;
    }
    
    /**
     * Gets minimum value from list of BrickNode elements
     * @param list
     * @return minimum integer value
     */
    public static int getMinValue(List<BrickNode> list){
        int minimum = list.get(0).getValue();
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getValue() < minimum){
                minimum = list.get(i).getValue();
            }
        }
        return minimum;
    }
    
}
