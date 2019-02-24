/*
 * Created on: September, 2018
 *
 */
package sortingvisualization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import static sortingvisualization.algorithms.BubbleSort.bubbleSort;
import static sortingvisualization.algorithms.BucketSort.bucketSort;
import static sortingvisualization.algorithms.CocktailShakerSort.cocktailShakerSort;
import static sortingvisualization.algorithms.CountingSort.countingSort;
import static sortingvisualization.algorithms.InsertionSort.insertionSort;
import static sortingvisualization.algorithms.MergeSort.mergeSort;
import static sortingvisualization.algorithms.QuickSort.quickSort;
import static sortingvisualization.algorithms.RadixSort.radixSort;
import static sortingvisualization.algorithms.SelectionSort.selectionSort;

/**
 *
 * @author Mykhailo Klunko
 */
public class ViewController {
    
    //TODO: create new metrics
    public static int N_VALUES = 12;
    public static final int SPACING = 60;
    //counting from center as 0; half of (spacing * number of elements)
    public static int TEN_LEFT_INDENT = (int)(((double)10 / 2) * -SPACING);
    public static int LEFT_INDENT = (int)(((double)N_VALUES / 2) * -SPACING);
    public static final int LEVEL1 = -350;
    public static final int LEVEL2 = 250 + LEVEL1;
    private final Font font = Font.font("Helvetica", 20);

    public static final Duration SPEED = Duration.millis(1000);
    public  double currentSpeed = 3;
    
    private final int max = 100;
    private final int min = 6;
    private final int couMax = 10; //check if 10 or 9?
    private final int couMin = 0;
    private final int radixMax = 9999;
    private final int radixMin = 0;
    //Style
    //colours of bricks:
    //COMPARE - selected, DEFAULT - main color
    public static final Color COMPARE = Color.AQUAMARINE;
    public static final Color DEFAULT = Color.DARKCYAN;
    public static final Color SORTED = Color.ORANGE;
    public static final Color LINE_SELECTION = Color.WHITE;
    
    private IntegerProperty nextTransitionIndex;
    private BooleanBinding anyPlayingAnim;
    private BooleanBinding stepForthBinding;
    private BooleanBinding stepBackBinding;
    private BooleanBinding playBinding;
    private ChangeListener<Number> speedListener;

    public ChangeListener<Number> getSpeedListener() {
        return speedListener;
    }

    public BooleanBinding getStepBackBinding() {
        return stepBackBinding;
    }

    public BooleanBinding getPlayBinding() {
        return playBinding;
    }
    
    public BooleanBinding getStepForthBinding() {
        return stepForthBinding;
    }
    
    private List<Animation> transitions;
    private List<BrickNode> list;
    private Pane displayPane;
    private Pane infoPanel;
    private Algorithm currentInstance;
    
    public ViewController(Data data, Pane displayPane, Pane infoPanel){
        this.transitions = data.getTransitions();
        this.list = data.getList();
        this.displayPane = displayPane;
        this.infoPanel = infoPanel;
        this.currentInstance = Algorithm.Bubble;
        nextTransitionIndex = new SimpleIntegerProperty();
    }
    
    public static int countIndent(int number){
        return (int)(((double)number / 2) * -SPACING);
    }
    
    private int[] generateRandomArray(int size, int min, int max){
        Random randomValue = new Random();
        int[] randomArray = new int[size];
        for(int i = 0; i < size; i++){
            randomArray[i] = randomValue.nextInt(max - min) + min + 1;
        }
        return randomArray;
    }
    
    /*
    * Animation actions
    */
    private void stopAllAnimations(){
        transitions.stream()
                .forEach(anim->anim.stop());
    }
    
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
    
    public void pause(){
        transitions.stream()
                .filter(anim -> anim.getStatus()==Animation.Status.RUNNING)
                .forEach(anim -> anim.setOnFinished(evt ->{
                    int index = nextTransitionIndex.get();
                    nextTransitionIndex.set(index+1);
                }));
    }
    
    public void goStepBack() {
        int index = nextTransitionIndex.get()-1;
        Animation anim = transitions.get(index);
        anim.setOnFinished(evt -> nextTransitionIndex.set(index));
        anim.setRate(-currentSpeed);
        anim.play();
    }
    
