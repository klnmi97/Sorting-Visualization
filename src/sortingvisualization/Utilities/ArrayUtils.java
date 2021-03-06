
package sortingvisualization.Utilities;

import java.util.List;
import java.util.Random;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Enums.Algorithm;
import sortingvisualization.NodeControllers.BrickNode;

/**
 * Tools for working with array
 * @author Mykhailo Klunko
 */
public class ArrayUtils {
    
    /**
     * Checks the array. If it is null, generates new in accordance with the type
     * @param type type of the algorithm
     * @param input array or null
     * @return existing or generated array
     */
    public static int[] checkArray(Algorithm type, int[] input){
        int[] outputArray;
        int currentMin = Constants.getMinimum(type);
        int currentMax = Constants.getMaximum(type);
        
        if(input != null){
            outputArray = input;
        } else {
            outputArray = generateRandomArray(Constants.DEFAULT_ITEM_COUNT, currentMin, currentMax);
        }
        
        return outputArray;
    }
    
    /**
     * Get minimum value from the integer array
     * @param array array of integers
     * @return minimum value
     */
    public static int getArrayMin(int[] array){
        int minimum = array[0];
        for(int i = 0; i < array.length; i++){
            if(array[i] < minimum){
                minimum = array[i];
            }
        }
        return minimum;
    }
    
    /**
     * Get maximum value from the integer array
     * @param array array of integers
     * @return maximum value
     */
    public static int getArrayMax(int[] array){
        int maximum = array[0];
        for(int i = 0; i < array.length; i++){
            if(array[i] > maximum){
                maximum = array[i];
            }
        }
        return maximum;
    }
    
    /**
     * Get maximum value from the list of BrickNodes
     * @param list array of BrickNodes
     * @return maximum value
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
     * Get minimum value from the list of BrickNodes
     * @param list array of BrickNodes
     * @return minimum value
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
    
    /**
     * Generate random array of integers
     * @param size size of array
     * @param min minimum value included
     * @param max maximum value included
     * @return array of integers
     */
    public static int[] generateRandomArray(int size, int min, int max) {
        Random randomValue = new Random();
        int[] randomArray = new int[size];
        for(int i = 0; i < size; i++){
            randomArray[i] = randomValue.nextInt((max - min) + 1) + min;
        }
        return randomArray;
    }
}
