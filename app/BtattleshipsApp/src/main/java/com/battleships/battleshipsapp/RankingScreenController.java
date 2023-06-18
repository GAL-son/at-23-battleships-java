package com.battleships.battleshipsapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class RankingScreenController {
    Stage stage;
    Integer uid;
    ArrayList<JSONObject> rankingInfo = new ArrayList<>();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private VBox leftVBox;

    @FXML
    private VBox rightVBox;



    @FXML
    private void initialize() {
        setUpRankingModels();

        for(int row = 0; row < rankingInfo.size(); row++){
            String nickname = rankingInfo.get(row).getString("login");
            Double score = rankingInfo.get(row).getDouble("gamerScore");

            Label nameLabel = new Label(nickname);
            Label scoreLabel = new Label(score.toString());

            leftVBox.getChildren().add(nameLabel);
            rightVBox.getChildren().add(scoreLabel);
        }
    }

    private void setUpRankingModels() {
        Connection connection = new Connection();
        Object lock = new Object();
        new Thread(() -> {
            try {
                String response = connection.get(Endpoints.RANKING.getEndpoint());
                JSONArray json = Connection.stringToJsonArray(response);

                for (int i = 0; i < json.length(); i++) {
                    rankingInfo.add(json.getJSONObject(i));
                }

                rankingInfo.sort((o1, o2) -> {
                    BigDecimal score1 = BigDecimal.ZERO;
                    BigDecimal score2 = BigDecimal.ZERO;
                    try {
                        score1 = (BigDecimal) o1.get("gamerScore");
                        score2 = (BigDecimal) o2.get("gamerScore");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return score2.compareTo(score1);
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Server unavailable");
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
    @FXML
    private void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Parent mainMenuScreen = loader.load();
        MainMenuController mainMenuController= loader.getController();
        mainMenuController.setUid(uid);
        mainMenuController.setStage(stage);
        stage.setScene( new Scene(mainMenuScreen));
    }

    public void setUid(Integer uid){
        this.uid = uid;
        System.out.println(uid);
    }
}
