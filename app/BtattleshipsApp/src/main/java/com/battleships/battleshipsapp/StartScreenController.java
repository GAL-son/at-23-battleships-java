package com.battleships.battleshipsapp;

import javafx.fxml.FXML;

/**
 * The controller class for the start screen of the Battleships application.
 */
public class StartScreenController {

    /**
     * Handles the event when the login button is clicked.
     * Sets the root FXML file to "login_screen".
     */
    @FXML
    protected void onLoginButtonClick() {
        App.setRoot("login_screen");
    }

    /**
     * Handles the event when the play as guest button is clicked.
     * Sets the root FXML file to "main_menu".
     */
    @FXML
    private void onPlayAsGuestButtonClick() {
        App.setRoot("main_menu");
    }
}
