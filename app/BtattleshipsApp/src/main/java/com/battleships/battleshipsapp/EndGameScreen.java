package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The EndGameScreen class represents the screen displayed at the end of a game.
 */
public class EndGameScreen {

    Stage stage;
    private Integer win;

    Integer uid;


    @FXML
    Label winnerLabel;

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * Switches the scene to the main menu.
     *
     * @throws IOException if an I/O error occurs while loading the main menu view.
     */
    @FXML
    public void goToMenu() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Parent menuView = loader.load();
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setStage(stage);
        mainMenuController.setUid(this.uid);
        stage.setScene(new Scene(menuView));
    }

    /**
     * Sets the result of the game (win or lose).
     *
     * @param a the result of the game (1 for win, 0 for lose).
     */
    public void setWin(Integer a) {
        this.win=a;

    }

    /**
     * Sets the stage for this EndGameScreen.
     *
     * @param stage the stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Updates the UI to display the result of the game.
     */
    public void draw() {
        winnerLabel.setText((this.win==1) ? "Wygrałeś!" : "Przegrałeś!");
    }

    /**
     * Constructs an instance of the EndGameScreen class.
     */
    public EndGameScreen() {

    }
}
