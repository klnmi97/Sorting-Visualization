/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;

import java.util.Arrays;
import java.util.Random;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author mihae
 */
public class InputDialog extends Dialog<Results> {
    
    private Label inputLbl;
    private Label choiseLbl;
    private Label errorLbl;
    private TextField inputTextField;
    private ComboBox<Algorithm> comboBox;
    
    private int maxInputValue; 
    
    public InputDialog(int maxInputValue){
        this.maxInputValue = maxInputValue;
        setTitle("New sorting");
        setHeaderText("Please, specify new sorting!");
        
        //dialog.initOwner(scene.getWindow()); //TODO: restyle
        
        DialogPane dialogPane = getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button okButton = (Button)getDialogPane().lookupButton(ButtonType.OK);
        
        Font dialogFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        inputLbl = new Label("Enter the sequence: ");
        inputLbl.setFont(dialogFont);
        choiseLbl = new Label("Choose an Algorithm: ");
        choiseLbl.setFont(dialogFont);
        errorLbl = new Label("");
        errorLbl.textFillProperty().setValue(Color.RED);
        errorLbl.setFont(dialogFont);
        
        inputTextField = new TextField(Arrays.toString(
                generateRandomArray(10))
                .replaceAll("\\s+", " ").replaceAll("\\[|\\]", ""));
        inputTextField.setPrefWidth(200);
        
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!isInputValid(inputTextField.getText())) {
                errorLbl.setText("Input values must be less than 100!");
                event.consume(); //not valid
            }
        });
        
        ObservableList<Algorithm> options =
            FXCollections.observableArrayList(Algorithm.values());
        comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().selectFirst();
        
        VBox leftColumn = new VBox(inputLbl,choiseLbl);
        leftColumn.setSpacing(12);
        leftColumn.setPadding(new Insets(4,4,4,4));
        VBox rightColumn = new VBox(inputTextField, comboBox);
        rightColumn.setSpacing(5);
        dialogPane.setContent(new VBox(8, new HBox(
                leftColumn, 
                rightColumn), 
                errorLbl));
        
        setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(inputTextField.getText(),
                    comboBox.getValue());
            }
            return null;
        });
        
        Platform.runLater(inputTextField::requestFocus);
    }
    
    private boolean isInputValid(String inputText){
        String[] intStr = inputText.split("(\\D+)");
            int [] input = new int[intStr.length];
            int maxValue = 0;
            for (int i = 0; i < intStr.length; i++) {
                input[i] = Integer.parseInt(intStr[i]);
                if(input[i] > maxValue){
                    maxValue = input[i];
                }
            }
            return maxValue <= maxInputValue;
    }
    
    private int[] generateRandomArray(int size){
        Random randomValue = new Random();
        int[] randomArray = new int[size];
        for(int i = 0; i < size; i++){
            randomArray[i] = randomValue.nextInt(maxInputValue - 6) + 7;
        }
        return randomArray;
    }
        
}



enum Algorithm {Bubble, Insertion, Selection, Merge, Quick}

class Results {

    String input;
    Algorithm algoritm;

    public Results(String input, Algorithm algorithm) {
        this.input = input;
        this.algoritm = algorithm;
    }
}
