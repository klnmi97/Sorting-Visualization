
package sortingvisualization.NodeControllers;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.Scaling;

/**
 * Class represents nodes that have height dependent on value
 * @author Mykhailo Klunko
 */
public final class DynamicNodes {
    
    private static final double SCALE = Scaling.computeDPIScale();
    
    private static final double NODE_HEIGHT = 200 * SCALE;
    private static final double NODE_WIDTH = 50 * SCALE;
    
    private static final int LEVEL1 = (int)(-350 * SCALE);
    private static final int LEVEL2 = (int)(250 * SCALE) + LEVEL1;
    private static final int SPACING = initSpacing();

    private static final Duration SPEED = Duration.millis(1000);
    
    private List<BrickNode> list;
    private final int currentMax;
    
    /**
     * Create dynamic nodes manager class
     * @param inputArray array with input values
     * @param max maximal defined value
     */
    public DynamicNodes(int[] inputArray, int max){
        this.currentMax = max;
        this.list = createList(inputArray, max);
    }
    
    /**
     * Get graphic nodes
     * @return list of nodes
     */
    public List<BrickNode> getNodes(){
        return list;
    }
    
    /**
     * Gets left side indent from center point for centering
     * @return counted left indent
     */
    public int getUpperLevelIndent() {
        return countIndent(list.size());
    }
    
    /**
     * Counts indent from center to left for the line of the nodes for centering
     * @param count number of nodes
     * @return left side spacing from center in pixels
     */
    public int countIndent(int count){
        return (int)(((double)count / 2) * -SPACING);
    }
    
    private List<BrickNode> createList(int[] inputArray, int currentMax){
        list = new ArrayList<>();
        for(int i = 0; i < inputArray.length; i++){
            BrickNode node = createValueNode(i, inputArray[i], currentMax, countIndent(inputArray.length));
            
            list.add(node);
        }
        return list;
    }
    
    private BrickNode createValueNode(int i, int value, int currentMax, int leftIndent) {
        return createCustomNode(i, value, currentMax, Constants.D_DEFAULT, leftIndent, LEVEL1);
    }
    
    /**
     * Create labels with position marks
     * @return list of text labels
     */
    public List<Text> createLabels(){
        return createLabelsList(list.size(), 1, LEVEL1 + 30);
    }
    
    /**
     * Create labels with number values for placeholders
     * @return list of text labels
     */
    public List<Text> createPlaceholderLabels(){
        return createLabelsList(currentMax + 1, 0, LEVEL2 + 30);
    }
    
    private List<Text> createLabelsList(int count, int step, int y){
        List<Text> labels = new ArrayList<>();
        int currentValue = 0;
        int leftIndent = countIndent(count);
        for(int i = 0; i < count; i++){
            Text label = new Text(String.valueOf(currentValue));
            StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
            label.setTranslateX(SPACING * i + leftIndent);
            label.setTranslateY(y);
            label.fontProperty().set(Constants.MAIN_FONT);
            labels.add(label);
            currentValue += step;
        }
        return labels;
    }
    
