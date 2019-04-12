/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.NodeControllers;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import sortingvisualization.Utilities.Scaling;

/**
 * Class represents color information manager
 * @author Mykhailo Klunko
 */
public class ColorInfoManager {
    
    private static double scalingFactor = Scaling.computeDPIScale();
    private static int NODE_SIZE = (int) (12 * scalingFactor);
    private static Color DESC_COLOR = Color.GRAY;
    
    /**
     * Creates line with information about color meaning 
     * @param infoPairs pair color - description
     * @return node with separation line and information about colors
     */
    public static Node createColorInfo(Pair<Color, String>... infoPairs) {
        
        HBox box = new HBox();
        for(Pair<Color, String> infoPair : infoPairs) {
            Rectangle colorRect = createShadowedBox(NODE_SIZE, 1, 1, 1, 1, 2);
            colorRect.setFill(infoPair.getKey());

            Text desc = new Text(" - " + infoPair.getValue());
            desc.setFill(DESC_COLOR);
            
            HBox wrapper = new HBox(colorRect, desc);
            wrapper.setAlignment(Pos.CENTER);
            box.getChildren().addAll(wrapper);
            
        }
        
        box.setSpacing(10 * scalingFactor);
        
        Line divider = new Line(0, 0, 400 * scalingFactor, 0);
        VBox colorInfo = new VBox(divider, box);
        return colorInfo;
    }
    
    private static Rectangle createShadowedBox(double size,
    double shadowWidth, double shadowHeight,
    double offsetX, double offsetY,
    double radius) {
        Rectangle r = new Rectangle(size, size);
        DropShadow e = new DropShadow();
        e.setWidth(shadowWidth);
        e.setHeight(shadowHeight);
        e.setOffsetX(offsetX);
        e.setOffsetY(offsetY);
        e.setRadius(radius);
        r.setEffect(e);
        return r;
}
    
}
