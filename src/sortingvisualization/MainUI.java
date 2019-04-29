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
import javafx.scene.control.ScrollPane;
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
import sortingvisualization.UI.AboutDialog;
import sortingvisualization.UI.InfoDialog;
import sortingvisualization.Utilities.ArrayUtils;
import sortingvisualization.Utilities.DescriptionReader;

/**
 * Main GUI application class
 * @author Mykhailo Klunko
 */
public class MainUI extends Application {
    
    private static final String HELP_MSG = "Click the side panel to hide it";
    private static final Algorithm DEFAULT_TYPE = Algorithm.Bubble;
    
    HBox algorithmButtonBox;
    MenuButton algorithmMenu;
    Label controlLbl;
    Label headerLbl;
    
    Button aboutBtn;
    Button playBtn;
    Button pauseBtn;
    Button toStartBtn;
    Button stepBackBtn;
    Button stepForthBtn;
    Button toEndBtn;
    Button resetCurrent;
    Button infoButton;
    Button newButton;
    Button showSidePanelBtn;
    Slider speedSlider;
    
    ScrollPane scroll;
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
    
    //current state
    ObjectProperty<Algorithm> currentAlgorithm;
    int[] currentArray;
    
    public MainUI(){
        this.currentAlgorithm = new SimpleObjectProperty<>();
        this.currentAlgorithm.setValue(DEFAULT_TYPE);
    }
    
