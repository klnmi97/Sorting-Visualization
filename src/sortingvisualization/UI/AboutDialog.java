/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Utilities.Scaling;

/**
 * Dialog with not editable text
 * @author Mykhailo Klunko
 */
public class AboutDialog {
    
    private static final double SCALE = Scaling.computeDPIScale();
    /**
     * Window with information about the application
     * @param ownerStage parent stage
     */
    public void showDescription(Stage ownerStage) {
        
        Stage stage = new Stage();
        BorderPane rootPane = new BorderPane();
        Text header = new Text("Algorithm Visualizer");
        header.setFont(Constants.MAIN_BOLD);
        
        VBox headerBox = new VBox(header);
        headerBox.setAlignment(Pos.CENTER);
        
        Text info = new Text("Created for bachelor thesis");
        info.setFont(Constants.DESC_FONT);
        Text place = new Text("Palacky University, Olomouc");
        place.setFont(Constants.DESC_FONT);
        Text author = new Text("Mykhailo Klunko, 2019");
        author.setFont(Constants.DESC_FONT);
        
        Text mail = new Text("michael.klunko@gmail.com");
        VBox mailBox = new VBox(mail);
        mailBox.setAlignment(Pos.CENTER);
        
        VBox aboutBox = new VBox(info, place, author);
        aboutBox.setAlignment(Pos.CENTER);
        aboutBox.setSpacing(10);
        
        rootPane.setTop(headerBox);
        rootPane.setCenter(aboutBox);
        rootPane.setBottom(mailBox);
        rootPane.setPadding(new Insets(10));
        
        Scene scene = new Scene(rootPane, 500 * SCALE, 200 * SCALE);
        scene.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                stage.close();
            }
        });
        
        stage.initOwner(ownerStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("About");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));
        stage.setScene(scene);
        stage.show();
    }
}
