
package sortingvisualization.NodeControllers;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.Scaling;

/**
 * Class that controls visual heap and corresponding visual array
 * @author Mykhailo Klunko
 */
public class Tree {
    private static final double SCALING_FACTOR = Scaling.computeDPIScale();
    
    private static final int NODE_SIZE = initNodeSize();
    private static final int ARRAY_ITEM_HEIGHT = initArrayItemHeight();
    private static final int ARRAY_ITEM_WIDTH = initArrayItemWidth();
    private static final int LEVEL_HEIGHT = initLevelHeight();
    
    private static final Duration ANIM_SPEED = Duration.millis(1000);
    
    //metrics
    private static final int ARRAY_SPACING = initArraySpacing();
    private final int TREE_SPACING;
    private final double SPACING_COEF;
    
    //graphic part
    private final List<BrickNode> treeNodes;
    private final List<BrickNode> arrayNodes;
    private final List<Line> childConnections;
    private final List<Circle> placeholders;
    
    /** Min height of the visual nodes in pixels */
    public final double childNodesHeight;
    
    /**
     * Instantiates the Tree class
     * @param inputArray integer array with data for visual representation
     */
    public Tree(int[] inputArray){
        this.TREE_SPACING = countSecondLevelSpacing(inputArray);
        this.SPACING_COEF = countSpacingCoefficient(inputArray);
        this.treeNodes = createList(inputArray);
        this.childConnections = createConnections();
        this.placeholders = createPlaceholders();
        this.arrayNodes = createGraphicArray(inputArray);
        this.childNodesHeight = computeChildrenHeight(inputArray);
    }

    /**
     * Get nodes of array graphic representation
     * @return list of nodes
     */
    public List<BrickNode> getArrayNodes() {
        return arrayNodes;
    }
    
    /**
     * Get graphic tree placeholders (node background circles)
     * @return list of circles for tree nodes background
     */
    public List<Circle> getPlaceholders() {
        return placeholders;
    }

    /**
     * Get node connection lines
     * @return list of lines
     */
    public List<Line> getChildConnections() {
        return childConnections;
    }
    
    //Graphic tree logic
    
    /**
     *
     * @return List of created in constructor graphic tree nodes
     */
    public List<BrickNode> getNodesList() {
        return this.treeNodes;
    }
    
    /**
     * Get minimal height of needed space
     * @return minimal height of viewport in px
     */
    public double getViewportMinHeight() {
        return childNodesHeight;
    }
    
    /**
     * Computes the X-th coordinate position of node based on 
     * the position in the array of nodes and current array size
     * @param posInArray position in array
     * @return X coordinate position of node
     */
    public int getNodeX(int posInArray){
        double x = countSpacing(getLevel(posInArray)) 
                * getPositionInLevel(posInArray) 
                + getLevelIndent(getLevel(posInArray));
        return (int)x;
    }
    
    /**
     * Computes Y coordinate position of node based on 
     * the position in the array of nodes
     * @param posInArray position in array
     * @param arraySize size of array with nodes
     * @return Y coordinate position of node
     */
    public int getNodeY(int posInArray, int arraySize){
        int y = (getNuberOfLevels(arraySize) 
                - getLevel(posInArray) + 1) 
                * -LEVEL_HEIGHT;
        return y;
    }
    
    //creates tree nodes
    private List<BrickNode> createList(int[] inputArray){
        List<BrickNode> list = new ArrayList<>();
        for(int i = 0; i < inputArray.length; i++){
            BrickNode node = createNode(inputArray[i], Constants.TREE_DEFAULT, 
                    getNodeX(i), getNodeY(i, inputArray.length));
            list.add(node);
        }
        return list;
    }
    