    public void goStepForth() {
        int index = nextTransitionIndex.get();
        Animation anim = transitions.get(index);
        anim.setOnFinished(evt -> nextTransitionIndex.set(index+1));
        anim.setRate(currentSpeed);
        anim.play();
    }
    
    private int getMinimum(Algorithm type){
        switch (type) {
            case Counting:
                return couMin;
            case Radix:
                return radixMin;
            default:
                return min;
        }
    }
    
    private int getMaximum(Algorithm type){
        switch (type) {
            case Counting:
                return couMax;
            case Radix:
                return radixMax;
            default:
                return max;
        }
    }
    
    //TODO: add exception handling
    private int getArrayMin(int[] array){
        int minimum = array[0];
        for(int i = 0; i < array.length; i++){
            if(array[i] < minimum){
                minimum = array[i];
            }
        }
        return minimum;
    }
    
    private int getArrayMax(int[] array){
        int maximum = array[0];
        for(int i = 0; i < array.length; i++){
            if(array[i] > maximum){
                maximum = array[i];
            }
        }
        return maximum;
    }
    
    //TODO: refactor!
    private BrickNode createFixedNode(int i, int value, double leftIndent, double topIndent){
        Rectangle rectangle = new Rectangle(50, 30);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.WHITE);
        //TODO: test on different screens!
        //rectangle.widthProperty().set(Screen.getPrimary().getVisualBounds().getWidth() * 0.033);
        Text text = new Text(String.valueOf(value));
        text.setFont(font);
        BrickNode node = new BrickNode(value);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        HBox numberBox = new HBox();
        String valueToSet = String.valueOf(value);
        int index = valueToSet.toCharArray().length - 1;
        for(char ch: valueToSet.toCharArray()){
            Text digit = new Text(Character.toString(ch));
            digit.setUserData(String.valueOf(index));
            digit.setFont(font);
            numberBox.getChildren().add(digit);
            numberBox.setAlignment(Pos.BOTTOM_CENTER);
            node.getDigits().add(digit);
            index--;
        }
        node.getChildren().addAll(rectangle, numberBox);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(SPACING * i + leftIndent);
        node.setTranslateY(topIndent);
        node.setShape(rectangle);
        return node;
    }
    
    private BrickNode createCustomNode(int i, int value, int currentMax, 
            Color color, double leftIndent, double topIndent) {
        int num = value;
        double percent = (double)num / currentMax;
        Rectangle rectangle = new Rectangle(50, (percent * 10 * 20) + 5);
        rectangle.setFill(color);
        
        Text text = new Text(String.valueOf(num));
        text.setFont(font);
        BrickNode node = new BrickNode(num);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        //node.setId(String.valueOf(num));
        //stackPane.setValue(num);
        node.getChildren().addAll(rectangle, text);
        BrickNode.setAlignment(text, Pos.BOTTOM_CENTER);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(SPACING * i + leftIndent);
        node.setTranslateY(topIndent);
        node.setShape(rectangle);
        return node;
    }
    
    private BrickNode createValueNode(int i, int value, int currentMax, int leftIndent) {
        return createCustomNode(i, value, currentMax, DEFAULT, leftIndent, LEVEL1);
    }
    
    /*
    * Animation bindings
    */
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
    
    private List<BrickNode> createGreyNodes(int count){
        List<BrickNode> subList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BrickNode stackPane = createCustomNode(i, i, count, Color.LIGHTGREY, 
                    countIndent(count), ViewController.LEVEL2);
            subList.add(stackPane);
        }
        return subList;
    }
    
    private List<Text> createLabelsList(int count, int step, int y){
        List<Text> labels = new ArrayList<>();
        int currentValue = 0;
        int leftIndent = ViewController.countIndent(count);
        for(int i = 0; i < count; i++){
            Text label = new Text(String.valueOf(currentValue));
            StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
            label.setTranslateX(SPACING * i + leftIndent);
            label.setTranslateY(y);
            label.fontProperty().set(font);
            labels.add(label);
            currentValue += step;
        }
        return labels;
    }
    
    private FlowPane createBucket(int x, int y, String labelText){
        FlowPane base = new FlowPane(Orientation.VERTICAL);
        base.setColumnHalignment(HPos.CENTER);
        base.setAlignment(Pos.BOTTOM_CENTER);
        Line baseLine = new Line(0, 0, 50, 0);
        baseLine.setStrokeWidth(5);
        Text label = new Text(labelText);
        label.setFont(font);
        base.getChildren().addAll(baseLine, label);
        base.setTranslateX(x);
        base.setTranslateY(y + 24); //TODO: smarter Y position. Problem with height of children because of FlowPane alignmen
        return base;
    }
    
    private List<FlowPane> createBucketList(int count, int startLabelCounter, int diff){
        List<FlowPane> buckets = new ArrayList<>();
        int y = ViewController.LEVEL2 + 10;
        int currentLabel = startLabelCounter;
        int leftIndent = countIndent(count); 
        for(int i = 0; i < count; i++){
            int x = SPACING * i + leftIndent;
            FlowPane bucket = createBucket(x, y, Integer.toString(currentLabel));
            buckets.add(bucket);
            currentLabel += diff;
        }
        return buckets;
    }
     
    private void initValues(){
        nextTransitionIndex.set(0);
        displayPane.getChildren().clear();
        infoPanel.getChildren().clear();
        list.clear();
        transitions.clear();
    }
    
    private void createBindings() {
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
    }
    
    public void initialize(Algorithm instanceType, int[] input){
        int[] generatedArray;
        Random random = new Random();
        currentInstance = instanceType;
        stopAllAnimations();
        initValues();
        
        int arrayMin;
        int arrayMax;
        int leftIndent;
        int currentMin = getMinimum(instanceType);
        int currentMax = getMaximum(instanceType);
        
        if(input != null){
            N_VALUES = input.length;
            generatedArray = input;
        } else {
            N_VALUES = 12; //TODO: add dependency on screen size
            generatedArray = generateRandomArray(N_VALUES, currentMin, currentMax - 1);
        }
        
        LEFT_INDENT = countIndent(N_VALUES);
        arrayMin = getArrayMin(generatedArray);
        arrayMax = getArrayMax(generatedArray);
        leftIndent = countIndent(N_VALUES);
        
        for(int i = 0; i < N_VALUES; i++){
            BrickNode node;
            switch (instanceType){
                case Bucket:
                case Radix:
                    node = createFixedNode(i, generatedArray[i], leftIndent, LEVEL1);
                    break;
                default:
                    node = createValueNode(i, generatedArray[i], currentMax, leftIndent);
            }
            list.add(node);
        }
        
        /*Polygon pol = QuickSort.createPointingTriangle();
        pol.setOpacity(0.4);
        pol.setTranslateX(ViewController.LEFT_INDENT);
        StackPane.setAlignment(pol, Pos.TOP_CENTER);*/
        displayPane.getChildren().addAll(list);
        //displayPane.getChildren().add(pol);
        
        //transitions = new ArrayList<>();
        
        switch(instanceType){
            case Bubble:
                transitions = bubbleSort(list, infoPanel);
                break;
            case CocktailShaker:
                transitions = cocktailShakerSort(list, infoPanel);
                break;
            case Insertion:
                transitions = insertionSort(list, infoPanel);
                break;
            case Selection:
                transitions = selectionSort(list, infoPanel);
                break;
            case Quick:
                transitions = quickSort(list, infoPanel);
                break;
            case Merge:
                transitions = mergeSort(list, infoPanel);
                break;
            case Counting:
                List<Text> positionLabels = createLabelsList(N_VALUES, 1, LEVEL1 + 30);
                List<Text> countLabels = createLabelsList(currentMax, 0, LEVEL2 + 30);
                transitions = countingSort(list, countLabels, currentMax, infoPanel);
                displayPane.getChildren().addAll(0, createGreyNodes(currentMax));
                displayPane.getChildren().addAll(countLabels);
                displayPane.getChildren().addAll(0, positionLabels);
                break;
            case Bucket:
                transitions = bucketSort(list, infoPanel);
                List<FlowPane> buckets = createBucketList((arrayMax - arrayMin) / 15 + 1, arrayMin, 15); //bucket size = 15 TODO: smarter desicion
                displayPane.getChildren().addAll(buckets);
                break;
            case Radix:
                transitions = radixSort(list, infoPanel);
                List<FlowPane> bucket = createBucketList(10, 0, 1); //TODO: get rid of magic numbers (count, min, increment)
                displayPane.getChildren().addAll(bucket);
                break;
            default:
                break;
        }
           
        createBindings();
    }

    
}
