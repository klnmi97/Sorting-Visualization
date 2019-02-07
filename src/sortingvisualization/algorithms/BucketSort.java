/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;
import sortingvisualization.ViewController;

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
        int bucketSize = 15;
        int bucketCount = (maxValue - minValue) / bucketSize + 1;
        List<List<BrickNode>> buckets = new ArrayList<List<BrickNode>>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<BrickNode>());
        }
        int bucketsLIndent = (int)(((double)bucketCount / 2) * -ViewController.SPACING);
        
        // Distribute input array values into buckets
        for (int i = 0; i < list.size(); i++) {
            int selectedBucket = (list.get(i).getValue() - minValue) / bucketSize;
            buckets.get(selectedBucket).add(list.get(i));
            int nextBucketVal = buckets.get(selectedBucket).size() - 1;
            anim.add(AnimUtils.moveTo(list.get(i), i, selectedBucket, nextBucketVal, ViewController.LEFT_INDENT, bucketsLIndent));
            //System.out.println(((Text)list.get(i).getChildren().stream().filter(e -> e instanceof Text).findFirst().get()).getText());
        }
        
        // Sort buckets and place back into input array
        int currentIndex = 0;
        for (int i = 0; i < buckets.size(); i++) {
            BrickNode[] bucketArray = new BrickNode[buckets.get(i).size()];
            bucketArray = buckets.get(i).toArray(bucketArray);
            sort(bucketArray, anim);
            for (int j = 0; j < bucketArray.length; j++) {
                list.set(currentIndex++, bucketArray[j]);
                anim.add(AnimUtils.moveFrom(bucketArray[j], currentIndex-1, i, j, bucketsLIndent, ViewController.LEFT_INDENT));
            }
        }
        
        return anim;
    }
    
     /*
    * Local Insertion sort for sorting buckets
    */
    private static void sort(BrickNode[] bucketArray, List<Animation> anim) {
        int n = bucketArray.length;
        
        for (int i = 1; i < n; ++i)
        {
            ParallelTransition moveUp = new ParallelTransition();
            BrickNode key = bucketArray[i];
            int j = i-1;
            while (j>=0 && bucketArray[j].compareTo(key) == 1) 
            {
                bucketArray[j + 1] = bucketArray[j];
                moveUp.getChildren().add(AnimUtils.moveY(bucketArray[j], j, j + 1));
                j = j-1;
            }
            bucketArray[j + 1] = key;
            //replace with new list implementation
            if(i != j + 1){
                ParallelTransition parallel = AnimUtils.makeParallel(
                    moveUp,
                    AnimUtils.moveY(key, i, j + 1));
                if(parallel.getChildren().size() > 0){
                    anim.add(parallel);
                }
            }
        }
    }
    
    private static void addPseudocode(Pane codePane, Pseudocode code) {
        code.addLines(codePane, 
                "create buckets",
                "distribute array into buckets",
                "for each bucket:",
                "  sort bucket with insertion sort",
                "  for each element in bucket:",
                "    place element back into input array");
    }

   
    
}
