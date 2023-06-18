package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller class for the main menu screen in the Battleships application.
 */
public class MainMenuController {

    private Stage stage;
    private Integer uid;

    /**
     * Constructs a new instance of the MainMenuController class.
     * Initializes the stage with the current window stage.
     */
    public MainMenuController() {
        this.stage = (Stage) App.getScene().getWindow();
    }

    /**
     * Sets the stage for this main menu screen.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the event when the log out button is clicked.
     * Navigates back to the start screen.
     *
     * @throws IOException if an error occurs while loading the start_screen2.fxml file
     */
    @FXML
    private void logOut() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start_screen2.fxml"));
        Parent startScreen = loader.load();
        StartScreen2 startScreen2 = loader.getController();
        startScreen2.setStage(stage);
        stage.setScene(new Scene(startScreen));
    }

    /**
     * Handles the event when the single player button is clicked.
     * Navigates to the ship setting screen for single player mode.
     *
     * @throws IOException if an error occurs while loading the set_ships.fxml file
     */
    @FXML
    private void playSinglePlayer() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("set_ships.fxml"));
        Parent setingView = loader.load();
        SetShipsController setShipsController = loader.getController();
        setShipsController.setMode(0);
        setShipsController.setLocalId(uid);

        stage.setScene(new Scene(setingView));
        setShipsController.stageInit(stage);
    }

    /**
     * Handles the event when the multiplayer button is clicked.
     * Navigates to the ship setting screen for multiplayer mode.
     *
     * @throws IOException if an error occurs while loading the set_ships.fxml file
     */
    @FXML
    private void playMultiPlayer() throws IOException {
        if (uid != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("set_ships.fxml"));
            Parent setingView = loader.load();
            SetShipsController setShipsController = loader.getController();
            setShipsController.setMode(1);
            setShipsController.setLocalId(uid);

            stage.setScene(new Scene(setingView));
            setShipsController.stageInit(stage);
        } else {
            return;
        }
    }

    /**
     * Handles the event when the ranking button is clicked.
     * Navigates to the ranking screen.
     *
     * @throws IOException if an error occurs while loading the ranking_screen.fxml file
     */
    @FXML
    private void showRanking() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ranking_screen.fxml"));
        Parent rankingScreen = loader.load();
        RankingScreenController rankingScreenController = loader.getController();
        rankingScreenController.setUid(uid);
        rankingScreenController.setStage(stage);
        stage.setScene(new Scene(rankingScreen));
    }

    /**
     * Sets the user ID for this main menu screen.
     *
     * @param uid the user ID to set
     */
    public void setUid(Integer uid) {
        this.uid = uid;
        System.out.println(uid);
    }
}