    /**
     * Create placeholders for dynamic nodes. Values starting from zero with
     * step of size one
     * @param count number of placeholders
     * @return nodes that represent placeholders
     */
    public List<BrickNode> createPlaceHolders(int count){
        List<BrickNode> subList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BrickNode stackPane = createCustomNode(i, i, count, Constants.PLACEHOLDER, 
                    countIndent(count), LEVEL2);
            subList.add(stackPane);
        }
        return subList;
    }
    
    private BrickNode createCustomNode(int i, int value, int currentMax, 
            Color color, double leftIndent, double topIndent) {
        int num = value;
        double percent = (double)num / currentMax;
        if(percent < 0.08){
            percent = 0;
        }
        Rectangle rectangle = new Rectangle(NODE_WIDTH, (percent * NODE_HEIGHT) + 5);
        rectangle.setFill(color);
        
        Text text = new Text(String.valueOf(num));
        text.setFont(Constants.MAIN_FONT);
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
    
    /*** Animations ***/
    
    /**
     * Changes node color
     * @param nodePosition position of the node to be changed
     * @param original original node color
     * @param newColor new node color
     * @return animation of color change
     */
    public Animation setColor(int nodePosition, Color original, Color newColor){
        return new FillTransition(SPEED, list.get(nodePosition).getShape(), original, newColor);
    }
    
    /**
     * Changes node color
     * @param node node to be changed
     * @param original original node color
     * @param newColor new node color
     * @return animation of color change
     */
    public Animation setColor(BrickNode node, Color original, Color newColor){
        return new FillTransition(SPEED, node.getShape(), original, newColor);
    }
    
    /**
     * Creates animation of two dynamic nodes selection by changing colors from 
     * default to color of selection. Only nodes with default color should 
     * be selected. These color constants can be found in the static Constants 
     * class.
     * @param node1 first node to highlight selection
     * @param node2 second node to highlight selection
     * @return animation of highlighting two nodes by changing color
     */
    public Animation selectNodes(int node1, int node2){
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                setColor(node1, Constants.D_DEFAULT, Constants.COMPARE), 
                setColor(node2, Constants.D_DEFAULT, Constants.COMPARE));
        return parallelTransition;
    }
    
    /**
     * Creates animation of two dynamic nodes unselection by changing colors from 
     * the color of selection to the default. Only nodes changed by {@link selectNodes} 
     * should be selected. These color constants can be found in the static Constants 
     * class.
     * @param node1 first node to change color to default
     * @param node2 second node to change color to default
     * @return animation of changing color of two nodes
     */
    public Animation unselectNodes(int node1, int node2){
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                setColor(node1, Constants.COMPARE, Constants.D_DEFAULT), 
                setColor(node2, Constants.COMPARE, Constants.D_DEFAULT));
        return parallelTransition;
    }
    
    /**
     * Swaps two dynamic nodes. Must be applied before the real swap
     * @param firstNode first node to swap (position in array)
     * @param secondNode second node to swap (position in array)
     * @param firstX position of the first node in the visual array
     * @param secondX position of the second node in the visual array
     * @return animation of swapping two nodes
     */
    public Animation swap(BrickNode firstNode, BrickNode secondNode, int firstX, int secondX) {
        int leftIndent = countIndent(list.size());
        double pos1 = firstX * SPACING + leftIndent;
        double pos2 = secondX * SPACING + leftIndent;
        TranslateTransition transition1 = new TranslateTransition();
        transition1.setDuration(SPEED);
        transition1.setNode(firstNode);
        transition1.setFromX(pos1);
        transition1.setToX(pos2);
        
        TranslateTransition transition2 = new TranslateTransition();
        transition2.setDuration(SPEED);
        transition2.setNode(secondNode);
        transition2.setFromX(pos2);
        transition2.setToX(pos1);
        
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(transition1, transition2);
        return parallelTransition;
    }
    
    /**
     * Swaps two dynamic nodes. Must be applied before the real swap
     * @param firstNode first node to swap (position in array)
     * @param secondNode second node to swap (position in array)
     * @return animation of swapping two nodes
     */
    public Animation swap(int firstNode, int secondNode) {
        return swap(list.get(firstNode), list.get(secondNode), firstNode, secondNode);
    }
    
    /**
     * Moves node from the upper line to lower line with different X coordinates
     * @param node node to move
     * @param fromX original Xth position of the node in the array
     * @param toX new Xth position of the node in the array
     * @param startLeftIndent left indent of the upper level
     * @param withLeftIndent left indent of the lower level
     * @return animation of moving node down
     */
    public Animation moveDownToX(BrickNode node, int fromX, int toX, 
            double startLeftIndent, double withLeftIndent) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(node);
        //get start position of node to allow backward animation
        transition.setDuration(SPEED);
        transition.setFromX(fromX * SPACING + startLeftIndent);
        transition.setFromY(LEVEL1);
        transition.setToX(toX * SPACING + withLeftIndent);
        transition.setToY(LEVEL2);
        return transition;
    }
    
    /**
     * Moves node from the upper line to lower line with different X coordinates
     * but the same line length
     * @param node node to move
     * @param fromX original Xth position of the node in the array
     * @param toX new Xth position of the node in the array
     * @return animation of moving node down
     */
    public Animation moveDownToX(BrickNode node, int toX, int fromX) {
        int leftIndent = countIndent(list.size());
        return moveDownToX(node, fromX, toX, leftIndent , leftIndent);
    }
    
    /**
     * Moves node from the lower level to the upper one with different positions
     * @param node node to move
     * @param fromX original position in the lower part
     * @param toX new position in the upper part
     * @param startLeftIndent left indent of the lower part
     * @param withLeftIndent left indent of the upper part
     * @return animation of the moving node up
     */
    public Animation moveUpToX(BrickNode node, int fromX, int toX, double startLeftIndent, double withLeftIndent){
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(node);
        transition.setDuration(SPEED);
        //get start position of node to allow backward animation
        transition.setFromX(fromX * SPACING + startLeftIndent);
        transition.setFromY(LEVEL2);
        transition.setToX(toX * SPACING + withLeftIndent);
        transition.setToY(LEVEL1);
        return transition;
    }
    
    /**
     * Moves node straight up from the lower level to the upper level without 
     * changing the Xth coordinate
     * @param node node to move
     * @return animation of moving the node up
     */
    public TranslateTransition moveNodeUp(BrickNode node){
        TranslateTransition moveNodeUp = new TranslateTransition();
        moveNodeUp.setNode(node);
        moveNodeUp.setDuration(SPEED);
        moveNodeUp.setFromY(LEVEL2);
        moveNodeUp.setToY(LEVEL1);
        return moveNodeUp;
    }
    
    /**
     * Creates animation of changing value of the Text node
     * @param label Text node to change
     * @param fromVal original value
     * @param descImp new value
     * @return animation of changing text
     */
    public Animation setText(Text label, String fromVal, String descImp) {
        String content = descImp;
        String oldVal = fromVal;
        return new Timeline(
            new KeyFrame(Duration.millis(0),
                new KeyValue(label.textProperty(), oldVal)),
            new KeyFrame(SPEED.multiply(0.9),
                new KeyValue(label.textProperty(), content)),
            new KeyFrame(Duration.millis(1),
                new KeyValue(label.fontProperty(), Font.font("Helvetica", 30))),
            new KeyFrame(SPEED,
                new KeyValue(label.fontProperty(), Constants.MAIN_FONT)));
    }
    
    /**
     * Get absolute minimal height of window needed for nodes to be in viewport
     * @return height in pixels
     */
    public double getViewportMinHeight() {
        return LEVEL1 * -1 + NODE_HEIGHT;
    }
    
    private static int initSpacing() {
        return (int)(60 * SCALE);
    }
}
