/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization;


import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sortingvisualization.Sorting.ActionInstance;
import sortingvisualization.Sorting.SortArray;
import sortingvisualization.algorithms.BubbleSort;

/**
 *
 * @author Misha
 */
public class Window extends Application {
    
    private MenuBar menuBar;
    SortArray array;
    List<ActionInstance> actions;
    
    int counter = 0;
    boolean isSelected = false;
    
    public Window(){
        array = new SortArray();
        array.generate(10, 80);
        BubbleSort sortAlgorithm = new BubbleSort();
        sortAlgorithm.sort(array);
        actions = array.actions;
    }
    
    @Override
    public void start(Stage primaryStage) {
        //menu
        
        Menu menuFile = new Menu("_File");
        menuFile.setMnemonicParsing(true);
        
        Menu menuEdit = new Menu("_Edit");
        menuEdit.setMnemonicParsing(true);
        
        Menu menuView = new Menu("_View");
        menuView.setMnemonicParsing(true);
        
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(a -> primaryStage.close());
        menuFile.getItems().add(menuItemExit);
        
        menuBar = new MenuBar();
	menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        
        
        Label algLbl = new Label("Algorithms: ");
        algLbl.getStyleClass().add("blcklabel");    
        
        Label controlLbl = new Label();
        controlLbl.getStyleClass().add("blcklabel");
        
        HBox algList = new HBox();
        
        Button alg1 = new Button("BUB");
        alg1.setTooltip(new Tooltip("Bubble Sort"));
        alg1.getStyleClass().add("button");
        
        Button alg2 = new Button("INS");
        alg2.setTooltip(new Tooltip("Insertion Sort"));
        alg2.getStyleClass().add("button");
        
        Button alg3 = new Button("SEL");
        alg3.setTooltip(new Tooltip("Selection Sort"));
        alg3.getStyleClass().add("button");
        
        Button alg4 = new Button("QUI");
        alg4.setTooltip(new Tooltip("Quick Sort"));
        alg4.getStyleClass().add("button");
        
        Button alg5 = new Button("MRG");
        alg5.setTooltip(new Tooltip("Merge Sort"));
        alg5.getStyleClass().add("button");
        
        Button alg6 = new Button("COU");
        alg6.setTooltip(new Tooltip("Counting Sort"));
        alg6.getStyleClass().add("button");
        
        algList.getChildren().addAll(algLbl, alg1, alg2, alg3, alg4, alg5, alg6);
        algList.setStyle("-fx-background-color: black");
        algList.setMinHeight(40);
        
        HBox controlBox = new HBox();
        
        Image playImg = new Image(getClass().getResourceAsStream("/play.png"));
        ImageView playImgV = new ImageView(playImg);
        playImgV.setFitHeight(25);
        playImgV.setFitWidth(25);
        
        Image pauseImg = new Image(getClass().getResourceAsStream("/pause.png"));
        ImageView pauseImgV = new ImageView(pauseImg);
        pauseImgV.setFitHeight(25);
        pauseImgV.setFitWidth(25);
        
        Button playBtn = new Button();
        playBtn.setTooltip(new Tooltip("Sort!"));
        playBtn.setGraphic(playImgV);
        playBtn.getStyleClass().add("playButton");
        
        
        
        playBtn.setOnAction(new EventHandler<ActionEvent>() {
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
        });
        
        Button rewindBackBtn = new Button();
        Image rewBackImg = new Image(getClass().getResourceAsStream("/rewind_back.png"));
        ImageView rbImgView = new ImageView(rewBackImg);
        rbImgView.setFitHeight(25);
        rbImgView.setFitWidth(25);
        rewindBackBtn.setGraphic(rbImgView);
        rewindBackBtn.getStyleClass().add("playButton");
        rewindBackBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                counter--;
                printInstance(actions, counter);
                
            }
            
        });
        
        
        Button rewindForthBtn = new Button();
        Image rewForthImg = new Image(getClass().getResourceAsStream("/rewind_forth.png"));
        ImageView rfImgView = new ImageView(rewForthImg);
        rfImgView.setFitHeight(25);
        rfImgView.setFitWidth(25);
        rewindForthBtn.setGraphic(rfImgView);
        rewindForthBtn.getStyleClass().add("playButton");
        rewindForthBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                counter++;
                printInstance(actions, counter);
            }
            
        });
        
        controlBox.getChildren().addAll(rewindBackBtn, playBtn, rewindForthBtn);
        controlBox.setStyle("-fx-background-color: black");
        controlBox.setMinHeight(40);
        
        
        //algList.setSpacing(10);
        
        VBox top = new VBox();
        top.getChildren().addAll(menuBar, algList);
        
        //graphic
        GraphicPane visualPane = new GraphicPane();
        
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setBottom(controlBox);
        root.setCenter(visualPane);
        
        
        
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add("style.css");
        primaryStage.setTitle("Sorting Alg Visualisation");
        primaryStage.setScene(scene);
        
        
        visualPane.setMaxSize(root.getWidth()/2, root.getHeight()/2);
        
        //primaryStage.setFullScreen(true); //left for menu
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }
    
    public void testSort(){
        //SortArray array = new SortArray();
        //array.generate(10, 80);
        //System.out.println(array);
        //BubbleSort sortAlgorithm = new BubbleSort();
        //sortAlgorithm.sort(array);
        
        
        for (int i = counter; i < actions.size(); i++, counter++) {
            try{
                printInstance(actions, i);
                Thread.sleep(500);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            if(isSelected == false) break;
        }
        
        //System.out.println(array);
    }
    
    public void printInstance(List<ActionInstance> action, int i){
        int[] temp = action.get(i).get();
                if(temp[0] == 0){
                    System.out.println("array[" + temp[1] + "] and array[" + temp[2] + "] swapped");
                } else if(temp[0] == 1){
                    System.out.println("array[" + temp[1] + "] and array[" + temp[2] + "] compared");
                }
    }

    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
        launch(args);
    }*/
    
    
}
