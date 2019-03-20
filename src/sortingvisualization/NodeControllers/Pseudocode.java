/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.NodeControllers;

import sortingvisualization.Controllers.ViewController;
import sortingvisualization.Utilities.Scaling;
import sortingvisualization.Utilities.AnimUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Mykhailo Klunko
 */
public class Pseudocode {
    
    private static final double scaling = Scaling.computeDPIScale();
    private static final Font font = Font.font("Courier New", FontWeight.MEDIUM, 13 * scaling); //TODO: maybe better font
    private final List<StackPane> codeLines;
    private final List<Boolean> selection;
    
    /**
     * Class that manages pseudocode creation and animation
     */
    public Pseudocode(){
        codeLines = new ArrayList<>();
        selection = new ArrayList<>();
    }
    
    /**
     * Creates code lines from strings
     * @param args code lines
     */
    public void addLines(String... args){
        for(String arg : args){
            codeLines.add(createLine(arg));
            selection.add(false);
        }
    }
    
    /**
     * Gets list of created code lines
     * @return
     */
    public List<StackPane> getCode(){
        return codeLines;
    }
    
    /**
     * Creates code line selection animation
     * @param line number of line to highlight
     * @return animation of line highlighting
     */
    public Animation selectLine(int line){
        return selectLines(line);
    }
    
    /**
     * Creates animation of all lines deselection
     * @return animation of lines deselection
     */
    public Animation unselectAll(){
        return selectLine(-1);
    }
    
    /**
     * Creates selection animation of multiple lines stated in parameters
     * @param lines lines of code to select
     * @return animation of code line selection
     */
    public Animation selectLines(int... lines){
        ParallelTransition pt = new ParallelTransition();
        
        for(int i = 0; i < codeLines.size(); i++){
            if(isLineSelected(i) && !isInArray(i, lines)){
                pt.getChildren().add(
                        AnimUtils.setColor(codeLines.get(i), 
                                ViewController.LINE_SELECTION, Color.AQUAMARINE));
                selection.set(i, Boolean.FALSE);
            }
        }
        for(int line : lines){
            if(!(line > codeLines.size() || line < 0) && !isLineSelected(line)){
                pt.getChildren().add(
                            AnimUtils.setColor(codeLines.get(line), 
                                    Color.AQUAMARINE, ViewController.LINE_SELECTION));
                selection.set(line, Boolean.TRUE);
            }
        }
        
        if(pt.getChildren().isEmpty()){
            return null;
        }
        return pt;
    }
    
    private boolean isLineSelected(int line){
        if(line >= codeLines.size() || line < 0){
            return false;
        }
        return selection.get(line);
    }
    
    private StackPane createLine(String line) {
        Rectangle rectangle = new Rectangle(390 * scaling, 25 * scaling);
        rectangle.setFill(Color.AQUAMARINE);
        
        Text text = new Text(line);
        text.setFont(font);
        StackPane node = new StackPane();
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        node.getChildren().addAll(rectangle, text);
        StackPane.setAlignment(text, Pos.CENTER_LEFT);
        node.setShape(rectangle);
        return node;
    }
    
    private boolean isInArray(int number, int[] array){
        return IntStream.of(array).anyMatch(x -> x == number);
    }
}