    //creates tree children connections
    private List<Line> createConnections(){
        if(treeNodes == null) return null;
        
        List<Line> connections = new ArrayList<>();
        int size = treeNodes.size();
        for(int i = 0; i < size; i++){
            int l = 2 * i + 1;
            int r = 2 * i + 2;
            int startX = getNodeX(i);
            int startY = getNodeY(i, size) + NODE_SIZE;
            if(l < size){
                int endX = getNodeX(l);
                int endY = getNodeY(l, size) + NODE_SIZE;
                Line left = new Line(startX, startY, endX, endY);
                left.setStrokeWidth(3);
                left.setStroke(Constants.TREE_DEFAULT);
                left.setTranslateX((startX + endX) / 2) ;
                left.setTranslateY((startY + endY) / 2);
                StackPane.setAlignment(left, Pos.BOTTOM_CENTER);
                connections.add(left);
            }
            if(r < size){
                int endX = getNodeX(r);
                int endY = getNodeY(r, size) + NODE_SIZE;
                Line right = new Line(startX, startY, endX, endY);
                right.setStrokeWidth(3);
                right.setStroke(Constants.TREE_DEFAULT);
                right.setTranslateX((startX + endX) / 2) ;
                right.setTranslateY((startY + endY) / 2);
                StackPane.setAlignment(right, Pos.BOTTOM_CENTER);
                connections.add(right);
            }
        }
        return connections;
    }
    
    //creates tree nodes background placeholders
    private List<Circle> createPlaceholders(){
        if(treeNodes == null) return null;
        
        List<Circle> placeHolders = new ArrayList<>();
        int size = treeNodes.size();
        for (int i = 0; i < size; i++) {
            Circle holder = new Circle(NODE_SIZE);
            holder.setFill(Constants.PLACEHOLDER_BKGRND);
            holder.setTranslateX(getNodeX(i));
            holder.setTranslateY(getNodeY(i, size));
            StackPane.setAlignment(holder, Pos.BOTTOM_CENTER);
            placeHolders.add(holder);
        }
        
        return placeHolders;
    }
    
    //left indent for each level of tree
    private double getLevelIndent(int level){
        return ((double)(getLevelWidth(level) - 1) / 2) * -countSpacing(level);
    }
    
    //count first spacing between nodes, depends on size of array
    private int countSecondLevelSpacing(int[] array){
        int levels = getNuberOfLevels(array.length);
        return (int)(65 * levels * SCALING_FACTOR);
    }
    
    //coefficient to change spacing for tree levels
    private double countSpacingCoefficient(int[] array){
        int levels = getNuberOfLevels(array.length);
        return (double)1 / levels;
    }
    
    //count spacicng between tree nodes for each level of tree
    private int countSpacing(int nodeLevel){
        int sp = TREE_SPACING - (int)((nodeLevel - 1) * (TREE_SPACING * SPACING_COEF));
        return  sp;
    }
    
    //get node's tree level, is computed from position in array
    private int getLevel(int arrayPosition){
        int position = arrayPosition;
        int level = 1;
        
        while(position != 0){
            if(position % 2 == 0){
                position -= 2;
            } else {
                position -= 1;
            }
            position /= 2;
            level++;
        }
        return level;
    }
    
    //count position in tree level
    private int getPositionInLevel(int i){
        int level = getLevel(i);
        return i - (int)Math.pow(2, level - 1) + 1;
    }
    
    //hight of tree
    private int getNuberOfLevels(int treeArraySize){
        return getLevel(treeArraySize - 1);
    }
    
    private int getLevelWidth(int level){
        return (int)Math.pow(2, level - 1);
    }
    
    private BrickNode createNode(int value, Color color, double x, double y) {
        Circle body = new Circle(NODE_SIZE);
        body.setFill(Constants.FILL);
        body.setStroke(color);
        body.setStrokeWidth(3);
        
        Text text = new Text(String.valueOf(value));
        text.setFont(Constants.MAIN_FONT);
        text.setTranslateY(-10);
        
        BrickNode node = new BrickNode(value);
        node.setPrefSize(body.getRadius() * 2, body.getRadius() * 2);
        //node.setId(String.valueOf(num));
        node.getChildren().addAll(body, text);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(x);
        node.setTranslateY(y);
        node.setShape(body);
        return node;
    }
    
