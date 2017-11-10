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
public class BubbleSort implements IAlgorithm {

    @Override
    public void sort(int[] array) {
        int n = array.length;  
        int temp = 0;  
        for(int i=0; i < n; i++){  
            for(int j=1; j < (n-i); j++){  
                if(array[j-1] > array[j]){   
                    temp = array[j-1];  
                    array[j-1] = array[j];  
                    array[j] = temp;  
                }  
                          
            }  
        }  
    }
    
}
