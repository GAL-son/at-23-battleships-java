package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginScreenController {

    private String uid;
    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label incorrectPasswordLabel;
    @FXML
    private void onLoginButtonClick(){
        boolean isLogged = login(loginField.getText(), passwordField.getText());

        if(isLogged){
            App.setRoot("main_menu");
        }else{
            incorrectPasswordLabel.setVisible(true);
        }
    }


    @FXML
    private void onCancelButtonClick(){
        App.setRoot("start_screen");
    }

    private boolean login(String login, String password){
        Connection connection = new Connection();
        RequestBody body = connection.playerRequestBody(login, password);
        AtomicBoolean isAuthorized = new AtomicBoolean(false);
        Object lock = new Object();
        new Thread(() -> {
            try{
                String response = connection.post(Endpoints.LOGIN.getEndpoint(),body);
                JSONObject json = Connection.stringToJson(response);

                System.out.println(json);
                if(json.has("status")){
                    String status = json.getString("status");
                    System.out.println("status"+status);
                }else{
                    isAuthorized.set(true);
                }

                setUid(String.valueOf(json.get("uid")));

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

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
}
