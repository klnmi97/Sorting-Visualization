
package sortingvisualization.NodeControllers;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Node representation class
 * @author Mykhailo Klunko
 */
public class BrickNode extends StackPane implements Comparable<BrickNode>{
    
    private final IntegerProperty value;
    private List<Text> digits;

    /**
     * Get list of node value digits
     * @return list of digits
     */
    public List<Text> getDigits() {
        return digits;
    }

    /**
     * Set digits of the node value
     * @param digits list of text representations
     */
    public void setDigits(List<Text> digits) {
        this.digits = digits;
    }

    /**
     * Instantiates new node
     * @param value value of the node
     */
    public BrickNode(int value){
        this.value = new SimpleIntegerProperty(value);
        this.digits = new ArrayList<>();
    }
    
    /**
     * Integer property of the node value
     * @return value property of the node
     */
    public final IntegerProperty valueProperty() {
        return this.value;
    }

    /**
     * Get value of the node
     * @return integer value
     */
    public int getValue() {
        return this.valueProperty().get();
    }	

    /**
     * Set value of the node
     * @param value integer value
     */
    public void setValue(final int value) {
        this.valueProperty().set(value);
    }

    @Override
    public int compareTo(BrickNode brick) {
        return Double.compare(valueProperty().get(), brick.getValue());
    }

    @Override
    public String toString() {
        return Integer.toString(getValue());
    }
    
    
}
