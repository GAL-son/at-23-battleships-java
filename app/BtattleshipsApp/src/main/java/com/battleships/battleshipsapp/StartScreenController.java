package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import java.io.IOException;

public class StartScreenController {

    @FXML
    protected void onLoginButtonClick() {

    }

    @FXML
    private void onPlayAsGuestButtonClick() throws IOException {
        App.setRoot("main_menu");
    }
}