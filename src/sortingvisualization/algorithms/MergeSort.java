/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.Arrays;
import sortingvisualization.Core.IAlgorithm;
import sortingvisualization.Sorting.SortArray;
import sortingvisualization.Sorting.SubArray;

/**
 *
 * @author mihae
 */
public class MergeSort implements IAlgorithm {

    @Override
    public void sort(SortArray array) {
        sort(array, 0, array.length() - 1);
    }
    
    private void sort(SortArray arr, int l, int r)
    {
        if (l < r)
        {
            // Find the middle point
            int m = l + (r-l)/2;
 
            // Sort first and second halves
            sort(arr, l, m);
            sort(arr , m+1, r);
 
            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }
    
    private void merge(SortArray arr, int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        /* Create temp arrays */
        SubArray L = new SubArray(n1);//[n1]; //would it change values than
        SubArray R = new SubArray(n2); //[n2];
 
        /*Copy data to temp arrays*/
        for (int i=0; i<n1; ++i)
            L.setValue(i, arr.getValue(l + i));//[l + i];
        for (int j=0; j<n2; ++j)
            R.setValue(j, arr.getValue(m + 1+ j));
        //L.set(Arrays.copyOfRange(arr.get(), l, n1));
        //R.set(Arrays.copyOfRange(arr.get(), m + l, n2));
 
 
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2)
        {
            if (L.getValue(i) <= R.getValue(j)/*L[i] <= R[j]*/)
            {
                //arr[k] = L[i];
                arr.setValue(k, L.getValue(i));
                i++;
            }
            else
            {
                //arr[k] = R[j];
                arr.setValue(k, R.getValue(j));
                j++;
            }
            k++;
        }
 
        /* Copy remaining elements of L[] if any */
        while (i < n1)
        {
            //arr[k] = L[i];
            arr.setValue(k, L.getValue(i));
            i++;
            k++;
        }
 
        /* Copy remaining elements of R[] if any */
        while (j < n2)
        {
            //arr[k] = R[j];
            arr.setValue(k, R.getValue(j));
            j++;
            k++;
        }
    }
    
}