    private void swapInternalArray(int firstNode, int secondNode){
        BrickNode swap = arrayNodes.get(firstNode); 
        arrayNodes.set(firstNode, arrayNodes.get(secondNode)); 
        arrayNodes.set(secondNode, swap);
    }
    
    //Graphic array logic
    
    private List<BrickNode> createGraphicArray(int[] inputArray){
        List<BrickNode> list = new ArrayList<>();
        
        int size = inputArray.length;
        for(int i = 0; i < size; i++){
            BrickNode node = createArrayNode(inputArray[i], getArrayNodeX(i, size), 
                    getArrayNodeY());
            list.add(node);
        }
        return list;
    }
    
    //left indent for centering of array (center alignment)
    private int getArrayIndent(int size) {
        return (int)(((double)size / 2) * -ARRAY_SPACING);
    }
    
    //counts array node's X position on stackpane with TOP_CENTER alignment
    private int getArrayNodeX(int position, int arraySize){
        int x = position * ARRAY_SPACING + getArrayIndent(arraySize);
        return x;
    }
    
    //counts array node's Y position on stackpane with TOP_CENTER alignment
    private int getArrayNodeY(){
        int topMargin = ARRAY_ITEM_HEIGHT / 2;
        return topMargin;
    }
    
    //creates node of the array
    private BrickNode createArrayNode(int value, double x, double y){
        Rectangle cell = new Rectangle(ARRAY_ITEM_WIDTH, ARRAY_ITEM_HEIGHT);
        cell.setFill(Constants.FILL);
        cell.setStroke(Constants.TREE_DEFAULT);
        
        Text text = new Text(String.valueOf(value));
        text.setFont(Constants.MAIN_FONT);
        
        BrickNode node = new BrickNode(value);
        node.setPrefSize(cell.getWidth(), cell.getHeight());
        //node.setId(String.valueOf(num));
        node.getChildren().addAll(cell, text);
        node.setAlignment(Pos.TOP_CENTER);
        BrickNode.setAlignment(text, Pos.TOP_CENTER);
        node.setTranslateX(x);
        node.setTranslateY(y);
        node.setShape(cell);
        return node;
    }
    
    /*** Animations ***/

    /**
     * Creates graphic animation of swapping two nodes in the graphic array
     * @param firstNode position of first node in array
     * @param secondNode position of second node in array
     * @return animation of swapping two nodes
     */
    public Animation swapInArray(int firstNode, int secondNode){
        int size = arrayNodes.size();
        ParallelTransition swap = new ParallelTransition();
        
        TranslateTransition lr = new TranslateTransition();
        lr.setNode(arrayNodes.get(firstNode));
        lr.setDuration(ANIM_SPEED);
        lr.setFromX(getArrayNodeX(firstNode, size));
        lr.setToX(getArrayNodeX(secondNode, size));
        
        TranslateTransition rl = new TranslateTransition();
        rl.setNode(arrayNodes.get(secondNode));
        rl.setDuration(ANIM_SPEED);
        rl.setFromX(getArrayNodeX(secondNode, size));
        rl.setToX(getArrayNodeX(firstNode, size));
        
        swap.getChildren().addAll(lr, rl);
        //swap items during swap in treeNodes array to have right order of nodes 
        //in the arrayNodes array
        swapInternalArray(firstNode, secondNode);
        return swap;
    }
    
