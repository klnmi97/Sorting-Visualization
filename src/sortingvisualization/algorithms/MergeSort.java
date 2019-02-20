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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sortingvisualization.AnimUtils;
import sortingvisualization.BrickNode;
import sortingvisualization.Pseudocode;
import sortingvisualization.ViewController;

/**
 *
 * @author Mykhailo Klunko
 */
public class MergeSort {

    public static List<Animation> mergeSort(ArrayList<BrickNode> list, Pane codePane) {
        int number = list.size();
        List<Animation> anim = new ArrayList<>();
        Pseudocode pc = new Pseudocode();
        addPseudocode(codePane, pc);
        sortRange(0, number - 1, anim, list, pc, 150, -1);
        return anim;
    }

    private static void sortRange(int low, int high, List<Animation> sq, 
            ArrayList<BrickNode> list, Pseudocode code, int newHue, int currentHue) {
        Color original;
        if(currentHue == -1){
             original = Color.hsb(180, 1.0, 0.55);
        } else {
            original = Color.hsb(currentHue, 1.0, 1.0);
        }
        //Color original = Color.hsb(currentHue, 1.0, 1.0);
        Color current = Color.hsb(newHue, 1.0, 1.0);
        // check if low is smaller then high, if not then the array is sorted
        if (low < high) {
            // Get the index of the element which is in the middle
            int middle = low + (high - low) / 2;
            
            ParallelTransition pt = new ParallelTransition();
            for(int i = low; i <= high; i++){
                pt.getChildren().add(AnimUtils.setColor(list.get(i), original, current));
            }
            sq.add(AnimUtils.makeParallel(
                    pt,
                    code.selectLines(1,2)));
            
            List<BrickNode> helperLow = new ArrayList<>();
            Color nextColorLow = Color.hsb(newHue - (newHue / 2), 1.0, 1.0);
            for(int i = low; i <=middle; i++){
                helperLow.add(list.get(i));
            }
            
            
            // Sort the left side of the array
            addAnimToList(sq, code.selectLine(3));
            sortRange(low, middle, sq, list, code, newHue - (newHue / 2), newHue);
            
            
            List<BrickNode> helperHigh = new ArrayList<>();
            Color nextColorHi = Color.hsb(newHue + (newHue / 2), 1.0, 1.0);
            for(int i = middle + 1; i <=high; i++){
                helperHigh.add(list.get(i));
            }
            
            
            // Sort the right side of the array
            addAnimToList(sq, code.selectLine(4));
            sortRange(middle + 1, high, sq, list, code, newHue + (newHue / 2), newHue);
            
            
            // Combine them both
            addAnimToList(sq, code.selectLine(5));
            merge(low, middle, high, list, sq, code);
            
            pt = new ParallelTransition();
            for(BrickNode node : helperLow){
                pt.getChildren().add(AnimUtils.setColor(node, nextColorLow, current));
            }
            for(BrickNode node : helperHigh){
                pt.getChildren().add(AnimUtils.setColor(node, nextColorHi, current));
            }
            sq.add(pt);
        } else {
            sq.add(AnimUtils.makeParallel(
                    AnimUtils.setColor(list.get(low), original, current),
                    code.selectLine(1)));
            addAnimToList(sq, code.selectLine(6));
        }
    }

    private static void merge(int low, int middle, int high, 
            ArrayList<BrickNode> list, List<Animation> sq, Pseudocode code) {
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
                sq.add(AnimUtils.makeParallel(
                        AnimUtils.moveDownToX(helperNodes[i], k, i),
                        code.selectLines(10, 11, 12)));
                i++;
            } else {
                list.set(k, helperNodes[j]);
                sq.add(AnimUtils.makeParallel(
                        AnimUtils.moveDownToX(helperNodes[j], k, j),
                        code.selectLines(10, 11, 13)));
                j++;
            }
            k++;
        }
        // Copy the rest of the left side of the array into the target array
        while (i <= middle) {
            list.set(k, helperNodes[i]);
            sq.add(AnimUtils.makeParallel(
                    AnimUtils.moveDownToX(helperNodes[i], k, i),
                    code.selectLines(14,15)));
            k++;
            i++;
        }

        // Even if we didn't move in the array because it was already ordered, 
        // move on screen for any remaining nodes in the target array.
        while (j <= high) {
            sq.add(AnimUtils.makeParallel(
                    AnimUtils.moveDownToX(helperNodes[j], k, j),
                    code.selectLines(14, 15)));
            k++;
            j++;
        }

        ParallelTransition moveUp = new ParallelTransition();

        for (int z = low; z <= high; z++) {
            TranslateTransition moveNodeUp = new TranslateTransition();
            moveNodeUp.setNode(helperNodes[z]);
            moveNodeUp.setDuration(ViewController.SPEED);
            //Set start Y position for reverse animation
            moveNodeUp.setFromY(ViewController.LEVEL2);
            moveNodeUp.setToY(ViewController.LEVEL1);
            moveUp.getChildren().add(moveNodeUp);
        }

        sq.add(AnimUtils.makeParallel(
                moveUp,
                code.selectLine(16)));
    }
    
    private static void addPseudocode(Pane codePane, Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(codePane, 
                "MergeSort(arr, left, right):",
                "  if left < right",
                "    mid = (left + right) / 2",
                "    MergeSort(arr, left, mid - 1)",
                "    MergeSort(arr, mid + 1, right)",
                "    Merge(arr, left, mid, right)",
                "  else return",
                "",
                "Merge(array, left, mid, right):", //8
                "  create array result[right - left]",
                "  while arrayLIndex <= mid and arrayRIndex <= right",
                "    if arrayLHeadValue < arrayRHeadValue", //11
                "      copy arrayLHeadValue to result",
                "    else: copy arrayRHeadValue",
                "  while(arrayL or arrayR has elements)", //14
                "    copy currentValue to result",
                "  copy elements back to original array");
    }
    
    private static void addAnimToList(List<Animation> animList, Animation... anims){
        for(Animation anim : anims){
            if(anim != null){
                animList.add(anim);
            }
        }
    }
}
