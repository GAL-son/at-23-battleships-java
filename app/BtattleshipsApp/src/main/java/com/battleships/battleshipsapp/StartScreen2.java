package com.battleships.battleshipsapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The class representing the second version of the start screen in the Battleships application.
 */
public class StartScreen2 {

    private Stage stage;

    /**
     * Constructs a new instance of the StartScreen2 class.
     */
    public StartScreen2() {

    }

    /**
     * Sets the stage for this start screen.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the event when the login button is clicked.
     *
     * @param actionEvent the event triggered by the login button click
     * @throws IOException if an error occurs while loading the login_screen.fxml file
     */
    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login_screen.fxml"));
        Parent startScreen = loader.load();
        LoginScreenController loginScreenController = loader.getController();
        loginScreenController.setStage(stage);
        stage.setScene(new Scene(startScreen));
    }

    /**
     * Handles the event when the play as guest button is clicked.
     *
     * @param actionEvent the event triggered by the play as guest button click
     * @throws IOException if an error occurs while loading the main_menu.fxml file
     */
    public void onPlayAsGuestButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Parent startScreen = loader.load();
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setStage(stage);
        stage.setScene(new Scene(startScreen));
    }
}
