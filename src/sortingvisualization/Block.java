/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author mihae
 */
public class Block {
    private BooleanProperty active = new SimpleBooleanProperty(true);
    private IntegerProperty row;
    private IntegerProperty column;
    private int height;
    private int width;
        
    public Block(int row, int column, int height, int width) {
            this.row = new SimpleIntegerProperty(row);
            this.column = new SimpleIntegerProperty(column);
            this.height = height;
            this.width = width;
    }
    
    public final BooleanProperty activeProperty() {
       	return this.active;
    }
        
    public final boolean isActive() {
        return this.activeProperty().get();
    }

    public final void setActive(final boolean active) {
        this.activeProperty().set(active);
    }
	
    public final IntegerProperty rowProperty() {
        return this.row;
    }

    public int getRow() {
	return this.rowProperty().get();
    }	

    public void setRow(final int row) {
	this.rowProperty().set(row);
    }
	
    public final IntegerProperty columnProperty() {
	return this.column;
    }

    public final int getColumn() {
	return this.columnProperty().get();
    }

    public final void setColumn(final int column) {
	this.columnProperty().set(column);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    
}
