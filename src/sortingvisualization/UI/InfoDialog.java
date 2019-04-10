/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.UI;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialog with not editable text
 * @author Mykhailo Klunko
 */
public class InfoDialog {
    private static String message = "Place for algorithm description";
    
    /**
     * Creates window with text information
     * @param ownerStage parent stage
     */
    public void showDescription(Stage ownerStage, String message) {
        
        Stage stage = new Stage();
        BorderPane rootPane = new BorderPane();
        Text description = new Text(message);
        description.setFill(Color.GRAY);
        rootPane.setCenter(description);
        
        Scene scene = new Scene(rootPane, 500, 500);
        //scene.getStylesheets().add("style.css");
        scene.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                stage.close();
            }
        });
        
        stage.initOwner(ownerStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Description");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));
        stage.setScene(scene);
        stage.setMinHeight(200);
        stage.setMinWidth(500);
        stage.show();
    }
}
