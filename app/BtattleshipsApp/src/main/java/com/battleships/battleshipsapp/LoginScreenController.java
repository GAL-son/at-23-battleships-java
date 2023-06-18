package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The controller class for the login screen in the Battleships application.
 */
public class LoginScreenController {
    private Stage stage;
    private Integer uid;

    /**
     * Constructs a new instance of the LoginScreenController class.
     * Initializes the stage with the current window stage.
     */
    public LoginScreenController() {
        this.stage = (Stage) App.getScene().getWindow();
    }

    /**
     * Sets the stage for this login screen.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label incorrectPasswordLabel;

    /**
     * Handles the event when the login button is clicked.
     * Attempts to log in with the provided credentials.
     * If successful, navigates to the main menu screen.
     */
    @FXML
    private void onLoginButtonClick() {
        boolean isLogged = login(loginField.getText(), passwordField.getText());

        if (isLogged) {
            try {
                goToMainMenu();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            incorrectPasswordLabel.setVisible(true);
        }
    }

    /**
     * Handles the event when the cancel button is clicked.
     * Navigates back to the start screen.
     *
     * @throws IOException if an error occurs while loading the start_screen2.fxml file
     */
    @FXML
    private void onCancelButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start_screen2.fxml"));
        Parent startScreen = loader.load();
        StartScreen2 startScreen2 = loader.getController();
        startScreen2.setStage(stage);
        stage.setScene(new Scene(startScreen));
    }

    /**
     * Handles the event when the register button is clicked.
     * Navigates to the register screen.
     *
     * @throws IOException if an error occurs while loading the register_screen.fxml file
     */
    @FXML
    private void onRegisterButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register_screen.fxml"));
        Parent registerScreen = loader.load();
        RegisterScreenController registerScreenController = loader.getController();
        registerScreenController.setStage(stage);
        stage.setScene(new Scene(registerScreen));
    }

    private boolean login(String login, String password) {
        Connection connection = new Connection();
        RequestBody body = connection.playerRequestBody(login, password);
        AtomicBoolean isAuthorized = new AtomicBoolean(false);
        Object lock = new Object();
        new Thread(() -> {
            try {
                String response = connection.post(Endpoints.LOGIN.getEndpoint(), body);
                JSONObject json = Connection.stringToJson(response);

                System.out.println(json);
                if (json.has("status")) {
                    String status = json.getString("status");
                    System.out.println("status" + status);
                } else {
                    isAuthorized.set(true);
                }

                setUid((Integer) json.get("uid"));

            } catch (IOException e) {
                throw new RuntimeException(e);
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

        return isAuthorized.get();
    }

    /**
     * Sets the uid (user id) for this login session.
     *
     * @param uid the uid to set
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    private void goToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Parent mainMenuView = loader.load();
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setUid(uid);
        mainMenuController.setStage(stage);
        stage.setScene(new Scene(mainMenuView));
    }
}
