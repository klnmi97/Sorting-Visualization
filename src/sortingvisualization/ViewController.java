/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author mihae
 */
public class ViewController {
    
    public static final int N_VALUES = 10;
    public static final int SPACING = 60;
    //counting from center as 0; half of (spacing * number of elements)
    public static int LEFT_INDENT = (int)(((double)N_VALUES / 2) * -SPACING);
    public static final int TOP_INDENT = 0;
    public static final int SORT_GROUP_MOVE_DELTA = 250;

    public static final Duration SPEED = Duration.millis(1000);
    
    private final int max = 100;
    private final int min = (int)(max * 0.1);
    //Style
    //colours of bricks:
    //COMPARE - selected, DEFAULT - main color
    public static final Color COMPARE = Color.AQUAMARINE;
    public static final Color DEFAULT = Color.DARKCYAN;
    public static final Color SORTED = Color.ORANGE;
}
