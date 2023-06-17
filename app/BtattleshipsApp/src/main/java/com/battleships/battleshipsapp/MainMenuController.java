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
    Integer uid = null;

    public MainMenuController() {
        this.stage = (Stage) App.getScene().getWindow();
    }
    public void setStage(Stage stage) {
        this.stage=stage;
    }

    @FXML
    private void logOut() throws IOException {
       // App.setRoot("start_screen");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start_screen.fxml"));
        Parent startScreen = loader.load();
        StartScreenController startScreenController = loader.getController();

        stage.setScene( new Scene(startScreen));
    }

    @FXML
    private void playSinglePlayer() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_ships.fxml"));
        Parent setingView = loader.load();
        SetShipsController setShipsController = loader.getController();
        setShipsController.setMode(0);
        setShipsController.setLocalId(0);

        stage.setScene( new Scene(setingView));
        setShipsController.stageInit(stage);
    }
    @FXML
    private void playMultiPlayer() throws IOException {
        if(uid!=null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("set_ships.fxml"));
            Parent setingView = loader.load();
            SetShipsController setShipsController = loader.getController();
            setShipsController.setMode(1);
            setShipsController.setLocalId(uid);//w przyszłości to musi byc uid zalogowanego gracza

            stage.setScene( new Scene(setingView));
            setShipsController.stageInit(stage);
        }else{
            return;
        }

    }

    public void setUid(Integer uid){
        this.uid = uid;
        System.out.println(uid);
    }
}
