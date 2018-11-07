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
        
        // Determine minimum and maximum values
        int minValue = list.get(0).getValue();
        int maxValue = list.get(0).getValue();
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getValue() < minValue) {
                minValue = list.get(i).getValue();
            } else if (list.get(i).getValue() > maxValue) {
                maxValue = list.get(i).getValue();
            }
        }
        
        // Initialise buckets
        int bucketSize = 5;
        int bucketCount = (maxValue - minValue) / bucketSize + 1;
        List<List<BrickNode>> buckets = new ArrayList<List<BrickNode>>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<BrickNode>());
        }
        
        // Distribute input array values into buckets
        for (int i = 0; i < list.size(); i++) {
            buckets.get((list.get(i).getValue() - minValue) / bucketSize).add(list.get(i));
        }
        
        // Sort buckets and place back into input array
        int currentIndex = 0;
        for (int i = 0; i < buckets.size(); i++) {
            BrickNode[] bucketArray = new BrickNode[buckets.get(i).size()];
            bucketArray = buckets.get(i).toArray(bucketArray);
            sort(bucketArray);
            for (int j = 0; j < bucketArray.length; j++) {
                list.set(currentIndex++, bucketArray[j]);
            }
        }
        
        return anim;
    }
    
    private static void addPseudocode(Pane codePane, Pseudocode code) {
        code.addLines(codePane, 
                "pseudocode",
                "code",
                "code",
                "code");
    }

    /*
    * Local Insertion sort for sorting buckets
    */
    private static void sort(BrickNode[] bucketArray) {
        int n = bucketArray.length;
        
        for (int i = 1; i < n; ++i)
        {
            BrickNode key = bucketArray[i];
            int j = i-1;
            while (j>=0 && bucketArray[j].compareTo(key) == 1) 
            {
                bucketArray[j + 1] = bucketArray[j];
                j = j-1;
            }
            bucketArray[j + 1] = key;
        }
    }
    
}
