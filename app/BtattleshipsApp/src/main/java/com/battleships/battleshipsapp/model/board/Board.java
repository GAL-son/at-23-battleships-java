package com.battleships.battleshipsapp.model.board;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The {@code Board} class represents a game board for a Battleships application.
 * It provides methods to initialize the board, set the size, and retrieve the size of the board.
 * The board is represented as a grid of fields.
 * Each field can be accessed using the {@code fields} list, which contains {@code ArrayList<Field>} objects representing rows.
 * * Implemetnts {@code Serializable } intercace so that it can be transfered
 */
public class Board implements Serializable {

    /**
     * The size of the board.
     */
    private int size = 10;

    /**
     * The grid of fields on the board.
     */
    public ArrayList<ArrayList> fields = new ArrayList<>();

    /**
     * Constructs a new board with a default size of 10x10.
     * Initializes the grid of fields by creating {@code ArrayList<Field>} objects for each row and {@code Field} objects for each column.
     */
    public Board() {
        for (int m = 0; m < this.size; m++) {
            fields.add(new ArrayList<Field>());
            for (int n = 0; n < this.size; n++) {
                fields.get(m).add(new Field());
            }
        }
    }

    /**
     * Sets the size of the board.
     *
     * @param size the new size of the board
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Retrieves the size of the board.
     *
     * @return the size of the board
     */
    public int getSize() {
        return size;
    }
}