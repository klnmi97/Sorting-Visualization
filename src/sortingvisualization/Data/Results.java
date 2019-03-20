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

    private String input;
    private Algorithm algoritm;

    public Results(String input, Algorithm algorithm) {
        this.input = input;
        this.algoritm = algorithm;
    }

    public String getInput() {
        return input;
    }

    public Algorithm getAlgoritm() {
        return algoritm;
    }
}
