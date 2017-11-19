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
public class AbstractSubArray implements ISortArray {

    private int[] array;
    
    public AbstractSubArray(int size){
        array = new int[size];
    }
    
    @Override
    public int[] get() {
        return array;
    }

    @Override
    public int getValue(int index) {
        return array[index];
    }
    
    @Override
    public void setValue(int index, int value) {
        array[index] = value;
    }

    @Override
    public void set(int[] array) {
        this.array = array;
    }

    @Override
    public int length() {
        return array.length;
    }
    
}
