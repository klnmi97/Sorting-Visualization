/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static sortingvisualization.algorithms.BubbleSort.bubbleSort;
import static sortingvisualization.algorithms.InsertionSort.insertionSort;
import static sortingvisualization.algorithms.MergeSort.mergeSort;
import static sortingvisualization.algorithms.QuickSort.quickSort;
import static sortingvisualization.algorithms.SelectionSort.selectionSort;

/**
 *
 * @author Misha
 */
public class Window extends Application {
    
    private final int max = 100;
    private final int min = (int)(max * 0.1);
    
    private MenuBar menuBar;
    Menu menuFile;
    Menu menuEdit;
    Menu menuView;
    MenuItem menuItemExit;
    MenuItem menuItemNew;
    
    HBox algorithmButtonBox;
    Label algLbl;
    Label controlLbl;
    
    Button alg1;
    Button alg2;
    Button alg3;
    Button alg4;
    Button alg5;
    Button alg6;
    Button playBtn;
    Button pauseBtn;
    Button stepBackBtn;
    Button stepForthBtn;
    Slider speedSlider;
    
    Pane displayPane;
    ArrayList<BrickNode> list;
    List<Animation> transitions = new ArrayList<Animation>();
    IntegerProperty nextTransitionIndex = new SimpleIntegerProperty();
    BooleanBinding anyPlaying;
    
    Scene scene;
    public Window(){}
    
    @Override
    public void start(Stage primaryStage) {
        displayPane = new StackPane();
        //menu
        initializeMenu(primaryStage);
        
        algLbl = new Label("Algorithms: ");
        algLbl.getStyleClass().add("blcklabel");    
        
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
        
        playBtn = new Button();
        playBtn.setTooltip(new Tooltip("Play!"));
        playBtn.setGraphic(playImgV);
        playBtn.getStyleClass().add("playButton");
        playBtn.setOnAction(event->play());
        
        pauseBtn = new Button();
        pauseBtn.setTooltip(new Tooltip("Pause"));
        pauseBtn.setGraphic(pauseImgV);
        pauseBtn.getStyleClass().add("playButton");
        pauseBtn.setOnAction(event->pause());
        
        stepBackBtn = new Button();
        Image rewBackImg = new Image(getClass().getResourceAsStream("/rewind_back.png"));
        ImageView rbImgView = new ImageView(rewBackImg);
        rbImgView.setFitHeight(25);
        rbImgView.setFitWidth(25);
        stepBackBtn.setGraphic(rbImgView);
        stepBackBtn.getStyleClass().add("playButton");
        stepBackBtn.setOnAction(event->goStepBack());
        
        stepForthBtn = new Button();
        Image rewForthImg = new Image(getClass().getResourceAsStream("/rewind_forth.png"));
        ImageView rfImgView = new ImageView(rewForthImg);
        rfImgView.setFitHeight(25);
        rfImgView.setFitWidth(25);
        stepForthBtn.setGraphic(rfImgView);
        stepForthBtn.getStyleClass().add("playButton");
        stepForthBtn.setOnAction(event->goStepForth());
        
        initialize(1, null);
        
        controlBox.getChildren().addAll(speedSlider, stepBackBtn, playBtn, pauseBtn, stepForthBtn);
        controlBox.setAlignment(Pos.CENTER);
        
        controlBox.setStyle("-fx-background-color: black");
        controlBox.setMinHeight(40);
        
        
        //algList.setSpacing(10);
        
        VBox top = new VBox();
        top.getChildren().addAll(menuBar, algorithmButtonBox);
        
        BorderPane root = new BorderPane();
        root.setCenter(displayPane);
        root.setTop(top);
        root.setBottom(controlBox);
        
        
        scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add("style.css");
        primaryStage.setTitle("Sorting Alg Visualisation");
        primaryStage.setScene(scene);
        
        //displayPane.setMaxSize(root.getWidth()/2, root.getHeight()/2);
        
        //primaryStage.setFullScreen(true); //left for menu
        //primaryStage.setMaximized(true);
        primaryStage.show();
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
        
        alg1 = new Button("BUBL");
        alg1.setTooltip(new Tooltip("Bubble Sort"));
        alg1.getStyleClass().add("button");
        alg1.setOnAction(event->initialize(1, null));
        
        alg2 = new Button("INS");
        alg2.setTooltip(new Tooltip("Insertion Sort"));
        alg2.getStyleClass().add("button");
        alg2.setOnAction(event->initialize(2, null));
        
        alg3 = new Button("SEL");
        alg3.setTooltip(new Tooltip("Selection Sort"));
        alg3.getStyleClass().add("button");
        alg3.setOnAction(event->initialize(3, null));
        
        alg4 = new Button("QUI");
        alg4.setTooltip(new Tooltip("Quick Sort"));
        alg4.getStyleClass().add("button");
        alg4.setOnAction(event->initialize(4, null));
        
        alg5 = new Button("MRG");
        alg5.setTooltip(new Tooltip("Merge Sort"));
        alg5.getStyleClass().add("button");
        alg5.setOnAction(event->initialize(5, null));
        
        /*alg6 = new Button("COU");
        alg6.setTooltip(new Tooltip("Counting Sort"));
        alg6.getStyleClass().add("button");*/
        
        algorithmButtonBox.getChildren().addAll(algLbl, alg1, alg2, alg3, alg4, alg5);
        algorithmButtonBox.setStyle("-fx-background-color: black");
        algorithmButtonBox.setMinHeight(40);
    }
    
