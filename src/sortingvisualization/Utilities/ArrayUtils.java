/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Utilities;

import java.util.List;
import java.util.Random;
import sortingvisualization.NodeControllers.BrickNode;

/**
 *
 * @author Mykhailo Klunko
 */
public class ArrayUtils {
    public static int getArrayMin(int[] array){
        int minimum = array[0];
        for(int i = 0; i < array.length; i++){
            if(array[i] < minimum){
                minimum = array[i];
            }
        }
        return minimum;
    }
    
    public static int getArrayMax(int[] array){
        int maximum = array[0];
        for(int i = 0; i < array.length; i++){
            if(array[i] > maximum){
                maximum = array[i];
            }
        }
        return maximum;
    }
    
    public static int getMaxValue(List<BrickNode> list){
        int maximum = list.get(0).getValue();
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getValue() > maximum){
                maximum = list.get(i).getValue();
            }
        }
        return maximum;
    }
    
    public static int getMinValue(List<BrickNode> list){
        int minimum = list.get(0).getValue();
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getValue() < minimum){
                minimum = list.get(i).getValue();
            }
        }
        return minimum;
    }
    
    public static int[] generateRandomArray(int size, int min, int max){
        Random randomValue = new Random();
        int[] randomArray = new int[size];
        for(int i = 0; i < size; i++){
            randomArray[i] = randomValue.nextInt(max - min) + min;
        }
        return randomArray;
    }
}
