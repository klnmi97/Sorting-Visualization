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
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class BucketSort extends Sorting implements AbstractAlgorithm{
    
    /**
     * Maximum capacity of one bucket
     */
    public static final int BUCKET_SIZE = 15;
    
    private List<BrickNode> list;
    private Pseudocode pc;
    
    public BucketSort(List<BrickNode> list, Pane infoPane){
        this.list = list;
        pc = new Pseudocode();
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    @Override
    public List<Animation> sort(){
        List<Animation> anim = new ArrayList<>();
        
        // Determine minimum and maximum values
        int minValue = getMinValue(list);
        int maxValue = getMaxValue(list);
        
        // Initialise buckets
        int bucketCount = (maxValue - minValue) / BUCKET_SIZE + 1;
        List<List<BrickNode>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
        int bucketsLIndent = (int)(((double)bucketCount / 2) * -ViewController.SPACING);
        
        // Distribute input array values into buckets
        for (int i = 0; i < list.size(); i++) {
            int selectedBucket = (list.get(i).getValue() - minValue) / BUCKET_SIZE;
            buckets.get(selectedBucket).add(list.get(i));
            int nextBucketVal = buckets.get(selectedBucket).size() - 1;
            
            anim.add(AnimUtils.makeParallel(
                    AnimUtils.moveTo(list.get(i), i, selectedBucket, 
                            nextBucketVal, ViewController.LEFT_INDENT, bucketsLIndent),
                    pc.selectLine(1)));
            //System.out.println(((Text)list.get(i).getChildren().stream().filter(e -> e instanceof Text).findFirst().get()).getText());
        }
        
        // Sort buckets and place back into input array
        int currentIndex = 0;
        for (int i = 0; i < buckets.size(); i++) {
            BrickNode[] bucketArray = new BrickNode[buckets.get(i).size()];
            bucketArray = buckets.get(i).toArray(bucketArray);
            sortStable(bucketArray, anim, pc);
            for (int j = 0; j < bucketArray.length; j++) {
                list.set(currentIndex++, bucketArray[j]);
                anim.add(AnimUtils.makeParallel(
                        AnimUtils.moveFrom(bucketArray[j], currentIndex-1, i, j, 
                                bucketsLIndent, ViewController.LEFT_INDENT),
                        pc.selectLines(4, 5)));
            }
        }
        anim.add(pc.unselectAll());
        return anim;
    }
    
     /*
    * Local Insertion sort for sorting buckets
    */
    private void sortStable(BrickNode[] bucketArray, List<Animation> anim, Pseudocode pc) {
        int n = bucketArray.length;
        
        addAnimToList(anim, pc.selectLines(2, 3));
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
                    anim.add(AnimUtils.makeParallel(
                                parallel,
                                pc.selectLines(2, 3)));
                } else{
                    anim.add(pc.selectLines(2, 3));
                }
            } 
        }
    }
    
    private void addPseudocode(Pseudocode code) {
        code.addLines( 
                "create buckets",
                "distribute array into buckets",
                "for each bucket:",
                "  sort bucket with insertion sort",
                "  for each element in bucket:",
                "    place element back into input array");
    }


    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
    
}
