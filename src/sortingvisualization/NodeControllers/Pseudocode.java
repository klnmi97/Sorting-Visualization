
package sortingvisualization.NodeControllers;

import sortingvisualization.Utilities.Scaling;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sortingvisualization.Constants.Constants;

/**
 * Pseudocode manager
 * @author Mykhailo Klunko
 */
public class Pseudocode {
    
    private static final double SCALE_FACTOR = Scaling.computeDPIScale();
    private static final Color BGND = Constants.PANEL_BGND;
    private final List<StackPane> codeLines;
    private final List<Boolean> selection;
    
    /**
     * Instantiates pseudocode manager class
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
     * @return Animation of code line selection
     */
    public Animation selectLines(int... lines){
        ParallelTransition pt = new ParallelTransition();
        
        for(int i = 0; i < codeLines.size(); i++){
            if(isLineSelected(i) && !isInArray(i, lines)){
                pt.getChildren().add(setColor(codeLines.get(i), Constants.LINE_SELECTION, BGND));
                selection.set(i, Boolean.FALSE);
            }
        }
        for(int line : lines){
            if(!(line > codeLines.size() || line < 0) && !isLineSelected(line)){
                pt.getChildren().add(setColor(codeLines.get(line), BGND, Constants.LINE_SELECTION));
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
        Rectangle rectangle = new Rectangle(420 * SCALE_FACTOR, 27 * SCALE_FACTOR);
        rectangle.setFill(BGND);
        
        Text text = new Text(line);
        text.setFont(Constants.CODE_FONT);
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
    
    private Animation setColor(StackPane node, Color fromColor, Color toColor){
        return new FillTransition(Duration.millis(1000), node.getShape(), fromColor, toColor);
    }
}
