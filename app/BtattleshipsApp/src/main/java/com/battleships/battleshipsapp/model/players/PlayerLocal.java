/**
 * The PlayerLocal class represents a local human player in the game.
 * It extends the Player class and provides additional methods and properties specific to local player behavior.
 */
package com.battleships.battleshipsapp.model.players;

import java.io.Serializable;
/**
 * The PlayerLocal class extends the abstract Player class and represents a local human player in the game.
 * It implements Serializable to allow object serialization.
 */
public class PlayerLocal extends Player implements Serializable {


    /**
     * Constructs a new PlayerLocal object with the specified ID.
     *
     * @param id the player's ID.
     */
    public PlayerLocal(int id) {
        super(id);
    }
}