    /**
     * Creates graphic animation of swapping two nodes in the created tree
     * @param firstNode position of first node in array
     * @param secondNode position of second node in array
     * @return animation of swapping two nodes
     */
    public Animation swapInTree(int firstNode, int secondNode){
        int size = treeNodes.size();
        ParallelTransition swap = new ParallelTransition();
        
        TranslateTransition lr = new TranslateTransition();
        lr.setNode(treeNodes.get(firstNode));
        lr.setDuration(ANIM_SPEED);
        lr.setFromX(getNodeX(firstNode));
        lr.setFromY(getNodeY(firstNode, size));
        lr.setToX(getNodeX(secondNode));
        lr.setToY(getNodeY(secondNode, size));
        
        TranslateTransition rl = new TranslateTransition();
        rl.setNode(treeNodes.get(secondNode));
        rl.setDuration(ANIM_SPEED);
        rl.setFromX(getNodeX(secondNode));
        rl.setFromY(getNodeY(secondNode, size));
        rl.setToX(getNodeX(firstNode));
        rl.setToY(getNodeY(firstNode, size));
        
        swap.getChildren().addAll(lr, rl);
        return swap;
    }
    
    /**
     * Creates parallel animation of swapping nodes in the graphic tree and in
     * the graphic array
     * @param firstNode position of first node in array
     * @param secondNode position of second node in array
     * @return parallel animation of swapping two nodes(tree and array)
     */
    public Animation swap(int firstNode, int secondNode){
        ParallelTransition swap = new ParallelTransition();
        
        Animation arraySwap = swapInArray(firstNode, secondNode);
        Animation treeSwap = swapInTree(firstNode, secondNode);
        swap.getChildren().addAll(arraySwap, treeSwap);
        return swap;
    }
    
    /**
     * Creates animation of highlighting two nodes from the virtually 
     * created tree. Its purpose is to show comparing or selection of two
     * nodes.
     * @param firstNode position of the first selected node in the initial array
     * @param secondNode position of the second selected node in the initial array
     * @return animation that temporary changes color of two nodes
     */
    public Animation compareInTree(int firstNode, int secondNode) {
        int size = treeNodes.size();
        SequentialTransition compare = new SequentialTransition();
        ParallelTransition select = new ParallelTransition();
        ParallelTransition unselect = new ParallelTransition();
        
        Animation changeStrokeFirst = repaintStroke(treeNodes.get(firstNode).getShape(), 
                Constants.TREE_DEFAULT, Constants.LIGHT_SELECTION);
        Animation repaintFirst = repaint(treeNodes.get(firstNode).getShape(), 
                Constants.FILL, Constants.SELECTION);
        Animation changeStrokeSecond = repaintStroke(treeNodes.get(secondNode).getShape(), 
                Constants.TREE_DEFAULT, Constants.LIGHT_SELECTION);
        Animation repaintSecond = repaint(treeNodes.get(secondNode).getShape(), 
                Constants.FILL, Constants.SELECTION);
        select.getChildren().addAll(changeStrokeFirst, repaintFirst, changeStrokeSecond, repaintSecond);
        
        Animation backStrokeFirst = repaintStroke(treeNodes.get(firstNode).getShape(), 
                Constants.LIGHT_SELECTION, Constants.TREE_DEFAULT);
        Animation paintBackFirst = repaint(treeNodes.get(firstNode).getShape(), 
                Constants.SELECTION, Constants.FILL);
        Animation backStrokeSecond = repaintStroke(treeNodes.get(secondNode).getShape(), 
                Constants.LIGHT_SELECTION, Constants.TREE_DEFAULT);
        Animation paintBackSecond = repaint(treeNodes.get(secondNode).getShape(), 
                Constants.SELECTION, Constants.FILL);
        unselect.getChildren().addAll(backStrokeFirst, paintBackFirst, 
                backStrokeSecond, paintBackSecond);
        
        compare.getChildren().addAll(select, unselect);
        return compare;
    }
    
