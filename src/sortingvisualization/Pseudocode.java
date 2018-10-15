/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author mihae
 */
public class Pseudocode {
    
    private List<StackPane> codeLines;
    private List<Boolean> selection;
    
    /*public List<StackPane> getCodeLines() {
        return codeLines;
    }*/
    public Pseudocode(){
        codeLines = new ArrayList<>();
        selection = new ArrayList<>();
    }
    
    public void addLines(Pane codePane, String... args){
        for(String arg : args){
            codeLines.add(createLine(arg));
            selection.add(false);
        }
        codePane.getChildren().addAll(codeLines);
    }
    
    public Animation selectLine(int line){
        return selectLines(line);
    }
    
    public Animation unselectAll(){
        return selectLine(-1);
    }
    
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
        
        if(pt.getChildren().size() == 0){
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
        Rectangle rectangle = new Rectangle(390, 25);
        rectangle.setFill(Color.AQUAMARINE);
        
        Text text = new Text(line);
        text.setFont(Font.font("Courier new", FontWeight.BOLD, 12));
        StackPane node = new StackPane();
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        //node.setId(String.valueOf(num));
        //stackPane.setValue(num);
        node.getChildren().addAll(rectangle, text);
        StackPane.setAlignment(text, Pos.CENTER_LEFT);
        //node.setAlignment(Pos.BOTTOM_CENTER);
        //node.setTranslateX(ViewController.SPACING * i + ViewController.LEFT_INDENT);
        //node.setTranslateY(ViewController.TOP_INDENT);
        node.setShape(rectangle);
        return node;
    }
    
    private boolean isInArray(int number, int[] array){
        return IntStream.of(array).allMatch(x -> x == number);
    }
}
