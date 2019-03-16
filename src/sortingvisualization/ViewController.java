/*
 * Created on: September, 2018
 *
 */
package sortingvisualization;

import NodeCreation.Tree;
import NodeCreation.DynamicNodes;
import NodeCreation.FixedNodes;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.application.Platform;
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
import sortingvisualization.algorithms.AbstractAlgorithm;
import sortingvisualization.algorithms.BubbleSort;
import sortingvisualization.algorithms.BucketSort;
import sortingvisualization.algorithms.CocktailShakerSort;
import sortingvisualization.algorithms.CountingSort;
import sortingvisualization.algorithms.HeapSort;
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
    public static final int DEFAULT_ITEM_COUNT = 12;
    public static int N_VALUES = 12;
    public static final int SPACING = 60;
    //counting from center as 0; half of (spacing * number of elements)
    public static int TEN_LEFT_INDENT = (int)(((double)10 / 2) * -SPACING);
    public static int LEFT_INDENT = (int)(((double)N_VALUES / 2) * -SPACING);
    public static final int LEVEL1 = -350;
    public static final int LEVEL2 = 250 + LEVEL1;
    public static final Font font = Font.font("Helvetica", 20);

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
     
    private int[] loadArray(Algorithm type, int[] input){
        int[] outputArray;
        int currentMin = getMinimum(type);
        int currentMax = getMaximum(type);
        
        if(input != null){
            N_VALUES = input.length;
            outputArray = input;
        } else {
            N_VALUES = DEFAULT_ITEM_COUNT; //TODO: add dependency on screen size
            outputArray = generateRandomArray(N_VALUES, currentMin, currentMax - 1);
        }
        
        return outputArray;
    }
    
    private void initValues(){
        displayPane.getChildren().clear();
        infoPanel.getChildren().clear();
    }
    
    private int[] preInit(Algorithm instanceType, int[] input){
        this.currentInstance = instanceType;
        initValues();
        
        int[] generatedArray = loadArray(instanceType, input);
        int size = generatedArray.length;
        
        this.LEFT_INDENT = countIndent(size);
        return generatedArray;
    }
    
    /**
     * Creates task that will create sorting animation flow
     * @param instanceType type of algorithm
     * @param input array, may be null
     * @return background task to create animation
     */
    public Task<List<Animation>> sort(Algorithm instanceType, int[] input){
       
        int[] generatedArray = preInit(instanceType, input);
        int currentMax = getMaximum(instanceType);
        
        return new Task<List<Animation>>() {
            @Override
            protected List<Animation> call() throws Exception {
                DynamicNodes dNodes = new DynamicNodes();
                FixedNodes fNodes = new FixedNodes();
                Tree tNodes;
                AbstractAlgorithm sorting;
                List<BrickNode> list;
                List<Animation> anim;
                switch(currentInstance){
                    case Bubble:
                        list = dNodes.createList(generatedArray, currentMax);
                        sorting = new BubbleSort(list, infoPanel);
                        break;
                    case CocktailShaker:
                        list = dNodes.createList(generatedArray, currentMax);
                        sorting = new CocktailShakerSort(list, infoPanel);
                        break;
                    case Insertion:
                        list = dNodes.createList(generatedArray, currentMax);
                        sorting = new InsertionSort(list, infoPanel);
                        break;
                    case Selection:
                        list = dNodes.createList(generatedArray, currentMax);
                        sorting = new SelectionSort(list, infoPanel);
                        break;
                    case Quick:
                        list = dNodes.createList(generatedArray, currentMax);
                        sorting = new QuickSort(list, infoPanel);
                        break;
                    case Merge:
                        list = dNodes.createList(generatedArray, currentMax);
                        sorting = new MergeSort(list, infoPanel);
                        break;
                    case Counting:
                        List<Text> positionLabels = createLabelsList(N_VALUES, 1, LEVEL1 + 30);
                        List<Text> countLabels = createLabelsList(getMaximum(instanceType), 0, LEVEL2 + 30);
                        List<BrickNode> placeHolders = createGreyNodes(getMaximum(instanceType));
                        list = dNodes.createList(generatedArray, currentMax);
                        sorting = new CountingSort(list, countLabels, infoPanel);
                        Platform.runLater(() -> {
                            displayPane.getChildren().addAll(0, placeHolders);
                            displayPane.getChildren().addAll(countLabels);
                            displayPane.getChildren().addAll(0, positionLabels);
                        });
                        break;
                    case Bucket:
                        list = fNodes.createList(generatedArray, currentMax);
                        sorting = new BucketSort(list, infoPanel);
                        int bucketCount = (ArrayUtils.getMaxValue(list) - ArrayUtils.getMinValue(list)) 
                                / BucketSort.BUCKET_SIZE + 1;
                        List<FlowPane> buckets = createBucketList(
                                bucketCount, ArrayUtils.getMinValue(list), BucketSort.BUCKET_SIZE);
                        Platform.runLater(() -> {
                            displayPane.getChildren().addAll(buckets);
                        });
                        break;
                    case Radix:
                        list = fNodes.createList(generatedArray, currentMax);
                        sorting = new RadixSort(list, infoPanel);
                        List<FlowPane> rbuckets = createBucketList(CNT_MAX, 0, 1); //TODO: get rid of magic numbers (count, min, increment)
                        Platform.runLater(() -> {
                            displayPane.getChildren().addAll(rbuckets);
                        });
                        break;
                    case Heap:
                        tNodes = new Tree(generatedArray);
                        list = tNodes.getNodesList();
                        sorting = new HeapSort(tNodes, infoPanel);
                        break;
                    default:
                        list = dNodes.createList(generatedArray, currentMax);
                        sorting = new BubbleSort(list, infoPanel);
                        break;
                }
                anim = sorting.sort();
                Platform.runLater(() -> {
                            displayPane.getChildren().addAll(list);
                        });
                return anim;
            }
        };
    }
}
