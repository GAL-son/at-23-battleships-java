package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private void logOut(){
        App.setRoot("start_screen");
    }

    @FXML
    private void playSinglePlayer() {
        Stage primaryStage = (Stage) App.getScene().getWindow();
        new SetShipsController(primaryStage);
    }
}