    //play previous animation
    public void goStepBack() {
        int index = nextTransitionIndex.get()-1;
        Animation anim = transitions.get(index);
        anim.setOnFinished(evt -> nextTransitionIndex.set(index));
        //Todo speedslider
        anim.setRate(-speedSlider.getValue());
        anim.play();
    }
    //play next animation
    public void goStepForth() {
        int index = nextTransitionIndex.get();
        Animation anim = transitions.get(index);
        anim.setOnFinished(evt -> nextTransitionIndex.set(index+1));
        //TODO speedslider
        anim.setRate(speedSlider.getValue());
        anim.play();
    }
    //03.09: fixed play
    public void initialize(int algorithm, int[] input){
        Random random = new Random();
        stopAllAnimations();
        nextTransitionIndex.set(0);
        displayPane.getChildren().clear();
        list = new ArrayList<>();
        
        if(input!=null){
            ViewController.N_VALUES = input.length;
            ViewController.LEFT_INDENT = (int)(((double)input.length / 2) 
                    * -ViewController.SPACING);
            for (int i = 0; i < ViewController.N_VALUES; i++) {
                BrickNode stackPane = createValueNode(i, input[i], max);
                list.add(stackPane);
            }
        }else{
            ViewController.N_VALUES = 10;
            ViewController.LEFT_INDENT = (int)(((double)ViewController.N_VALUES / 2) 
                    * -ViewController.SPACING);
            for (int i = 0; i < ViewController.N_VALUES; i++) {
                int value = random.nextInt(max - min + 1) + min;
                BrickNode stackPane = createValueNode(i, value/*customInputArray[i]*/, max);
                list.add(stackPane);
            }
        }
        

        displayPane.getChildren().addAll(list);
        transitions = new ArrayList<>();
        
        switch(algorithm){
            case 1:
                transitions = bubbleSort(list, transitions);
                break;
            case 2:
                transitions = insertionSort(list, transitions);
                break;
            case 3:
                transitions = selectionSort(list, transitions);
                break;
            case 4:
                transitions = quickSort(list, transitions);
                break;
            case 5:
                transitions = mergeSort(list, transitions);
                break;
            default:
        }
           
        anyPlaying = createAnyPlayingBinding(transitions);
        
        stepForthBtn.disableProperty().bind(
                nextTransitionIndex.greaterThanOrEqualTo(transitions.size())
                .or(anyPlaying)
        );
        
        stepBackBtn.disableProperty().bind(
                nextTransitionIndex.lessThanOrEqualTo(0)
                .or(anyPlaying));
        
        playBtn.disableProperty().bind(
                nextTransitionIndex.greaterThanOrEqualTo(transitions.size())
                .or(anyPlaying)
        );
        
        speedSlider.valueProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                for(int i = 0; i < transitions.size(); i++){
                    transitions.get(i).setRate(newValue.doubleValue());
                }
            }
            
        });
        
    }
    
    private void stopAllAnimations(){
        transitions.stream()
                .forEach(anim->anim.stop());
    }
    
    //TODO: Bind and finish
    public void play(){
        if(nextTransitionIndex.get()<transitions.size()){
            int index = nextTransitionIndex.get();
            Animation anim = transitions.get(index);
            anim.setOnFinished(evt -> {
                nextTransitionIndex.set(index+1);
                play();
            });
            anim.setRate(speedSlider.getValue());
            anim.play();}
    }
    //22.06 new pause() method with stream
    public void pause(){
        transitions.stream()
                .filter(anim -> anim.getStatus()==Animation.Status.RUNNING)
                .forEach(anim -> anim.setOnFinished(evt ->{
                    int index = nextTransitionIndex.get();
                    nextTransitionIndex.set(index+1);
                }));
    }
    
    private BrickNode createValueNode(int i, int value, int currentMax) {
        
        int num;
        if(value < min)
            num = min;
        else 
            num = value;
        double percent = (double)num / currentMax;
        Rectangle rectangle = new Rectangle(50, (percent * 10 * 20) + 5);
        rectangle.setFill(ViewController.DEFAULT);
        
        Text text = new Text(String.valueOf(num));
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        BrickNode stackPane = new BrickNode(num);
        stackPane.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        stackPane.setId(String.valueOf(num));
        //stackPane.setValue(num);
        stackPane.getChildren().addAll(rectangle, text);
        BrickNode.setAlignment(text, Pos.TOP_CENTER);
        stackPane.setAlignment(Pos.TOP_CENTER);
        stackPane.setTranslateX(ViewController.SPACING * i + ViewController.LEFT_INDENT);
        stackPane.setTranslateY(ViewController.TOP_INDENT);
        stackPane.setShape(rectangle);
        return stackPane;
    }
    
     /*bidings*/
     private BooleanBinding createAnyPlayingBinding(List<Animation> transitions) {
        return new BooleanBinding() {
            { // Anonymous constructor
                // bind to the status properties of all the transitions
                // (i.e. mark this binding as invalid if any of the status properties change)
                transitions.stream()
                    .map(Animation::statusProperty)
                    .forEach(this::bind);
            }
            @Override
            protected boolean computeValue() {
                // return true if any of the transitions statuses are equal to RUNNING:
                return transitions.stream()
                    .anyMatch(anim -> anim.getStatus()==Animation.Status.RUNNING);
            }
        };

    }

    private void createNewSorting() {
        int[] customInput;
        InputDialog dialog = new InputDialog(max);
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
                case Bubble:
                    initialize(1, customInput);
                    break;
                case Insertion:
                    initialize(2, customInput);
                    break;
                case Selection:
                    initialize(3, customInput);
                    break;
                case Quick:
                    initialize(4, customInput);
                    break;
                case Merge:
                    initialize(5, customInput);
                    break;
                default:
                    initialize(1, customInput);
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