    @Override
    public void start(Stage primaryStage) {
        double windowSizeFactor = Scaling.computeDPIScale();
        
        displayPane = new StackPane();
        scroll = new ScrollPane();
        scroll.setContent(displayPane);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        
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
        speedSlider.setTooltip(new Tooltip("Animations speed"));
        speedSlider.getStyleClass().add("controlSlider");
        
        //init upper panel
        initializeUpperPanel(primaryStage);
        
        HBox controlBox = new HBox();
        
        //load button images, create buttons
        initControlButtons(primaryStage);
        
        Region leftRegion = new Region();
        Region rightRegion = new Region();
        HBox.setHgrow(rightRegion, Priority.ALWAYS);
        HBox.setHgrow(leftRegion, Priority.ALWAYS);
        
        controlBox.getChildren().addAll(resetCurrent, leftRegion, speedSlider, toStartBtn, stepBackBtn, 
                playBtn, pauseBtn, stepForthBtn, toEndBtn, rightRegion, newButton, infoButton);
        controlBox.setAlignment(Pos.CENTER);
        
        controlBox.getStyleClass().add("control-box");
        controlBox.setMinHeight(40);
        
        VBox top = new VBox();
        top.getChildren().addAll(algorithmButtonBox);
        root = new BorderPane();
        
        sidePanel = new StackPane();
        sidePanel.setPrefWidth(420 * windowSizeFactor);
        sidePanel.setBackground(new Background(new BackgroundFill(Constants.PANEL_BGND, CornerRadii.EMPTY, Insets.EMPTY)));
        sidePanel.setOnMouseClicked(event-> {
            root.setRight(showSidePanelBtn);
        });
        
        ScrollPane sideScroll = new ScrollPane();
        sideScroll.setContent(sidePanel);
        sideScroll.setFitToHeight(true);
        sideScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        codePane = new FlowPane();
        codePane.setPadding(new Insets(30, 10, 50, 10));
        
        infoPane = new FlowPane();
        infoPane.setPadding(new Insets(15, 10, 15, 10));
        infoPane.setBackground(new Background(new BackgroundFill(Constants.LOW_PAN_BGND, CornerRadii.EMPTY, Insets.EMPTY)));
        infoPane.setMaxHeight(sidePanel.getPrefHeight() * 0.2);
        
        topSidePanel = new VBox();
        topSidePanel.setAlignment(Pos.TOP_CENTER);
        topSidePanel.getChildren().addAll(headerLbl, codePane);
        topSidePanel.setPadding(new Insets(0, 0, 50, 0));
        
        //StackPane.setAlignment(topSidePanel, Pos.TOP_CENTER);
        StackPane.setAlignment(infoPane, Pos.BOTTOM_CENTER);
        sidePanel.getChildren().addAll(topSidePanel, infoPane);
        showSidePanelBtn.setOnAction(event->{
            root.setRight(sideScroll);
            showToastMessage(primaryStage, HELP_MSG);});
        
        creator = new ViewController(displayPane, codePane, infoPane);
        controller = new AnimationController();
        
        root.setCenter(scroll);
        root.setTop(top);
        root.setBottom(controlBox);
        root.setRight(sideScroll);
        
        BorderPane.setAlignment(showSidePanelBtn, Pos.CENTER_RIGHT);
        
        initialize(DEFAULT_TYPE, null);
        
        scene = new Scene(root, 1280 * windowSizeFactor, 800 * windowSizeFactor);
        scene.getStylesheets().add("style.css");
        
        setupKeyShortcuts(primaryStage);
        
        primaryStage.setTitle("Algorithm Visualizer");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/appicon.png")));
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1100);
        primaryStage.setMaximized(true);
        primaryStage.show();
        showToastMessage(primaryStage, HELP_MSG);
    }
    
    private void initializeUpperPanel(Stage parentStage) {
        algorithmButtonBox = new HBox();
        algorithmMenu = new MenuButton("Algorithms");
        
        for(Algorithm type : Algorithm.values()){
            MenuItem aItem = new MenuItem(type.getName());
            aItem.setOnAction(action -> initialize(type, null));
            algorithmMenu.getItems().add(aItem);
        }
        
        aboutBtn = new Button("About");
        aboutBtn.setTooltip(new Tooltip("About application"));
        aboutBtn.getStyleClass().add("controllButton");
        aboutBtn.setOnAction(event -> showAbout(parentStage));
        
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        
        algorithmButtonBox.getChildren().addAll(algorithmMenu, region, aboutBtn);
        algorithmButtonBox.getStyleClass().add("default_background");
        algorithmButtonBox.setMinHeight(40);
    }
    
    private ImageView loadImageView(String resourcePath) {
        Image image = new Image(getClass().getResourceAsStream(resourcePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        return imageView;
    }
    
    private void initControlButtons(Stage stage) {
        ImageView playImgV = loadImageView("/play.png");
        ImageView pauseImgV = loadImageView("/pause.png");
        ImageView startImgView = loadImageView("/start.png");
        ImageView rbImgView = loadImageView("/rewind_back.png");
        ImageView rfImgView = loadImageView("/rewind_forth.png");
        ImageView endImgView = loadImageView("/end.png");
        ImageView refreshIV = loadImageView("/refresh.png");
        
        playBtn = new Button();
        playBtn.setTooltip(new Tooltip("Play!"));
        playBtn.setGraphic(playImgV);
        playBtn.getStyleClass().add("controllButton");
        playBtn.setOnAction(event -> controller.play());
        
        pauseBtn = new Button();
        pauseBtn.setTooltip(new Tooltip("Pause"));
        pauseBtn.setGraphic(pauseImgV);
        pauseBtn.getStyleClass().add("controllButton");
        pauseBtn.setOnAction(event -> controller.pause());
        
        toStartBtn = new Button();
        toStartBtn.setTooltip(new Tooltip("Go to start"));
        toStartBtn.setGraphic(startImgView);
        toStartBtn.getStyleClass().add("controllButton");
        toStartBtn.setOnAction(event -> initialize(currentAlgorithm.get(), currentArray));
        
        stepBackBtn = new Button();
        stepBackBtn.setTooltip(new Tooltip("Step Back"));
        stepBackBtn.setGraphic(rbImgView);
        stepBackBtn.getStyleClass().add("controllButton");
        stepBackBtn.setOnAction(event -> controller.goStepBack());
        
        stepForthBtn = new Button();
        stepForthBtn.setTooltip(new Tooltip("Step Forth"));
        stepForthBtn.setGraphic(rfImgView);
        stepForthBtn.getStyleClass().add("controllButton");
        stepForthBtn.setOnAction(event -> controller.goStepForth());
        
        toEndBtn = new Button();
        toEndBtn.setTooltip(new Tooltip("Go to end"));
        toEndBtn.setGraphic(endImgView);
        toEndBtn.getStyleClass().add("controllButton");
        toEndBtn.setOnAction(event -> controller.playFast());
        
        showSidePanelBtn = new Button("<");
        showSidePanelBtn.setMaxWidth(10);
        showSidePanelBtn.setMinHeight(70);
        showSidePanelBtn.getStyleClass().add("sideButton");
        
        resetCurrent = new Button();
        resetCurrent.setTooltip(new Tooltip("New random"));
        resetCurrent.setGraphic(refreshIV);
        resetCurrent.getStyleClass().add("controllButton");
        resetCurrent.setOnAction(event -> resetCurrent());

        newButton = new Button("NEW");
        newButton.setTooltip(new Tooltip("Create sorting"));
        newButton.getStyleClass().add("button");
        newButton.setOnAction(event -> openNewSortingDialog());
        
        infoButton = new Button("INFO");
        infoButton.setTooltip(new Tooltip("About " + DEFAULT_TYPE.getName()));
        infoButton.getStyleClass().add("button");
        infoButton.setOnAction(event -> showDescription(stage));
        
        currentAlgorithm.addListener((obs, oldValue, newValue) -> {
            infoButton.setTooltip(new Tooltip("About " + newValue.getName()));
        });
    }
    
    //visualization initialization
    private void initialize(Algorithm type, int[] input) {
        resetCurrent.setDisable(true);
        disableControls(true);
        currentAlgorithm.setValue(type);
        currentArray = ArrayUtils.checkArray(type, input);
        Task<List<Animation>> sortingTask = creator.sort(type, currentArray);
        sortingTask.setOnSucceeded(e -> {
            headerLbl.setText(type.getName());
            BindingData bindings = controller.setupInstance(sortingTask.getValue());
            initButtonBinding(bindings);
            resetCurrent.setDisable(false);
            //refresh bounds for new instance
            displayPane.minWidthProperty().setValue(creator.getChildrenWidth());
            displayPane.minHeightProperty().setValue(creator.getChildrenHeight());
            
        });
        sortingTask.setOnFailed(e -> {
            
        });
        sortingTask.run();
    }
    
    private void initButtonBinding(BindingData bindings) {
        stepForthBtn.disableProperty().bind(bindings.getStepForthBinding());
        toStartBtn.disableProperty().bind(bindings.getStepBackBinding());
        stepBackBtn.disableProperty().bind(bindings.getStepBackBinding());
        toEndBtn.disableProperty().bind(bindings.getStepForthBinding());
        playBtn.disableProperty().bind(bindings.getPlayBinding());
        speedSlider.valueProperty().addListener(bindings.getSpeedListener());
    }
    
    private void unbindControls() {
        toStartBtn.disableProperty().unbind();
        stepForthBtn.disableProperty().unbind();
        stepBackBtn.disableProperty().unbind();
        toEndBtn.disableProperty().unbind();
        playBtn.disableProperty().unbind();
    }
    
    private void disableControls(boolean isDisabled) {
        unbindControls();
        toStartBtn.disableProperty().set(isDisabled);
        stepForthBtn.disableProperty().set(isDisabled);
        stepBackBtn.disableProperty().set(isDisabled);
        toEndBtn.disableProperty().set(isDisabled);
        playBtn.disableProperty().set(isDisabled);
    }
    
    private void setupKeyShortcuts(Stage stage){
        scene.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.R && !event.isShortcutDown()) {
                event.consume();
                if(playBtn.isDisabled()){
                    pauseBtn.fire();
                } else {
                    playBtn.fire();
                }
            } else if(event.getCode() == KeyCode.F1) { 
                showAbout(stage);
            } else if(event.getCode() == KeyCode.F2) {
                showDescription(stage);
            } else if(event.getCode() == KeyCode.F4) {
                openNewSortingDialog();
            } else if(event.getCode() == KeyCode.F5) {
                resetCurrent();
            } else if(event.getCode() == KeyCode.ESCAPE && !event.isShortcutDown()) {
                stage.close();
            } else if(event.getCode() == KeyCode.A && event.isShortcutDown()) {
                stepBackBtn.fire();
            } else if(event.getCode() == KeyCode.D && event.isShortcutDown()) {
                stepForthBtn.fire();
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
        DescriptionReader reader = new DescriptionReader();
        String desc = reader.readDescription(currentAlgorithm.get());
        InfoDialog info = new InfoDialog();
        info.showDescription(primaryStage, desc, currentAlgorithm.get().getName());
    }
    
    private void showAbout(Stage primaryStage) {
        AboutDialog info = new AboutDialog();
        info.showDescription(primaryStage);
    }

    private void resetCurrent() {
        initialize(currentAlgorithm.getValue(), null);
    }
    
    /*private void disableDoubleclick(Button b) {
        b.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    System.out.println("Double clicked");
                }
            }
        });
    }*/
    
    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
