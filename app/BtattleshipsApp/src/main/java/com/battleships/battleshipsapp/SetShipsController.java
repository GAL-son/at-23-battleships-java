package com.battleships.battleshipsapp;

import com.battleships.battleshipsapp.model.Game;
import com.battleships.battleshipsapp.model.Move;
import com.battleships.battleshipsapp.model.board.Field;
import com.battleships.battleshipsapp.model.players.PlayerLocal;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SetShipsController {
    private static final int BOARD_SIZE = 10;
    private Stage stage;
    private Integer mode = 0;

    private Integer local_uid;

    Integer alignemnet = 0;
    Game game_this;

    HashMap<String,Button> butonMap =new HashMap<String,Button>();
    HashMap<String,Node> AdditionalELMap =new HashMap<String,Node>();

    public SetShipsController(Stage stage) {
        this.stage = stage;
        initializeUI();

    }
    public SetShipsController() {


    }
    public void stageInit(Stage primaryStage) {
        stage = primaryStage;
        initializeUI();
        stage.show();
    }

    private void changeAlignment() {
        this.alignemnet=(alignemnet+1)%2;
    }

    public void setMode(Integer mode) {
        this.mode=mode;
        System.out.println("wybrano tryb:"+ mode);
    }

    public void setLocalId(Integer uid) {
        this.local_uid=uid;
        System.out.println("polaczony gracz o id : "+ uid);
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

        goBackButton.setOnAction(e-> {
            try {
                goBackToMainMenu(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        playButton.setOnAction(e-> {
            try {
                goToGameScreen(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

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

        label4.setText(String.valueOf(a.get(3)) + "x 4-masztowiec");
        label3.setText(String.valueOf(a.get(2)) + "x 3-masztowiec");
        label2.setText(String.valueOf(a.get(1)) + "x 2-masztowiec");
        label1.setText(String.valueOf(a.get(0)) + "x 1-masztowiec");

    }

    private void createGame() {
        try {
            Game game = new Game(1, this.mode);
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
            game_this.getPlayer1().setId(this.local_uid);//TEMPORARY
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

    private void goBackToMainMenu(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Parent mainMenuView = loader.load();
        MainMenuController mainMenuController = loader.getController();
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(mainMenuView));
        mainMenuController.setStage(stage);
        mainMenuController.setUid(local_uid);
        stage.show();
    }

    private void goToGameScreen(ActionEvent event) throws IOException {


        if (this.game_this.getPlayer1().shipsSizes.isEmpty()) {
            {
               if (this.game_this.getType()==1)
               {
                   System.out.println("lokalne id:"+ this.local_uid);
                   if (joinGame()) {
                       System.out.println("MultiplayerTree: join");
                       System.out.println("loby joined  onClick: ");
                   }else {
                       System.out.println("not joined");
                       return;
                   }
                   while (true) {
                       boolean gameFound = gameFound();
                       System.out.println("MultiplayerTree"+ "game  not yetfound");
                       if (gameFound == true) {
                           System.out.println("MultiplayerTree" + "game found");
                           break;
                       }
                   }
                   if (gameFound() == true) {
                       try {
                           if (!gameSendMap()) {
                               System.out.println("MultiplayerTree"+ "map sent,but failed");
                               return;
                           } else {
                               System.out.println("MultiplayerTree"+ "map sent");
                               while (true) {
                                   boolean gameStarted = getGameStateForIfStarted();
                                   if (gameStarted == true)
                                       break;
                               }
                               TimeUnit.SECONDS.sleep(3);
                               getEnemyBoard();
                           }
                       } catch (JSONException e) {
                           throw new RuntimeException(e);
                       } catch (InterruptedException e) {
                           throw new RuntimeException(e);
                       }

                   }
               }
            }

            //przesłąnie gry

                FXMLLoader loader = new FXMLLoader(getClass().getResource("game_screen.fxml"));
                Parent gameScreenView = loader.load();
                GameScreenController gameScreenController = loader.getController();
                gameScreenController.setGame(game_this);
                stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                stage.setScene( new Scene(gameScreenView));
                gameScreenController.stageInit(stage);

        }
    }

    private Boolean getEnemyBoard() {
        Connection conn = new Connection();
        Map<String, String> map = new HashMap<String, String>() {{
            put("uid", String.valueOf(game_this.getPlayer1().getId()));
        }};
        Object lock = new Object();
        new Thread(() -> {
            try {
                String response = conn.get(Endpoints.GAME_START.getEndpoint(), map);
                System.out.println("the response on geting state of enemy "+ response);
                JSONObject json = Connection.stringToJson(response);


                System.out.println("the response"+ response);
                if (json.has("status")) {
                    System.out.println("status:"+ response);
                } else {
                    setEnemyFromState(json);
                }


            } catch (JSONException e) {
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
        return true;

    }

    private void setEnemyFromState(JSONObject json) {
        ArrayList<Integer> shipAtrib = null;
        JSONArray jsonArr = json.getJSONArray("ships");
        JSONObject jsonInterior;
        JSONArray jsonArrInterior;

        Boolean pom1;

        for (int n = 0; n < jsonArr.length(); n++) {
            System.out.println("adding enemy ships atribs "+ "ships nr" + (n + 1));
            shipAtrib = new ArrayList<Integer>();
            jsonInterior = (JSONObject) jsonArr.get(n);
            shipAtrib.add(jsonInterior.getInt("size"));

            jsonArrInterior = (JSONArray) jsonInterior.get("fieldxy");
            shipAtrib.add(jsonArrInterior.getInt(0));
            shipAtrib.add(jsonArrInterior.getInt(1));

            pom1 = jsonInterior.getBoolean("vertical");
            shipAtrib.add((pom1) ? 1 : 0);

            game_this.getPlayer2().shipsCoordsAndAlignment.add(shipAtrib);
            System.out.println("addedenemy ships atribs"+ "ships nr" + (n + 1));
        }
        try {
            setEnemyShipsFromState();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setEnemyShipsFromState() throws Exception {
        if (game_this.getPlayer2().shipsCoordsAndAlignment.size() != 10) {
            System.out.println("adding ships to fields" +  "statków mniej niz 10 ");
            throw new RuntimeException();
        }
        System.out.println("adding ships to fields "+ "statków jest  10 ");
        for (ArrayList<Integer> a : game_this.getPlayer2().shipsCoordsAndAlignment) {
            System.out.println("adding ships to fields "+ "ships created");
            game_this.place_ship(new Move(a.get(1), a.get(2), 0), 2, a.get(3));
        }
    }

    private boolean getGameStateForIfStarted() {

        Connection conn = new Connection();


        Map<String, String> map = new HashMap<String, String>() {{
            put("uid", String.valueOf(game_this.getPlayer1().getId()));
        }};

        AtomicBoolean gameStarted = new AtomicBoolean(false);
        Object lock = new Object();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                String response = conn.get(Endpoints.GAME_STATE.getEndpoint(), map);
                System.out.println("TheResponseGameState "+ response);
                JSONObject json = Connection.stringToJson(response);


                if (!json.has("isStarted")) {
                    System.out.println("status "+ response);
                } else {
                    if ((boolean) json.get("isStarted"))
                        gameStarted.set(true);
                }


            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
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
        return gameStarted.get();
    }

    private boolean gameSendMap() {
        String reqBodyShips = createSetBody();

        Connection conn = new Connection();
        RequestBody body = conn.setGameBody(game_this.getPlayer1().getId(), reqBodyShips);
        AtomicBoolean shipsSet = new AtomicBoolean(false);
        Object lock = new Object();

        new Thread(() -> {
            try {
                String response = conn.post(Endpoints.GAME_SET.getEndpoint(), body);
                System.out.println("the response "+ response);
                // JSONObject json = Connection.stringToJson(response);

                // Log.i("the response", response);
                if (!response.equals("false")) {

                    System.out.println("status "+ response);
                } else {

                    shipsSet.set(true);
                }


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
        return shipsSet.get();
    }

    private String createSetBody() {
        JSONObject jsonParent = new JSONObject();
        JSONArray ships = new JSONArray();
        JSONObject jsonInterior;
        JSONArray shipxy;
        for (ArrayList<Integer> a : ((PlayerLocal) game_this.getPlayer1()).shipsCoordsAndAlignment) {
            System.out.println("sendingStateCheck1 "+ "size:" + a.get(0) + " \nx:" + a.get(1) + " y:" + a.get(2) + "\n vertical:" + ((a.get(3) == 0) ? "false" : "true"));
            jsonInterior = new JSONObject();
            shipxy = new JSONArray();
            jsonInterior.put("size", a.get(0));
            shipxy.put(a.get(1));
            shipxy.put(a.get(2));
            jsonInterior.put("fieldxy", shipxy);
            jsonInterior.put("vertical", ((a.get(3) == 0) ? false : true));
            ships.put(jsonInterior);


        }
        jsonParent.put("ships", ships);
        String reqBoody = jsonParent.toString();
       System.out.println("sendingStateCheck2: request body:"+  reqBoody);
        return reqBoody;
    }

    private boolean gameFound() {
        Connection conn2 = new Connection();
        RequestBody body = conn2.searchingGameBody(game_this.getPlayer1().getId());
        AtomicBoolean gameFound = new AtomicBoolean(false);
        Object lock = new Object();
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                String response = conn2.post(Endpoints.GAME_QUEUE.getEndpoint(), body);
                //  JSONObject json = Connection.stringToJson(response);
                System.out.println("queue response"+ response);
                if (response.equals("false")) {
                    //String status = json.getString("status");
                    System.out.println("no user of this id logged in"+ response);
                } else {
                    if (response.equals("true")) {
                        gameFound.set(true);
                    } else {
                        System.out.println("game not found yet");
                    }
                }


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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

        return gameFound.get();
    }

    private boolean joinGame() {
        Connection conn = new Connection();
        RequestBody body = conn.searchingGameBody(game_this.getPlayer1().getId());
        AtomicBoolean lobbyJoined = new AtomicBoolean(false);

        Object lock = new Object();
        new Thread(() -> {
            try {
                String response = conn.post(Endpoints.GAME_JOIN.getEndpoint(), body);
                System.out.println("the response "+ response);
                // JSONObject json = Connection.stringToJson(response);

                // Log.i("the response", response);
                if (!response.equals("true")) {

                    System.out.println("status "+response);
                } else {
                    lobbyJoined.set(true);
                }


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

        return lobbyJoined.get();

    }

}
