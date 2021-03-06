
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.NodeControllers.Tree;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.VariablesInfo;


/**
 * Class for creation animation flow(code, sorting, variables) for 
 * Heap sorting algorithm.
 * @author Mykhailo Klunko
 */
public class HeapSort extends Sorting implements AbstractAlgorithm {

    private Tree binaryTree;
    private Pseudocode pc;
    private List<BrickNode> list;
    private VariablesInfo vars;
    
    private int heapline = 2;
    
    /**
     * Creates a new instance of Heap Sort algorithm animation flow 
     * creator class
     * @param binaryHeap instance of heap graphic controller
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public HeapSort(Tree binaryHeap, VariablesInfo vars, Pane infoPane) {
        this.binaryTree = binaryHeap;
        this.list = binaryTree.getNodesList();
        this.pc = new Pseudocode();
        this.vars = vars;
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }

    /**
     * Creates animation flow for the Heap sorting algorithm
     * @return list of animation steps
     */
    @Override
    public List<Animation> sort() {
        List<Animation> anim = new ArrayList<>();
        
        int n = list.size(); 
  
        heapline = 2;
        // Build heap (rearrange array) 
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(list, anim, n, i); 
        } 
            
  
        // One by one extract an element from heap 
        for (int i = n - 1; i >= 0; i--) 
        { 
            if(i != 0) {
                addAnimations(anim, binaryTree.swap(0, i),
                                    pc.selectLines(3, 4),
                                    vars.setText("Swap root(" + list.get(0) 
                                            + ") and " + list.get(i)));
            }
            // Move current root to end 
            BrickNode temp = list.get(0); 
            list.set(0, list.get(i)); 
            list.set(i, temp); 

            addAnimations(anim, binaryTree.setSorted(i),
                    vars.setText(list.get(i) + " is sorted"));
            heapline = 5;
            
            // call max heapify on the reduced heap
            heapify(list, anim, i, 0); 
        }
        addAnimations(anim, pc.unselectAll(),
                vars.setText("Array is sorted"));
        return anim;
    }
    
    void heapify(List<BrickNode> list, List<Animation> anim, int n, int i) { 
        int largest = i; // Initialize largest as root 
        int l = 2*i + 1; // left = 2*i + 1 
        int r = 2*i + 2; // right = 2*i + 2 
  
        // If left child is larger than root 
        if (l < n) {
            
            if (list.get(l).getValue() > list.get(largest).getValue()){
                
                addAnimations(anim, binaryTree.compare(l, largest),
                                    pc.selectLines(heapline, 10, 11),
                                    vars.setText("Check if " + list.get(l) 
                                            + " > " + list.get(largest) 
                                            + "\nSet largest to " + list.get(l)));
                largest = l; 
            } else {
                addAnimations(anim, binaryTree.compare(l, largest),
                                    pc.selectLines(heapline, 10, 12),
                                    vars.setText("Check if " + list.get(l) 
                                            + " > " + list.get(largest)));
            }
        } /*else {
            addAnimations(anim, pc.selectLines(heapline, 10));
        }*/
        
        // If right child is larger than largest so far 
        if (r < n) {
            if(list.get(r).getValue() > list.get(largest).getValue()) {
                addAnimations(anim, binaryTree.compare(r, largest),
                                    pc.selectLines(heapline, 13, 14),
                                    vars.setText("Check if " + list.get(r) 
                                            + " > " + list.get(largest) 
                                            + "\nSet largest to " + list.get(r)));
                
                largest = r;
            } else {
                addAnimations(anim, binaryTree.compare(r, largest),
                                    pc.selectLines(heapline, 13),
                                    vars.setText("Check if " + list.get(r) 
                                            + " > " + list.get(largest)));
            }
        } /*else {
            addAnimations(anim, pc.selectLines(heapline, 13));
        }*/
        
        
        // If largest is not root 
        if (largest != i) {
            addAnimations(anim, binaryTree.swap(i, largest),
                                pc.selectLines(heapline, 15, 16, 17),
                                vars.setText("Swap " + list.get(i) + " and " 
                                        + list.get(largest)));
            
            BrickNode swap = list.get(i); 
            list.set(i, list.get(largest)); 
            list.set(largest, swap); 
            
            // Recursively heapify the affected sub-tree 
            heapify(list, anim, n, largest); 
        } /*else {
            //addAnimations(anim, pc.selectLines(heapline, 15));
        }*/
    }
    
    private void addPseudocode(Pseudocode code) {
        code.addLines("HeapSort(A):",
                      "  for i = n/2 - 1 downto 0:",
                      "    Heapify(A, n, i)",
                      "  for i = n - 1 downto 1:",
                      "    swap(A[0], A[i])",
                      "    Heapify(A, i, 0)", //5
                      "",
                      "Heapify(A, heapsize, i):",
                      "  l = LeftChild(i)",
                      "  r = RightChild(i)",
                      "  if l ≤ heapsize and A[l] > A[i]:", //10
                      "    then largest = l",
                      "  else largest = i",
                      "  if r ≤ heapsize and A[r] > A[largest]:",
                      "    then largest = r",
                      "  if largest ≠ i:", //15
                      "    then: swap(A[i], A[largest])",
                      "          Heapify(A, heapsize, largest)");
    }
    
    private void addCodeToUI(Pane codePane) {
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
