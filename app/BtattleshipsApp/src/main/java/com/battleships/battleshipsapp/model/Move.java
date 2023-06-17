package com.battleships.battleshipsapp.model;


/**
 *  Class Represents a move in the battleship game.
 */
public class Move {
    public int positionX;
    public int positionY;

    public int type;//0 - shot , 1 - creating


    /**
     * Constructs a new move with the specified position and type.
     *
     * @param positionX The X coordinate of the move.
     * @param positionY The Y coordinate of the move.
     * @param type      The type of the move (0 for shot, 1 for creating).// not always the case//
     * @throws Exception if the move is out of bounds.
     */
    public Move(int positionX, int positionY, int type) throws  Exception {
       //System.out.println("move on  x:"+positionX+" y:"+positionY);
        //Log.i("", "move on  x:"+positionX+" y:"+positionY);
        if(positionX>9 || positionY>9||positionX<0||positionY<0)
            throw new Exception ("move out of boundries");

        this.positionX = positionX;
        this.positionY = positionY;
        this.type = type;
    }

    /**
     * Sets the X coordinate of the move.
     *
     * @param positionX The X coordinate to set.
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    /**
     * Sets the Y coordinate of the move.
     *
     * @param positionY The Y coordinate to set.
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /**
     * Sets the type of the move.
     *
     * @param type The type to set (0 for shot, 1 for creating).
     */
    public void setType(int type) {
        this.type = type;
    }

}
