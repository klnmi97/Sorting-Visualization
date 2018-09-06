/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sortingvisualization.Core.IAlgorithm;
import sortingvisualization.Sorting.ActionInstance;
import sortingvisualization.Sorting.SortArray;
import sortingvisualization.algorithms.BubbleSort;
import static sortingvisualization.algorithms.BubbleSort.bubbleSort;
import sortingvisualization.algorithms.InsertionSort;
import sortingvisualization.algorithms.MergeSort;
import sortingvisualization.algorithms.QuickSort;
import sortingvisualization.algorithms.SelectionSort;

/**
 *
 * @author Misha
 */
public class Window extends Application {
    
    private final int ARRAY_SIZE = 10;
    private final int MAX_ARRAY_VAL = 80;
    private final int max = 100;
    private final int min = (int)(max * 0.1);
    
    private MenuBar menuBar;
    Menu menuFile;
    Menu menuEdit;
    Menu menuView;
    MenuItem menuItemExit;
    
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
    
    Pane displayPane;
    ArrayList<BrickNode> list;
    List<Animation> transitions = new ArrayList<Animation>();
    IntegerProperty nextTransitionIndex = new SimpleIntegerProperty();
    BooleanBinding anyPlaying;
    
    IAlgorithm sortAlgorithm;
    SortArray array;
    List<ActionInstance> actions;
    
    
    int counter = 0;
    boolean isSelected = false;
    
    public Window(){
        //sortAlgorithm = new BubbleSort();
        //initNewSorting();
    }
    
    @Override
    public void start(Stage primaryStage) {
        //menu
        initializeMenu(primaryStage);
        
        algLbl = new Label("Algorithms: ");
        algLbl.getStyleClass().add("blcklabel");    
        
        controlLbl = new Label();
        controlLbl.getStyleClass().add("blcklabel");
        
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
        playBtn.setTooltip(new Tooltip("Sort!"));
        playBtn.setGraphic(playImgV);
        playBtn.getStyleClass().add("playButton");
        playBtn.setOnAction(event->play());
        
        
        /*playBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(!isSelected){
                    playBtn.setGraphic(pauseImgV);
                    Runnable task = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        testSort();
                    }
                };
                Thread backgroundThread = new Thread(task);
                backgroundThread.setDaemon(true);
                isSelected = true;
                backgroundThread.start();
                } else {
                    playBtn.setGraphic(playImgV);
                    isSelected = false;
                }
                
            }
        });*/
        
        pauseBtn = new Button("Pause");
        pauseBtn.setOnAction(event->pause());
        
        stepBackBtn = new Button();
        Image rewBackImg = new Image(getClass().getResourceAsStream("/rewind_back.png"));
        ImageView rbImgView = new ImageView(rewBackImg);
        rbImgView.setFitHeight(25);
        rbImgView.setFitWidth(25);
        stepBackBtn.setGraphic(rbImgView);
        stepBackBtn.getStyleClass().add("playButton");
        stepBackBtn.setOnAction(event->goStepBack());
        /*stepBackBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(counter == actions.size()){
                    counter--;
                }
                if(counter >= 0){
                    printInstance(actions, counter);
                    counter--;
                }
            }
        });*/
        
        
        stepForthBtn = new Button();
        Image rewForthImg = new Image(getClass().getResourceAsStream("/rewind_forth.png"));
        ImageView rfImgView = new ImageView(rewForthImg);
        rfImgView.setFitHeight(25);
        rfImgView.setFitWidth(25);
        stepForthBtn.setGraphic(rfImgView);
        stepForthBtn.getStyleClass().add("playButton");
        stepForthBtn.setOnAction(event->goStepForth());
        /*stepForthBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(counter + 1 < actions.size()){
                    counter++;
                    printInstance(actions, counter);
                }
            }
        });*/
        
        controlBox.getChildren().addAll(stepBackBtn, playBtn, pauseBtn, stepForthBtn);
        controlBox.setStyle("-fx-background-color: black");
        controlBox.setMinHeight(40);
        
        
        //algList.setSpacing(10);
        
        VBox top = new VBox();
        top.getChildren().addAll(menuBar, algorithmButtonBox);
        
        //graphic
        displayPane = new Pane();
        initialize();
        
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setBottom(controlBox);
        root.setCenter(displayPane);
        
        
        
        Scene scene = new Scene(root, 1280, 720);
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
        
        menuBar = new MenuBar();
	menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
    }
    
