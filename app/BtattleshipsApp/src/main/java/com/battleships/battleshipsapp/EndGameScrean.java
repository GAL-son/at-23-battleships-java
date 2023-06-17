package com.battleships.battleshipsapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class EndGameScrean {

    Stage stage;
    private Integer win;

    Integer uid;


    @FXML
    Label winnerLabel;

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @FXML
    public void goToMenu() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Parent menuView = loader.load();
        MainMenuController mainMenuController = loader.getController();
        //  mainMenuController.setMode(1);
        //  mainMenuController.setLocalId(9);//w przyszłości to musi byc uid zalogowanego gracza
        mainMenuController.setStage(stage);
        mainMenuController.setUid(this.uid);
        stage.setScene(new Scene(menuView));
        // setShipsController.stageInit(stage);

    }

    public void setWin(Integer a) {
       this.win=a;

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void draw() {
        winnerLabel.setText((this.win==1) ? "wygrałes" : "przegrałeś" );
    }

    public EndGameScrean() {

    }
}
