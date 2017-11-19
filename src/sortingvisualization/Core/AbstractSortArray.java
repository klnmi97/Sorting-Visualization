/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Core;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author mihae
 */
public abstract class AbstractSortArray implements ISortArray {
    
    private int[] array;

    @Override
    public int[] get() {
        return array;
    }

    @Override
    public int getValue(int index){
       return array[index];
    }
    
    @Override
    public void set(int[] array) {
        this.array = array;
    }

    @Override
    public void setValue(int index, int value) {
        array[index] = value;
    }

    @Override
    public int length() {
        return array.length;
    }

    public void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public boolean compare(int i, int j) {
        return array[i] > array[j];
    }

    public void shuffle() {
        Random rnd = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--)
        {
          int index = rnd.nextInt(i + 1);
          int a = array[index];
          array[index] = array[i];
          array[i] = a;
        }
    }

    public void generate(int size, int max) {
        array = new int[size];
        Random randomNum = new Random();
        for (int i=0; i<size; i++){
            array[i] = randomNum.nextInt(max);
        }
    }
    
    public boolean biggerEqual(int i, int j){
        return array[i] >= array[j];
    }
    
    @Override
    public String toString(){
        String outputStr = "";
        for(int i = 0; i < array.length; i++){
            outputStr += array[i];
            if(i != array.length - 1){
                outputStr += ", ";
            } else{
                outputStr += ".\n";
            }
        }
        return outputStr;
    }
    
}
