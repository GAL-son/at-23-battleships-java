package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class StartScreenController {
    @FXML
    private void onLoginButtonClick() {
        App.setRoot("login_screen");
    }
    @FXML
    private void onPlayAsGuestButtonClick() {
        App.setRoot("main_menu");
    }
}