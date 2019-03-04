/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;

/**
 *
 * @author Mykhailo Klunko
 * Class that contains data for
 * view and controller classes
 */
public class Data {
    
    private List<Animation> transitions;
    private List<BrickNode> list;
    private Algorithm currentInstance;

    public Data(){
        transitions = new ArrayList<>();
        list = new ArrayList<>();
        currentInstance = Algorithm.Bubble;
    }
    
    public Algorithm getCurrentInstance() {
        return currentInstance;
    }

    public void setCurrentInstance(Algorithm currentInstance) {
        this.currentInstance = currentInstance;
    }

    public List<Animation> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Animation> transitions) {
        this.transitions = transitions;
    }

    public List<BrickNode> getList() {
        return list;
    }

    public void setList(List<BrickNode> list) {
        this.list = list;
    }
    
}

