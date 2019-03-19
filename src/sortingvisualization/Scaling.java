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
    
    public static double computeCoeff(){
        Screen screen = Screen.getPrimary();
        Rectangle2D visualBounds = screen.getVisualBounds();
        double width = visualBounds.getWidth();
        double height = visualBounds.getHeight();
        double dpi = screen.getDpi();
        double diag = Math.sqrt(width * width + height * height);
        double diagIn = diag / dpi;
        return diagIn;
    }
    
    //JAVA 9 features
    /*Screen screen = Screen.getPrimary();
    double dpi = screen.getDpi();
    double scaleX = screen.getOutputScaleX();
    double scaleY = screen.getOutputScaleY();*/
}
