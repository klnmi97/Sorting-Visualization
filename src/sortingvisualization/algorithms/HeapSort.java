/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import sortingvisualization.Pseudocode;
import NodeCreation.Tree;
import sortingvisualization.BrickNode;


/**
 *
 * @author Mykhailo Klunko
 */
public class HeapSort extends Sorting implements AbstractAlgorithm {

    private Tree binaryTree;
    private Pseudocode pc;
    private List<BrickNode> list;
    
    public HeapSort(Tree binaryHeap, Pane infoPane){
        this.binaryTree = binaryHeap;
        this.list = binaryTree.getNodesList();
        this.pc = new Pseudocode();
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }

    @Override
    public List<Animation> sort() {
        List<Animation> anim = new ArrayList<>();
        
        int n = list.size(); 
  
        // Build heap (rearrange array) 
        for (int i = n / 2 - 1; i >= 0; i--) 
            heapify(list, anim, n, i); 
  
        // One by one extract an element from heap 
        for (int i = n - 1; i >= 0; i--) 
        { 
            anim.add(binaryTree.swap(0, i));
            // Move current root to end 
            BrickNode temp = list.get(0); 
            list.set(0, list.get(i)); 
            list.set(i, temp); 

            anim.add(binaryTree.hide(i));
            // call max heapify on the reduced heap 
            heapify(list, anim, i, 0); 
        }
        
        return anim;
    }
    
    void heapify(List<BrickNode> list, List<Animation> anim, int n, int i) 
    { 
        int largest = i; // Initialize largest as root 
        int l = 2*i + 1; // left = 2*i + 1 
        int r = 2*i + 2; // right = 2*i + 2 
  
        // If left child is larger than root 
        if (l < n) {
            anim.add(binaryTree.compare(l, largest));
            
            if (list.get(l).getValue() > list.get(largest).getValue()){
                largest = l; 
            }
        }
            
            
        
            
        // If right child is larger than largest so far 
        if (r < n) { 
            anim.add(binaryTree.compare(r, largest));
            
            if(list.get(r).getValue() > list.get(largest).getValue()) {
                largest = r;
            }
        }

        // If largest is not root 
        if (largest != i) 
        {
            anim.add(binaryTree.swap(i, largest));
            
            BrickNode swap = list.get(i); 
            list.set(i, list.get(largest)); 
            list.set(largest, swap); 
            
            // Recursively heapify the affected sub-tree 
            heapify(list, anim, n, largest); 
        } 
    }
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines("some pseudocode here");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
