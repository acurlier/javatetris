package com.user.io;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;


public class UserDisplay extends Application implements EventHandler<KeyEvent>{

    private final TextArea loggingArea = new TextArea("");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        ///////SETUP THE STAGE
        primaryStage.setTitle("JAVATetris");

        GridPane root = new GridPane();
        root.setPadding(new Insets(25, 25, 25, 25));
        root.setHgap(10);
        root.setVgap(10);

        GridPane gameGrid = new GridPane();

        loggingArea.setEditable(false);
        loggingArea.setPrefHeight(50);

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Label KeyInput = new Label("Keyboard Input:");
        TextField KeyOutputPrint = new TextField();

        root.add(KeyInput, 0,0);
        root.add(loggingArea,0,1);

        Scene scene = new Scene(root, 250, 150); // width & height
        loggingArea.requestFocus();

        ///////CONFIGURE UI EVENT HANDLING
        loggingArea.setOnKeyPressed(this);

        ///////DISPLAY UI
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    // method to handle key press
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> loggingArea.setText("UP key press detected");
            case LEFT -> loggingArea.setText("LEFT key press detected");
            case RIGHT -> loggingArea.setText("RIGHT key press detected");
            case DOWN -> loggingArea.setText("DOWN key press detected");
            case SPACE -> loggingArea.setText("SPACE key press detected");
            case CONTROL -> loggingArea.setText("CTRL key press detected");
            default -> loggingArea.setText("Invalid key pressed");
        }

    }
}
