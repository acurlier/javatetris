package com.user.io;

import com.game.compute.GameManager;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GamePanel extends Application {

    private final TextArea _loggingArea;
    private final GridPane _root;
    private final GridPane _gameGrid;
    private final Label _KeyInput;
    private TextField _KeyOutputPrint;

    private final Button _startButton;

    private final int _gameScreenWidth = 18;
    private final int _gameScreenHeight = 27;

    private GameManager _gameM;

    //public static void main(String[] args) {
    // launch(args);
    // }

    public GamePanel() {

        _root = new GridPane();
        _gameGrid = new GridPane();
        _loggingArea = new TextArea("");
        _KeyInput = new Label("Keyboard Input:");

        Text sceneTitle = new Text("MainScene");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        //configure the main window grid
        _root.setPadding(new Insets(25, 25, 25, 25));
        _root.setHgap(10);
        _root.setVgap(10);

        //configure the game grid
        _gameGrid.setPadding(new Insets(5, 5, 5, 5));
        _gameGrid.setHgap(0);
        _gameGrid.setVgap(0);

        _loggingArea.setEditable(false);
        _loggingArea.setPrefHeight(50);

        // add controls
        _startButton = new Button("Click me");

        //game manager
        _gameM = new GameManager(_gameScreenWidth, _gameScreenHeight, 0.5f);
    }

    @Override
    public void start(Stage primaryStage) {

        ///////SETUP THE STAGE
        primaryStage.setTitle("JAVATetris");

        int _cellSize = 15;
        int gameGridSizeX = _cellSize * _gameScreenWidth;
        int gameGridSizeY = _cellSize * _gameScreenHeight;

        _gameGrid.setPrefSize(gameGridSizeX, gameGridSizeY);
        generateGameGrid(_gameScreenHeight, _gameScreenWidth);

        _root.add(_KeyInput, 0, 0);
        _root.add(_loggingArea, 0, 1);
        _root.add(_gameGrid, 0, 2);
        _root.add(_startButton, 0, 3);

        Scene scene = new Scene(_root, 350, 550); // width & height
        _startButton.requestFocus();

        ///////CONFIGURE UI EVENT HANDLING

        _loggingArea.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case UP -> {
                    _loggingArea.setText("UP key press detected");
                }
                case LEFT -> {
                    _loggingArea.setText("LEFT key press detected");
                    _gameM.moveLateralCurrentBlock("LEFT");
                }
                case RIGHT -> {
                    _loggingArea.setText("RIGHT key press detected");
                    _gameM.moveLateralCurrentBlock("RIGHT");
                }
                case DOWN -> {
                    _loggingArea.setText("DOWN key press detected");
                    _gameM.moveDownCurrentBlock();
                }
                case SPACE -> _loggingArea.setText("SPACE key press detected");
                case CONTROL -> {
                    _loggingArea.setText("CTRL key press detected");
                    _gameM.rotateCurrentBlock();

                }
                default -> _loggingArea.setText("Invalid key pressed");
            }
            displayGameMatrix();
        });

        _startButton.setOnAction(actionEvent -> {
            //animateGameGrid();
            playGame();
            _loggingArea.requestFocus();
        });

        ///////DISPLAY UI
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void generateGameGrid(int w, int h) {

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {

                Rectangle tile = new Rectangle(15, 15);
                tile.setFill(Color.WHITE);
                // tile.setStroke(Color.BLACK);
                GridPane.setRowIndex(tile, i);
                GridPane.setColumnIndex(tile, j);
                _gameGrid.getChildren().addAll(tile);

            }
        }
    }

    private void playGame() {
        System.out.println("playGame started");
        boolean lose = false;
        _gameM.instantiateCurrentBlock();
        displayGameMatrix();

//        while (!lose){
//
//            boolean[][] gameMatrix = _gameM.updateGameMatrix();
//            displayGameMatrix(gameMatrix);
//            lose = _gameM.getGameStatus();
//        }
    }

    private void displayGameMatrix() {

        boolean[][] gameMatrix = _gameM.getGameMatrices();

        ObservableList<Node> tileList = _gameGrid.getChildren();
        for (int i = 0; i < _gameScreenHeight; i++) {
            for (int j = 0; j < _gameScreenWidth; j++) {

                for (Node node : tileList) {
                    if ((node instanceof Rectangle) && (GridPane.getRowIndex(node) == i &&
                            GridPane.getColumnIndex(node) == j)) {
                        if (gameMatrix[i][j]) { // +2 to cope with the left free gap of the game matrix
                            ((Rectangle) node).setFill(Color.BLACK);
                        } else {
                            ((Rectangle) node).setFill(Color.WHITE);
                        }
                    }
                }
            }
        }
        System.out.println("matrix display updated");
    }

    private void animateGameGrid() {
        SequentialTransition preGameAnimation = new SequentialTransition();

        SequentialTransition sequenceBlink1 = generateAnimationList(Color.WHITE, Color.BLACK);
        SequentialTransition sequenceBlink2 = generateAnimationList(Color.BLACK, Color.WHITE);

        preGameAnimation.getChildren().add(sequenceBlink1);
        preGameAnimation.getChildren().add(sequenceBlink2);

        preGameAnimation.playFromStart();
    }

    private SequentialTransition generateAnimationList(Color color1, Color color2) {
        SequentialTransition sequenceBlink = new SequentialTransition();
        sequenceBlink.setDelay(Duration.millis(0.05f));
        ObservableList<Node> tileList = _gameGrid.getChildren();
        for (int i = 0; i < _gameScreenHeight; i++) {
            for (int j = 0; j < _gameScreenWidth; j++) {

                for (Node node : tileList) {
                    if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j) {
                        if (node instanceof Rectangle) {
                            FillTransition ft = new FillTransition(Duration.millis(10), ((Rectangle) node),
                                    color1, color2);
                            sequenceBlink.getChildren().add(ft);

                        }
                    }
                }
            }
        }
        return sequenceBlink;
    }
}