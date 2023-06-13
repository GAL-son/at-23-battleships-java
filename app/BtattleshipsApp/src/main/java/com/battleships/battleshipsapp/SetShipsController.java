package com.battleships.battleshipsapp;

import com.battleships.battleshipsapp.model.Game;
import com.battleships.battleshipsapp.model.Move;
import com.battleships.battleshipsapp.model.board.Field;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SetShipsController {
    private static final int BOARD_SIZE = 10;
    private Stage stage;

    Integer alignemnet = 0;
    Game game_this;

    HashMap<String,Button> butonMap =new HashMap<String,Button>();
    HashMap<String,Node> AdditionalELMap =new HashMap<String,Node>();

    public SetShipsController(Stage stage) {
        this.stage = stage;
        initializeUI();


    }
    private void changeAlignment() {
        this.alignemnet=(alignemnet+1)%2;
    }


    private void initializeUI() {

       createGame();
        Button clearButton = new Button("Clear Board");
        Button turnButton = new Button("Turn Ship");

        turnButton.setOnAction(e-> changeAlignment());
        clearButton.setOnAction(e->clearBoard());

        Label label1 = new Label("Liczba statków 4x: 1");
        Label label2 = new Label("Liczba statków 3x: 2");
        Label label3 = new Label("Liczba statków 2x: 3");
        Label label4 = new Label("Liczba statków 1x: 4");

        {//dodanie labelków do mapy
            this.AdditionalELMap.put("shipCount4",label1);
            this.AdditionalELMap.put("shipCount3",label2);
            this.AdditionalELMap.put("shipCount2",label3);
            this.AdditionalELMap.put("shipCount1",label4);
        }


        GridPane gridPane = createBoard();

        Button goBackButton = new Button("Go Back");
        Button playButton = new Button("Play");
        
        {////dodanie buttonów do mapy
            this.AdditionalELMap.put("playButton",playButton);
            this.AdditionalELMap.put("goBackButton",goBackButton);

        }

        // Ustawienie preferowanego rozmiaru przycisków
        clearButton.setPrefWidth(100);
        turnButton.setPrefWidth(100);
        goBackButton.setPrefWidth(100);
        playButton.setPrefWidth(100);

        VBox labelsBox = new VBox(label1, label2, label3, label4);
        labelsBox.setAlignment(Pos.CENTER);
        HBox topButtonsBox = new HBox(clearButton, turnButton);
        topButtonsBox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(topButtonsBox, gridPane);
        vbox.setAlignment(Pos.CENTER);
        HBox bottomButtonsBox = new HBox(goBackButton, playButton);
        bottomButtonsBox.setAlignment(Pos.CENTER);

        VBox gridContainer = new VBox(gridPane);
        gridContainer.setAlignment(Pos.CENTER);

        VBox root = new VBox(topButtonsBox, labelsBox, gridContainer, bottomButtonsBox);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 1000, 800); // Ustawienie wymiarów sceny
        drawBoardPlacing();
        stage.setScene(scene);
        stage.show();
    }

    private void clearBoard() {

        for (int i = 0; i < 10; i++) {
            for (int x = 0; x < 10; x++) {
               System.out.println("czyszczenie:"+ "usuwanie referencji");
                ((Field) (game_this.getPlayer1().getPlayerBard().fields.get(i).get(x))).oocupyingShip = null;
                System.out.println("efekt suawaniareferencji:"+ "wynik:" + ((Field) (game_this.getPlayer1().getPlayerBard().fields.get(i).get(x))).isOccupied());
            }
        }
        game_this.getPlayer1().shipsSizes.clear();
        game_this.getPlayer1().ships.clear();
        for (int i = 1; i < 5; i++) {
            for (int x = i - 1; x < 4; x++) {
                System.out.println("czyszczenie:"+ "dodano rozmiar" + i);
                game_this.getPlayer1().shipsSizes.add(i);
            }
        }
        drawBoardPlacing();
        updateCountShips();//chaneg
    }

    private void updateCountShips() {
        ArrayList<Integer> a = Game.histogram(game_this.getPlayer1().shipsSizes);
        a = Game.histogram(game_this.getPlayer1().shipsSizes);

        Label label4=(Label)this.AdditionalELMap.get("shipCount4");
        Label label3=(Label)this.AdditionalELMap.get("shipCount3");
        Label label2=(Label)this.AdditionalELMap.get("shipCount2");
        Label label1=(Label)this.AdditionalELMap.get("shipCount1");

        label4.setText(String.valueOf(a.get(3)) + "x lotniskowiec");
        label3.setText(String.valueOf(a.get(2)) + "x 3 masztowiec");
        label2.setText(String.valueOf(a.get(1)) + "x łódź podwodna");
        label1.setText(String.valueOf(a.get(0)) + "x P O N T O N");

    }

    private void createGame() {
        try {
            Game game = new Game(1, 0);
            game_this = game;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (game_this.getType() == 0) {
            try {
                vsAiProcedure();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            //setLocalPlayerID();
            game_this.getPlayer1().setId(2);//TEMPORARY
        }
    }

    private void vsAiProcedure() throws Exception {

        game_this.place_ship(new Move(0, 0, 0), 2, 1);

        Random random = new Random();
        int x, y, align;
        while (game_this.getPlayer2().shipsSizes.size() > 0) {
            x = random.nextInt(10);
            y = random.nextInt(10);
            align = random.nextInt(10);
            game_this.place_ship(new Move(x, y, 0), 2, align%2);
        }
    }

    private void drawBoardPlacing() {
        Button button;
        System.out.println("board drawn");
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                button = this.butonMap.get(String.valueOf(x)+String.valueOf(y));
                if (((Field) (game_this.getPlayer1().getPlayerBard().fields.get(y).get(x))).isOccupied()) {

                    button.setStyle(
                                    "-fx-background-color: " + toHex(Color.RED) + ";"
                             +      "-fx-border-color: " + toHex(Color.BLACK) + ";"
                    );

                } else {
                    button.setStyle(
                                            "-fx-background-color: " + toHex(Color.WHITE) + ";"
                                    +       "-fx-border-color: " + toHex(Color.BLACK) + ";"
                    );

                }
            }
        }//koniec petli rysującej grida
        Label label4=(Label) this.AdditionalELMap.get("shipCount4");
        Label label3=(Label) this.AdditionalELMap.get("shipCount3");
        Label label2=(Label) this.AdditionalELMap.get("shipCount2");
        Label label1=(Label) this.AdditionalELMap.get("shipCount1");
        label4.setText("Pierdol");
        label3.setText("sie");
        label2.setText("rozuimiesz");
        label1.setText("?");

        //ustawienie ilosci statków do postawienia
        updateCountShips();

    }

    private GridPane createBoard() {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(50, 50); // Ustawienie preferowanego rozmiaru przycisku
                button.setId(row+","+col); // Ustawienie ID przycisku
                button.setOnAction(e -> handleButtonClick(button)); // Przypisanie zdarzenia obsługującego
                gridPane.add(button, col, row);
                this.butonMap.put(String.valueOf(row)+String.valueOf(col),button);
            }
        }

        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private void handleButtonClick(Button button) {
        String id = getButtonId(button);
        System.out.println("Clicked button ID: " + id);
        String x,y;
        y=id.substring(0,1);
        x=id.substring(2,3);
        System.out.println("x" +x);
        System.out.println("y" +y);

        try {
            game_this.place_ship(new Move(Integer.parseInt(x), Integer.parseInt(y), 0), 1, this.alignemnet);
            drawBoardPlacing();
           // updateCountShips();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        drawBoardPlacing();
    }

    private String toHex(Color color) {
        return "#" + color.toString().substring(2, 8);
    }

    private String getButtonId(Button button) {
        String id = button.getId();
        if (id != null && !id.isEmpty()) {
            return id;
        }
        return null;
    }
}
