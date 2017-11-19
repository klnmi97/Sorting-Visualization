/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import sortingvisualization.Core.IAlgorithm;
import sortingvisualization.Sorting.SortArray;

/**
 *
 * @author mihae
 */
public class InsertionSort implements IAlgorithm {

    @Override
    public void sort(SortArray array) {  
        for (int i = 1; i < array.length(); i++) {
            for(int j = i ; j > 0 ; j--){
                if(array.compare(j - 1, j)/*input[j] < input[j-1]*/){
                    /*temp = input[j];
                    input[j] = input[j-1];
                    input[j-1] = temp;*/
                    array.swap(j, j - 1);
                }
            }
        }
    }
    
}
