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
    public void setValue(int index, int value);
    public void set(int[] array);
    public int length();
}
