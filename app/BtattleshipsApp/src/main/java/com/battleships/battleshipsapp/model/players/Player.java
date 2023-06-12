package com.battleships.battleshipsapp.model.players;

import com.battleships.battleshipsapp.model.Move;
import com.battleships.battleshipsapp.model.board.Board;
import com.battleships.battleshipsapp.model.board.Field;
import com.battleships.battleshipsapp.model.ship.Ship;


import java.io.Serializable;
import java.util.ArrayList;

abstract public class Player implements Serializable {

    int id;
    Board playerBoard= new Board();

    private boolean moove_token= false;

    public boolean isMoove_token() {
        return moove_token;
    }

    public void setMoove_token(boolean moove_token) {
        this.moove_token = moove_token;
    }

    public  ArrayList<ArrayList<Integer>> shipsCoordsAndAlignment=new ArrayList<ArrayList<Integer>>();


    public ArrayList<Ship> ships=new ArrayList<Ship>();
   public ArrayList<Integer> shipsSizes=new ArrayList<Integer>();
    public int getId() {
        return id;
    }

    public Board getPlayerBard() {
        return playerBoard;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayerBoard (Board playerBoard) {
        this.playerBoard = playerBoard;
    }


    public Player(int id) {
        this.id = id;
    }
    public Field getField(Move move)
    {
        ArrayList<Field> array =this.playerBoard.fields.get(move.positionX);
        Field field= array.get(move.positionY);

        return field;
    }


}




