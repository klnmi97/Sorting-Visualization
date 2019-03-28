/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;


import sortingvisualization.Enums.Algorithm;
import sortingvisualization.Controllers.ViewController;
import sortingvisualization.Controllers.AnimationController;
import sortingvisualization.Data.Results;
import sortingvisualization.UI.InputDialog;
import sortingvisualization.UI.Toast;
import sortingvisualization.Data.BindingData;
import sortingvisualization.Utilities.Scaling;

import java.util.List;
import java.util.Optional;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.concurrent.Task;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sortingvisualization.Constants.Constants;
import sortingvisualization.UI.InfoDialog;

/**
 *
 * @author Mykhailo Klunko
 */
public class Window extends Application {
    
    private static final String toastMessage = "Click the side panel to hide it";
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
    
    Button playBtn;
    Button pauseBtn;
    Button stepBackBtn;
    Button stepForthBtn;
    Button infoButton;
    Button showSidePanelBtn;
    Slider speedSlider;
    
    Pane displayPane;
    StackPane sidePanel;
    VBox topSidePanel;
    FlowPane codePane;
    FlowPane infoPane;
    
    BindingData buttonBindings;
    ViewController creator;
    AnimationController controller;
    
    Scene scene;
    public Window(){}
    
    @Override
    public void start(Stage primaryStage) {
        double windowSizeFactor = Scaling.computeDPIScale();
        
        displayPane = new StackPane();
        
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
        speedSlider.setValue(2);
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
        
        infoButton = new Button("INFO");
        infoButton.getStyleClass().add("button");
        infoButton.setOnAction(event -> showDescription(primaryStage));
        
        Region leftRegion = new Region();
        Region rightRegion = new Region();
        HBox.setHgrow(rightRegion, Priority.ALWAYS);
        HBox.setHgrow(leftRegion, Priority.ALWAYS);
        
        controlBox.getChildren().addAll(leftRegion, speedSlider, stepBackBtn, 
                playBtn, pauseBtn, stepForthBtn, rightRegion, infoButton);
        controlBox.setAlignment(Pos.CENTER);
        
        controlBox.setStyle("-fx-background-color: black");
        controlBox.setMinHeight(40);
        
        VBox top = new VBox();
        top.getChildren().addAll(menuBar, algorithmButtonBox);
        BorderPane root = new BorderPane();
        
        sidePanel = new StackPane();
        sidePanel.setPrefWidth(420 * windowSizeFactor);
        sidePanel.setBackground(new Background(new BackgroundFill(Constants.PANEL_BGND, CornerRadii.EMPTY, Insets.EMPTY)));
        sidePanel.setOnMouseClicked(event->
            {root.setRight(showSidePanelBtn);});
        
        codePane = new FlowPane();
        codePane.setPadding(new Insets(30, 10, 30, 10));
        
        infoPane = new FlowPane();
        infoPane.setPadding(new Insets(15, 10, 15, 10));
        infoPane.setBackground(new Background(new BackgroundFill(Constants.LOW_PAN_BGND, CornerRadii.EMPTY, Insets.EMPTY)));
        infoPane.setMaxHeight(sidePanel.getPrefHeight() * 0.2);
        
        topSidePanel = new VBox();
        topSidePanel.setAlignment(Pos.TOP_CENTER);
        topSidePanel.getChildren().addAll(headerLbl, codePane);
        
        StackPane.setAlignment(topSidePanel, Pos.TOP_CENTER);
        StackPane.setAlignment(infoPane, Pos.BOTTOM_CENTER);
        sidePanel.getChildren().addAll(topSidePanel, infoPane);
        infoPane.setMaxHeight(sidePanel.getPrefHeight() * 0.2);
        showSidePanelBtn.setOnAction(event->{
            root.setRight(sidePanel);
            showToastMessage(primaryStage, toastMessage);});
        
        creator = new ViewController(displayPane, codePane, infoPane);
        controller = new AnimationController();
        
        root.setCenter(displayPane);
        root.setTop(top);
        root.setBottom(controlBox);
        root.setRight(sidePanel);
        BorderPane.setAlignment(showSidePanelBtn, Pos.CENTER_RIGHT);
        
        initialize(Algorithm.Bubble, null);
        
        scene = new Scene(root, 1280 * windowSizeFactor, 720 * windowSizeFactor);
        scene.getStylesheets().add("style.css");
        
        setupKeyShortcuts(primaryStage);
        
        primaryStage.setTitle("Algorithm Tutor");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1100);
        primaryStage.show();
        showToastMessage(primaryStage, toastMessage);
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
        menuItemNew.setOnAction(event->openNewSortingDialog());
        menuItemNew.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        menuFile.getItems().add(menuItemNew);
        
        menuBar = new MenuBar();
	menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
    }
    
    private void initializeUpperPanel(){
        algorithmButtonBox = new HBox(algLbl);
        
        for(Algorithm type : Algorithm.values()){
            Button aButton = new Button(type.getShortName());
            aButton.setTooltip(new Tooltip(type.getName()));
            aButton.getStyleClass().add("button");
            aButton.setOnAction(action -> initialize(type, null));
            algorithmButtonBox.getChildren().add(aButton);
        }
        
        algorithmButtonBox.setStyle("-fx-background-color: black");
        algorithmButtonBox.setMinHeight(40);
    }
    
    private void initialize(Algorithm type, int[] input){
        //TODO: create loading
        headerLbl.setText(type.getName());
        Task<List<Animation>> sortingTask = creator.sort(type, input);
        sortingTask.setOnSucceeded(e->{
            BindingData bindings = controller.setupInstance(sortingTask.getValue());
            initButtonBinding(bindings);
        });
        sortingTask.run();
    }
    
    private void initButtonBinding(BindingData bindings){
        stepForthBtn.disableProperty().bind(bindings.getStepForthBinding());
        stepBackBtn.disableProperty().bind(bindings.getStepBackBinding());
        playBtn.disableProperty().bind(bindings.getPlayBinding());
        speedSlider.valueProperty().addListener(bindings.getSpeedListener());
    }
    
    private void setupKeyShortcuts(Stage stage){
        scene.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });
    }
    
    private void showToastMessage(Stage primaryStage, String toastMessage){
        Toast.makeText(primaryStage, toastMessage, 3500, 500, 500);
    }
    
    private void openNewSortingDialog() {
        
        scene.getRoot().setEffect(new GaussianBlur(5));
        
        InputDialog dialog = new InputDialog();
        dialog.setOnHidden(event->{scene.getRoot().setEffect(new GaussianBlur(0));});
        
        Optional<Results> result = dialog.showAndWait();
        
        if (result.isPresent()){
            int[] customInput = result.get().getInput();
            initialize(result.get().getAlgoritm(), customInput);
        }
    }

    private void showDescription(Stage primaryStage) {
        InfoDialog info = new InfoDialog();
        info.showDescription(primaryStage);
    }
}
