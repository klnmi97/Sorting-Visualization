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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
    private static final Algorithm DEFAULT_TYPE = Algorithm.Bubble;
    
    HBox algorithmButtonBox;
    MenuButton algorithmMenu;
    Label controlLbl;
    Label headerLbl;
    
    Button playBtn;
    Button pauseBtn;
    Button stepBackBtn;
    Button stepForthBtn;
    Button resetCurrent;
    Button infoButton;
    Button newButton;
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
    
    BorderPane root;
    Scene scene;
    
    ObjectProperty<Algorithm> current;
    
    public Window(){
        this.current = new SimpleObjectProperty<>();
        this.current.setValue(DEFAULT_TYPE);
    }
    
    @Override
    public void start(Stage primaryStage) {
        double windowSizeFactor = Scaling.computeDPIScale();
        
        displayPane = new StackPane();
        
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
        
        Image refreshImg = new Image(getClass().getResourceAsStream("/refresh.png"));
        ImageView refreshIV = new ImageView(refreshImg);
        refreshIV.setFitHeight(25);
        refreshIV.setFitWidth(25);
        
        playBtn = new Button();
        playBtn.setTooltip(new Tooltip("Play!"));
        playBtn.setGraphic(playImgV);
        playBtn.getStyleClass().add("controllButton");
        playBtn.setOnAction(event->controller.play());
        
        pauseBtn = new Button();
        pauseBtn.setTooltip(new Tooltip("Pause"));
        pauseBtn.setGraphic(pauseImgV);
        pauseBtn.getStyleClass().add("controllButton");
        pauseBtn.setOnAction(event->controller.pause());
        
        stepBackBtn = new Button();
        stepBackBtn.setTooltip(new Tooltip("Step Back"));
        stepBackBtn.setGraphic(rbImgView);
        stepBackBtn.getStyleClass().add("controllButton");
        stepBackBtn.setOnAction(event->controller.goStepBack());
        
        stepForthBtn = new Button();
        stepForthBtn.setTooltip(new Tooltip("Step Forth"));
        stepForthBtn.setGraphic(rfImgView);
        stepForthBtn.getStyleClass().add("controllButton");
        stepForthBtn.setOnAction(event->controller.goStepForth());
        
        showSidePanelBtn = new Button("<");
        showSidePanelBtn.setMaxWidth(10);
        showSidePanelBtn.setMinHeight(70);
        showSidePanelBtn.getStyleClass().add("sideButton");
        
        resetCurrent = new Button();
        resetCurrent.setTooltip(new Tooltip("New random"));
        resetCurrent.setGraphic(refreshIV);
        resetCurrent.getStyleClass().add("controllButton");
        resetCurrent.setOnAction(event->resetCurrent());

        newButton = new Button("NEW");
        newButton.setTooltip(new Tooltip("Create sorting"));
        newButton.getStyleClass().add("button");
        newButton.setOnAction(event -> openNewSortingDialog());
        
        infoButton = new Button("INFO");
        infoButton.setTooltip(new Tooltip("About " + DEFAULT_TYPE.getName()));
        infoButton.getStyleClass().add("button");
        infoButton.setOnAction(event -> showDescription(primaryStage));
        
        current.addListener((obs, oldValue, newValue) -> {
            infoButton.setTooltip(new Tooltip("About " + newValue.getName()));
        });
        
        Region leftRegion = new Region();
        Region rightRegion = new Region();
        HBox.setHgrow(rightRegion, Priority.ALWAYS);
        HBox.setHgrow(leftRegion, Priority.ALWAYS);
        
        controlBox.getChildren().addAll(resetCurrent, leftRegion, speedSlider, stepBackBtn, 
                playBtn, pauseBtn, stepForthBtn, rightRegion, newButton, infoButton);
        controlBox.setAlignment(Pos.CENTER);
        
        controlBox.setStyle("-fx-background-color: black");
        controlBox.setMinHeight(40);
        
        VBox top = new VBox();
        top.getChildren().addAll(algorithmButtonBox);
        root = new BorderPane();
        
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
        
        initialize(DEFAULT_TYPE, null);
        
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
    
    private void initializeUpperPanel(){
        algorithmButtonBox = new HBox();
        algorithmMenu = new MenuButton("Algorithms");
        
        for(Algorithm type : Algorithm.values()){
            MenuItem aItem = new MenuItem(type.getName());
            aItem.setOnAction(action -> initialize(type, null));
            algorithmMenu.getItems().add(aItem);
        }
        
        algorithmButtonBox.getChildren().add(algorithmMenu);
        algorithmButtonBox.getStyleClass().add("default_background");
        algorithmButtonBox.setMinHeight(40);
    }
    
    private void initialize(Algorithm type, int[] input){
        resetCurrent.setDisable(true);
        current.setValue(type);
        headerLbl.setText(type.getName());
        Task<List<Animation>> sortingTask = creator.sort(type, input);
        sortingTask.setOnSucceeded(e->{
            BindingData bindings = controller.setupInstance(sortingTask.getValue());
            initButtonBinding(bindings);
            resetCurrent.setDisable(false);
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
            if(event.getCode() == KeyCode.R) {
                event.consume();
                if(playBtn.isDisabled()){
                    controller.pause();
                } else {
                    controller.play();
                }
            } else if(event.getCode() == KeyCode.F1) { 
                showDescription(stage);
            } else if(event.getCode() == KeyCode.F4) {
                openNewSortingDialog();
            } else if(event.getCode() == KeyCode.F5) {
                resetCurrent();
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

    private void resetCurrent() {
        initialize(current.getValue(), null);
    }
}
