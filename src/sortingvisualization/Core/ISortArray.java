/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Core;

/**
 *
 * @author mihae
 */
public interface ISortArray {
    
    public int[] get();
    public int getValue(int index);
    public void set(int[] array);
    public void generate(int size, int max);
    public int length();
    public void swap(int i, int j);
    /**
     * Compare if array[i]>array[j]
     * @return - true if array[i]>array[j]
     */
    public boolean compare(int i, int j);
    public void shuffle();
}
