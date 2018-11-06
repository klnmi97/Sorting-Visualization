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
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static sortingvisualization.algorithms.BubbleSort.bubbleSort;
import sortingvisualization.algorithms.BucketSort;
import static sortingvisualization.algorithms.CocktailShakerSort.cocktailShakerSort;
import sortingvisualization.algorithms.CountingSort;
import static sortingvisualization.algorithms.CountingSort.countingSort;
import static sortingvisualization.algorithms.InsertionSort.insertionSort;
import static sortingvisualization.algorithms.MergeSort.mergeSort;
import static sortingvisualization.algorithms.QuickSort.quickSort;
import static sortingvisualization.algorithms.SelectionSort.selectionSort;

/**
 *
 * @author Mykhailo Klunko
 */
public class Window extends Application {
    
    private final int max = 10;
    private final int min = (int)(max * 0.05);
    
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
    
    Button alg1;
    Button alg2;
    Button alg3;
    Button alg4;
    Button alg5;
    Button alg6;
    Button alg7;
    Button alg8;
    Button playBtn;
    Button pauseBtn;
    Button stepBackBtn;
    Button stepForthBtn;
    Button showSidePanelBtn;
    Slider speedSlider;
    
    Pane displayPane;
    VBox sidePanel;
    FlowPane codePane;
    ArrayList<BrickNode> list;
    List<Animation> transitions = new ArrayList<Animation>();
    IntegerProperty nextTransitionIndex = new SimpleIntegerProperty();
    BooleanBinding anyPlayingAnim;
    
    Scene scene;
    public Window(){}
    
    @Override
    public void start(Stage primaryStage) {
        displayPane = new StackPane();
        codePane = new FlowPane();
        codePane.setPadding(new Insets(30, 10, 30, 10));
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
        
        initialize(1, generateRandomArray(10, min, max));
        
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
        
        alg6 = new Button("C-SH");
        alg6.setTooltip(new Tooltip("CocktailShaker Sort"));
        alg6.getStyleClass().add("button");
        alg6.setOnAction(event->initialize(6, null));
        
        alg7 = new Button("COU");
        alg7.setTooltip(new Tooltip("Counting Sort"));
        alg7.getStyleClass().add("button");
        alg7.setOnAction(event->initialize(7, null));
        
        alg8 = new Button("BCKT(beta)");
        alg8.setTooltip(new Tooltip("Bucket Sort"));
        alg8.getStyleClass().add("button");
        alg8.setOnAction(event->initialize(8, null));
        
        algorithmButtonBox.getChildren().addAll(algLbl, alg1, alg6, alg2, alg3, alg4, alg5, alg7, alg8);
        algorithmButtonBox.setStyle("-fx-background-color: black");
        algorithmButtonBox.setMinHeight(40);
    }
    
    private int[] generateRandomArray(int size, int min, int max){
        Random randomValue = new Random();
        int[] randomArray = new int[size];
        for(int i = 0; i < size; i++){
            randomArray[i] = randomValue.nextInt(max - min) + min + 1;
        }
        return randomArray;
    }
    
    //play previous animation
    public void goStepBack() {
        int index = nextTransitionIndex.get()-1;
        Animation anim = transitions.get(index);
        anim.setOnFinished(evt -> nextTransitionIndex.set(index));
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
        codePane.getChildren().clear();
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
            ViewController.N_VALUES = 12;
            ViewController.LEFT_INDENT = (int)(((double)ViewController.N_VALUES / 2) 
                    * -ViewController.SPACING);
            for (int i = 0; i < ViewController.N_VALUES; i++) {
                int value = random.nextInt(max - min) + min;
                BrickNode stackPane;
                if(algorithm == 8)
                    stackPane = createBucketNode(i, value, ViewController.LEFT_INDENT, ViewController.TOP_INDENT);
                else
                    stackPane = createValueNode(i, value/*customInputArray[i]*/, max);
                list.add(stackPane);
            }
        }
        
        /*Polygon pol = QuickSort.createPointingTriangle();
        pol.setOpacity(0.4);
        pol.setTranslateX(ViewController.LEFT_INDENT);
        StackPane.setAlignment(pol, Pos.TOP_CENTER);*/
        displayPane.getChildren().addAll(list);
        //displayPane.getChildren().add(pol);
        
        transitions = new ArrayList<>();
        
        switch(algorithm){
            case 1:
                transitions = bubbleSort(list, codePane);
                headerLbl.setText("Bubble Sort");
                break;
            case 2:
                transitions = insertionSort(list, codePane);
                headerLbl.setText("Insertion Sort");
                break;
            case 3:
                transitions = selectionSort(list, codePane);
                headerLbl.setText("Selection Sort");
                break;
            case 4:
                transitions = quickSort(list, codePane);
                headerLbl.setText("Quick Sort");
                break;
            case 5:
                transitions = mergeSort(list, codePane);
                headerLbl.setText("Merge Sort");
                break;
            case 6:
                transitions = cocktailShakerSort(list, codePane);
                headerLbl.setText("Cocktail Shaker Sort");
                break;
            case 7:
                List<Label> labels = createLabelsList(max);
                transitions = countingSort(list, labels, max, codePane);
                headerLbl.setText("Counting Sort");
                displayPane.getChildren().addAll(0, createCountingArrayVis(max));
                displayPane.getChildren().addAll(labels);
                break;
            case 8:
                transitions = BucketSort.bucketSort(list, codePane);
                headerLbl.setText("Bucket Sort");
                break;
            default:
        }
           
