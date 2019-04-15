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
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sortingvisualization.Constants.Constants;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.Controllers.ViewController;
import sortingvisualization.NodeControllers.DynamicNodes;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 * Class for creation animation flow(code, sorting, variables) for 
 * Merge sorting algorithm.
 * @author Mykhailo Klunko
 */
public class MergeSort extends Sorting implements AbstractAlgorithm {

    private static final int ROOT = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = -1;
    
    private final DynamicNodes mngr;
    private final List<BrickNode> list;
    private final Pseudocode code;
    private final VariablesInfo vars;
    
    /**
     * Creates a new instance of Merge Sort algorithm animation flow 
     * creator class
     * @param manager node manager
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public MergeSort(DynamicNodes manager, VariablesInfo vars, Pane infoPane) {
        this.mngr = manager;
        this.list = manager.getNodes();
        this.code = new Pseudocode();
        this.vars = vars;
        addPseudocode(code);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates animation flow for the Merge sorting algorithm
     * @return list of animation steps
     */
    @Override
    public List<Animation> sort() {
        int number = list.size();
        List<Animation> anim = new ArrayList<>();
        
        sortRange(0, number - 1, anim, list, Constants.DEFAULT, ROOT);
        addAnimations(anim, code.unselectAll(),
                vars.setText("Array is sorted"));
        return anim;
    }

    private void sortRange(int low, int high, List<Animation> anim, 
            List<BrickNode> list, Color old, int child) {
        
        Color current;
        int recursionLine;
        switch (child) {
            case LEFT:
                current = getNodeColor(high);
                recursionLine = 3;
                break;
            case ROOT:
                current = old;
                recursionLine = 0;
                break;
            default:
                current = getNodeColor(list.size() - high);
                recursionLine = 4;
                break;
        }
        // check if low is smaller then high, if not then the array is sorted
        if (low < high) {
            // Get the index of the element which is in the middle
            int middle = low + (high - low) / 2;
            
            Color cLeft = getNodeColor(middle);
            Color cRight = getNodeColor(list.size() - high);
            ParallelTransition pt = new ParallelTransition();
            for(int i = low; i <= high; i++){
                pt.getChildren().add(mngr.setColor(list.get(i), old, current));
            }
            String msg;
            switch(child){
                case LEFT:
                    msg = String.format("Middle point mid = %d. "
                            + "Recursively apply MergeSort to the array from %d to %d. ", 
                            high, low, high);
                    break;
                case RIGHT:
                    msg = String.format("Middle point mid = %d. "
                            + "Recursively apply MergeSort to the array from %d to %d. ", 
                            low - 1, low, high);
                    break;
                default:
                    msg = String.format("Apply MergeSort to the array from %d to %d.", low, high);
                    
            }
            addAnimations(anim, pt,
                code.selectLines(recursionLine),
                vars.setText(msg));
            List<BrickNode> helperLow = new ArrayList<>();
            for(int i = low; i <=middle; i++){
                helperLow.add(list.get(i));
            }
            
            // Sort the left side of the array
            sortRange(low, middle, anim, list, current, LEFT);
            
            List<BrickNode> helperHigh = new ArrayList<>();
            for(int i = middle + 1; i <=high; i++){
                helperHigh.add(list.get(i));
            }
            
            // Sort the right side of the array
            sortRange(middle + 1, high, anim, list, current, RIGHT);
            
            // Combine them both
            merge(low, middle, high, list, anim);
            
            pt = new ParallelTransition();
            for(BrickNode node : helperLow){
                pt.getChildren().add(mngr.setColor(node, cLeft, current));
            }
            for(BrickNode node : helperHigh){
                pt.getChildren().add(mngr.setColor(node, cRight, current));
            }
            addAnimations(anim, pt);
        } else {
            if(child == LEFT || child == RIGHT){
                addAnimations(anim, code.selectLine(recursionLine),
                        vars.setText("Apply MergeSort to %s", list.get(low)),
                    mngr.setColor(list.get(low), old, current));
            }
        }
    }

