package com.battleships.battleshipsapp;

import javafx.fxml.FXML;

import java.io.IOException;

public class MainMenuController {

    @FXML

    private void logOut() throws IOException {
        App.setRoot("start_screen");
    }
}
