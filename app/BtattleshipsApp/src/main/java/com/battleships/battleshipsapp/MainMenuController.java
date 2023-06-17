package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private void playSinglePlayer() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_ships.fxml"));
        Parent setingView = loader.load();
        SetShipsController setShipsController = loader.getController();
        setShipsController.setMode(0);

        stage.setScene( new Scene(setingView));
        setShipsController.stageInit(stage);
    }
    @FXML
    private void playMultiPlayer() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_ships.fxml"));
        Parent setingView = loader.load();
        SetShipsController setShipsController = loader.getController();
        setShipsController.setMode(1);
        setShipsController.setLocalId(8);//w przyszłości to musi byc uid zalogowanego gracza

        stage.setScene( new Scene(setingView));
        setShipsController.stageInit(stage);
    }
}
