package com.battleships.battleshipsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main class of the Battleships application.
 */
public class App extends Application {

    private static Scene scene;

    /**
     * The entry point of the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Initializes the JavaFX application.
     *
     * @param stage the primary stage for this application
     * @throws IOException if an error occurs while loading the FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("start_screen"), 1000, 800);
        stage.setTitle("Battleships");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the root FXML file for the scene.
     *
     * @param fxml the name of the FXML file (without the extension)
     */
    static void setRoot(String fxml){
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads an FXML file and returns the root element.
     *
     * @param fxml the name of the FXML file (without the extension)
     * @return the root element of the loaded FXML file
     * @throws IOException if an error occurs while loading the FXML file
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Retrieves the scene currently used by the application.
     *
     * @return the current scene
     */
    public static Scene getScene() {
        return scene;
    }
}
