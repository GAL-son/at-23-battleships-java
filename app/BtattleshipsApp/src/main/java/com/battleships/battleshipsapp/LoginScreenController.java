package com.battleships.battleshipsapp;

import javafx.fxml.FXML;

public class LoginScreenController {

    @FXML
    private void onCancelButtonClick(){
        App.setRoot("start_screen");
    }

}
