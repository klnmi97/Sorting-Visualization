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
public class InsertionSort implements IAlgorithm {

    @Override
    public void sort(int[] array) {  
        for (int j = 1; j < array.length; j++) {  
            int key = array[j];  
            int i = j-1;  
            while ( (i > -1) && ( array [i] > key ) ) {  
                array [i+1] = array [i];  
                i--;  
            }  
            array[i+1] = key;  
        }  
    }
    
}
