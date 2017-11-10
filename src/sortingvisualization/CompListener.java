/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

/**
 *
 * @author mihae
 */
public interface CompListener {
    void numbersComp(int pos1, int pos2);
    void numbersSwap(int num1, int num2);
    void sorted(int pos);
    void getkey(int key);
    //void assign(int to, int from);
}