    private void initializeUpperPanel(){
        algorithmButtonBox = new HBox();
        
        alg1 = new Button("BUBL");
        alg1.setTooltip(new Tooltip("Bubble Sort"));
        alg1.getStyleClass().add("button");
        alg1.setOnAction(event->initialize());
        /*alg1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                sortAlgorithm = new BubbleSort();
                initNewSorting();
            }
        });*/
        
        alg2 = new Button("INS");
        alg2.setTooltip(new Tooltip("Insertion Sort"));
        alg2.getStyleClass().add("button");
        /*alg2.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                sortAlgorithm = new InsertionSort();
                initNewSorting();
            }
        });*/
        
        alg3 = new Button("SEL");
        alg3.setTooltip(new Tooltip("Selection Sort"));
        alg3.getStyleClass().add("button");
        /*alg3.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                sortAlgorithm = new SelectionSort();
                initNewSorting();
            }
        });*/
        
        alg4 = new Button("QUI");
        alg4.setTooltip(new Tooltip("Quick Sort"));
        alg4.getStyleClass().add("button");
        /*alg4.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                sortAlgorithm = new QuickSort();
                initNewSorting();
            }
        });*/
        
        alg5 = new Button("MRG");
        alg5.setTooltip(new Tooltip("Merge Sort"));
        alg5.getStyleClass().add("button");
        /*alg5.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                sortAlgorithm = new MergeSort();
                initNewSorting();
            }
        });*/
        
        alg6 = new Button("COU");
        alg6.setTooltip(new Tooltip("Counting Sort"));
        alg6.getStyleClass().add("button");
        
        
        algorithmButtonBox.getChildren().addAll(algLbl, alg1, alg2, alg3, alg4, alg5, alg6);
        algorithmButtonBox.setStyle("-fx-background-color: black");
        algorithmButtonBox.setMinHeight(40);
    }
    
    //play previous animation
    public void goStepBack() {
        int index = nextTransitionIndex.get()-1;
        Animation anim = transitions.get(index);
        anim.setOnFinished(evt -> nextTransitionIndex.set(index));
        //Todo speedslider
        anim.setRate(/*-speedSlider.getValue()*/ -3);
        anim.play();
    }
    //play next animation
    public void goStepForth() {
        int index = nextTransitionIndex.get();
        Animation anim = transitions.get(index);
        anim.setOnFinished(evt -> nextTransitionIndex.set(index+1));
        //TODO speedslider
        anim.setRate(/*speedSlider.getValue()*/3);
        anim.play();
    }
    //03.09: fixed play
    public void initialize(){
        Random random = new Random();
        stopAllAnimations();
        nextTransitionIndex.set(0);
        displayPane.getChildren().clear();
        list = new ArrayList<>();
        
        for (int i = 0; i < ViewController.N_VALUES; i++) {
            int value = random.nextInt(max - min + 1) + min;
            BrickNode stackPane = createValueNode(i, value/*customInputArray[i]*/, max);
            list.add(stackPane);
        }

        displayPane.getChildren().addAll(list);
        transitions = new ArrayList<>();
        
        //transitions = mergeSort(list, transitions);
        transitions = bubbleSort(list, transitions);
        //transitions = insertionSort(list, transitions);
        //transitions = selectionSort(list, transitions);
        //transitions = quickSort(list, transitions);
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
        
        /*speedSlider.valueProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                for(int i = 0; i < transitions.size(); i++){
                    transitions.get(i).setRate(newValue.doubleValue());
                }
            }
            
        });*/
        
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
            //TODO:speedslider
            anim.setRate(/*speedSlider.getValue()*/3);
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
        
        //int max = 51;
        int num;
        if(value < min)
            num = min;
        else 
            num = value;
        double percent = (double)num / currentMax;
        Rectangle rectangle = new Rectangle(40, (percent * 10 * 20) + 5);
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
    
    /*private void initNewSorting(){
        counter = 0;
        array = new SortArray();
        array.generate(ARRAY_SIZE, MAX_ARRAY_VAL);
        System.out.println(array);
        sortAlgorithm.sort(array);
        actions = array.actions;
    }
    
    public void testSort(){
        if(counter < 0 || counter == actions.size()){
            System.out.println();
            counter = 0;
        } 
        for (int i = counter; i < actions.size(); i++, counter++) {
            try{
                if(isSelected == false) break;
                printInstance(actions, i);
                Thread.sleep(500);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    
    public void printInstance(List<ActionInstance> action, int i){
        int[] temp = action.get(i).get();
                if(temp[0] == 0){
                    System.out.println("array[" + temp[1] + "] and array[" + temp[2] + "] swapped");
                } else if(temp[0] == 1){
                    System.out.println("array[" + temp[1] + "] and array[" + temp[2] + "] compared");
                }
    }*/

    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
        launch(args);
    }*/
    
    
}
