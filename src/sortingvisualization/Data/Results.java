/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Data;

import sortingvisualization.Enums.Algorithm;

/**
 *
 * @author Mykhailo Klunko
 */
public class Results {

    private int[] input;
    private Algorithm algoritm;

    public Results(int[] input, Algorithm algorithm) {
        this.input = input;
        this.algoritm = algorithm;
    }

    public int[] getInput() {
        return input;
    }

    public Algorithm getAlgoritm() {
        return algoritm;
    }
}
