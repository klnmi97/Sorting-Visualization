/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Sorting;

import sortingvisualization.Core.AbstractSortArray;

/**
 *
 * @author mihae
 */
public class SortArray extends AbstractSortArray {
        
    @Override
    public boolean compare(int i, int j){
        System.out.println("array[" + i + "] and array[" + j + "] compared");
        return super.compare(i, j);
    }

    @Override
    public boolean biggerEqual(int i, int j) {
        System.out.println("array[" + i + "] and array[" + j + "] compared");
        return super.biggerEqual(i, j); 
    }
    
    
    
    @Override
    public void swap(int i, int j) {
        super.swap(i, j);
        System.out.println("array[" + i + "] and array[" + j + "] swapped");
    }
}
