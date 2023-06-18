package com.battleships.server.api.model;

import org.json.JSONArray;

/**
 * Class representing a board field
 */
public class Field {
    /**
     * Field constructor with x and y cooridinates
     * @param x 
     * @param y
     */
    public Field(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Method returns field String in format:
     * {@code [x,y]}
     */
    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    /**
     * Method returns field in form of JSONArray
     * @return JSONArray containing x and y coordinates
     */
    public JSONArray toJsonArray()
    {
        JSONArray xy = new JSONArray();
        xy.put(x);
        xy.put(y);
        return xy; 
    }

    public final int x, y;
}