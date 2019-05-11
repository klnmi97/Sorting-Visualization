
package sortingvisualization.Controllers;

import sortingvisualization.Data.BindingData;
import java.util.List;
import javafx.animation.Animation;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Controller of animation. Provides functions for playing, stopping and stepping
 * through the list of animations
 * @author Mykhailo Klunko
 */
public class AnimationController {
    
    private static final int MAX_SPEED = 9999;
    private List<Animation> transitions = null;
    private final IntegerProperty nextTransitionIndex;
    private BooleanBinding anyPlayingAnim;
    private BooleanBinding stepForthBinding;
    private BooleanBinding stepBackBinding;
    private BooleanBinding playBinding;
    private ChangeListener<Number> speedListener;
    
    private double currentSpeed = 3;
    
    /**
     * Instantiates AnimationController class
     */
    public AnimationController(){
        nextTransitionIndex = new SimpleIntegerProperty(0);
    }
    
    /**
     * Initializes new cycle of sorting animation
     * @param transitions - list of created animations
     * @return bindings for step-back, step-forth, play buttons and slider listener
     */
    public BindingData setupInstance(List<Animation> transitions){
        if(this.transitions != null){
            stopAllAnimations();
        }
        nextTransitionIndex.set(0);
        this.transitions = transitions;
        return createBindings();
    }
    
    private void stopAllAnimations(){
        transitions.stream()
                .forEach(anim->anim.stop());
    }
    
    /**
     * Plays animations from list got by setupInstance function
     */
    public void play(){
        if(nextTransitionIndex.get()<transitions.size()){
            int index = nextTransitionIndex.get();
            Animation anim = transitions.get(index);
            anim.setOnFinished(evt -> {
                nextTransitionIndex.set(index+1);
                play();
            });
            anim.setRate(currentSpeed);
            anim.play();}
    }
    
    /**
     * Plays animations on the maximal speed (fast forward)
     */
    public void playFast() {
        if(nextTransitionIndex.get()<transitions.size()){
            int index = nextTransitionIndex.get();
            Animation anim = transitions.get(index);
            anim.setOnFinished(evt -> {
                nextTransitionIndex.set(index+1);
                playFast();
            });
            anim.setRate(MAX_SPEED);
            anim.play();}
    }
    
    /**
     * Stops current animation
     */
    public void pause(){
        transitions.stream()
                .filter(anim -> anim.getStatus()==Animation.Status.RUNNING)
                .forEach(anim -> anim.setOnFinished(evt ->{
                    int index = nextTransitionIndex.get();
                    nextTransitionIndex.set(index+1);
                }));
    }
    
    /**
     * Plays previous step
     */
    public void goStepBack() {
        if(!stepBackBinding.get()){
            int index = nextTransitionIndex.get()-1;
            Animation anim = transitions.get(index);
            anim.setOnFinished(evt -> nextTransitionIndex.set(index));
            anim.setRate(-currentSpeed);
            anim.play();
        }
    }
    
    /**
     * Play next step
     */
    public void goStepForth() {
        if(!stepForthBinding.get()){
            int index = nextTransitionIndex.get();
            Animation anim = transitions.get(index);
            anim.setOnFinished(evt -> nextTransitionIndex.set(index+1));
            anim.setRate(currentSpeed);
            anim.play();
        }
    }
    
    private BooleanBinding createAnyPlayingBinding(List<Animation> transitions) {
        return new BooleanBinding() {
            { // Anonymous constructor
                // bind to the status properties of all the transitions
                // (i.e. mark this binding as invalid if any of the status properties change)
                transitions.stream()
                    .map(Animation::statusProperty)
                    .forEach(this::bind);
            }
            @Override
            protected boolean computeValue() {
                // return true if any of the transitions statuses are equal to RUNNING:
                return transitions.stream()
                    .anyMatch(anim -> anim.getStatus()==Animation.Status.RUNNING);
            }
        };

    }
    
    /*
     * Creates bindings that reacts on the main actions
     */
    private BindingData createBindings() {
        anyPlayingAnim = createAnyPlayingBinding(transitions);
        stepForthBinding = nextTransitionIndex.greaterThanOrEqualTo(transitions.size())
                .or(anyPlayingAnim);
        stepBackBinding = nextTransitionIndex.lessThanOrEqualTo(0)
                .or(anyPlayingAnim);
        playBinding = nextTransitionIndex.greaterThanOrEqualTo(transitions.size())
                .or(anyPlayingAnim);
        speedListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            for(int i = 0; i < transitions.size(); i++){
                transitions.get(i).setRate(newValue.doubleValue());
                currentSpeed = newValue.doubleValue();
            }
        };
        return new BindingData(stepForthBinding, stepBackBinding, playBinding, speedListener);
    }
}
