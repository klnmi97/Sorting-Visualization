/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 *
 * @author mihae
 */
public class Scaling {
    
    public static void computeScaling(){
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double coef = visualBounds.getWidth() * 0.000625;
        System.out.println(coef);
    }
}
