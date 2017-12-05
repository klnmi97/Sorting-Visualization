/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

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
        
        Application.launch(Window.class, args);
        
    }
    
}
