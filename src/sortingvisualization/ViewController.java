/*
 * Created on: September, 2018
 *
 */
package sortingvisualization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
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
import sortingvisualization.algorithms.BubbleSort;
import sortingvisualization.algorithms.BucketSort;
import sortingvisualization.algorithms.CocktailShakerSort;
import sortingvisualization.algorithms.CountingSort;
import sortingvisualization.algorithms.InsertionSort;
import sortingvisualization.algorithms.MergeSort;
import sortingvisualization.algorithms.QuickSort;
import sortingvisualization.algorithms.RadixSort;
import sortingvisualization.algorithms.SelectionSort;

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
    
    public static final int MAX = 100;
    public static final int MIN = 6;
    public static final int CNT_MAX = 10; //check if 10 or 9?
    public static final int CNT_MIN = 0;
    public static final int RDX_MAX = 9999;
    public static final int RDX_MIN = 0;
    //Style
    //colours of bricks:
    //COMPARE - selected, DEFAULT - main color
    public static final Color COMPARE = Color.AQUAMARINE;
    public static final Color DEFAULT = Color.DARKCYAN;
    public static final Color SORTED = Color.ORANGE;
    public static final Color LINE_SELECTION = Color.WHITE;
    
    private int[] currentArray;
    private Pane displayPane;
    private Pane infoPanel;
    private Algorithm currentInstance;
    
    public ViewController(Pane displayPane, Pane infoPanel){
        this.displayPane = displayPane;
        this.infoPanel = infoPanel;
        this.currentInstance = Algorithm.Bubble;
        currentArray = generateRandomArray(N_VALUES, MIN, MAX);
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
    
    private int getMinimum(Algorithm type){
        switch (type) {
            case Counting:
                return CNT_MIN;
            case Radix:
                return RDX_MIN;
            default:
                return MIN;
        }
    }
    
    private int getMaximum(Algorithm type){
        switch (type) {
            case Counting:
                return CNT_MAX;
            case Radix:
                return RDX_MAX;
            default:
                return MAX;
        }
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
        displayPane.getChildren().clear();
        infoPanel.getChildren().clear();
    }
    
    private List<BrickNode> initialize(Algorithm instanceType, int[] input){
        int[] generatedArray;
        Random random = new Random();
        List<BrickNode> list = new ArrayList<>();
        currentInstance = instanceType;
        initValues();
        
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
        
        this.currentArray = generatedArray;
        LEFT_INDENT = countIndent(N_VALUES);
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
        return list;
    }
    
    public Task<List<Animation>> sort(Algorithm instanceType, int[] input){
       
        List<BrickNode> list = initialize(instanceType, input);
        displayPane.getChildren().addAll(list);
        return new Task<List<Animation>>() {
            @Override
            protected List<Animation> call() throws Exception {
                List<Animation> anim = new ArrayList<>();
                switch(currentInstance){
                    case Bubble:
                        BubbleSort bubble = new BubbleSort(list, infoPanel);
                        anim = bubble.sort();
                        break;
                    case CocktailShaker:
                        CocktailShakerSort shaker = new CocktailShakerSort(list, infoPanel);
                        anim = shaker.sort();
                        break;
                    case Insertion:
                        InsertionSort insert = new InsertionSort(list, infoPanel);
                        anim = insert.sort();
                        break;
                    case Selection:
                        SelectionSort select = new SelectionSort(list, infoPanel);
                        anim = select.sort();
                        break;
                    case Quick:
                        QuickSort quick = new QuickSort(list, infoPanel);
                        anim = quick.sort();
                        break;
                    case Merge:
                        MergeSort merge = new MergeSort(list, infoPanel);
                        anim = merge.sort();
                        break;
                    case Counting:
                        List<Text> positionLabels = createLabelsList(N_VALUES, 1, LEVEL1 + 30);
                        List<Text> countLabels = createLabelsList(getMaximum(instanceType), 0, LEVEL2 + 30);
                        List<BrickNode> placeHolders = createGreyNodes(getMaximum(instanceType));
                        CountingSort counting = new CountingSort(list, countLabels, infoPanel);
                        anim = counting.sort();
                        Platform.runLater(() -> {
                            displayPane.getChildren().addAll(0, placeHolders);
                            displayPane.getChildren().addAll(countLabels);
                            displayPane.getChildren().addAll(0, positionLabels);
                        });
                        break;
                    case Bucket:
                        BucketSort bucket = new BucketSort(list, infoPanel);
                        anim = bucket.sort();
                        int bucketCount = (ArrayUtils.getMaxValue(list) - ArrayUtils.getMinValue(list)) / 15 + 1;
                        List<FlowPane> buckets = createBucketList(bucketCount, ArrayUtils.getMinValue(list), 15); //TODO: get rid of magic numbers (count, min, increment)
                        Platform.runLater(() -> {
                            displayPane.getChildren().addAll(buckets);
                        });
                        break;
                    case Radix:
                        RadixSort radix = new RadixSort(list, infoPanel);
                        anim = radix.sort();
                        List<FlowPane> rbuckets = createBucketList(10, 0, 1); //TODO: get rid of magic numbers (count, min, increment)
                        Platform.runLater(() -> {
                            displayPane.getChildren().addAll(rbuckets);
                        });
                        break;
                    default:
                        //BubbleSort bs = new BubbleSort(list);
                        //anim = bs.bubbleSort(list);
                        break;
                }
                
                return anim;
            }
        };
    }
    
}
