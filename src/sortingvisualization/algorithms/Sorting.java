/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.algorithms;

import java.util.List;
import javafx.animation.Animation;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import sortingvisualization.BrickNode;

/**
 *
 * @author mihae
 */
public abstract class Sorting {

    
    public abstract void addCodeToUI(Pane codePane);
    
    protected void addAnimToList(List<Animation> animList, Animation... anims){
        for(Animation anim : anims){
            if(anim != null){
                animList.add(anim);
            }
        }
    }
    
}
