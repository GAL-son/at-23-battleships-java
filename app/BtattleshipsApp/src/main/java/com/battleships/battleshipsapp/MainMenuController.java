package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    Stage stage;

    public MainMenuController() {
        this.stage = (Stage) App.getScene().getWindow();
    }
    public void setStage(Stage stage) {
        this.stage=stage;
    }

    @FXML
    private void logOut(){
        App.setRoot("start_screen");
    }

    @FXML
    private void playSinglePlayer() {

        new SetShipsController(this.stage);
    }
}
