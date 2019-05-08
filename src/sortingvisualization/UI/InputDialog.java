/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.UI;

import sortingvisualization.Utilities.ArrayUtils;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sortingvisualization.Constants.Constants;
import sortingvisualization.Enums.Algorithm;
import sortingvisualization.Data.Results;

/**
 * Dialog window for getting user input data
 * @author Mykhailo Klunko
 */
public class InputDialog extends Dialog<Results> {
    
    private final int MAX_INPUT_LENGTH = 20;
    private final int MIN_INPUT_LENGTH = 3;
    
    private Label inputLbl;
    private Label choiseLbl;
    private Label errorLbl;
    private TextField inputTextField;
    private ComboBox<Algorithm> comboBox;
    
    private int maxInputValue;
    private int minInputValue;
    
    public InputDialog(){
        this.maxInputValue = Constants.MAX;
        this.minInputValue = Constants.MIN;
        setTitle("New sorting");
        setHeaderText("Enter data");
        
        DialogPane dialogPane = getDialogPane();
        dialogPane.setPrefWidth(450);
        Scene scene = dialogPane.getScene();
        Stage stage = (Stage) scene.getWindow();
        
        scene.getStylesheets().add("dialog.css");
        dialogPane.getStyleClass().add("dialog");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));
        
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button okButton = (Button)getDialogPane().lookupButton(ButtonType.OK);
        
        inputLbl = new Label("Enter the sequence: ");
        choiseLbl = new Label("Choose an Algorithm: ");
        errorLbl = new Label("");
        errorLbl.textFillProperty().setValue(Color.RED);
        
        ObservableList<Algorithm> options =
            FXCollections.observableArrayList(Algorithm.values());
        comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().selectFirst();
        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Algorithm>(){
            @Override
            public void changed(ObservableValue<? extends Algorithm> observable, Algorithm oldValue, Algorithm newValue) {
                updateBounds(newValue);
                try{
                    isInputValid(inputTextField.getText(), minInputValue, maxInputValue);
                } catch(Exception e){
                    errorLbl.setText("Invalid input! " + e.getMessage());
                }
            }
            
        });
        comboBox.setPrefWidth(250);
        
        inputTextField = new TextField(Arrays.toString(
                ArrayUtils.generateRandomArray(10, minInputValue, maxInputValue))
                .replaceAll("\\s+", " ").replaceAll("\\[|\\]", ""));
        inputTextField.setPrefWidth(250);
        
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try{
                isInputValid(inputTextField.getText(), minInputValue, maxInputValue);
            } catch(Exception e){
                errorLbl.setText("Invalid input! " + e.getMessage());
                event.consume();
            }
        });
        
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.add(choiseLbl, 1, 1);
        grid.add(comboBox, 2, 1);
        grid.add(inputLbl, 1, 2);
        grid.add(inputTextField, 2, 2);
        grid.add(errorLbl, 1, 3, 2, 1);
        dialogPane.setContent(grid);
        
        setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                int[] result = parseArray(inputTextField.getText());
                return new Results(result,
                    comboBox.getValue());
            }
            return null;
        });
        
        Platform.runLater(inputTextField::requestFocus);
    }
    
    //update minimum and maximum values
    private void updateBounds(Algorithm type){
        this.minInputValue = Constants.getMinimum(type);
        this.maxInputValue = Constants.getMaximum(type);
    }
    
    private boolean isInputValid(String inputText, int min, int max) throws Exception{
        
        if(inputText.isEmpty() || !inputText.matches(".*\\d.*")){
            throw new RuntimeException("Input cannot be empty");
        }
        
        String[] intStr = inputText.split("(\\D+)");
        int [] input = new int[intStr.length];
        
        for (int i = 0; i < intStr.length; i++) {
            try{
                input[i] = Integer.parseInt(intStr[i]);
            } catch(Exception e){
                throw new Exception("");
            }
        }
        
        int minValue = ArrayUtils.getArrayMin(input);
        int maxValue = ArrayUtils.getArrayMax(input);
        
        if(maxValue > max){
            throw new Exception("Input value must be less than " + (max + 1));
        }
        if(minValue < min){
            throw new Exception("Input value must be grater than or equal to " + min);
        }
        if(intStr.length < MIN_INPUT_LENGTH){
            throw new Exception("Enter at least " + MIN_INPUT_LENGTH + " numbers");
        } 
        else if(intStr.length > MAX_INPUT_LENGTH){ //14 due to sidepanel & min size
            throw new Exception("Maximum count of numbers is " + MAX_INPUT_LENGTH );
        }
        return true;
    }
    
    //use only after checking if it is correct
    private int[] parseArray(String inputText){
        String[] intStr = inputText.split("(\\D+)");
        int [] input = new int[intStr.length];
        for (int i = 0; i < intStr.length; i++) {
            input[i] = Integer.parseInt(intStr[i]);
        }
        return input;
    }
}