    private void merge(int low, int middle, int high, List<BrickNode> list, 
            List<Animation> anim) {
        BrickNode[] helperNodes = new BrickNode[list.size()];
        // Copy both parts into the helper array
        for (int i = low; i <= high; i++) {
            helperNodes[i] = list.get(i);
        }

        int i = low;
        int j = middle + 1;
        int k = low;
        // Copy the smallest values from either the left or the right side back
        // to the original array

        while (i <= middle && j <= high) {
            
            if (helperNodes[i].getValue() <= helperNodes[j].getValue()) {
                list.set(k, helperNodes[i]);
                addAnimations(anim, code.selectLines(5, 9, 10, 11),
                        vars.setText("Check if %s < %s \nMove %s to tempArray[%d]", 
                                helperNodes[i], helperNodes[j], helperNodes[i], k - low),
                        mngr.moveDownToX(helperNodes[i], k, i));
                i++;
            } else {
                list.set(k, helperNodes[j]);
                addAnimations(anim, code.selectLines(5, 9, 10, 12),
                        vars.setText("Check if %s < %s \nMove %s to tempArray[%d]", 
                                helperNodes[i], helperNodes[j], helperNodes[j], k - low),
                        mngr.moveDownToX(helperNodes[j], k, j));
                j++;
            }
            k++;
        }
        // Copy the rest of the left side of the array into the target array
        while (i <= middle) {
            list.set(k, helperNodes[i]);
            addAnimations(anim, code.selectLines(5, 13, 14),
                    vars.setText("arrayR is empty. Move %s from arrayL to tempArray[%d]", helperNodes[i], k - low),
                    mngr.moveDownToX(helperNodes[i], k, i));
            k++;
            i++;
        }

        // Even if we didn't move in the array because it was already ordered, 
        // move on screen for any remaining nodes in the target array.
        while (j <= high) {
            addAnimations(anim, code.selectLines(5, 13, 14),
                    vars.setText("arrayL is empty. Move %s from arrayR to tempArray[%d]", helperNodes[j], k - low),
                    mngr.moveDownToX(helperNodes[j], k, j));
            k++;
            j++;
        }

        ParallelTransition moveUp = new ParallelTransition();

        for (int z = low; z <= high; z++) {
            TranslateTransition moveNodeUp = new TranslateTransition();
            moveNodeUp.setNode(helperNodes[z]);
            moveNodeUp.setDuration(ViewController.SPEED);
            moveNodeUp.setFromY(ViewController.LEVEL2);
            moveNodeUp.setToY(ViewController.LEVEL1);
            moveUp.getChildren().add(moveNodeUp);
        }

        addAnimations(anim, moveUp,
                code.selectLines(5, 15),
                vars.setText("Arrays are merged, move items back to the original array"));
    }
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(
                "MergeSort(arr, left, right):",
                "  if left < right",
                "    mid = (left + right) / 2",
                "    MergeSort(arr, left, mid)",
                "    MergeSort(arr, mid + 1, right)",
                "    Merge(arr, left, mid, right)",
                "",
                "Merge(array, left, mid, right):", //7
                "  create array result[right - left]",
                "  while arrayLIndex ⩽ mid and arrayRIndex ⩽ right",
                "    if arrayLHeadValue < arrayRHeadValue", //10
                "      copy arrayLHeadValue to tempArray",
                "    else: copy arrayRHeadValue to tempArray",
                "  while(arrayL or arrayR has elements)", //13
                "    copy currentValue to tempArray",
                "  copy elements back to original array");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(code.getCode());
        });
    }
    
    private Color getNodeColor(int position){
        int hue = (360 / list.size()) * position;
        Color nodeColor = Color.hsb(hue, 1.0, 1.0);
        return nodeColor;
    }
}
