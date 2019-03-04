/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import sortingvisualization.algorithms.BubbleSort;

/**
 *
 * @author mihae
 */
public class AnimationJob extends Service<List<Animation>> {

    private List<BrickNode> list;
    private Algorithm currentInstance;
    
    public AnimationJob(List<BrickNode> list, Algorithm instanceType) {
        this.list = list;
        this.currentInstance = instanceType;
    }
    
    @Override
    protected Task<List<Animation>> createTask() {
        return new Task<List<Animation>>() {
            @Override
            protected List<Animation> call() throws Exception {
                List<Animation> anim = new ArrayList<>();
                switch(currentInstance){
                    case Bubble:
                        BubbleSort bs = new BubbleSort(list);
                        anim = bs.bubbleSort(list);
                        break;
                    default:
                        bs = new BubbleSort(list);
                        anim = bs.bubbleSort(list);
                        break;
                }
                
                return anim;
            }
        };
    }
    
}
