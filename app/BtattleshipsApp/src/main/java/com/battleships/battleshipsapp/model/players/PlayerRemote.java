/**
 * The PlayerRemote class represents a remote player in the game.
 * It extends the Player class and provides additional methods and properties specific to remote player behavior.
 */
package com.battleships.battleshipsapp.model.players;
import java.io.Serializable;


/**
 * The PlayerRemote class extends the abstract Player class and represents a remote player in the game.
 * It implements Serializable to transfer object.
 */
public class PlayerRemote extends Player implements Serializable {



    /**
     * Constructs a new PlayerRemote object with the specified ID.
     *
     * @param id the player's ID.
     */
    public PlayerRemote(int id) {
        super(id);
    }




}