        anyPlayingAnim = createAnyPlayingBinding(transitions);
        
        stepForthBtn.disableProperty().bind(nextTransitionIndex.greaterThanOrEqualTo(transitions.size())
                .or(anyPlayingAnim)
        );
        
        stepBackBtn.disableProperty().bind(nextTransitionIndex.lessThanOrEqualTo(0)
                .or(anyPlayingAnim));
        
        playBtn.disableProperty().bind(nextTransitionIndex.greaterThanOrEqualTo(transitions.size())
                .or(anyPlayingAnim)
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
    
    private List<BrickNode> createCountingArrayVis(int count){
        List<BrickNode> subList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BrickNode stackPane = createCustomNode(i, i, count, Color.LIGHTGREY, 
                    ViewController.DEFAULT_LEFT_INDENT, ViewController.SORT_GROUP_MOVE_DELTA);
            subList.add(stackPane);
        }
        return subList;
    }
    
    private List<Label> createLabelsList(int count){
        List<Label> labels = new ArrayList<>();
        for(int i = 0; i < count; i++){
            Label label = new Label("0");
            StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
            label.setTranslateX(ViewController.SPACING * i + ViewController.DEFAULT_LEFT_INDENT);
            label.setTranslateY(ViewController.SORT_GROUP_MOVE_DELTA + 30);
            label.fontProperty().set(Font.font("Helvetica", 20));
            labels.add(label);
        }
        return labels;
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
        
        /*int num;
        if(value < min)
            num = min;
        else 
            num = value;
        double percent = (double)num / currentMax;
        Rectangle rectangle = new Rectangle(50, (percent * 10 * 20) + 5);
        rectangle.setFill(ViewController.DEFAULT);
        
        Text text = new Text(String.valueOf(num));
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        BrickNode node = new BrickNode(num);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        //node.setId(String.valueOf(num));
        //stackPane.setValue(num);
        node.getChildren().addAll(rectangle, text);
        BrickNode.setAlignment(text, Pos.BOTTOM_CENTER);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(ViewController.SPACING * i + ViewController.LEFT_INDENT);
        node.setTranslateY(ViewController.TOP_INDENT);
        node.setShape(rectangle);*/
        return createCustomNode(i, value, currentMax, ViewController.DEFAULT, 
                ViewController.LEFT_INDENT, ViewController.TOP_INDENT);
    }
    
    private BrickNode createCustomNode(int i, int value, int currentMax, 
            Color color, double leftIndent, double topIndent) {
        
        int num;
        if(value < min)
            num = min;
        else 
            num = value;
        double percent = (double)num / currentMax;
        Rectangle rectangle = new Rectangle(50, (percent * 10 * 20) + 5);
        rectangle.setFill(color);
        
        Text text = new Text(String.valueOf(num));
        text.setFont(Font.font("Helvetica", 20));
        BrickNode node = new BrickNode(num);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        //node.setId(String.valueOf(num));
        //stackPane.setValue(num);
        node.getChildren().addAll(rectangle, text);
        BrickNode.setAlignment(text, Pos.BOTTOM_CENTER);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(ViewController.SPACING * i + leftIndent);
        node.setTranslateY(topIndent);
        node.setShape(rectangle);
        return node;
    }
    
    /***** Bucket, Radix *****/
    
    private BrickNode createBucketNode(int i, int value, double leftIndent, double topIndent){
        //TODO: add numbers color change
        Rectangle rectangle = new Rectangle(50, 40);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.WHITE);
        //test on different screens!
        //rectangle.widthProperty().set(Screen.getPrimary().getVisualBounds().getWidth() * 0.033);
        Text text = new Text(String.valueOf(value));
        text.setFont(Font.font("Helvetica", 20));
        BrickNode node = new BrickNode(value);
        node.setPrefSize(rectangle.getWidth(), rectangle.getHeight());
        node.getChildren().addAll(rectangle, text);
        BrickNode.setAlignment(text, Pos.BOTTOM_CENTER);
        node.setAlignment(Pos.BOTTOM_CENTER);
        node.setTranslateX(ViewController.SPACING * i + leftIndent);
        node.setTranslateY(topIndent);
        node.setShape(rectangle);
        return node;
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
                case Bubble:
                    initialize(1, customInput);
                    break;
                case CocktailShaker:
                    initialize(6, customInput);
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
                case Counting:
                    initialize(7, customInput);
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