    /**
     * Creates animation of highlighting two nodes from the
     * created graphic array. Its purpose is to show comparing or selection of 
     * two nodes.
     * @param firstNode position of the first selected node in the initial array
     * @param secondNode position of the second selected node in the initial array
     * @return animation that temporary changes color of two nodes in the array 
     * in the tree
     */
    public Animation compareInArray(int firstNode, int secondNode) {
        int size = treeNodes.size();
        SequentialTransition compare = new SequentialTransition();
        ParallelTransition select = new ParallelTransition();
        ParallelTransition unselect = new ParallelTransition();
        
        Animation selectFirst = repaint(arrayNodes.get(firstNode).getShape(), Constants.FILL, Constants.SELECTION);
        Animation selectSecond = repaint(arrayNodes.get(secondNode).getShape(), Constants.FILL, Constants.SELECTION);
        select.getChildren().addAll(selectFirst, selectSecond);
        
        Animation unselectFirst = repaint(arrayNodes.get(firstNode).getShape(), Constants.SELECTION, Constants.FILL);
        Animation unselectSecond = repaint(arrayNodes.get(secondNode).getShape(), Constants.SELECTION, Constants.FILL);
        unselect.getChildren().addAll(unselectFirst, unselectSecond);
        
        compare.getChildren().addAll(select, unselect);
        return compare;
    }
    
    /**
     * Creates animation of selection(comparing) two nodes in tree and array
     * @param firstNode position of first node
     * @param secondNode position of second node
     * @return animation of back and forth color change
     */
    public Animation compare(int firstNode, int secondNode){
        ParallelTransition compare = new ParallelTransition();
        
        Animation arrayHighlight = compareInArray(firstNode, secondNode);
        Animation treeHighlight = compareInTree(firstNode, secondNode);
        compare.getChildren().addAll(arrayHighlight, treeHighlight);
        return compare;
    }
    
    //changes stroke color of the shape
    private Animation repaintStroke(Shape shape, Color fromValue, Color toValue) {
        StrokeTransition repainting = new StrokeTransition();
        repainting.setShape(shape);
        repainting.setDuration(ANIM_SPEED);
        repainting.setFromValue(fromValue);
        repainting.setToValue(toValue);
        return repainting;
    }
    
    //changes background color of shape
    private Animation repaint(Shape shape, Color fromValue, Color toValue) {
        FillTransition repaint = new FillTransition();
        repaint.setShape(shape);
        repaint.setDuration(ANIM_SPEED);
        repaint.setFromValue(fromValue);
        repaint.setToValue(toValue);
        return repaint;
    }
    
    /**
     * Animation that shows that node is sorted. Node in the array becomes gray
     * and node in the tree fades 
     * @param nodePosition position of node
     * @return animation of node that is sorted
     */
    public Animation setSorted(int nodePosition) {
        ParallelTransition setSorted = new ParallelTransition();
        
        Animation array = repaint(arrayNodes.get(nodePosition).getShape(), Constants.FILL, Constants.ARRAY_SORTED);
        Animation tree = hideNode(treeNodes.get(nodePosition));
        Animation circle = hideNode(placeholders.get(nodePosition));
        if(nodePosition > 0){
            Animation connection = hideNode(childConnections.get(nodePosition - 1));
            setSorted.getChildren().add(connection);
        }
        
        setSorted.getChildren().addAll(array, tree, circle);
        return setSorted;
    }
    
    private Animation hideNode(Node node) {
        FadeTransition hide = new FadeTransition();
        hide.setNode(node);
        hide.setDuration(ANIM_SPEED);
        hide.setFromValue(1);
        hide.setToValue(0);
        
        return hide;
    }
    
    /*** Init utils ***/
    
    private static int initNodeSize() {
        return (int)(25 * SCALING_FACTOR);
    }
    
    private static int initArraySpacing() {
        return ARRAY_ITEM_WIDTH + 10;
    }

    private static int initArrayItemHeight() {
        return (int)(30 * SCALING_FACTOR);
    }
    
    private static int initArrayItemWidth() {
        return (int)(50 * SCALING_FACTOR);
    }
    
    private static int initLevelHeight() {
        return (int)(100 * SCALING_FACTOR);
    }

    private double computeChildrenHeight(int[] array) {
        int minimumArrayTreeSpace = 100;
        return getNodeY(0, array.length) * -1 + ARRAY_ITEM_HEIGHT + minimumArrayTreeSpace;
    }
}
