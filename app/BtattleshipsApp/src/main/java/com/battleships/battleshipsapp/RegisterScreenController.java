package com.battleships.battleshipsapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.IOException;

/**
 * The controller class for the register screen in the Battleships application.
 */
public class RegisterScreenController {

    private Stage stage;

    /**
     * Constructs a new instance of the RegisterScreenController class.
     */
    public RegisterScreenController() {

    }

    /**
     * Sets the stage for this register screen.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField repeatPasswordField;

    /**
     * Handles the event when the register button is clicked.
     *
     * @param actionEvent the event triggered by the register button click
     * @throws IOException if an error occurs while loading the start_screen2.fxml file
     */
    public void onRegisterButtonClick(ActionEvent actionEvent) throws IOException {
        register(loginField.getText(), passwordField.getText());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("start_screen2.fxml"));
        Parent startScreen = loader.load();
        StartScreen2 startScreen2 = loader.getController();
        startScreen2.setStage(stage);
        stage.setScene(new Scene(startScreen));
    }

    /**
     * Handles the event when the cancel button is clicked.
     *
     * @param actionEvent the event triggered by the cancel button click
     * @throws IOException if an error occurs while loading the start_screen2.fxml file
     */
    public void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start_screen2.fxml"));
        Parent startScreen = loader.load();
        StartScreen2 startScreen2 = loader.getController();
        startScreen2.setStage(stage);
        stage.setScene(new Scene(startScreen));
    }

    private void register(String login, String password) {
        Connection connection = new Connection();
        RequestBody body = connection.registerPlayerBody(login, password, "");

        Object lock = new Object();
        new Thread(() -> {
            try {
                String response = connection.post(Endpoints.REGISTER.getEndpoint(), body);
                JSONObject json = Connection.stringToJson(response);

                System.out.println(json);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                synchronized (lock) {
                    lock.notify();
                }
            }
        }).start();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
