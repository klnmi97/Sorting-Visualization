/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.Scaling;

/**
 * Dialog with not editable text
 * @author Mykhailo Klunko
 */
public class InfoDialog {
    
    private static final double SCALE = Scaling.computeDPIScale();
    /**
     * Creates window with text information
     * @param ownerStage parent stage
     * @param message main text
     * @param heading heading for the information window
     */
    public void showDescription(Stage ownerStage, String message, String heading) {
        
        Stage stage = new Stage();
        BorderPane rootPane = new BorderPane();
        Text header = new Text(heading);
        header.setFont(Constants.DESC_HEADER_FONT);
        
        VBox headerBox = new VBox(header);
        headerBox.setAlignment(Pos.CENTER);
        
        TextArea textBox = new TextArea(message); 
        textBox.setWrapText(true);
        textBox.setEditable(false);
        textBox.setFont(Constants.DESC_FONT);
        textBox.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");
        
        rootPane.setTop(headerBox);
        rootPane.setCenter(textBox);
        rootPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        Scene scene = new Scene(rootPane, 300 * SCALE, 300 * SCALE);
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
        //stage.setMinHeight(200);
        //stage.setMinWidth(400);
        stage.show();
    }
}
