/*
 * Created on: September, 2018
 *
 */
package sortingvisualization.Controllers;

import sortingvisualization.Utilities.Scaling;
import sortingvisualization.Utilities.ArrayUtils;
import sortingvisualization.NodeControllers.Tree;
import sortingvisualization.NodeControllers.DynamicNodes;
import sortingvisualization.NodeControllers.FixedNodes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Enums.Algorithm;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.VariablesInfo;
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
    
    private static final double scaling = initScaling();
    
    //TODO: create new metrics
    public static final int DEFAULT_ITEM_COUNT = 12;
    public static int N_VALUES = 12;
    public static final int SPACING = initSpacing();
    public static int TEN_LEFT_INDENT = countIndent(10);
    public static int LEFT_INDENT = countIndent(N_VALUES);
    public static final int LEVEL1 = (int)(-350 * scaling);
    public static final int LEVEL2 = (int)(250 * scaling) + LEVEL1;
    public static final Font font = Font.font("Helvetica", 20 * scaling);

    public static final Duration SPEED = Duration.millis(1000);
    public  double currentSpeed = 3;
    
    
    //Style
    
    
    private int[] currentArray;
    private final Pane displayPane;
    private final Pane codePanel;
    private Pane infoPanel;
    private Algorithm currentInstance;
    
    private int childrenWidth;
    private double childrenHeight;
    
    /**
     * Nodes and their animation creation controller
     * @param displayPane main pane to show graphic items
     * @param codePanel panel for placing pseudocode
     * @param infoPanel panel for placing variables state
     */
    public ViewController(Pane displayPane, Pane codePanel, Pane infoPanel){
        this.displayPane = displayPane;
        this.codePanel = codePanel;
        this.infoPanel = infoPanel;
        this.currentInstance = Algorithm.Bubble;
        this.currentArray = ArrayUtils.generateRandomArray(N_VALUES, Constants.MIN, Constants.MAX);
        this.childrenWidth = DEFAULT_ITEM_COUNT * SPACING;
    }
    
    /**
     * Get width of graphic nodes on screen
     * @return width of children nodes for displayPane in px
     */
    public int getChildrenWidth() {
        return childrenWidth;
    }
    
    /**
     * Get min height needed for graphic nodes on screen
     * @return width of children nodes for displayPane in px
     */
    public double getChildrenHeight() {
        return childrenHeight;
    }
    
    private static double initScaling() {
        return Scaling.computeDPIScale();
    }
    
    private static int initSpacing() {
        return (int)(60 * scaling);
    }
    
    public static int countIndent(int number){
        return (int)(((double)number / 2) * -SPACING);
    }
    
    private FlowPane createBucket(int x, int y, String labelText){
        FlowPane base = new FlowPane(Orientation.VERTICAL);
        base.setColumnHalignment(HPos.CENTER);
        base.setAlignment(Pos.BOTTOM_CENTER);
        Line baseLine = new Line(0, 0, 50 * scaling, 0);
        baseLine.setStrokeWidth(5 * scaling);
        Text label = new Text(labelText);
        label.setFont(font);
        base.getChildren().addAll(baseLine, label);
        base.setTranslateX(x);
        base.setTranslateY(y + 24 * scaling); //TODO: smarter Y position. Problem with height of children because of FlowPane alignmen
        return base;
    }
    
    private List<FlowPane> createBucketList(int count, int startLabelCounter, int diff){
        List<FlowPane> buckets = new ArrayList<>();
        int y = ViewController.LEVEL2 + (int)(10 * scaling);
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
        int currentMin = Constants.getMinimum(type);
        int currentMax = Constants.getMaximum(type);
        
        if(input != null){
            N_VALUES = input.length;
            outputArray = input;
        } else {
            N_VALUES = DEFAULT_ITEM_COUNT; //TODO: add dependency on screen size
            outputArray = ArrayUtils.generateRandomArray(N_VALUES, currentMin, currentMax - 1);
        }
        
        return outputArray;
    }
    
    private void initValues(){
        displayPane.getChildren().clear();
        codePanel.getChildren().clear();
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
    public Task<List<Animation>> sort(Algorithm instanceType, int[] input) {
       
        int[] generatedArray = preInit(instanceType, input);
        int currentMax = Constants.getMaximum(instanceType);
        
        return new Task<List<Animation>>() {
            @Override
            protected List<Animation> call() throws Exception {
                DynamicNodes dNodes = new DynamicNodes(generatedArray, currentMax);
                FixedNodes fNodes = new FixedNodes();
                Tree tNodes;
                VariablesInfo currentInfo = new VariablesInfo(400 * scaling);
                AbstractAlgorithm sorting;
                List<BrickNode> list;
                List<Animation> anim;
                switch(currentInstance){
                    case Bubble:
                        list = dNodes.getNodes();
                        sorting = new BubbleSort(list, currentInfo, codePanel);
                        childrenHeight = dNodes.getViewportMinHeight();
                        break;
                    case CocktailShaker:
                        list = dNodes.getNodes();
                        sorting = new CocktailShakerSort(list, currentInfo, codePanel);
                        childrenHeight = dNodes.getViewportMinHeight();
                        break;
                    case Insertion:
                        list = dNodes.getNodes();
                        sorting = new InsertionSort(list, currentInfo, codePanel);
                        childrenHeight = dNodes.getViewportMinHeight();
                        break;
                    case Selection:
                        list = dNodes.getNodes();
                        sorting = new SelectionSort(list, currentInfo, codePanel);
                        childrenHeight = dNodes.getViewportMinHeight();
                        break;
                    case Quick:
                        list = dNodes.getNodes();
                        sorting = new QuickSort(list, currentInfo, codePanel);
                        childrenHeight = dNodes.getViewportMinHeight();
                        break;
                    case Merge:
                        list = dNodes.getNodes();
                        sorting = new MergeSort(list, currentInfo, codePanel);
                        childrenHeight = dNodes.getViewportMinHeight();
                        break;
                    case Counting:
                        List<Text> countLabels = dNodes.createPlaceholderLabels();
                        list = dNodes.getNodes();
                        sorting = new CountingSort(list, countLabels, currentInfo, codePanel);
                        addChildrenAsync(displayPane, 0, dNodes.createPlaceHolders(currentMax));
                        addChildrenAsync(displayPane, countLabels);
                        addChildrenAsync(displayPane, 0, dNodes.createLabels());
                        childrenHeight = dNodes.getViewportMinHeight();
                        break;
                    case Bucket:
                        list = fNodes.createList(generatedArray, currentMax);
                        sorting = new BucketSort(list, currentInfo, codePanel);
                        int bucketCount = (ArrayUtils.getMaxValue(list) - ArrayUtils.getMinValue(list)) 
                                / BucketSort.BUCKET_SIZE + 1;
                        List<FlowPane> buckets = createBucketList(
                                bucketCount, ArrayUtils.getMinValue(list), BucketSort.BUCKET_SIZE);
                        addChildrenAsync(displayPane, buckets);
                        childrenHeight = LEVEL1 * -1 + (int)fNodes.NODE_HEIGHT;
                        break;
                    case Radix:
                        list = fNodes.createList(generatedArray, currentMax);
                        sorting = new RadixSort(list, currentInfo, codePanel);
                        List<FlowPane> rbuckets = createBucketList(Constants.CNT_MAX, 0, 1); //TODO: get rid of magic numbers (count, min, increment)
                        addChildrenAsync(displayPane, rbuckets);
                        childrenHeight = LEVEL1 * -1 + (int)fNodes.NODE_HEIGHT;
                        break;
                    case Heap:
                        tNodes = new Tree(generatedArray);
                        list = tNodes.getNodesList();
                        sorting = new HeapSort(tNodes, currentInfo, codePanel);
                        addChildrenAsync(displayPane, tNodes.getChildConnections());
                        addChildrenAsync(displayPane, tNodes.getPlaceholders());
                        addChildrenAsync(displayPane, tNodes.getArrayNodes());
                        childrenHeight = (int) tNodes.getMinViewPortHeight();
                        break;
                    default:
                        list = dNodes.getNodes();
                        sorting = new BubbleSort(list, currentInfo, codePanel);
                        break;
                }
                anim = sorting.sort();
                
                childrenWidth = (list.size() + 1) * SPACING;
                
                addChildrenAsync(displayPane, list);
                addChildrenAsync(infoPanel, currentInfo.getInfoField());
                return anim;
            }
        };
    }
    
    private void addChildrenAsync(Pane pane, Node node) {
        Platform.runLater(() -> {
            pane.getChildren().add(node);
        });
    }
    
    private void addChildrenAsync(Pane pane, Collection<? extends Node> c) {
        Platform.runLater(() -> {
            pane.getChildren().addAll(c);
        });
    }
    
    private void addChildrenAsync(Pane pane, int index, Collection<? extends Node> c) {
        Platform.runLater(() -> {
            pane.getChildren().addAll(index, c);
        });
    }
}
