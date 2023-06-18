package com.battleships.server.api.model;

import org.json.JSONObject;

/**
 * Class representing a move that can be made in game
 */
public class Move {
    
    /**
     * Move constructor
     * @param uid ID of player that made move
     * @param x x coordinate of move
     * @param y y coordinate of move
     */
    public Move(int uid, int x, int y)
    {
        this.move = new Field(x, y);
        this.uid = uid;
    }

    /**
     * Move constructor using Field
     * @param uid ID of player that made move
     * @param move Field object containing x and y cooridnates of move
     */
    public Move(int uid, Field move) {
        this.uid = uid;
        this.move = move;
    }

    /**
     * Method for geting move field
     * @return Field object containing x and y cooridnates of move
     */
    public Field getMove() {
        return move;
    }

    /**
     * Method for geting ID of player that made the move
     * @return ID of player
     */
    public int getUid() {
        return uid;
    }

    /**
     * Method for geting x coordinate of move
     * @return x coordinate
     */
    public int getX() {
        return this.move.x;
    }

    /**
     * Method for geting y coordinate of move
     * @return y coordinate
     */
    public int getY() {
        return this.move.y;
    }

    /**
     * Method that converts Move object to JSONObject
     * @return JSONObject in format:
     * <pre>
     * {"uid": uid, "field": [x, y] }
     * </pre>
     */
    public JSONObject toJsonObject()
    {
        JSONObject obj = new JSONObject();
        obj.put("uid", uid);
        obj.put("field", move.toJsonArray());
        return obj;
    }

    private Field move;
    private int uid;
}
