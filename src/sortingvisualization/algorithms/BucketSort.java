/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.scene.layout.Pane;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;

/**
 *
 * @author mihae
 */
public class BucketSort {
    
    public static List<Animation> bucketSort(ArrayList<BrickNode> list, Pane codePane){
        List<Animation> anim = new ArrayList<>();
        Pseudocode pc = new Pseudocode();
        addPseudocode(codePane, pc);
        
        return anim;
    }
    
    private static void addPseudocode(Pane codePane, Pseudocode code) {
        code.addLines(codePane, 
                "pseudocode",
                "code",
                "code",
                "code");
    }
    
}
