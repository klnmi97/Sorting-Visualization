
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import sortingvisualization.Constants.Constants;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.DynamicNodes;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 * Class for creation an animation flow(code, sorting, variables) for 
 * the Quick sort algorithm.
 * @author Mykhailo Klunko
 */
public class QuickSort extends Sorting implements AbstractAlgorithm {

    private final DynamicNodes mngr;
    private final List<BrickNode> list;
    private final Pseudocode code;
    private final VariablesInfo vars;
    
    /**
     * Creates a new instance of the Quick Sort algorithm animation flow 
     * creator class
     * @param manager node manager
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public QuickSort(DynamicNodes manager, VariablesInfo vars, Pane infoPane) {
        this.mngr = manager;
        this.list = manager.getNodes();
        this.code = new Pseudocode();
        this.vars = vars;
        addPseudocode(code);
        addCodeToUI(infoPane);
    }
    
    @Override
    public List<Animation> sort(){
        List<Animation> anim = new ArrayList<>();
        
        quickSort(list, 0, list.size() - 1, anim, code);
        addAnimations(anim, code.unselectAll(),
                vars.setText("Array is sorted"));
        
        return anim;
    } 
     
    private void quickSort(List<BrickNode> list, int low, int high, List<Animation> anim, Pseudocode code) 
    { 
        //addAnimToList(anim, code.selectLine(1));
        
        if (low < high) 
        { 
            /* pi is partitioning index, arr[pi] is  
              now at right place */
            int partitionIndex = partition(list, low, high, anim, code);
            if(low < partitionIndex - 1) {
                addAnimations(anim, code.selectLine(3),
                        vars.setText("Recursively apply QuickSort to array from %d to %d", 
                                low, partitionIndex - 1));
            }
            quickSort(list, low, partitionIndex - 1, anim, code); 
            if(partitionIndex + 1 < high) {
                addAnimations(anim, code.selectLine(4),
                        vars.setText("Recursively apply QuickSort to array from %d to %d", 
                                partitionIndex + 1, high));
            }
            quickSort(list, partitionIndex+1, high, anim, code); 
        }
        else if(low == high){
            addAnimations(anim, vars.setText("%s is sorted", list.get(high)),
                    mngr.setColor(high, Constants.D_DEFAULT, Constants.SORTED));
        }
    } 
     
    private int partition(List<BrickNode> list, int low, int high, 
            List<Animation> anim, Pseudocode code) 
    { 
        ParallelTransition parallelTransition = new ParallelTransition();
        for (int k = low; k <= high; k++) {
            parallelTransition.getChildren().add(mngr.moveDownToX(list.get(k), k, k));            
        }
        addAnimations(anim, code.selectLine(2),
                parallelTransition);
        
        BrickNode pivot = list.get(high);
        int index = low; // index of smaller element
        
        addAnimations(anim, code.selectLines(2,7,8,9),
                vars.setText("Select %s as a pivot\nA[index] is %s", pivot, list.get(index)),
                mngr.setColor(high, Constants.D_DEFAULT, Constants.SELECTED));
        
        for (int j=low; j<high; j++) 
        { 
            addAnimations(anim, code.selectLines(2, 10),
                    vars.setText("Check if %s < %s, A[index] is %s", list.get(j), pivot, list.get(index)),
                    mngr.setColor(j, Constants.D_DEFAULT, Constants.COMPARE));
            // If current element is smaller than or 
            // equal to pivot
            if (list.get(j).getValue() <= pivot.getValue()) {
                addAnimations(anim, code.selectLines(2, 11),
                        vars.setText("Swap %s and %s", list.get(j), list.get(index)),
                        mngr.swap(index, j));
                
                BrickNode temp = list.get(index); 
                list.set(index, list.get(j)); 
                list.set(j, temp);
                
                addAnimations(anim, code.selectLines(2, 12),
                        vars.setText("A[index] is now %s", list.get(index + 1)),
                        mngr.setColor(index, Constants.COMPARE, Constants.D_DEFAULT));
                index++;
                
            } else {
                addAnimations(anim, mngr.setColor(j, Constants.COMPARE, Constants.D_DEFAULT));
            } 
            
        } 
  
        addAnimations(anim, code.selectLines(2, 13),
                vars.setText("Swap %s and %s", list.get(index), pivot),
                mngr.swap(index, high));
        // swap arr[i+1] and arr[high] (or pivot)
        BrickNode temp = list.get(index); 
        list.set(index, list.get(high)); 
        list.set(high, temp);
        
        addAnimations(anim, vars.setText("%s is now sorted", list.get(index)),
                mngr.setColor(pivot, Constants.SELECTED, Constants.SORTED));
        
        parallelTransition = new ParallelTransition();
        for (int k = low; k <= high; k++) {
            parallelTransition.getChildren().add(mngr.moveNodeUp(list.get(k)));            
        }
        addAnimations(anim, code.selectLines(2, 14),
                parallelTransition);
        return index; 
    }
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(
                "QuickSort(A, low, high):",
                "  if low < high",
                "    mid = Partition(A, low, high)",
                "    QuickSort(A, low, mid - 1)",
                "    QuickSort(A, mid + 1, high)",
                "",
                "Partition(A, low, high):",
                "  pivot = A[high]",
                "  index = low",
                "  for j = low to high - 1",
                "    if A[j] ≤ pivot",
                "      swap(A[j], A[index])",
                "      index++",
                "  swap(A[index], pivot)",
                "  return index + 1");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(code.getCode());
        });
    }
}
