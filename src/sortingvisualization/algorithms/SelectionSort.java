
package sortingvisualization.algorithms;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import sortingvisualization.Constants.Constants;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.DynamicNodes;
import sortingvisualization.NodeControllers.Pseudocode;
import sortingvisualization.NodeControllers.VariablesInfo;

/**
 * Class for creation animation flow(code, sorting, variables) for 
 * Selection sorting algorithm.
 * @author Mykhailo Klunko
 */
public class SelectionSort extends Sorting implements AbstractAlgorithm {

    private final DynamicNodes mngr;
    private final List<BrickNode> list;
    private final Pseudocode pc;
    private final VariablesInfo vars;
    
    /**
     * Creates a new instance of the Insertion Sort algorithm animation flow 
     * creator class
     * @param manager node manager
     * @param vars instance of variables information class
     * @param infoPane pane where the code will be placed
     */
    public SelectionSort(DynamicNodes manager, VariablesInfo vars, Pane infoPane) {
        this.mngr = manager;
        this.list = manager.getNodes();
        this.pc = new Pseudocode();
        this.vars = vars;
        addPseudocode(pc);
        addCodeToUI(infoPane);
    }
    
    /**
     * Creates animation flow for the Insertion sorting algorithm
     * @return list of animation steps
     */
    @Override
    public List<Animation> sort(){
        List<Animation> anim = new ArrayList<>();
        int arrayLength = list.size();
        ParallelTransition parallelTransition;
        ParallelTransition compareTransition;
        
        for (int i = 0; i < arrayLength - 1; i++){  
            int index = i;
            
            addAnimations(anim, pc.selectLines(2, 3),
                    vars.setText("Set %s at position %d as current minimum", list.get(i), i),
                    mngr.setColor(i, Constants.D_DEFAULT, Constants.SELECTED));
            compareTransition = new ParallelTransition();
            
            for (int j = i + 1; j < arrayLength; j++) {
                
                compareTransition.getChildren().add(mngr.setColor(j, Constants.D_DEFAULT, Constants.COMPARE));
                addAnimations(anim, pc.selectLine(4),
                        vars.setText("Check if %s < %s", list.get(j), list.get(index)),
                        compareTransition);
                
                if (list.get(j).getValue() < list.get(index).getValue()){ 
                    
                    addAnimations(anim, pc.selectLine(5),
                            vars.setText("Set %s at position %d as current minimum", list.get(j), j),
                            mngr.setColor(index, Constants.SELECTED, Constants.D_DEFAULT),
                            mngr.setColor(j, Constants.COMPARE, Constants.SELECTED));
                    
                    index = j;  //searching for lowest index 
                    
                    compareTransition = new ParallelTransition();
                } else {
                    
                    compareTransition = new ParallelTransition();
                    compareTransition.getChildren().add(mngr.setColor(j, Constants.COMPARE, Constants.D_DEFAULT));
                }
            }
            
            addAnimations(anim, compareTransition);
            parallelTransition = new ParallelTransition();
            
            if(index != i){
                addAnimations(anim, pc.selectLine(6), 
                        vars.setText("Swap %s and %s", list.get(index), list.get(i)),
                        new SequentialTransition(
                        mngr.setColor(i, Constants.D_DEFAULT, Constants.SELECTED), 
                        mngr.swap(index, i)));
                
                parallelTransition.getChildren().add(mngr.setColor(i, Constants.SELECTED, 
                        Constants.D_DEFAULT));
            }
            parallelTransition.getChildren().add(mngr.setColor(
                    index, Constants.SELECTED, Constants.SORTED));
            addAnimations(anim, pc.selectLine(6),
                    vars.setText("%s is now sorted", list.get(index)),
                    parallelTransition);
            
            BrickNode smallerNumber = list.get(index);   
            list.set(index, list.get(i));
            list.set(i, smallerNumber);
            
            
        }
        addAnimations(anim, pc.unselectAll(),
                vars.setText("Array is sorted"),
                mngr.setColor(arrayLength - 1, Constants.D_DEFAULT, Constants.SORTED));
        return anim;
    }
    
    private void addPseudocode(Pseudocode code){
        //TODO: improve pseudocode
        code.addLines(
                "SelectionSort(A):",
                " for i = 0 to size(A) - 1",
                "   set the first unsorted element as the minimum",
                "     for i = 1 to size(A) - 1",
                "       if currentElement < currentMin",
                "         set currentElement as currentMin",
                "       swap currentMin with first unsorted element");
    }
    
    private void addCodeToUI(Pane codePane){
        Platform.runLater(() -> {
            codePane.getChildren().addAll(pc.getCode());
        });
    }
}
