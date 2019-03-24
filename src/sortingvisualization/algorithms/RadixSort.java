/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sortingvisualization.Utilities.AnimUtils;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.Controllers.ViewController;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 * Class for creation animation flow(code, sorting, variables) for 
 * Radix sorting algorithm.
 * @author Mykhailo Klunko
 */
public class RadixSort extends Sorting implements AbstractAlgorithm {
    
    private static final int DIGITS = 10;
    private final List<BrickNode> list;
    private final Pseudocode code;
    private final VariablesInfo vars;
    
    /**
     * Creates a new instance of the Radix Sort algorithm animation flow 
     * creator class
     * @param list list of nodes to animate
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public RadixSort(List<BrickNode> list, VariablesInfo vars, Pane infoPane){
        this.list = list;
        this.code = new Pseudocode();
        this.vars = vars;
        addPseudocode(code);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates  an animation flow for the Radix sorting algorithm
     * @return list of animation steps
     */
    @Override
    public List<Animation> sort() {
        List<Animation> anim = new ArrayList<>();
        int maxValue = getMaxValue(list);
        int maxLength = (int) (Math.log10(maxValue) + 1);
        vars.setDefaultText(String.format("d is %d(max number of digits)", maxLength));
        
        for (int exp = 1, i = 0; maxValue/exp > 0; exp *= DIGITS, i++) {
            addAnimations(anim, code.selectLine(1),
                    vars.setText("i is %d", i),
                    AnimUtils.setDigitsColor(list, i, Color.BLACK, Color.RED));
            
            countSort(list, list.size(), exp, anim);
            
            addAnimations(anim, vars.setText(""),
                    AnimUtils.setDigitsColor(list, i, Color.RED, Color.BLACK));
        }
        addAnimations(anim, code.unselectAll(),
                vars.setText("Array is sorted"));
        return anim;
    }
    
    
    private void countSort(List<BrickNode> list, int n, int exp, List<Animation> anim) {
        BrickNode output[] = new BrickNode[n];
        int i; 
        int count[] = new int[DIGITS];
        int helperCount[] = new int[DIGITS];
        Arrays.fill(count,0); 
  
        // Store count of occurrences in count[] 
        for (i = 0; i < n; i++){ 
            int bucket = (list.get(i).getValue() / exp) % DIGITS;
            int currentDigit = (int) (Math.log10(exp));
            count[bucket]++; 
            
            addAnimations(anim, code.selectLine(2),
                    vars.setText("Sorting with Counting sort by %d. digit:\n%d in %s", currentDigit, bucket, list.get(i)),
                    AnimUtils.moveTo(list.get(i), i, bucket, count[bucket] - 1, 
                    ViewController.LEFT_INDENT, ViewController.countIndent(DIGITS)));
        }
  
        //Fill helper array for animation
        for(i = 0; i < DIGITS; i++) {
            helperCount[i] = count[i];
        }
        
        // Change count[i] so that count[i] now contains 
        // actual position of this digit in output[] 
        for (i = 1; i < DIGITS; i++) {
            count[i] += count[i - 1]; 
        }
        
        // Build the output list 
        for (i = n - 1; i >= 0; i--) {
            int digit = (list.get(i).getValue() / exp) % DIGITS;
            int toX = count[digit] - 1;
            int fromY = helperCount[digit] - 1;
            
            output[toX] = list.get(i);
            
            addAnimations(anim, vars.setText("Place %s to the position %d in the output array", list.get(i), toX),
                    AnimUtils.moveFrom(list.get(i), toX, digit, fromY, 
                    ViewController.countIndent(10), ViewController.LEFT_INDENT));
            
            count[digit]--; 
            helperCount[digit]--;
        } 
  
        // Copy the output array to list, so that list now 
        // contains sorted numbers according to curent digit 
        for (i = 0; i < n; i++) {
            list.set(i, output[i]); 
        }
    }
    
    private void addPseudocode(Pseudocode code) {
        code.addLines(
                "RadixSort(A, d):",
                "  for i = 0 to d - 1:",
                "    do Counting(Stable) Sort for i-th digit");
    }
    
    private void addCodeToUI(Pane codePane) {
        Platform.runLater(() -> {
            codePane.getChildren().addAll(code.getCode());
        });
    }
}
