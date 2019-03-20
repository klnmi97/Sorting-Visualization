/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Data;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author mihae
 */
public class BindingData {
    
    private BooleanBinding stepForthBinding;
    private BooleanBinding stepBackBinding;
    private BooleanBinding playBinding;
    private ChangeListener<Number> speedListener;

    public BindingData(BooleanBinding forthBinding, BooleanBinding backBinding, 
            BooleanBinding playBinding, ChangeListener<Number> speedListener){
        this.stepForthBinding = forthBinding;
        this.stepBackBinding = backBinding;
        this.playBinding = playBinding;
        this.speedListener = speedListener;
    }
    
    public BooleanBinding getStepForthBinding() {
        return stepForthBinding;
    }

    public void setStepForthBinding(BooleanBinding stepForthBinding) {
        this.stepForthBinding = stepForthBinding;
    }

    public BooleanBinding getStepBackBinding() {
        return stepBackBinding;
    }

    public void setStepBackBinding(BooleanBinding stepBackBinding) {
        this.stepBackBinding = stepBackBinding;
    }

    public BooleanBinding getPlayBinding() {
        return playBinding;
    }

    public void setPlayBinding(BooleanBinding playBinding) {
        this.playBinding = playBinding;
    }

    public ChangeListener<Number> getSpeedListener() {
        return speedListener;
    }

    public void setSpeedListener(ChangeListener<Number> speedListener) {
        this.speedListener = speedListener;
    }
    
}
