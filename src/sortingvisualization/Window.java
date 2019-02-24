/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Mykhailo Klunko
 */
public class Window extends Application {
    
    private int max = 100;
    private int min = (int)(max * 0.05);
    
    private MenuBar menuBar;
    Menu menuFile;
    Menu menuEdit;
    Menu menuView;
    MenuItem menuItemExit;
    MenuItem menuItemNew;
    
    HBox algorithmButtonBox;
    Label algLbl;
    Label controlLbl;
    Label headerLbl;
    
    Button alg0;
    Button alg1;
    Button alg2;
    Button alg3;
    Button alg4;
    Button alg5;
    Button alg6;
    Button alg7;
    Button alg8;
    Button alg9;
    Button playBtn;
    Button pauseBtn;
    Button stepBackBtn;
    Button stepForthBtn;
    Button showSidePanelBtn;
    Slider speedSlider;
    
    Pane displayPane;
    VBox sidePanel;
    FlowPane codePane;
    
    Data data; 
    ViewController controller;
    
    Scene scene;
    public Window(){}
    
    @Override
    public void start(Stage primaryStage) {
        displayPane = new StackPane();
        codePane = new FlowPane();
        codePane.setPadding(new Insets(30, 10, 30, 10));
        data = new Data();
        controller = new ViewController(data, displayPane, codePane);
        //menu
        initializeMenu(primaryStage);
        
        algLbl = new Label("Algorithms: ");
        algLbl.getStyleClass().add("blcklabel");    
        headerLbl = new Label();
        headerLbl.getStyleClass().add("headerlabel");
        controlLbl = new Label();
        controlLbl.getStyleClass().add("blcklabel");
        
        speedSlider = new Slider();
        speedSlider.setMin(1);
        speedSlider.setMax(7);
        speedSlider.setValue(3);
        speedSlider.setMajorTickUnit(3);
        speedSlider.setMinorTickCount(1);
        speedSlider.setBlockIncrement(1);
        
        initializeUpperPanel();
        
        HBox controlBox = new HBox();
        
        Image playImg = new Image(getClass().getResourceAsStream("/play.png"));
        ImageView playImgV = new ImageView(playImg);
        playImgV.setFitHeight(25);
        playImgV.setFitWidth(25);
        
        Image pauseImg = new Image(getClass().getResourceAsStream("/pause.png"));
        ImageView pauseImgV = new ImageView(pauseImg);
        pauseImgV.setFitHeight(25);
        pauseImgV.setFitWidth(25);
        
        Image rewBackImg = new Image(getClass().getResourceAsStream("/rewind_back.png"));
        ImageView rbImgView = new ImageView(rewBackImg);
        rbImgView.setFitHeight(25);
        rbImgView.setFitWidth(25);
        
        Image rewForthImg = new Image(getClass().getResourceAsStream("/rewind_forth.png"));
        ImageView rfImgView = new ImageView(rewForthImg);
        rfImgView.setFitHeight(25);
        rfImgView.setFitWidth(25);
        
        playBtn = new Button();
        playBtn.setTooltip(new Tooltip("Play!"));
        playBtn.setGraphic(playImgV);
        playBtn.getStyleClass().add("playButton");
        playBtn.setOnAction(event->controller.play());
        
        pauseBtn = new Button();
        pauseBtn.setTooltip(new Tooltip("Pause"));
        pauseBtn.setGraphic(pauseImgV);
        pauseBtn.getStyleClass().add("playButton");
        pauseBtn.setOnAction(event->controller.pause());
        
        stepBackBtn = new Button();
        stepBackBtn.setTooltip(new Tooltip("Step Back"));
        stepBackBtn.setGraphic(rbImgView);
        stepBackBtn.getStyleClass().add("playButton");
        stepBackBtn.setOnAction(event->controller.goStepBack());
        
        stepForthBtn = new Button();
        stepForthBtn.setTooltip(new Tooltip("Step Forth"));
        stepForthBtn.setGraphic(rfImgView);
        stepForthBtn.getStyleClass().add("playButton");
        stepForthBtn.setOnAction(event->controller.goStepForth());
        
        showSidePanelBtn = new Button("<");
        showSidePanelBtn.setMaxWidth(10);
        showSidePanelBtn.setMinHeight(70);
        showSidePanelBtn.getStyleClass().add("sideButton");
        
        
        controlBox.getChildren().addAll(speedSlider, stepBackBtn, playBtn, pauseBtn, stepForthBtn);
        controlBox.setAlignment(Pos.CENTER);
        
        controlBox.setStyle("-fx-background-color: black");
        controlBox.setMinHeight(40);
        
        //algList.setSpacing(10);
        
        VBox top = new VBox();
        top.getChildren().addAll(menuBar, algorithmButtonBox);
        BorderPane root = new BorderPane();
        
        sidePanel = new VBox();
        sidePanel.setPrefWidth(400);
        sidePanel.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));
        sidePanel.getChildren().addAll(headerLbl, codePane);
        sidePanel.setAlignment(Pos.TOP_CENTER);
        sidePanel.setOnMouseClicked(event->
            {/*sidePanel.setVisible(false);
            sidePanel.setManaged(false);*/
            root.setRight(showSidePanelBtn);});
        
        showSidePanelBtn.setOnAction(event->{
            root.setRight(sidePanel);
            Toast.makeText(primaryStage, "Click the side panel to hide it", 3500, 500, 500);});
        
        root.setCenter(displayPane);
        root.setTop(top);
        root.setBottom(controlBox);
        root.setRight(sidePanel);
        BorderPane.setAlignment(showSidePanelBtn, Pos.CENTER_RIGHT);
        
        initialize(Algorithm.Bubble, null);
        
        scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add("style.css");
        primaryStage.setTitle("Sorting Alg Visualisation");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1100);
        //displayPane.setMaxSize(root.getWidth()/2, root.getHeight()/2);
        
        //primaryStage.setFullScreen(true); //left for menu
        //primaryStage.setMaximized(true);
        primaryStage.show();
        Toast.makeText(primaryStage, "Click the side panel to hide it", 3500, 500, 500);
    }
    
    private void initializeMenu(Stage primaryStage){
        menuFile = new Menu("_File");
        menuFile.setMnemonicParsing(true);
        
        menuEdit = new Menu("_Edit");
        menuEdit.setMnemonicParsing(true);
        
        menuView = new Menu("_View");
        menuView.setMnemonicParsing(true);
        
        menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(a -> primaryStage.close());
        menuFile.getItems().add(menuItemExit);
        
        menuItemNew = new MenuItem("Create sorting");
        menuItemNew.setOnAction(event->createNewSorting());
        menuFile.getItems().add(menuItemNew);
        
        menuBar = new MenuBar();
	menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
    }
    
    private void initializeUpperPanel(){
        algorithmButtonBox = new HBox();
        
        alg0 = new Button("BUBL");
        alg0.setTooltip(new Tooltip("Bubble Sort"));
        alg0.getStyleClass().add("button");
        alg0.setOnAction(event->initialize(Algorithm.Bubble, null));
        
        alg1 = new Button("C-SH");
        alg1.setTooltip(new Tooltip("CocktailShaker Sort"));
        alg1.getStyleClass().add("button");
        alg1.setOnAction(event->initialize(Algorithm.CocktailShaker, null));
        
        alg2 = new Button("INS");
        alg2.setTooltip(new Tooltip("Insertion Sort"));
        alg2.getStyleClass().add("button");
        alg2.setOnAction(event->initialize(Algorithm.Insertion, null));
        
        alg3 = new Button("SEL");
        alg3.setTooltip(new Tooltip("Selection Sort"));
        alg3.getStyleClass().add("button");
        alg3.setOnAction(event->initialize(Algorithm.Selection, null));
        
        alg4 = new Button("QUI");
        alg4.setTooltip(new Tooltip("Quick Sort"));
        alg4.getStyleClass().add("button");
        alg4.setOnAction(event->initialize(Algorithm.Quick, null));
        
        alg5 = new Button("MRG");
        alg5.setTooltip(new Tooltip("Merge Sort"));
        alg5.getStyleClass().add("button");
        alg5.setOnAction(event->initialize(Algorithm.Merge, null));
        
        alg6 = new Button("COU");
        alg6.setTooltip(new Tooltip("Counting Sort"));
        alg6.getStyleClass().add("button");
        alg6.setOnAction(event->initialize(Algorithm.Counting, null));
        
        alg7 = new Button("BCKT");
        alg7.setTooltip(new Tooltip("Bucket Sort"));
        alg7.getStyleClass().add("button");
        alg7.setOnAction(event->initialize(Algorithm.Bucket, null));
        
        alg8 = new Button("RDX");
        alg8.setTooltip(new Tooltip("Radix Sort"));
        alg8.getStyleClass().add("button");
        alg8.setOnAction(event->initialize(Algorithm.Radix, null));
        
        algorithmButtonBox.getChildren().addAll(algLbl, alg0, alg1, alg2, alg3, alg4, alg5, alg6, alg7, alg8);
        algorithmButtonBox.setStyle("-fx-background-color: black");
        algorithmButtonBox.setMinHeight(40);
    }
    
    
    //03.09: fixed play
    private void initialize(Algorithm type, int[] input){
        controller.initialize(type, input);
        initButtonBinding();
        headerLbl.setText(type.getName());
    }
    
    private void initButtonBinding(){
        stepForthBtn.disableProperty().bind(controller.getStepForthBinding());
        stepBackBtn.disableProperty().bind(controller.getStepBackBinding());
        playBtn.disableProperty().bind(controller.getPlayBinding());
        speedSlider.valueProperty().addListener(controller.getSpeedListener());
    }
    
    /*
     * Custom input dialog call
     */
    private void createNewSorting() {
        int[] customInput;
        InputDialog dialog = new InputDialog(max, min);
        scene.getRoot().setEffect(new GaussianBlur(5));
        dialog.setOnHidden(event->{scene.getRoot().setEffect(new GaussianBlur(0));});
        Optional<Results> result = dialog.showAndWait();
        
        if (result.isPresent()){
            String[] intStr = result.get().input.split("(\\D+)");
            customInput = new int[intStr.length];
            for (int i = 0; i < intStr.length; i++) {
                customInput[i] = Integer.parseInt(intStr[i]);
            }
            switch(result.get().algoritm){
                case Bubble: case CocktailShaker: case Insertion: case Selection:
                case Quick: case Merge:
                    initialize(result.get().algoritm, customInput);
                    break;
                default:
                    initialize(Algorithm.Bubble, customInput);
            } 
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
        launch(args);
    }*/
    
    
}
