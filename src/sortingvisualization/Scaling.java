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
    
    public static double computeResolutionScale(){
        Screen screen = Screen.getPrimary();
        Rectangle2D visualBounds = screen.getVisualBounds();
        double coefficient = visualBounds.getWidth() * 0.000625;
        return coefficient;
    }
    
    public static double computeDPIScale(){
        Screen screen = Screen.getPrimary();
        return screen.getDpi() * 0.0085;
    }
    
    //JAVA 9 features
    /*Screen screen = Screen.getPrimary();
    double dpi = screen.getDpi();
    double scaleX = screen.getOutputScaleX();
    double scaleY = screen.getOutputScaleY();*/
}
