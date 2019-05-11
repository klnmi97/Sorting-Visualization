
package sortingvisualization.Utilities;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Tool that manages scaling based on the screen resolution or DPI
 * @author Mykhailo Klunko
 */
public class Scaling {
    
    /**
     * Computes scaling factor for the current main screen based on the resolution
     * @return scaling factor
     */
    public static double computeResolutionScale(){
        Screen screen = Screen.getPrimary();
        Rectangle2D visualBounds = screen.getVisualBounds();
        double coefficient = visualBounds.getWidth() * 0.000625;
        return coefficient;
    }
    
    /**
     * Computes scaling factor for the current main screen based on the DPI
     * @return scaling factor
     */
    public static double computeDPIScale(){
        Screen screen = Screen.getPrimary();
        return screen.getDpi() * 0.0085;
    }
    
    
    //JAVA 9 features for the future builds
    /*Screen screen = Screen.getPrimary();
    double dpi = screen.getDpi();
    double scaleX = screen.getOutputScaleX();
    double scaleY = screen.getOutputScaleY();*/
}
