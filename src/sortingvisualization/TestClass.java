/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.ArrayList;

/**
 *
 * @author mihae
 */
public class TestClass implements CompListener{

    ArrayList a = new ArrayList();
    
    @Override
    public void numbersComp(int pos1, int pos2) {
        System.out.println("numbers " + pos1 + " and " + pos2 + " compared");
    }

    @Override
    public void numbersSwap(int num1, int num2) {
        System.out.println("numbers " + num1 + " and " + num2 + " swapped");    
    }

    @Override
    public void sorted(int pos) {
        a.add(pos);
        System.out.print("sorted: ");
        for (int i = a.size() - 1; i >= 0; i--) {
            System.out.print(a.get(i) + ", ");
        }
        System.out.println();
    }

    @Override
    public void getkey(int key) {
        System.out.println("current min = " + key);
    }
    
    
}
