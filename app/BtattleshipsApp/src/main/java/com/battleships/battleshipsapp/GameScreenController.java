package com.battleships.battleshipsapp;

import com.battleships.battleshipsapp.model.Game;
import com.battleships.battleshipsapp.model.Move;
import com.battleships.battleshipsapp.model.board.Field;
import com.battleships.battleshipsapp.model.players.PlayerAi;
import com.battleships.battleshipsapp.model.ship.Ship;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class GameScreenController {
    private static final int BOARD_SIZE = 10;
    private Stage stage;
    Game game;

    HashMap<String, Button> butonMapPlayer1 = new HashMap<String, Button>();
    HashMap<String, Button> butonMapPlayer2 = new HashMap<String, Button>();

    HashMap<String, Node> labelsPlayer1 = new HashMap<String, Node>();
    HashMap<String, Node> labelsPlayer2 = new HashMap<String, Node>();
    HashMap<String, Node> others= new HashMap<String, Node>();

    public GameScreenController(Stage primaryStage) {
        stage = primaryStage;
        initializeUI();
        stage.show();
    }

    public void stageInit(Stage primaryStage) {
        stage = primaryStage;
        initializeUI();
        stage.show();
    }

    public GameScreenController() {
        // System.out.println(this.game.getPlayer1().getId());
    }

    public void setGame(Game game_) {
        this.game = game_;
        //  System.out.println(this.game.getPlayer1().getId());
    }


    public void setStage(Stage stage) {
        this.stage = stage;
        initializeUI();
    }

    public void initializeUI() {

        System.out.println(this.game.getPlayer1().getId());
        // Top Label
        Label turnLabel = new Label("Tura: 1");
        turnLabel.setStyle("-fx-font-size: 20;");
        HBox topBox = new HBox(turnLabel);
        topBox.setAlignment(Pos.CENTER);

        this.others.put("turn",turnLabel);


        // Player Ships Labels
        Label playerShipsLabel1 = new Label("Liczba statków 4x: 1");
        Label playerShipsLabel2 = new Label("Liczba statków 3x: 2");
        Label playerShipsLabel3 = new Label("Liczba statków 2x: 3");
        Label playerShipsLabel4 = new Label("Liczba statków 1x: 4");

        {
            this.labelsPlayer1.put("shipCount4", playerShipsLabel1);
            this.labelsPlayer1.put("shipCount3", playerShipsLabel2);
            this.labelsPlayer1.put("shipCount2", playerShipsLabel3);
            this.labelsPlayer1.put("shipCount1", playerShipsLabel4);
        }

        VBox playerShipsLabels = new VBox(playerShipsLabel1, playerShipsLabel2, playerShipsLabel3, playerShipsLabel4);
        playerShipsLabels.setAlignment(Pos.CENTER);

        // Your Board Label
        Label yourBoardLabel = new Label("Your Board");
        yourBoardLabel.setAlignment(Pos.CENTER);
        // Your Board GridPane
        GridPane yourBoardGridPane = createBoard(1);

        // Enemy Ships Labels
        Label enemyShipsLabel1 = new Label("Liczba statków 4x: 1");
        Label enemyShipsLabel2 = new Label("Liczba statków 3x: 2");
        Label enemyShipsLabel3 = new Label("Liczba statków 2x: 3");
        Label enemyShipsLabel4 = new Label("Liczba statków 1x: 4");

        {
            this.labelsPlayer2.put("shipCount4", enemyShipsLabel1);
            this.labelsPlayer2.put("shipCount3", enemyShipsLabel2);
            this.labelsPlayer2.put("shipCount2", enemyShipsLabel3);
            this.labelsPlayer2.put("shipCount1", enemyShipsLabel4);
        }

        VBox enemyShipsLabels = new VBox(enemyShipsLabel1, enemyShipsLabel2, enemyShipsLabel3, enemyShipsLabel4);
        enemyShipsLabels.setAlignment(Pos.CENTER);
        // Enemy Board Label
        Label enemyBoardLabel = new Label("Enemy Board");

        // Enemy Board GridPane
        GridPane enemyBoardGridPane = createBoard(2);

        // Left Box
        VBox leftBox = new VBox(50, playerShipsLabels, yourBoardLabel, yourBoardGridPane);
        leftBox.setSpacing(10);

        // Right Box
        VBox rightBox = new VBox(50, enemyShipsLabels, enemyBoardLabel, enemyBoardGridPane);
        rightBox.setSpacing(10);

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBox);
        mainLayout.setLeft(leftBox);
        mainLayout.setRight(rightBox);

        // Set scene
        Scene scene = new Scene(mainLayout, 1050, 800);
        stage.setScene(scene);
        drawBoardPlacing();
    }

    private GridPane createBoard(int p) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(50, 50); // Ustawienie preferowanego rozmiaru przycisku
                button.setId(row + "," + col); // Ustawienie ID przycisku
                button.setOnAction(e -> handleButtonClick(button)); // Przypisanie zdarzenia obsługującego
                gridPane.add(button, col, row);

                if (p == 1) {
                    this.butonMapPlayer1.put(String.valueOf(row) + String.valueOf(col), button);
                }
                if (p == 2) {
                    this.butonMapPlayer2.put(String.valueOf(row) + String.valueOf(col), button);
                }
            }
        }

        return gridPane;
    }


    private void handleButtonClick(Button button) {
        String id = getButtonId(button);
        System.out.println("Clicked button ID: " + id);
        String x, y;
        y = id.substring(0, 1);
        x = id.substring(2, 3);
        System.out.println("x" + x);
        System.out.println("y" + y);

        if (this.game.getType()==0)//is singleplayer
        {
            if(  ((Field)(game.getPlayer2().getPlayerBard().fields.get(Integer.valueOf(x)).get(Integer.valueOf(y)))).getWasHit() != true && this.game.getState()<2  /*&& game.getTurn() % 2 == 0*/)
            {
                try {
                    hitingProcedure(new Move(Integer.valueOf(x),Integer.valueOf(y), 0), 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                ArrayList<Integer> AImove = ((PlayerAi) (game.getPlayer2())).getAImove(game.getPlayer1().getPlayerBard());
                //Log.i("ai", "pozyskano koordynaty");
                if (((Field) (game.getPlayer1().getPlayerBard().fields.get(AImove.get(0)).get(AImove.get(1)))).getWasHit() != true && game.getState() < 2) {
                    try {
                        hitingProcedure(new Move(AImove.get(0), AImove.get(1), 0), 2);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    //Log.i("aha", "ai oddalo strzal w to smao miejsce");
                }

            }else {
                if (game.getState() == 2) {
                    System.out.println("game ended");
                    Label turn = (Label) this.others.get("turn");
                    turn.setText("game ended");
                }
            }





        }

        drawBoardPlacing();
    }

    private void hitingProcedure(Move move, int player) {

        if (game.getType() == 1) {
            if (player == 1)
                game.getPlayer1().setMoove_token(false);
            if (player == 2)
                game.getPlayer1().setMoove_token(true);
        }

        countHP();
        game.hitField(move, player);
        countHP();
        game.nextTurn();
        Label turn = (Label) this.others.get("turn");
        turn.setText("tura: "+this.game.getTurn());




    }

    private void countHP() {
        Integer pom1 = 0, pom2 = 0;
        for (Ship s : game.getPlayer1().ships) {
            pom1 += s.getHealth();
        }
        for (Ship s : game.getPlayer2().ships) {
            pom2 += s.getHealth();
        }
        System.out.println("countHP: " + "hp p1= " + pom1 + " p2 = " + pom2);
        if (game.getType() == 0) {
            if (pom1 == 0) {
                game.winner = game.getPlayer2().getId();
                GameEndProcedure(game.winner);
            }
            if (pom2 == 0) {
                game.winner = game.getPlayer1().getId();
                GameEndProcedure(game.winner);
            }
        } else if (game.getType() == 1) {

            System.out.println("countHP: " + "p1:" + pom1 + " p2:" + pom2 + "czy gra skonczona?: " + game.gameStateFromServer.isFinished());
            //  if (game.gameStateFromServer.isFinished()==true)
            {

                System.out.println("countHP: " + "p1:" + pom1 + " p2:" + pom2);
                if (pom1 == 0) {
                    game.winner = game.getPlayer2().getId();
                    GameEndProcedure(game.winner);
                }
                if (pom2 == 0) {
                    game.winner = game.getPlayer1().getId();
                    GameEndProcedure(game.winner);
                }
            }

        }
    }

    private void GameEndProcedure(int winner) {
        game.setState(2);
        if (game.winner == game.getPlayer1().getId())
            System.out.println("gre wygrał gracz: " + 1 + "zajeło mu to " + game.getTurnFull());

        if (game.winner == game.getPlayer2().getId())
            System.out.println("gre wygrał gracz: " + 2 + "zajeło mu to " + game.getTurnFull());


        // Log.i("koniec", "gre wygrał gracz: " + game.winner + "zajeło mu to " + game.getTurnFull());

    }

    private String getButtonId(Button button) {
        String id = button.getId();
        if (id != null && !id.isEmpty()) {
            return id;
        }
        return null;
    }

    private String toHex(Color color) {
        return "#" + color.toString().substring(2, 8);
    }


    private void drawBoardPlacing() {
        Button button;
        System.out.println("board drawn");
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                button = this.butonMapPlayer1.get(String.valueOf(x) + String.valueOf(y));
                if (((Field) (game.getPlayer1().getPlayerBard().fields.get(y).get(x))).isOccupied()) {
                    button.setStyle("-fx-background-color: " + toHex(Color.GREEN) + ";" + "-fx-border-color: " + toHex(Color.BLACK) + ";");
                    if(  ((Field) (game.getPlayer1().getPlayerBard().fields.get(y).get(x))).getWasHit())
                    {
                        button.setStyle("-fx-background-color: " + toHex(Color.RED) + ";" + "-fx-border-color: " + toHex(Color.BLACK) + ";");
                    }
                } else {
                    button.setStyle("-fx-background-color: " + toHex(Color.WHITE) + ";" + "-fx-border-color: " + toHex(Color.BLACK) + ";");
                    if(  ((Field) (game.getPlayer1().getPlayerBard().fields.get(y).get(x))).getWasHit())
                    {
                        button.setStyle("-fx-background-color: " + toHex(Color.GRAY) + ";" + "-fx-border-color: " + toHex(Color.BLACK) + ";");
                    }
                }
            }
        }

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                button = this.butonMapPlayer2.get(String.valueOf(x) + String.valueOf(y));
                if (((Field) (game.getPlayer2().getPlayerBard().fields.get(y).get(x))).getWasHit()) {

                    if (((Field) (game.getPlayer2().getPlayerBard().fields.get(y).get(x))).isOccupied()) {
                        button.setStyle("-fx-background-color: " + toHex(Color.RED) + ";" + "-fx-border-color: " + toHex(Color.BLACK) + ";");
                    }
                    else
                    {
                        button.setStyle("-fx-background-color: " + toHex(Color.GRAY) + ";" + "-fx-border-color: " + toHex(Color.BLACK) + ";");
                    }
                } else {
                    button.setStyle("-fx-background-color: " + toHex(Color.WHITE) + ";" + "-fx-border-color: " + toHex(Color.BLACK) + ";");
                }
//                if (((Field) (game.getPlayer2().getPlayerBard().fields.get(y).get(x))).isOccupied()) {                                                //DEBUG
//                    button.setStyle("-fx-background-color: " + toHex(Color.RED) + ";" + "-fx-border-color: " + toHex(Color.BLACK) + ";");             //DEBUG
//                } else {                                                                                                                              //DEBUG
//                    button.setStyle("-fx-background-color: " + toHex(Color.WHITE) + ";" + "-fx-border-color: " + toHex(Color.BLACK) + ";");           //DEBUG
//                }                                                                                                                                     //DEBUG

            }
        }

        //ustawienie ilosci statków do postawienia
        Label turn =(Label) others.get("turn");
        turn.setText("tura:"+ String.valueOf(this.game.getTurnFull()));
        updateCountShips();

    }

    private void updateCountShips() {
        ArrayList<Integer> p1 = Game.histogramInGame(game.getPlayer1().ships);
        ArrayList<Integer> p2 = Game.histogramInGame(game.getPlayer2().ships);


        {
            Label label4 = (Label) this.labelsPlayer1.get("shipCount4");
            Label label3 = (Label) this.labelsPlayer1.get("shipCount3");
            Label label2 = (Label) this.labelsPlayer1.get("shipCount2");
            Label label1 = (Label) this.labelsPlayer1.get("shipCount1");

            label4.setText(String.valueOf(p1.get(3)) + "x 4-masztowiec");
            label3.setText(String.valueOf(p1.get(2)) + "x 3-masztowiec");
            label2.setText(String.valueOf(p1.get(1)) + "x 2-masztowiec");
            label1.setText(String.valueOf(p1.get(0)) + "x 1-masztowiec");
        }

        {
            Label label4 = (Label) this.labelsPlayer2.get("shipCount4");
            Label label3 = (Label) this.labelsPlayer2.get("shipCount3");
            Label label2 = (Label) this.labelsPlayer2.get("shipCount2");
            Label label1 = (Label) this.labelsPlayer2.get("shipCount1");

            label4.setText(String.valueOf(p2.get(3)) + "x 4-masztowiec");
            label3.setText(String.valueOf(p2.get(2)) + "x 3-masztowiec");
            label2.setText(String.valueOf(p2.get(1)) + "x 2-masztowiec");
            label1.setText(String.valueOf(p2.get(0)) + "x 1-masztowiec");
        }
    }
}


