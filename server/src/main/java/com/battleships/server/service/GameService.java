package com.battleships.server.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.battleships.server.api.Exceptions.NoUserException;
import com.battleships.server.api.model.Game;
import com.battleships.server.api.model.User;

/**
 * Class responsible for storing and managing currently running games.
 */
@Service
public class GameService {
    List<Game> activeGames;
    List<User> userQueue;
    Map<User, Game> playersInGame;

    /**
     * GameService constructor. Initializes nessesary lists
     */
    public GameService() {
        activeGames = new LinkedList<Game>();
        userQueue = new LinkedList<User>();
        playersInGame = new HashMap<User, Game>();
    }
    
    /**
     * Method for geting Game reference by its ID
     * @param activeGameId ID of currently active game
     * @return Game reference with given ID
     */
    public Game getGame(int activeGameId) {
        for(Game g : activeGames) {
            if(g.getGameId() == activeGameId) return g;
        }
        
        return null;
    }
    
    /**
     * Method for adding user into the game queue
     * @param user User joining queue
     * @return True if user has been successfully added into the queue
     */
    public boolean enterQueue(User user) {
        userQueue.add(user);
        return userQueue.contains(user);        
    }
    
    /**
     * Method used for updating queue state for given User.
     * It performs nessesseary operations for finding opponent and creating a new game when possible.
     * @param user User whose queue state we want to update
     * @return Game reference
     */
    public Game updateQueue(User user) {
        // CHECK IF PLAYER ALREADY IN GAME
        if(getPlayerGame(user)!= null) {
            return getPlayerGame(user);
        }

        /* DEBUG */ System.out.println(userQueue);

        // QUEUE IS EMPTY
        if(userQueue.size() == 1) return null;
        
        // FIND OPPONENT
        User opponent = null;
        Float scoreDifference = null;
        for(User u : userQueue)
        {   
            if( u.getUid() == user.getUid()) continue;
            float newScoreDifference = Math.abs(user.getGamerScore() - u.getGamerScore()); 
            if(scoreDifference == null || scoreDifference > newScoreDifference)
            {
                scoreDifference = newScoreDifference;
                opponent = u;
            }
        }

        /* DEBUG */ //System.out.println("Opponent found: " + opponent + " withd score difference" + scoreDifference);

        if(opponent == null) throw new NoUserException("Error when finding opponent!!!");
        
        // OPPONENT FOUND - CREATE GAME
        // Find empty game id
        int maxid = 0;
        for(Game g : activeGames) {
            int gid = g.getGameId();
            if(gid > maxid) maxid = gid; 
        }
        
        // Push Game to list
        Game game = new Game(maxid, 10);
        
        // Add Players
        try {
            game.playerJoin(user);
            userQueue.remove(user);
            playersInGame.put(user, game);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed adding player");
            e.printStackTrace();
            return null;
        }
        
        try {
            game.playerJoin(opponent);
            userQueue.remove(opponent);
            playersInGame.put(opponent, game);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed adding opponent");
            e.printStackTrace();
            return null;
        }
        
        /* DEBUG */ // System.out.println(userQueue);
        addGame(game);
        
        return game;
    }

    /**
     * Method that gives a Game reference which is played by given User
     * @param user whose game we want to get
     * @return Game reference which is played by User (Can be <b>null</b>)
     */
    public Game getPlayerGame(User user) {
        for(Map.Entry<User, Game> m : playersInGame.entrySet()) {
            if(user.getUid() == m.getKey().getUid()) return m.getValue();
        }
        return null;
    }

    /**
     * Method used to ending a game for given user
     * @param user User whose game we want to end
     */
    public void endGame(User user) {
        Game game = playersInGame.get(user);        
        playersInGame.remove(user);

        if(!playersInGame.containsValue(game)) {
            activeGames.remove(game);
        }
    }

    // Helper functions

    /**
     * Method that adds Game into activeGames
     * @param game Game that we want to make active
     */
    private void addGame(Game game)
    {
        activeGames.add(game);
    }
    
    
}
