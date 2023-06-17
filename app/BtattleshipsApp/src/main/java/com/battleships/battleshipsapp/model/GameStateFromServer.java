package com.battleships.battleshipsapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents the game state received from the server.
 */
public class GameStateFromServer {


    private int turnid;
    private int gid;

    private int oponentScore;
    private String oponentLogin;
    private int playerScore;
    private String playerLogin;
    private Integer lastx=null;
    private Integer lasty=null;

    private int lastShootingUserID;


    /**
     * Returns the ID of the last shooting user.
     *
     * @return The ID of the last shooting user.
     */
    public int getLastShootingUserID() {
        return lastShootingUserID;
    }



    public void setPlayerLogin(String playerLogin) {
        this.playerLogin = playerLogin;
    }

    public void setLastx(int lastx) {
        this.lastx = lastx;
    }

    public void setLasty(int lasty) {
        this.lasty = lasty;
    }

    public String getPlayerLogin() {
        return playerLogin;
    }

    /**
     * Returns the  X coordinate of the  last move made .
     *
     * @return the  X coordinate of the  last move made.
     */
    public Integer getLastx() {
        return lastx;
    }
    /**
     * Returns the  Y coordinate of the  last move made .
     *
     * @return the  Y coordinate of the  last move made.
     */
    public Integer getLasty() {
        return lasty;
    }

    private boolean isStarted;
    private boolean isFinished;

    /**
     * Returns the ID of player that has turn at the moment.
     *
     * @return The turn ID.
     */
    public int getTurnid() {
        return turnid;
    }


    /**
     * Returns the ID of the game, as on server.
     *
     * @return The game ID on server.
     */
    public int getGid() {
        return gid;
    }

    /**
     * Returns the opponent's score.
     *
     * @return The opponent's score.
     */
    public int getOponentScore() {
        return oponentScore;
    }
    /**
     * Returns the opponent's login name.
     *
     * @return The opponent's login name.
     */
    public String getOponentLogin() {
        return oponentLogin;
    }

    /**
     * Returns  local's player's score.
     *
     * @return local's player's score.
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Returns the player's login name.
     *
     * @return The player's login name.
     */
    public String playerLogin() {
        return playerLogin;
    }

    /**
     * Returns whether the game has started.
     *
     * @return true if the game has started, false otherwise.
     */
    public boolean isStarted() {
        return isStarted;
    }
    /**
     * Returns whether the game has finished.
     *
     * @return true if the game has finished, false otherwise.
     */
    public boolean isFinished() {
        return isFinished;
    }




    /**
     * Sets the opponent's score.
     *
     * @param oponentScore The opponent's score to set.
     */
    public void setOponentScore(int oponentScore) {
        this.oponentScore = oponentScore;
    }
    /**
     * Sets the opponent's score.
     *
     * @param oponentLogin The opponent's score to set.
     */
    public void setOponentLogin(String oponentLogin) {
        this.oponentLogin = oponentLogin;
    }

    /**
     * Sets the  local players's score.
     *
     * @param playerScore The opponent's score to set.
     */
    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    /**
     * Sets boolean value saying if game is started
     *
     * @param started true, or false depending on what
     */
    public void setStarted(boolean started) {
        isStarted = started;
    }
    /**
     * Sets boolean value saying if game is Finished
     *
     * @param finished true if game is Finished, false if not
     */
    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    /**
     * Sets id  of the lats shooting player
     *
     * @param lastShootingUserID Id  of player
     */
    public void setLastShootingUserID(int lastShootingUserID) {
        this.lastShootingUserID = lastShootingUserID;
    }

    /**
     *  Method to parse state of an  object to String in a readable format
     * @return String representing state of the object
     */
    @Override
    public String toString() {
        return "GameStateFromServer{" +
                "turnid=" + turnid +
                ", gid=" + gid +
                ", oponentScore=" + oponentScore +
                ", oponentLogin='" + oponentLogin + '\'' +
                ", playerScore=" + playerScore +
                ", playerLogin='" + playerLogin + '\'' +
                ", lastx=" + lastx +
                ", lasty=" + lasty +
                ", lastShootingUserID=" + lastShootingUserID +
                ", isStarted=" + isStarted +
                ", isFinished=" + isFinished +
                '}';
    }

    /**
     * Empty Constructior  for creating empty Objects
     */
    public GameStateFromServer() {

    }

    /**
     * Static Function to Read data writen in Json Format and  to set Objects atributes to coresponding values, returns  object of  a type {@code  GameDateFromServer}
     * @param state JSONObject with the staate of a game, recived from server
     * @return   object of a type {@code  GameDateFromServer}, containing state of a game
     * @throws JSONException
     */
    static public GameStateFromServer getState(JSONObject state) throws JSONException {

        GameStateFromServer tmp1 = new GameStateFromServer();

        tmp1.turnid = state.getInt("turnId");
        tmp1.gid = state.getInt("gid");
        JSONObject enemy = (JSONObject) state.get("opponent");
        JSONObject you = (JSONObject) state.get("opponent");

        tmp1.oponentLogin = enemy.getString("login");
        tmp1.oponentScore = enemy.getInt("score");

        tmp1.playerLogin = you.getString("login");
        tmp1.playerScore = you.getInt("score");

        if(state.has("lastMove")) {

            JSONObject jsoonInner = state.getJSONObject("lastMove");
            JSONArray movexy = jsoonInner.getJSONArray("field");

            tmp1.lastx = movexy.getInt(0);
            tmp1.lasty = movexy.getInt(1);
            tmp1.lastShootingUserID = jsoonInner.getInt("uid");
        }


        return tmp1;
    }
}
