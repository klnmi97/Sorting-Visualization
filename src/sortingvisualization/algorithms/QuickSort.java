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
public class QuickSort implements IAlgorithm {

    @Override
    public void sort(SortArray array) {
        quickSort(array, 0, array.length() - 1);
    }
    
    private void quickSort(SortArray arr, int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is 
              now at right place */
            int pi = partition(arr, low, high);
 
            // Recursively sort elements before
            // partition and after partition
            quickSort(arr, low, pi-1);
            quickSort(arr, pi+1, high);
        }
    }
    
    private int partition(SortArray arr, int low, int high)
    {
        //int pivot = arr.getValue(high); 
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            // If current element is smaller than or
            // equal to pivot
            if (arr.biggerEqual(high, j)/*arr[j] <= pivot*/)
            {
                i++;
 
                // swap arr[i] and arr[j]
                /*int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;*/
                arr.swap(i, j);
            }
        }
 
        // swap arr[i+1] and arr[high] (or pivot)
        /*int temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;*/
        arr.swap(i + 1, high);
 
        return i+1;
    }
    
}
