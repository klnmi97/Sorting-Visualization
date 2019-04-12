/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.NodeControllers;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import sortingvisualization.Utilities.Scaling;

/**
 *
 * @author Mykhailo Klunko
 */
public class ColorInfoManager {
    
    private static double scalingFactor = Scaling.computeDPIScale();
    
    public static Node createColorInfo(Pair<Color, String>... infoPairs) {
        
        HBox box = new HBox();
        for(Pair<Color, String> infoPair : infoPairs) {
            Rectangle colorRect = new Rectangle(12 * scalingFactor, 12 * scalingFactor);
            colorRect.setFill(infoPair.getKey());
            colorRect.setStroke(Color.BLACK);

            Text desc = new Text(" - " + infoPair.getValue());
            desc.setFill(Color.GREY);
            
            HBox wrapper = new HBox(colorRect, desc);
            wrapper.setAlignment(Pos.CENTER);
            box.getChildren().addAll(wrapper);
            
        }
        
        box.setSpacing(10 * scalingFactor);
        
        Line divider = new Line(0, 0, 500, 0);
        VBox colorInfo = new VBox(divider, box);
        return colorInfo;
    }
    
}
