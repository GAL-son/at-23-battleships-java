package com.battleships.battleshipsapp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SetShipsController {
    private static final int BOARD_SIZE = 10;
    private Stage stage;

    public SetShipsController(Stage stage) {
        this.stage = stage;
        initializeUI();
    }

    private void initializeUI() {
        Button clearButton = new Button("Clear Board");
        Button turnButton = new Button("Turn Ship");

        Label label1 = new Label("Liczba statków 4x: 1");
        Label label2 = new Label("Liczba statków 3x: 2");
        Label label3 = new Label("Liczba statków 2x: 3");
        Label label4 = new Label("Liczba statków 1x: 4");

        GridPane gridPane = createBoard();

        Button goBackButton = new Button("Go Back");
        Button playButton = new Button("Play");

        // Ustawienie preferowanego rozmiaru przycisków
        clearButton.setPrefWidth(100);
        turnButton.setPrefWidth(100);
        goBackButton.setPrefWidth(100);
        playButton.setPrefWidth(100);

        VBox labelsBox = new VBox(label1, label2, label3, label4);
        labelsBox.setAlignment(Pos.CENTER);
        HBox topButtonsBox = new HBox(clearButton, turnButton);
        topButtonsBox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(topButtonsBox, gridPane);
        vbox.setAlignment(Pos.CENTER);
        HBox bottomButtonsBox = new HBox(goBackButton, playButton);
        bottomButtonsBox.setAlignment(Pos.CENTER);

        VBox gridContainer = new VBox(gridPane);
        gridContainer.setAlignment(Pos.CENTER);

        VBox root = new VBox(topButtonsBox, labelsBox, gridContainer, bottomButtonsBox);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 1000, 800); // Ustawienie wymiarów sceny
        stage.setScene(scene);
        stage.show();
    }

    private GridPane createBoard() {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(50, 50); // Ustawienie preferowanego rozmiaru przycisku
                button.setId(row+","+col); // Ustawienie ID przycisku
                button.setOnAction(e -> handleButtonClick(button)); // Przypisanie zdarzenia obsługującego
                gridPane.add(button, col, row);
            }
        }

        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private void handleButtonClick(Button button) {
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
}
