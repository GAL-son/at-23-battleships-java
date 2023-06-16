package com.battleships.battleshipsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("start_screen"), 1000, 800);
        stage.setTitle("Battleships");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml){
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Scene getScene() {
        return scene;
    }
    public static void main(String[] args) {
        launch();
    }
}
