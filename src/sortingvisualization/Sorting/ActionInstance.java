/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Sorting;

/**
 *
 * @author mihae
 */
public class ActionInstance {
    
    private int numberOfAction;
    /*
    * 0 - swap
    * 1 - compare
    * 2 - select
    */
    private int parametr1;
    private int parametr2;
    
    public ActionInstance(int numberOfAction, int parametr){
        this.numberOfAction = numberOfAction;
        this.parametr1 = parametr;
    }
    
    public ActionInstance(int numberOfAction, int parametr, int parametr2){
        this.numberOfAction = numberOfAction;
        this.parametr1 = parametr;
        this.parametr2 = parametr2;
    }
    
    public int[] get(){
        if(numberOfAction == 0 || numberOfAction == 1){
            return new int[]{numberOfAction, parametr1, parametr2};
        } else return new int[]{numberOfAction, parametr1};
    }
    
}
