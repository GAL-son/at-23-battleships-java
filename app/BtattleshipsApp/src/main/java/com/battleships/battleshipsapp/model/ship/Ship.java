/**
 * The Ship class represents a ship in the game.
 * It implements Serializable to allow object transfer
 */
package com.battleships.battleshipsapp.model.ship;

import com.battleships.battleshipsapp.model.Move;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Ship class represents a ship in the game.
 * It holds information about the ship's size, health, type, and fields it occupies.
 * It provides methods to manipulate and retrieve ship information.
 */
public class Ship implements Serializable {
    private int size;
    private int health;
    private String type;//??
    public ArrayList<ArrayList<Integer>> pola=new ArrayList<>();

    /**
     * Constructs a new Ship object with the specified size.
     * health and type are automaticly set
     *
     * @param size the size of the ship.
     */
    public Ship(int size) {
        this.size = size;
        this.health = size;
        this.type = resolveType(size);
    }

    /**
     * Adds a new field to the ship's positions based on the provided move.
     * @param move the move containing the position coordinates.
     */
    public void addField(Move move)
    {

       ArrayList<Integer> tmpArr= new ArrayList<>();
        tmpArr.add(move.positionX);
        tmpArr.add(move.positionY);
       // Log.i("pole dodane","pole dodane");
       pola.add(tmpArr);

    }

    /**
     * Resolves the ship type based on its size.
     *
     * @param size the size of the ship.
     * @return the type of the ship as a String.
     */
    private String resolveType(int size) {
        String name;
        switch (size) {
            case 1:
                name = "kuter torpedowy";
                break;
            case 2:
                name = "łódź podwodna";
                break;
            case 3:
                name = "ciężki krążownik";
                break;
            case 4:
                name = "lotniskowiec";
                break;
            default:
                name = "???";
        }
        return name;
    }

    /**
     * Hits the ship, reducing its health by one.
     */
    public void hitShip() {
        this.health--;
        //
    }

    /**
     * Checks if the ship is sunk (health is zero).
     * @return true if the ship is sunk, false otherwise.
     */
    private boolean isSunk()
    {
        if(this.health==0)
        {
            return true;
        }
        else
        {
            return  false;

        }
    }
    /**
     * Retrieves the size of the ship.
     *
     * @return the size of the ship.
     */
    public int getSize() {
        return size;
    }

    /**
     * Retrieves the current health of the ship.
     *
     * @return the health of the ship.
     */
    public int getHealth() {
        return health;
    }


    /**
     * Retrieves the type of the ship.
     *
     * @return the type of the ship.
     */
    public String getType() {
        return type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Sets the health of the ship.
     *
     * @param health the health of the ship.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Sets the type of the ship.
     *
     * @param type the type of the ship.
     */
    public void setType(String type) {
        this.type = type;
    }

}
