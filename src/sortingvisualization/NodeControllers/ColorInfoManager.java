/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.NodeControllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.Scaling;

/**
 * Class represents color information manager
 * @author Mykhailo Klunko
 */
public class ColorInfoManager {
    
    private static final String HEADER = "Colors meaning:";
    private static double scalingFactor = Scaling.computeDPIScale();
    private static int NODE_SIZE = (int) (12 * scalingFactor);
    private static Color DESC_COLOR = Color.GRAY;
    
    /**
     * Creates line with information about color meaning 
     * @param infoPairs pair color - description
     * @return node with separation line and information about colors
     */
    public static Node createColorInfo(Pair<Color, String>... infoPairs) {
        
        VBox box = new VBox();
        for(Pair<Color, String> infoPair : infoPairs) {
            Rectangle colorRect = createShadowedBox(NODE_SIZE, 1, 1, 1, 1, 2);
            colorRect.setFill(infoPair.getKey());

            Text desc = new Text(" - " + infoPair.getValue());
            desc.setFill(DESC_COLOR);
            
            HBox wrapper = new HBox(colorRect, desc);
            wrapper.setAlignment(Pos.BOTTOM_LEFT);
            box.getChildren().addAll(wrapper);
            
        }
        
        box.setPadding(new Insets(0, 0, 5, 0));
        
        Text header = new Text(HEADER);
        header.setFont(Constants.DESC_FONT);
        
        VBox colorInfo = new VBox(header, box);
        colorInfo.setAlignment(Pos.BOTTOM_LEFT);
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
