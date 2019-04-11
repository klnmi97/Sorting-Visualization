/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Constants;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sortingvisualization.Enums.Algorithm;
import sortingvisualization.Utilities.Scaling;

/**
 *
 * @author Mykhailo Klunko
 */
public class Constants {
    
    public static final Color PANEL_BGND = /*Color.valueOf("#F0C243")*/Color.AQUAMARINE;
    public static final Color LOW_PAN_BGND = Color.LIGHTPINK;
    //colours of bricks:
    //COLORS FOR STATIC AND DYNAMIC NODES
    public static final Color COMPARE = /*Color.valueOf("#F2355B");*/Color.AQUAMARINE;
    public static final Color DEFAULT = /*Color.valueOf("#02A694");*/Color.DARKCYAN;
    public static final Color SORTED = /*Color.valueOf("#F38902");//*/Color.ORANGE;
    public static final Color SELECTED = Color.RED;
    public static final Color LINE_SELECTION = Color.WHITE;
    public static final Color PLACEHOLDER = Color.LIGHTGRAY;
    
    //COLORS FOR TREE NODES
    public static final Color FILL = Color.WHITE;
    public static final Color PLACEHOLDER_BKGRND = Color.LIGHTGRAY;
    public static final Color SELECTION = Color.AQUAMARINE;
    public static final Color LIGHT_SELECTION = Color.GRAY;
    public static final Color TREE_DEFAULT = Color.BLACK;
    public static final Color ARRAY_SORTED = Color.GAINSBORO;
    
    public static final Font font = Font.font("Helvetica", 20 * Scaling.computeDPIScale());
    
    public static final int MAX = 100;
    public static final int MIN = 0;
    public static final int CNT_MAX = 10;
    public static final int CNT_MIN = 0;
    public static final int RDX_MAX = 9999;
    public static final int RDX_MIN = 0;
    
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
