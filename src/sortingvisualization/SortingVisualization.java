/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import javafx.application.Application;

/**
 *
 * @author Mykhailo Klunko
 */
public class SortingVisualization {

    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        TestClass t = new TestClass();
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
        Application.launch(Window.class, args);
        
    }
    
}
