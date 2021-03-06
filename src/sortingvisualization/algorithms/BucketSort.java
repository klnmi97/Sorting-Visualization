
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import sortingvisualization.Constants.Constants;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.NodeControllers.FixedNodes;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 * Class for creation animation flow(code, sorting, variables) for 
 * Bucket sorting algorithm.
 * @author Mykhailo Klunko
 */
public class BucketSort extends Sorting implements AbstractAlgorithm{
    
    /**
     * Maximum capacity of one bucket
     */
    private final int BUCKET_SIZE;
    
    private final FixedNodes mngr;
    private final List<BrickNode> list;
    private final Pseudocode code;
    private final VariablesInfo vars;
    
    /**
     * Creates a new instance of Bucket Sort algorithm animation flow 
     * creator class
     * @param nodeManager fixed nodes manager
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public BucketSort(FixedNodes nodeManager, VariablesInfo vars, Pane infoPane) {
        this.mngr = nodeManager;
        this.list = nodeManager.getNodes();
        this.BUCKET_SIZE = list.size();
        this.code = new Pseudocode();
        this.vars = vars;
        addPseudocode(code);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates animation flow for the Bucket sorting algorithm
     * @return list of animation steps
     */
    @Override
    public List<Animation> sort(){
        List<Animation> anim = new ArrayList<>();
        
        // Determine minimum and maximum values
        int minValue = Constants.MIN;//getMinValue(list);
        int maxValue = Constants.MAX;//getMaxValue(list);
        
        
        // Initialise buckets
        int bucketCount = (maxValue - minValue) / BUCKET_SIZE + 1;
        List<List<BrickNode>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
        
        vars.setDefaultText("Created " + bucketCount + " buckets of expected capacity " 
                + BUCKET_SIZE + " items");
        // Distribute input array values into buckets
        for (int i = 0; i < list.size(); i++) {
            int selectedBucket = (list.get(i).getValue() - minValue) / BUCKET_SIZE;
            buckets.get(selectedBucket).add(list.get(i));
            int nextBucketVal = buckets.get(selectedBucket).size() - 1;
            
            int bucketMin = minValue + selectedBucket * BUCKET_SIZE;
            int bucketMax = minValue + selectedBucket * BUCKET_SIZE + BUCKET_SIZE;
            
            addAnimations(anim, code.selectLine(2),
                    vars.setText("Move %d. item of value %s to the bucket of range %d-%d", 
                            i, list.get(i), bucketMin, bucketMax),
                    mngr.moveDownTo(list.get(i), i, selectedBucket, 
                            0, nextBucketVal, bucketCount));
        }
        
        // Sort buckets and place back into input array
        int currentIndex = 0;
        for (int i = 0; i < buckets.size(); i++) {
            BrickNode[] bucketArray = new BrickNode[buckets.get(i).size()];
            bucketArray = buckets.get(i).toArray(bucketArray);
            sortStable(bucketArray, i, anim);
            for (int j = 0; j < bucketArray.length; j++) {
                list.set(currentIndex++, bucketArray[j]);
                
                addAnimations(anim, code.selectLines(5, 6),
                        vars.setText("Move %d. item in bucket of value %s to the position %d", 
                                j, bucketArray[j], currentIndex-1),
                        mngr.moveUpTo(bucketArray[j], currentIndex-1, i, j, 0, bucketCount));
            }
        }
        addAnimations(anim, code.unselectAll(),
                vars.setText("Array is sorted"));
        return anim;
    }
    
   /*
    * Local Insertion sort for sorting buckets
    */
    private void sortStable(BrickNode[] bucketArray, int bucket, List<Animation> anim) {
        int n = bucketArray.length;
        
        if(bucketArray.length > 0) {
            addAnimations(anim, code.selectLines(3, 4),
                    vars.setText("Sort %d. bucket with insertion sort", bucket));
        }
        for (int i = 1; i < n; ++i)
        {
            ParallelTransition moveUp = new ParallelTransition();
            BrickNode key = bucketArray[i];
            int j = i-1;
            while (j>=0 && bucketArray[j].compareTo(key) == 1) 
            {
                bucketArray[j + 1] = bucketArray[j];
                moveUp.getChildren().add(mngr.changeLowY(bucketArray[j], j, j + 1));
                j = j-1;
            }
            bucketArray[j + 1] = key;
            //replace with new list implementation
            if(i != j + 1){
                ParallelTransition parallel = new ParallelTransition(
                    moveUp,
                    mngr.changeLowY(key, i, j + 1));
                
                if(parallel.getChildren().size() > 0){
                    
                    addAnimations(anim, parallel,
                                code.selectLines(3, 4),
                                vars.setText("Sort %d. bucket with insertion sort", bucket));
                } else{
                    addAnimations(anim, code.selectLines(3, 4),
                            vars.setText("Sort %d. bucket with insertion sort", bucket));
                }
            } 
        }
    }
    
    private void addPseudocode(Pseudocode code) {
        code.addLines(
                "BucketSort(A):",
                "  create buckets",
                "  distribute array into buckets",
                "  for each bucket:",
                "    sort bucket with insertion sort",
                "    for each element in bucket:",
                "      place element back into input array");
    }


    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(code.getCode());
        });
    }
    
}
