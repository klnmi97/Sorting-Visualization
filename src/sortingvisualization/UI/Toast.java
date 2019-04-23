/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.UI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
/**
 * Message over the main window in Android toast style
 * @author Mykhailo Klunko
 */
public final class Toast {
    
    /**
     * Creates a toast message over the main window
     * @param ownerStage stage which owns this toast
     * @param toastMsg message to show
     * @param toastDelay time for showing message in ms
     * @param fadeInDelay fade in animation time in ms
     * @param fadeOutDelay fade out animation time in ms
     */
    public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay)
    {
        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);
        
        ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
            double stageWidth = newValue.doubleValue();
            toastStage.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - stageWidth / 2);
        };
        ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
            double stageHeight = newValue.doubleValue();
            toastStage.setY(ownerStage.getY() + ownerStage.getHeight() / 7 * 6);   
        };

        toastStage.widthProperty().addListener(widthListener);
        toastStage.heightProperty().addListener(heightListener);

        toastStage.setOnShown(e -> {
            toastStage.widthProperty().removeListener(widthListener);
            toastStage.heightProperty().removeListener(heightListener);
        });
        
        Text text = new Text(toastMsg);
        //text.setFont(Font.font("Verdana", 16));
        text.getStyleClass().add("toastfont");
        text.setFill(Color.WHITE);

        StackPane root = new StackPane(text);
        //root.setStyle("-fx-background-radius: 0; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 10px;");
        root.getStyleClass().add("toast");
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("style.css");
        toastStage.setScene(scene);
        toastStage.show();
        
        //for main window to stay in focus
        ownerStage.requestFocus();
        
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 1)); 
        fadeInTimeline.getKeyFrames().add(fadeInKey1);   
        fadeInTimeline.setOnFinished((ae) -> 
                {
                    new Thread(() -> {
                        try
                        {
                            Thread.sleep(toastDelay);
                        }
                        catch (InterruptedException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                           Timeline fadeOutTimeline = new Timeline();
                            KeyFrame fadeOutKey1 = new KeyFrame(
                                    Duration.millis(fadeOutDelay), 
                                    new KeyValue (toastStage.getScene().getRoot()
                                            .opacityProperty(), 0)); 
                            fadeOutTimeline.getKeyFrames().add(fadeOutKey1);   
                            fadeOutTimeline.setOnFinished((event) -> toastStage.close()); 
                            fadeOutTimeline.play();
                    }).start();
                }); 
        fadeInTimeline.play();
    }
}
