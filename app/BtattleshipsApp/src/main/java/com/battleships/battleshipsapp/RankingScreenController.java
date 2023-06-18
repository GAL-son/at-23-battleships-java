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

/**
 * The controller class for the ranking screen in the Battleships application.
 */
public class RankingScreenController {

    private Stage stage;
    private Integer uid;
    private ArrayList<JSONObject> rankingInfo = new ArrayList<>();

    /**
     * Sets the stage for this ranking screen.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private VBox leftVBox;

    @FXML
    private VBox rightVBox;

    /**
     * Initializes the ranking screen.
     * Sets up the ranking models and populates the ranking table.
     */
    @FXML
    private void initialize() {
        setUpRankingModels();

        for (int row = 0; row < rankingInfo.size(); row++) {
            String nickname = rankingInfo.get(row).getString("login");
            Double score = rankingInfo.get(row).getDouble("gamerScore");

            Label nameLabel = new Label(nickname);
            Label scoreLabel = new Label(score.toString());

            leftVBox.getChildren().add(nameLabel);
            rightVBox.getChildren().add(scoreLabel);
        }
    }

    /**
     * Sets up the ranking models by fetching data from the server.
     */
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

    /**
     * Handles the event when the back button is clicked.
     * Navigates back to the main menu screen.
     *
     * @throws IOException if an error occurs while loading the main_menu.fxml file
     */
    @FXML
    private void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Parent mainMenuScreen = loader.load();
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setUid(uid);
        mainMenuController.setStage(stage);
        stage.setScene(new Scene(mainMenuScreen));
    }

    /**
     * Sets the user ID for this ranking screen.
     *
     * @param uid the user ID to set
     */
    public void setUid(Integer uid) {
        this.uid = uid;
        System.out.println(uid);
    }
}
