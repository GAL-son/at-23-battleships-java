package com.battleships.battleshipsapp.model.board;


import com.battleships.battleshipsapp.model.ship.Ship;

import java.io.Serializable;

/**
 *
 *The {@code Field} class represents a single field on the game board.
 *It can either be empty or occupied by a ship. Can also be hit or not
 * * Implemetnts {@code Serializable } intercace so that it can be transfered
 */
public class Field  implements Serializable {
    public Ship oocupyingShip; // The ship occupying the field, if any.
    private boolean wasHit;// Indicates whether the field was hit.

    public int test;

    /**
     * Constructs a new Field object.
     * The initial state is empty and not hit.
     */
     public Field()
     {
         oocupyingShip=null;
         wasHit=false;

     }


    /**
     * Hits the field and updates the state accordingly.
     * If the field is occupied by a ship, the ship's hitShip() method is called.
     */
    public void hitField() {
        this.wasHit=true;
        if( oocupyingShip!=null)
        {
            this.oocupyingShip.hitShip();
            //Log.i("ship hit",  "ship was hit, its curent hp is: "+this.oocupyingShip.getHealth());
        }
    }

    /**
     * Checks if the field is occupied by a ship.
     *
     * @return true if the field is occupied, false if it is not.
     */
    public Boolean isOccupied() {

        if(oocupyingShip==null)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    /**
     * returns reference the ship occupying the field.
     *
     * @return the Ship object occupying the field, or null if the field is empty.
     */
    public Ship getOocupyingShip() {
        return oocupyingShip;
    }

    /**
     * Checks if the field was hit.
     *
     * @return true if the field was hit, false otherwise.
     */
    public boolean getWasHit() {
        return wasHit;
    }


    /**
     * Sets the ship reference that  to ships ocupying that field.
     * @param oocupyingShip the Ship object to be set as occupying the field.
     */
    public void setOocupyingShip(Ship oocupyingShip) {
        this.oocupyingShip = oocupyingShip;
    }

    /**
     * Sets the hit status of the field.
     * @param wasHit true to hit the field, false to 'unhit' it otherwise.
     */
    public void setWasHit(boolean wasHit) {
        this.wasHit = wasHit;
    }

}

