package com.battleships.battleshipsapp;

import javafx.fxml.FXML;

public class StartScreenController {
    @FXML
    protected void onLoginButtonClick() {
        App.setRoot("login_screen");
    }

    @FXML
    private void onPlayAsGuestButtonClick() {
        App.setRoot("main_menu");
    }
}