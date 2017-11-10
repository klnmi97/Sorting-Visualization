/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author mihae
 */
public class SortingLogic {
    
    int[] numbers = new int[10];
    private List<CompListener> listeners = new ArrayList<CompListener>();

    public void addListener(CompListener toAdd) {
        listeners.add(toAdd);
    }
    
    public int[] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }
    
    public SortingLogic(){
        random();
    }
    
    private boolean firstBigger(int first, int second, int firstIndex, int secondIndex){
        for (CompListener c : listeners)
                c.numbersComp(firstIndex, secondIndex);
        if(first > second){
            return true;
        }
        return false;
    }
    
    private void swap(int [] array, int firstIndex, int secondIndex){
        for (CompListener c : listeners)
                c.numbersSwap(firstIndex, secondIndex);
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
                    
    }
    
    private int setkey(int index){
        for (CompListener c : listeners)
                c.getkey(index);
        return numbers[index];
    }
    
    private int setId(int index){
        for (CompListener c : listeners)
                c.getkey(index);
        return index;
    }
    
    public void random(){
        Random randomNum = new Random();
        for (int i=0; i<10; i++){
            numbers[i] = randomNum.nextInt(51);
        }
        
    }
    
    public void bubbleSort(){
        int n = numbers.length;  
        int temp = 0;  
        for(int i=0; i < n; i++){  
            for(int j=1; j < (n-i); j++){  
                /*for (CompListener c : listeners)
                        c.numbersComp(j - 1, j);*/
                if(firstBigger(numbers[j-1], numbers[j], j - 1, j)){
                    swap(numbers, j-1, j);
                    /*temp = numbers[j-1];  
                    numbers[j-1] = numbers[j];  
                    numbers[j] = temp;  
                    for (CompListener c : listeners)
                        c.numbersSwap(j - 1, j);*/
                    }
                if(j == n-i-1){
                        for (CompListener c : listeners)
                        c.sorted(numbers[j]);
                }
            }
        }
    }
    
    public void insertionSort(){
        int n = numbers.length;
        for (int i=1; i<n; ++i)
        {
            int key = setkey(i);
            
            int j = i-1;
 
            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            
            while (j>=0 && firstBigger(numbers[j], key, i, j))
            {
                /*for (CompListener c : listeners)
                        c.numbersComp(i, j);*/
                numbers[j+1] = numbers[j];
                j = j-1;
            }
            numbers[j+1] = key;
        }
    }
    
    public void selectionSort(){
        int n = numbers.length;
 
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = setId(i);
            for (int j = i+1; j < n; j++)
                if (firstBigger(numbers[min_idx], numbers[j], min_idx, j)){
                    min_idx = setId(j);
                }
 
            // Swap the found minimum element with the first
            // element
            swap(numbers, min_idx, i);
            /*int temp = numbers[min_idx];
            numbers[min_idx] = numbers[i];
            numbers[i] = temp;*/
        }
    }
    
    public void quickSort(){
        
    }
    
    public void mergeSort(){
        
    }
    
    public void countingSort(){
        
    }
}
