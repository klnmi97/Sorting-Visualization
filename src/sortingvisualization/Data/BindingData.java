
package sortingvisualization.Data;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;

/**
 * Holder of the bindings for main control buttons
 * @author Mykhailo Klunko
 */
public class BindingData {
    
    private BooleanBinding stepForthBinding;
    private BooleanBinding stepBackBinding;
    private BooleanBinding playBinding;
    private ChangeListener<Number> speedListener;

    /**
     * Instantiates BindingData class
     * @param forthBinding binding of button that controls stepping forth
     * @param backBinding binding of button that controls stepping back
     * @param playBinding binding of button that controls starting/resuming
     * @param speedListener listener of the speed slider
     */
    public BindingData(BooleanBinding forthBinding, BooleanBinding backBinding, 
            BooleanBinding playBinding, ChangeListener<Number> speedListener){
        this.stepForthBinding = forthBinding;
        this.stepBackBinding = backBinding;
        this.playBinding = playBinding;
        this.speedListener = speedListener;
    }
    
    /**
     * Gets binding of the button that controls stepping forth
     * @return BooleanBinding for the button
     */
    public BooleanBinding getStepForthBinding() {
        return stepForthBinding;
    }

    /**
     * Sets binding of the button that controls stepping forth
     * @param stepForthBinding BooleanBinding for the button
     */
    public void setStepForthBinding(BooleanBinding stepForthBinding) {
        this.stepForthBinding = stepForthBinding;
    }

    /**
     * Gets binding of the button that controls stepping back
     * @return BooleanBinding for the button
     */
    public BooleanBinding getStepBackBinding() {
        return stepBackBinding;
    }

    /**
     * Sets binding of the button that controls stepping back
     * @param stepBackBinding BooleanBinding for the button
     */
    public void setStepBackBinding(BooleanBinding stepBackBinding) {
        this.stepBackBinding = stepBackBinding;
    }

    /**
     * Gets binding of the button that controls starting and resuming animations
     * @return BooleanBinding for the button
     */
    public BooleanBinding getPlayBinding() {
        return playBinding;
    }

    /**
     * Sets binding of the button that starts and resumes animations
     * @param playBinding BooleanBinding for the button
     */
    public void setPlayBinding(BooleanBinding playBinding) {
        this.playBinding = playBinding;
    }

    /**
     * Gets listener for the slider, that controls speed of animations
     * @return change listener
     */
    public ChangeListener<Number> getSpeedListener() {
        return speedListener;
    }

    /**
     * Sets listener for the slider, that controls speed of animations
     * @param speedListener change listener
     */
    public void setSpeedListener(ChangeListener<Number> speedListener) {
        this.speedListener = speedListener;
    }
    
}
