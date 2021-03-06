
package sortingvisualization.Controllers;

import sortingvisualization.Utilities.Scaling;
import sortingvisualization.NodeControllers.Tree;
import sortingvisualization.NodeControllers.DynamicNodes;
import sortingvisualization.NodeControllers.FixedNodes;
import java.util.Collection;
import java.util.List;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Enums.Algorithm;
import sortingvisualization.NodeControllers.BrickNode;
import sortingvisualization.NodeControllers.ColorInfoManager;
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
 * Controller for creating animations and sorting process
 * @author Mykhailo Klunko
 */
public class ViewController {
    
    private static final double SCALING = initScaling();
    
    private static final int SPACING = initSpacing();

    public  double currentSpeed = 3;
   
    private final Pane displayPane;
    private final Pane codePanel;
    private final Pane infoPanel;
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
        this.childrenWidth = Constants.DEFAULT_ITEM_COUNT * SPACING;
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
        return (int)(60 * SCALING);
    }
    
    private void clear(){
        displayPane.getChildren().clear();
        codePanel.getChildren().clear();
        infoPanel.getChildren().clear();
    }
    
    private int[] preInit(Algorithm instanceType, int[] input){
        this.currentInstance = instanceType;
        clear();
        
        return input;
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
                return algorithmSelector(generatedArray, currentMax);
            }
        };
    }
    
    /*
     * Switcher of algorithms. Makes main setup.
     */
    private List<Animation> algorithmSelector(int[] generatedArray, int currentMax) {
        DynamicNodes dNodes = new DynamicNodes(generatedArray, currentMax);
        FixedNodes fNodes = new FixedNodes(generatedArray, currentMax);
        Tree tNodes = new Tree(generatedArray);
        
        VariablesInfo currentInfo = new VariablesInfo(400 * SCALING);
        AbstractAlgorithm sorting;
        List<BrickNode> list;
        List<Animation> anim;
        
        switch(currentInstance) {
            case Bubble:
                list = dNodes.getNodes();
                sorting = new BubbleSort(dNodes, currentInfo, codePanel);
                addColorInfo(new Pair<>(Constants.COMPARE, "comparing"), 
                             new Pair<>(Constants.SORTED, "sorted"));
                addChildrenAsync(displayPane, 0, dNodes.createLabels());
                childrenHeight = dNodes.getViewportMinHeight();
                break;
            case CocktailShaker:
                list = dNodes.getNodes();
                sorting = new CocktailShakerSort(dNodes, currentInfo, codePanel);
                addColorInfo(new Pair<>(Constants.COMPARE, "comparing"), 
                             new Pair<>(Constants.SORTED, "sorted"));
                addChildrenAsync(displayPane, 0, dNodes.createLabels());
                childrenHeight = dNodes.getViewportMinHeight();
                break;
            case Insertion:
                list = dNodes.getNodes();
                sorting = new InsertionSort(dNodes, currentInfo, codePanel);
                addColorInfo(new Pair<>(Constants.SORTED, "sorted"),
                             new Pair<>(Constants.SELECTED, "selection from unsorted part"),
                             new Pair<>(Constants.COMPARE, "selection from sorted part"));
                addChildrenAsync(displayPane, 0, dNodes.createLabels());
                childrenHeight = dNodes.getViewportMinHeight();
                break;
            case Selection:
                list = dNodes.getNodes();
                sorting = new SelectionSort(dNodes, currentInfo, codePanel);
                addColorInfo(new Pair<>(Constants.SORTED, "sorted"),
                             new Pair<>(Constants.SELECTED, "current min"),
                             new Pair<>(Constants.COMPARE, "compared item"));
                addChildrenAsync(displayPane, 0, dNodes.createLabels());
                childrenHeight = dNodes.getViewportMinHeight();
                break;
            case Quick:
                list = dNodes.getNodes();
                sorting = new QuickSort(dNodes, currentInfo, codePanel);
                addColorInfo(new Pair<>(Constants.SELECTED, "pivot"),
                             new Pair<>(Constants.COMPARE, "current item"),
                             new Pair<>(Constants.SORTED, "item is on final position"));
                childrenHeight = dNodes.getViewportMinHeight();
                break;
            case Merge:
                list = dNodes.getNodes();
                sorting = new MergeSort(dNodes, currentInfo, codePanel);
                addChildrenAsync(displayPane, 0, dNodes.createLabels());
                childrenHeight = dNodes.getViewportMinHeight();
                break;
            case Counting:
                List<Text> countLabels = dNodes.createPlaceholderLabels();
                list = dNodes.getNodes();
                sorting = new CountingSort(dNodes, countLabels, currentInfo, codePanel);
                addChildrenAsync(displayPane, 0, dNodes.createPlaceHolders(currentMax + 1));
                addChildrenAsync(displayPane, countLabels);
                addChildrenAsync(displayPane, 0, dNodes.createLabels());
                childrenHeight = dNodes.getViewportMinHeight();
                break;
            case Bucket:
                list = fNodes.getNodes();
                sorting = new BucketSort(fNodes, currentInfo, codePanel);
                int bucketCount = (Constants.MAX - Constants.MIN) / list.size() + 1;
                List<FlowPane> buckets = fNodes.createBucketList(
                        bucketCount, Constants.MIN, list.size());
                addChildrenAsync(displayPane, buckets);
                addChildrenAsync(displayPane, 0, fNodes.createLabels());
                childrenHeight = fNodes.getViewportMinHeight();
                break;
            case Radix:
                list = fNodes.getNodes();
                sorting = new RadixSort(fNodes, currentInfo, codePanel);
                List<FlowPane> rbuckets = fNodes.createBucketList(Constants.CNT_MAX + 1, 0, 1); //TODO: get rid of magic numbers (count, min, increment)
                addChildrenAsync(displayPane, rbuckets);
                addChildrenAsync(displayPane, 0, fNodes.createLabels());
                childrenHeight = fNodes.getViewportMinHeight();
                break;
            case Heap:
                list = tNodes.getNodesList();
                sorting = new HeapSort(tNodes, currentInfo, codePanel);
                addChildrenAsync(displayPane, tNodes.getChildConnections());
                addChildrenAsync(displayPane, tNodes.getPlaceholders());
                addChildrenAsync(displayPane, tNodes.getArrayNodes());
                addColorInfo(new Pair<>(Constants.SELECTION, "comparing"), 
                             new Pair<>(Constants.ARRAY_SORTED, "sorted"));
                childrenHeight = tNodes.getViewportMinHeight();
                break;
            default:
                list = dNodes.getNodes();
                sorting = new BubbleSort(dNodes, currentInfo, codePanel);
                addColorInfo(new Pair<>(Constants.COMPARE, "comparing"), 
                             new Pair<>(Constants.SORTED, "sorted"));
                addChildrenAsync(displayPane, 0, dNodes.createLabels());
                childrenHeight = dNodes.getViewportMinHeight();
                break;
        }
        anim = sorting.sort();

        childrenWidth = (list.size() + 1) * SPACING;

        addChildrenAsync(displayPane, list);
        addChildrenAsync(infoPanel, currentInfo.getInfoField());
        return anim;
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
    
    private void addColorInfo(Pair<Color, String>... description) {
        addChildrenAsync(displayPane, ColorInfoManager.createColorInfo(description));
    }
}
