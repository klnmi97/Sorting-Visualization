/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import sortingvisualization.Core.IAlgorithm;

/**
 *
 * @author mihae
 */
public class SelectionSort implements IAlgorithm{

    @Override
    public void sort(int[] array) {
        for (int i = 0; i < array.length - 1; i++)  
        {  
            int index = i;  
            for (int j = i + 1; j < array.length; j++){  
                if (array[j] < array[index]){  
                    index = j;//searching for lowest index  
                }  
            }  
            int smallerNumber = array[index];   
            array[index] = array[i];  
            array[i] = smallerNumber;  
        }  
    }
    
}
