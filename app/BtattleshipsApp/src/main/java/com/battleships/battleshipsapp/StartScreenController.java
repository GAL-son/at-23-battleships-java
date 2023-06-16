package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class StartScreenController {
    @FXML
    protected void onLoginButtonClick() {

    }

    @FXML
    private void onPlayAsGuestButtonClick() {
        App.setRoot("main_menu");
    }
}