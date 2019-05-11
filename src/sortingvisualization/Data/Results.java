
package sortingvisualization.Data;

import sortingvisualization.Enums.Algorithm;

/**
 * Holder for the user input data from the InputDialog
 * @author Mykhailo Klunko
 */
public class Results {

    private int[] input;
    private Algorithm algoritm;

    /**
     * Instantiates Results class
     * @param input array of integers
     * @param algorithm chosen algorithm type
     */
    public Results(int[] input, Algorithm algorithm) {
        this.input = input;
        this.algoritm = algorithm;
    }

    /**
     * Gets the array of integers entered by user
     * @return array of integers
     */
    public int[] getInput() {
        return input;
    }

    /**
     * Gets algorithm selected by user
     * @return type of the algorithm from the Algorithm enum
     */
    public Algorithm getAlgoritm() {
        return algoritm;
    }
}
