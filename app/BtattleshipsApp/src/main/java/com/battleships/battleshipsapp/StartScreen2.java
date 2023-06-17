package com.battleships.battleshipsapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartScreen2 {

    Stage stage;

    public StartScreen2(){

    }

    public void setStage(Stage stage) {
        this.stage=stage;
    }
    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login_screen.fxml"));
        Parent startScreen = loader.load();
        LoginScreenController loginScreenController = loader.getController();
        loginScreenController.setStage(stage);
        stage.setScene( new Scene(startScreen));
    }

    public void onPlayAsGuestButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Parent startScreen = loader.load();
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setStage(stage);
        stage.setScene( new Scene(startScreen));
    }
}
