/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author Mykhailo Klunko
 */
public class BrickNode extends StackPane implements Comparable<BrickNode>{
    
    private IntegerProperty value;
    private List<Text> digits;

    public List<Text> getDigits() {
        return digits;
    }

    public void setDigits(List<Text> digits) {
        this.digits = digits;
    }

    public BrickNode(int value){
        this.value = new SimpleIntegerProperty(value);
        this.digits = new ArrayList<>();
    }
    
    public final IntegerProperty valueProperty() {
		return this.value;
	}

	public int getValue() {
		return this.valueProperty().get();
	}	

	public void setValue(final int value) {
		this.valueProperty().set(value);
	}

    @Override
    public int compareTo(BrickNode brick) {
        return Double.compare(valueProperty().get(), brick.getValue());
    }
}
