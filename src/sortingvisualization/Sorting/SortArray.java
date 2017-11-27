/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Sorting;

import java.util.ArrayList;
import java.util.List;
import sortingvisualization.Core.AbstractSortArray;

/**
 *
 * @author mihae
 */
public class SortArray extends AbstractSortArray {
        
    public List<ActionInstance> actions;
    
    public SortArray(){
        actions = new ArrayList<>();
    }
    
    @Override
    public boolean compare(int i, int j){
        //System.out.println("array[" + i + "] and array[" + j + "] compared");
        
        actions.add(new ActionInstance(1, i, j));
        return super.compare(i, j);
    }

    @Override
    public boolean biggerEqual(int i, int j) {
        //System.out.println("array[" + i + "] and array[" + j + "] compared");
        actions.add(new ActionInstance(1, i, j));
        return super.biggerEqual(i, j); 
    }
    
    
    
    @Override
    public void swap(int i, int j) {
        super.swap(i, j);
        //System.out.println("array[" + i + "] and array[" + j + "] swapped");
        actions.add(new ActionInstance(0, i, j));
    }
}
