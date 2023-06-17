package com.battleships.battleshipsapp.model.players;

import com.battleships.battleshipsapp.model.Move;
import com.battleships.battleshipsapp.model.board.Board;
import com.battleships.battleshipsapp.model.board.Field;
import com.battleships.battleshipsapp.model.ship.Ship;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Player class represents a player in the game.
 * It is an abstract class that is a basis for real types of players that exist in game
 * It provides methods and properties to manage the player's moves, ships, and game board.
 * Implements {@code Serializable } interface so that it can be transferred
 */
abstract public class Player implements Serializable {

    int id;// polayer id
    Board playerBoard= new Board(); // Board atached to this player

    private boolean moove_token= false; // token used for determining turns

    /**
     * Returns the value indicating whether it is the player's turn to make a move.
     *
     * @return true if it is the player's turn, false, if it is not.
     */
    public boolean isMoove_token() {
        return moove_token;
    }

    /**
     * Sets the value indicating whether it is the player's turn to make a move.
     *
     * @param moove_token true if it is the player's turn, false otherwise.
     */
    public void setMoove_token(boolean moove_token) {
        this.moove_token = moove_token;
    }

    public  ArrayList<ArrayList<Integer>> shipsCoordsAndAlignment=new ArrayList<ArrayList<Integer>>();// stores values that  clearly determine location of a ships on a board

    public ArrayList<Ship> ships=new ArrayList<Ship>();// stores Ships of specific Player
   public ArrayList<Integer> shipsSizes=new ArrayList<Integer>();//holds preset that determines how many shpis of what size are to nbe used in game

    /**
     * Returns players ID.
     *
     * @return  player's ID.
     */
    public int getId() {
        return id;
    }
    /**
     * Returns player's Board.
     *
     * @return  player's Board.
     */
    public Board getPlayerBard() {
        return playerBoard;
    }

    /**
     * Sets player's ID.
     *
     * @param id the player's ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the player's board.
     *
     * @param playerBoard the Board object to be set as the player's game board.
     */
    public void setPlayerBoard (Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * Constructs a new Player object with the specified ID.
     *
     * @param id the player's ID.
     */
    public Player(int id) {
        this.id = id;
    }

    /**
     * Retrieves the field on the player's game board at the specified position.
     *
     * @param move the Move object containing the coordinates.
     * @return the Field object at the specified position.
     */
    public Field getField(Move move)
    {
        ArrayList<Field> array =this.playerBoard.fields.get(move.positionX);
        Field field= array.get(move.positionY);

        return field;
    }


}




