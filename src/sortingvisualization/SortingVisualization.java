/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.List;
import javafx.application.Application;
import sortingvisualization.Sorting.ActionInstance;

import sortingvisualization.Sorting.SortArray;
import sortingvisualization.algorithms.BubbleSort;
import sortingvisualization.algorithms.CocktailShakerSort;
import sortingvisualization.algorithms.HeapSort;
import sortingvisualization.algorithms.InsertionSort;
import sortingvisualization.algorithms.MergeSort;
import sortingvisualization.algorithms.QuickSort;
import sortingvisualization.algorithms.SelectionSort;

/**
 *
 * @author Mykhailo Klunko
 */
public class SortingVisualization {

    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws InterruptedException {
        /*TestClass t = new TestClass();
        SortingLogic s = new SortingLogic();
        int[] a = s.getNumbers();
        for(int i = 0; i < a.length; i++){
            System.out.print(a[i] + ", ");
        }
        System.out.println();
        s.addListener(t);
        s.selectionSort();
        a = s.getNumbers();
        for(int i = 0; i < a.length; i++){
            System.out.print(a[i] + ", ");
        }
        Application.launch(Window.class, args);*/
        
        
        SortArray array = new SortArray();
        array.generate(10, 80);
        System.out.println(array);
        BubbleSort sortAlgorithm = new BubbleSort();
        sortAlgorithm.sort(array);
        
        List<ActionInstance> actions = array.actions;
        for (int i = 0; i < actions.size(); i++) {
            int[] temp = actions.get(i).get();
            if(temp[0] == 0){
                System.out.println("array[" + temp[1] + "] and array[" + temp[2] + "] swapped");
            } else if(temp[0] == 1){
                System.out.println("array[" + temp[1] + "] and array[" + temp[2] + "] compared");
            }
            Thread.sleep(500);
        }
        
        System.out.println(array);
    }
    
}
