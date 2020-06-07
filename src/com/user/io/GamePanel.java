package com.user.io;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GamePanel extends Application {

    public final TextArea loggingArea;
    private final GridPane _root;
    private final GridPane _gameGrid;
    private final Label _KeyInput;

    public final Button startButton;

    private final int _gameScreenWidth = 18; //10
    private final int _gameScreenHeight = 27; //40

    final JFXPanel fxPanel = new JFXPanel();

    public GamePanel() {
        System.out.println("coucou from constructor (JAVAFX)");
        _root = new GridPane();
        _gameGrid = new GridPane();
        loggingArea = new TextArea("");
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

        loggingArea.setEditable(false);
        loggingArea.setPrefHeight(50);

        // add controls
        startButton = new Button("Start Game");

    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("coucou from start (JAVAFX)");
        ///////SETUP THE STAGE
        primaryStage.setTitle("JAVATetris");

        int _cellSize = 15;
        int gameGridSizeX = _cellSize * _gameScreenWidth;
        int gameGridSizeY = _cellSize * _gameScreenHeight;

        _gameGrid.setPrefSize(gameGridSizeX, gameGridSizeY);
        generateGameGrid(_gameScreenHeight, _gameScreenWidth);

        _root.add(_KeyInput, 0, 0);
        _root.add(loggingArea, 0, 1);
        _root.add(_gameGrid, 0, 2);
        _root.add(startButton, 0, 3);

        Scene scene = new Scene(_root, 350, 550); // width & height
        startButton.requestFocus();

        ///////CONFIGURE UI EVENT HANDLING

//        loggingArea.setOnKeyPressed(keyEvent -> {
//            KeyCode e = keyEvent.getCode();
//            String message = _gameM.getUserInput(e);
//            loggingArea.setText(message);
//        });

//        startButton.setOnAction(actionEvent -> {
//            //animateGameGrid();
//            try {
//                playGame();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            loggingArea.requestFocus();
//        });

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

    public void displayGameMatrix(boolean[][] gameMatrix) {

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
