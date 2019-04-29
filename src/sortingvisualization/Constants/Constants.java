/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Constants;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sortingvisualization.Enums.Algorithm;
import sortingvisualization.Utilities.Scaling;

/**
 * Constants which are used in program
 * @author Mykhailo Klunko
 */
public class Constants {
    
    /*number of items for random generation*/
    public static final int DEFAULT_ITEM_COUNT = 12;
    
    /*side panel background color*/
    public static final Color PANEL_BGND = /*Color.valueOf("#F0C243")*/Color.AQUAMARINE;
    /*variable state panel background color*/
    public static final Color LOW_PAN_BGND = Color.LIGHTPINK;
    /*pseudo code background selection highlight color*/
    public static final Color LINE_SELECTION = Color.WHITE;
    /*inactive dynamic node color*/ //used in the counting sort
    public static final Color PLACEHOLDER = Color.LIGHTGRAY;
    
    //COLORS FOR DYNAMIC NODES
    /*fill of dynamic-sized node while comparing*/
    public static final Color COMPARE = Color.AQUAMARINE;/*Color.valueOf("#F2355B");*/
    /*dynamic node default fill*/
    public static final Color D_DEFAULT = Color.DARKCYAN;/*Color.valueOf("#02A694");*/
    /*dynamic node in the sorted state*/
    public static final Color SORTED = Color.ORANGE;/*Color.valueOf("#F38902");*/
    /*dynamic node selection color*/
    public static final Color SELECTED = Color.RED; /*Color.valueOf("#F38902");//*/
    
    //COLORS FOR STATIC(FIXED) NODES
    /*bucket line color*/
    public static final Color BUCKET = Color.BLACK;
    /*fixed-size node border color*/
    public static final Color STROKE = Color.BLACK;
    /*fixed-size node default fill*/
    public static final Color F_DEFAULT = Color.WHITE;
    
    //COLORS FOR TREE NODES
    /*graphic tree node fill*/
    public static final Color FILL = Color.WHITE;
    /*graphic tree node background color*/
    public static final Color PLACEHOLDER_BKGRND = Color.LIGHTGRAY;
    /*graphic tree node selection fill color*/
    public static final Color SELECTION = Color.AQUAMARINE;
    /*graphic tree node border selection color*/
    public static final Color LIGHT_SELECTION = Color.GRAY;
    /*graphic tree border default color*/
    public static final Color TREE_DEFAULT = Color.BLACK;
    /*tree node manager graphic array background color for sorted state*/
    public static final Color ARRAY_SORTED = Color.GAINSBORO;
    
    /*main font*/
    public static final Font MAIN_FONT = Font.font("Helvetica", 20 * Scaling.computeDPIScale());
    /*main bold font*/
    public static final Font MAIN_BOLD = Font.font("Helvetica", FontWeight.BOLD, 20 * Scaling.computeDPIScale());
    /*small main font*/
    public static final Font DESC_FONT = Font.font("Helvetica", 15 * Scaling.computeDPIScale());
    /*small header font*/ //(used in dialog)
    public static final Font DESC_HEADER_FONT = Font.font("Helvetica", FontWeight.BOLD, 16 * Scaling.computeDPIScale());
    /*pseudo code font*/
    public static final Font CODE_FONT = Font.font("Courier New", FontWeight.MEDIUM, 13 * Scaling.computeDPIScale());
    /*variables info font*/
    public static final Font INFO_FONT = Font.font("Courier New", FontWeight.MEDIUM, 14 * Scaling.computeDPIScale());
    
    //constants for range of algorithms. Do not change it!
    /*general maximum*/
    public static final int MAX = 100;
    /*general minimun*/
    public static final int MIN = 0;
    /*maximum for counting sort*/
    public static final int CNT_MAX = 10;
    /*minimum for counting sort*/
    public static final int CNT_MIN = 0;
    /*maximum for radix sort*/
    public static final int RDX_MAX = 9999;
    /*minimum for radix sort*/
    public static final int RDX_MIN = 0;
    
    /**
     * Gets minimum permitted range value for the algorithm of current instance
     * @param type algorithm to get minimum value for
     * @return minimum for the chosen algorithm
     */
    public static int getMinimum(Algorithm type){
        switch (type) {
            case Counting:
                return Constants.CNT_MIN;
            case Radix:
                return Constants.RDX_MIN;
            default:
                return Constants.MIN;
        }
    }
    
    /**
     * Gets maximum permitted range value for the algorithm of current instance
     * @param type algorithm to get minimum value for
     * @return maximum for the chosen algorithm
     */
    public static int getMaximum(Algorithm type){
        switch (type) {
            case Counting:
                return Constants.CNT_MAX;
            case Radix:
                return Constants.RDX_MAX;
            default:
                return Constants.MAX;
        }
    }
}
