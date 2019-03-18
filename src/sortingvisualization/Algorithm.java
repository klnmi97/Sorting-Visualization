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

public enum Algorithm {
    Bubble          (0, "Bubble Sort", "BUBL"), 
    CocktailShaker  (1, "Cocktail-Shaker Sort", "CSh"), 
    Insertion       (2, "Insertion Sort", "INS"), 
    Selection       (3, "Selection Sort", "SEL"), 
    Merge           (4, "Merge Sort", "MRG"), 
    Quick           (5, "Quick Sort", "QUI"), 
    Counting        (6, "Counting Sort", "COU"),
    Bucket          (7, "Bucket Sort", "BCKT"),
    Radix           (8, "Radix Sort", "RDX"),
    Heap            (9, "Heap Sort", "HEAP");
    
    private int index;
    private String name;
    private String shortName;
    
    Algorithm(int index){  
        this.index = index;
    }
    
    Algorithm(int index, String name, String shortName){
        this.index = index;
        this.name = name;
        this.shortName = shortName;
    }
    
    public int getIndex(){return index;}
    public String getName(){return name;}
    public String getShortName(){return shortName;}
    
    @Override
    public String toString(){
        return this.getName();
    }
}