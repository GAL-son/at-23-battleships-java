package com.battleships.battleshipsapp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameScreenController {
    private static final int BOARD_SIZE = 10;
    private Stage stage;

    public GameScreenController(Stage primaryStage) {
        stage = primaryStage;
        initializeUI();
        stage.show();
    }
    

    private void initializeUI() {
        // Top Label
        Label turnLabel = new Label("Tura: 1");
        turnLabel.setStyle("-fx-font-size: 20;");
        HBox topBox = new HBox(turnLabel);
        topBox.setAlignment(Pos.CENTER);


        // Player Ships Labels
        Label playerShipsLabel1 = new Label("Liczba statków 4x: 1");
        Label playerShipsLabel2 = new Label("Liczba statków 3x: 2");
        Label playerShipsLabel3 = new Label("Liczba statków 2x: 3");
        Label playerShipsLabel4 = new Label("Liczba statków 1x: 4");
        VBox playerShipsLabels = new VBox(playerShipsLabel1, playerShipsLabel2, playerShipsLabel3, playerShipsLabel4);
        playerShipsLabels.setAlignment(Pos.CENTER);

        // Your Board Label
        Label yourBoardLabel = new Label("Your Board");
        yourBoardLabel.setAlignment(Pos.CENTER);
        // Your Board GridPane
        GridPane yourBoardGridPane = createBoard();

        // Enemy Ships Labels
        Label enemyShipsLabel1 = new Label("Liczba statków 4x: 1");
        Label enemyShipsLabel2 = new Label("Liczba statków 3x: 2");
        Label enemyShipsLabel3 = new Label("Liczba statków 2x: 3");
        Label enemyShipsLabel4 = new Label("Liczba statków 1x: 4");
        VBox enemyShipsLabels = new VBox(enemyShipsLabel1, enemyShipsLabel2, enemyShipsLabel3, enemyShipsLabel4);
        enemyShipsLabels.setAlignment(Pos.CENTER);
        // Enemy Board Label
        Label enemyBoardLabel = new Label("Enemy Board");

        // Enemy Board GridPane
        GridPane enemyBoardGridPane = createBoard();

        // Left Box
        VBox leftBox = new VBox(50, playerShipsLabels, yourBoardLabel, yourBoardGridPane);
        leftBox.setSpacing(10);

        // Right Box
        VBox rightBox = new VBox(50, enemyShipsLabels, enemyBoardLabel, enemyBoardGridPane);
        rightBox.setSpacing(10);

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBox);
        mainLayout.setLeft(leftBox);
        mainLayout.setRight(rightBox);

        // Set scene
        Scene scene = new Scene(mainLayout, 1050, 800);
        stage.setScene(scene);
    }

    private GridPane createBoard() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(50, 50); // Ustawienie preferowanego rozmiaru przycisku
                button.setId(row+","+col); // Ustawienie ID przycisku
                button.setOnAction(e -> handleButtonClick(button)); // Przypisanie zdarzenia obsługującego
                gridPane.add(button, col, row);
            }
        }

        return gridPane;
    }

    private void handleButtonClick(Button button){
        String id = getButtonId(button);
        System.out.println("Clicked button ID: " + id);
    }

    private String getButtonId(Button button) {
        String id = button.getId();
        if (id != null && !id.isEmpty()) {
            return id;
        }
        return null;
    }

    private String toHex(Color color) {
        return "#" + color.toString().substring(2, 8);
    }
}
