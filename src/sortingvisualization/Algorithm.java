/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

/**
 *
 * @author Mykhailo Klunko
 */

enum Algorithm {
    Bubble          (0, "Bubble Sort"), 
    CocktailShaker  (1, "Cocktail-Shaker Sort"), 
    Insertion       (2, "Insertion Sort"), 
    Selection       (3, "Selection Sort"), 
    Merge           (4, "Merge Sort"), 
    Quick           (5, "Quick Sort"), 
    Counting        (6, "Counting Sort"),
    Bucket          (7, "Bucket Sort"),
    Radix           (8, "Radix Sort");
    //Heap            (9);
    
    private int index;
    private String name;
    
    Algorithm(int index){  
        this.index = index;
    }
    
    Algorithm(int index, String name){
        this.index = index;
        this.name = name;
    }
    
    public int getIndex(){return index;}
    public String getName(){return name;}
}