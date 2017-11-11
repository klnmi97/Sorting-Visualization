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
public class CocktailShakerSort implements IAlgorithm {

    @Override
    public void sort(int[] array) {
        boolean swapped;
        do {
          swapped = false;
          for (int i =0; i<=  array.length  - 2;i++) {
            if (array[ i ] > array[ i + 1 ]) {
              //test whether the two elements are in the wrong order
              int temp = array[i];
              array[i] = array[i+1];
              array[i+1]=temp;
              swapped = true;
            }
          }
          if (!swapped) {
            //we can exit the outer loop here if no swaps occurred.
            break;
          }
          swapped = false;
          for (int i= array.length - 2;i>=0;i--) {
            if (array[ i ] > array[ i + 1 ]) {
              int temp = array[i];
              array[i] = array[i+1];
              array[i+1]=temp;
              swapped = true;
            }
          }
        //if no elements have been swapped, then the list is sorted
        } while (swapped);
    }
    
}
