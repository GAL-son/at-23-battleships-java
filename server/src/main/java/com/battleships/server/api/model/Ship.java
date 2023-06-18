package com.battleships.server.api.model;

/**
 * Class representing ship
 */
public class Ship
{
    public int size;
    public int hp;
    public boolean sunk;

    /**
     * Ship constructor
     * @param size size of the ship (<i>eg.: 4</i>)
     */
    public Ship(int size) {
        this.size = size;
        this.hp = size;
        this.sunk = false;
    }

    /**
     * Method used to hit ship (ship takes damage).
     * When ship hp goes to zero - ship is sunk
     */
    public void hit()
    {
        hp--;
        if(hp <= 0) {
            sunk = true;
        }
    }

    /**
     * Method used for checking if ship is sunk
     * @return True if ship is sunk
     */
    public boolean isSunk() {
        return sunk;
    }
}