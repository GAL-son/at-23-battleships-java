package com.battleships.server.api.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.battleships.server.api.Exceptions.InvalidFieldException;
import com.battleships.server.api.Exceptions.PlayerNotInGameExeption;

/**
 * Class representing game state
 */
public class Game {
    private int gameId;

    // Game data
    private int turn; // 0 - P1, 1 - P2
    private int turnNum = 0;
    private int boardSize;
    private boolean gameStarted;
    private boolean gameFinished;

    // Player 1 data
    private User player1;
    private List<Ship> p1Ships;
    private Ship[][] p1Board; 
    private int p1FieldsAlive;
    private String p1ShipSetup;
    private float p1Score;
    
    // Player 2 data
    private User player2;
    private List<Ship> p2Ships;
    private Ship[][] p2Board;
    private int p2FieldsAlive;
    private String p2ShipSetup;
    private float p2Score;

    /** Move History */ 
    private List<Move> history;
        
    /**
     * Game constructor.
     * Initializes variables and lists
     * @param gid new game ID
     * @param boardSize size of the board
     */
    public Game(int gid, int boardSize) {
        this.gameId = gid;
        this.boardSize = boardSize;

        gameStarted = false;
        gameFinished = false;

        // Generate random statring turn
        Random rand = new Random();
        turn = rand.nextInt(2);

        // Generate boards of given size
        p1Board = new Ship[boardSize][boardSize];
        p2Board = new Ship[boardSize][boardSize];

        // Set relevant references to null
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++) {
                p1Board[x][y] = null;
                p2Board[x][y] = null;
            }
        }

        // Create ship lists
        p1Ships = new LinkedList<>();
        p2Ships = new LinkedList<>();
       
        // No players at the begining
        player1 = null;
        player2 = null;

        // Set number of ships
        p1FieldsAlive = 0;
        p2FieldsAlive = 0;

        // Set match score
        p1Score = 0;
        p2Score = 0;

        // Initiate move history
        history = new LinkedList<>();
    }

    /**
     * Get game ID
     * @return game ID
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Get game turn - information which player can now make a move
     * @return 0 or 1 depending which player can now move
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Return player 1 reference
     * @return User who is player 1
     */
    public User getPlayer1() {
        return player1;
    }

    /**
     * Return player 2 reference
     * @return User who is player 2
     */
    public User getPlayer2() {
        return player2;
    }

    /**
     * Method used to check if the game has started
     * @return {@code true} id game has started, {@code false} otherwise
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Get number of turns that the game has
     * @return number of turns
     */
    public int getTurnNum() {
        return turnNum;
    }

    /**
     * Returns given player ship setup based on the uid
     * @param uid uid of player that setup we want to get
     * @return {@link com.battleships.server.api.controller.MainController#setShips(int, String) Json formatted string} containing ship placement
     */
    public String getPlayerSetup(int uid) {
        int player = getPlayerFromPid(uid);

        if(player == 0) {
            return p1ShipSetup;
        } else {
            return p2ShipSetup;
        }
    }

    /**
     * Used to set player setup variable
     * @param uid ID of player whose setup we want to save
     * @param setup {@link com.battleships.server.api.controller.MainController#setShips(int, String) Json formatted string} contiaining setup
     */
    public void setPlayerSetup(int uid, String setup) {
        int player = getPlayerFromPid(uid);

        if(player == 0) {
            p1ShipSetup = setup;
        } else {
            p2ShipSetup = setup;
        }
    }

    /**
     * Returns last move from move history
     * @return {@link com.battleships.server.api.model.Move Move} that was most recently made
     */
    public Move getLastMove()
    {
        return (history.isEmpty()) ? null : history.get(history.size()-1);
    }

    // Game Functions
    /**
     * Used to add given user int game
     * @param user {@link com.battleships.server.api.model.User User} that we want to add into game
     * @throws Exception when game is full
     */
    public void playerJoin(User user) throws Exception {
        if(gameStarted || gameFinished) throw new Exception("Game in progress");

        if(player1 == null) {
            player1 = user;
        } else if(player2 == null) {
            player2 = user;
        } else {
            throw new Exception("GAME FULL");
        }
    }

    /**
     * Method used for checking if given user is in this game
     * @param user {@link com.battleships.server.api.model.User User} that we want to check against
     * @return {@code true} if player is in this game, {@code false} otherwise
     */
    public boolean isPlayerInGame(User user) {
        return (user.getUid() == player1.getUid() || user.getUid() == player2.getUid()); 
    }

    /**
     * Method used to place one ship into the board
     * @param pid ID of Player that places the ship
     * @param shipSize new ship size
     * @param shipFields List of {@link com.battleships.server.api.model.Field Fields} that contain given ship
     * @throws Exception when ship is invalid size
     */
    public void setShip(int pid, int shipSize, List<Field> shipFields) throws Exception {
        if(!(shipSize > 0 && shipSize < 5) || shipFields.size() != shipSize) {
            // TODO: Create invalid ship size exception
            throw new Exception("INVALID SHIP SIZE");
        }

        Ship ship = new Ship(shipSize);
        int player = getPlayerFromPid(pid);        

        // TODO: Create fully functional field checking
        if(player == 0){
            p1Ships.add(ship);
            p1FieldsAlive += shipSize;
            for(Field f : shipFields) {
                if(!isFieldCorrect(f) && p1Board[f.x][f.y] == null) throw new Exception("INVALID FIELD");
                p1Board[f.x][f.y] = ship;
            }
        }

        if(player == 1){
            p2Ships.add(ship);
            p2FieldsAlive += shipSize;
            for(Field f : shipFields) {
                if(!isFieldCorrect(f) && p2Board[f.x][f.y] == null) throw new Exception("INVALID FIELD");
                p2Board[f.x][f.y] = ship;
            } 
        }

        if(p1Ships.size() == 10 && p2Ships.size() == 10) gameStarted = true;
    }

    /**
     * Method used for making a move in game
     * @param move {@link com.battleships.server.api.model.Move Move} that is made in game
     * @return {@code true} if given move was effective (ship was hit), {@code false} otherwise
     */
    public Boolean makeMove(Move move){
        if(!gameStarted || gameFinished) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not started or is finished");
        if(!isFieldCorrect(move.getMove())) throw new InvalidFieldException();

        int player = getPlayerFromPid(move.getUid());

        // true - hit, false - missed
        boolean moveResult = false;
        history.add(move);

        if(player == 0) {
            Ship ship = p2Board[move.getX()][move.getY()] ;
            if(moveResult = (ship != null)) {
                /* DEBUG */ System.out.println("P1 SHIP HIT");
                ship.hit();
                p1Score += 1;
                p2FieldsAlive--;
                if(ship.isSunk()) {
                    /* DEBUG */ System.out.println("P2 SHIP SUNK");
                    p1Score += 5;
                }
            } // else 
            nextTurn();
        } else {
            Ship ship = p1Board[move.getX()][move.getY()];
            if(moveResult = (ship != null)) {
                /* DEBUG */ System.out.println("P2 SHIP HIT");
                ship.hit();
                p1FieldsAlive--;
                p2Score += 1;
                if(ship.isSunk()) {
                    /* DEBUG */ System.out.println("P1 SHIP SUNK");
                    
                    p2Score += 5;
                }
            } // else
            nextTurn();
        }

        turnNum++;
        /* DEBUG */ System.out.println("TURN" + turnNum);
        /* DEBUG */ System.out.println("Move: " + move.toJsonObject() + moveResult);
        /* DEBUG */ System.out.println("SHIPS ALIVE, P-" + player1.getUid() + ":" + p1FieldsAlive);
        /* DEBUG */ System.out.println("SHIPS ALIVE, P-" + player2.getUid() + ":" + p2FieldsAlive);


        gameFinished = isGameOver();

        return moveResult;
    }

    /**
     * Method used for checkong if game has ended
     * @return {@code true} if game has ended, {@code false} otherwise
     */
    public boolean isGameOver() {
        return (gameStarted && (p1FieldsAlive <= 0 || p2FieldsAlive <= 0));
    }

    /**
     * Method used for building current game state
     * @param pid ID of user from whose perspective we want to build a game state
     * @return {@link com.battleships.server.api.controller.MainController#getGameState(int) JSON formatted string} containing game state
     */
    public JSONObject getGameStateUpdate(int pid) {
        JSONObject gameState = new JSONObject();
        gameState.put("gid", this.getGameId());
        User player;
        User opponent;

        // Resplve perspective
        if(pid == player1.getUid()) {
            player = player1;
            opponent = player2;
        } else {
            player = player2;
            opponent = player1;
        }

        // Build gamestate JSON
        JSONObject user = new JSONObject();
        user.put("login", player.getLogin());
        user.put("score", player.getGamerScore());
        gameState.put("player", user);

        JSONObject enemy = new JSONObject();
        enemy.put("login",opponent.getLogin());
        enemy.put("score", opponent.getGamerScore());
        gameState.put("opponent", enemy);
        gameState.put("turnId", getTurnPid());
        gameState.put("isStarted", gameStarted);
        gameState.put("isFinished", gameFinished);
        gameState.put("lastMove", (getLastMove() != null) ?  this.getLastMove().toJsonObject() : null);        

        return gameState;
    }

    /**
     * Returns opponent ID
     * @param pid ID of player whose opponent we want to find in this game
     * @return ID of opponent of user with given ID
     */
    public int getOpponentPid(int pid) {
        if(pid == player1.getUid()) return player2.getUid();
        if(pid == player2.getUid()) return player1.getUid();
        throw new PlayerNotInGameExeption();
    }

    public int getPlayerPid(int pid) {
        if(pid == player1.getUid()) return player1.getUid();
        if(pid == player2.getUid()) return player2.getUid();
        throw new PlayerNotInGameExeption();
    }

    /**
     * Method that moves next turn
     */
    public void nextTurn()
    {
        turn = (++turn)%2;
    }

    /**
     * Method returns score of player with given ID
     * @param uid ID of player whose score we want to get
     * @return Game score 
     */
    public float getPlayerScore(int uid) {
        if(getPlayerFromPid(uid) == 0) return p1Score;
        else return p2Score;
    }

    /**
     * Method used for geting winner ID
     * @return ID of player that won
     */
    public int getWinnerUid() {
        if(!gameFinished)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GAME NOT FINISHED");

        if(p1FieldsAlive <= 0) return player2.getUid();
        else return player1.getUid();
    }

    /**
     * Method used for giving up a game
     * @param uid ID of player that wants to give up
     */
    public void giveUp(int uid) {
        int player = getPlayerFromPid(uid);

        if(player == 0) {
            p1FieldsAlive = 0;
        } else {
            p2FieldsAlive = 0;
        }

    }

    /* DEBUG */ public void printP1Board()
    {
        for(int y = 0; y < boardSize; y++)
        {
            for(int x = 0; x < boardSize; x++)
            {
                System.out.print((p1Board[x][y] == null) ? "_|" : "X|");
            }
            System.out.println();
        }
        System.out.println();

    }

    /* DEBUG */ public void printP2Board()
    {
        for(int y = 0; y < boardSize; y++)
        {
            for(int x = 0; x < boardSize; x++)
            {
                System.out.print((p2Board[x][y] == null) ? "_|" : "X|");
            }
            System.out.println();
        }
        System.out.println();
    }


    // Helper functions
    /**
     * Converts user ID into local player number (0 or 1)
     * @param pid ID of player that we want to convert
     * @return (0 or 1)
     * @throws PlayerNotInGameException when player with given ID in neither of the players in Game
     */
    private int getPlayerFromPid(int pid) throws PlayerNotInGameExeption{
        if(pid == player1.getUid()) return 0;
        if(pid == player2.getUid()) return 1;
        throw new PlayerNotInGameExeption();
    }
    
    /**
     * Checks if given field is contains within board limits
     * @param field {@link com.battleships.server.api.model.Field Field} that we want to check
     * @return {@code true} if field is within limits, {@code false} otherwise
     */
    private Boolean isFieldCorrect(Field field){
        return ((field.x >= 0 && field.x < boardSize) && (field.y >= 0 && field.y < boardSize));
    }

    /**
     * Converts current turn into player ID that can make a move
     * @return player ID that can make a move
     */
    private int getTurnPid()
    {
        if(turn == 0) return player1.getUid();
        else return player2.getUid();
    }
}