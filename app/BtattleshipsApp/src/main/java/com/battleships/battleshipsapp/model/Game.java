
package com.battleships.battleshipsapp.model;



import com.battleships.battleshipsapp.model.board.Field;
import com.battleships.battleshipsapp.model.players.Player;
import com.battleships.battleshipsapp.model.players.PlayerAi;
import com.battleships.battleshipsapp.model.players.PlayerLocal;
import com.battleships.battleshipsapp.model.players.PlayerRemote;
import com.battleships.battleshipsapp.model.ship.Ship;
import com.battleships.battleshipsapp.model.board.Board;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents Instance of Battleships Game,
 * Implements Serializable for transfering object between viwes
 */
public class Game implements Serializable {

    private int id;
    private int state;
    // for now 0-in progress ship placing, 1-in progress shooting , 2- ended
    private int statePlayer1 = 0;
    private int statePlayer2 = 0;
    private int type;
    // for now -> with 0 - with ai, 1 - online
    private int turn = 0;
    private int player_active;
    private Player player1;
    private Player player2;

    public GameStateFromServer gameStateFromServer;

    public int winner = 0;

    /**
     * Increments value of turn, representing moving to the next turn
     */
    public void nextTurn() {
        turn = (turn + 1);
    }

    /**
     * Constructor Creating game object
     *
     * @param id identificator of a game instance
     * @param type type of the gamne in numeric: 0 for with AI, 1 for multiplayer
     * @throws Exception throws exception if the  game type does not mach any possible in game
     */
    public Game(int id, int type) throws Exception {
        //seting stater to in progres, ship placing
        this.state = 0;
        this.turn = 0;
        this.player_active = 0;
        //0 would mean that no player is active, as in phase of palcing ships, both can moove


        //recoginisng game mode
        System.out.println("witaj w grze w statki");
        this.id = id;
        if (type >= 0 && type <= 1) {
            this.type = type;
        } else {
            this.state = 2;
            throw new Exception("type specified not recoginised");
        }

        this.player1 = new PlayerLocal(1);//add resolving login to id

        //seting up right players
        switch (this.type) {
            case 0:
                this.player2 = new PlayerAi(999999);
                break;
            case 1:
                this.player2 = new PlayerRemote(2);
                break;
            default:
                this.state = 2;
                throw new Exception("type specified not recoginised");
        }

        //specifing ships sizes


        for (int i = 1; i < 5; i++)
            for (int x = i - 1; x < 4; x++) {
                player1.shipsSizes.add(i);
                player2.shipsSizes.add(i);
            }

      //  Log.i("test", "game creation ");


    }

    /**
     * Method for Placing ships, unused in the current version
     *
     * @param move Object of type move containing locatol and alignment of ship to b placed
     * @param player number identifying witch player in game took this action
     */
    public void getMove(Move move, int player) {
        if (this.state == 0) {
          //  Log.i("debug", "placing ship for player" + String.valueOf(player));
            place_ship(move, player, 0);
        }
        if (this.state == 1) {
        }
    }

    /**
     *  Method  hiting specified field on specified player's board
     *  also takes care of shooting fields bordering  destroyed ship
     * @param move object of type move specifing where to hit
     * @param player  number identifying witch player in game took this action
     */
    public void hitField(Move move, int player) {
        Player atacked_player;
        if (player == 1)
            atacked_player = this.player2;
        else if (player == 2) {
            atacked_player = this.player1;
        } else {
            return;
        }
       // Log.i("test1", "player is selected");


        ((Field) atacked_player.getPlayerBard().fields.get(move.positionX).get(move.positionY)).hitField();

        if (((Field) atacked_player.getPlayerBard().fields.get(move.positionX).get(move.positionY)).getOocupyingShip() != null) {
            if (((Field) atacked_player.getPlayerBard().fields.get(move.positionX).get(move.positionY)).getOocupyingShip().getHealth() == 0) {
               // Log.i("statek zniszczony", "hitField:  statek zniszczony");

                ArrayList<ArrayList<Integer>> pola_this = ((Field) atacked_player.getPlayerBard().fields.get(move.positionX).get(move.positionY)).getOocupyingShip().pola;
               // Log.i("debug", "ilosc pul=" + pola_this.size());
                for (ArrayList<Integer> a : pola_this) {
                   // Log.i("fields", "statek zniszczony byl na polu" + a.get(0) + ", " + a.get(1));
                    int x = a.get(0);
                    int y = a.get(1);

                    for (int n = -1; n < 2; n++) {
                        for (int m = -1; m < 2; m++) {
                            if (n == 0 && m == 0)
                                break;
                            x = x + n;
                            y = y + m;
                            if (x < 10 && x >= 0 && y < 10 && y >= 0) {
                                if ((((Field) atacked_player.getPlayerBard().fields.get(x).get(y)).isOccupied() != true))
                                    ((Field) atacked_player.getPlayerBard().fields.get(x).get(y)).hitField();
                            }
                            x = a.get(0);
                            y = a.get(1);
                        }
                        x = a.get(0);
                        y = a.get(1);
                    }

                    if (x < 10 && x >= 0 && y + 1 < 10 && y + 1 >= 0) {
                        if ((((Field) atacked_player.getPlayerBard().fields.get(x).get(y + 1)).isOccupied() != true))
                            ((Field) atacked_player.getPlayerBard().fields.get(x).get(y + 1)).hitField();
                    }
                }
            }
        }

        if (player == 1) {
            this.player2 = atacked_player;
        } else if (player == 2) {
            this.player1 = atacked_player;
        }


    }

    /**
     * Method for placing ships on the board.
     *It also  takes care of binding Ships to fields, and checking if ship can be placed on specified location
     * @param move move object of type move specifying where to place the ship
     * @param player number identifying witch player in game took this action
     * @param aline number determining if ship is to be placed vertically or horizontally
     */
    public void place_ship(Move move, int player, int aline) {

        //wybiramy gracza ktory ruszyl
        Player currentPlayer;
        if (player == 1)
            currentPlayer = this.player1;
        else if (player == 2) {
            currentPlayer = this.player2;
        } else {
            return;
        }


        //jesli gracz ktury rusza nie ma juz statkuw do psotawienia zakoncz wykonywanie
        if (currentPlayer.shipsSizes.isEmpty()) {
           // Log.i("debug", "all placed for player  " + String.valueOf(player));
            return;
        }

        //pobiez  rozmiar nastepnego statku
        int size = currentPlayer.shipsSizes.get(currentPlayer.shipsSizes.size() - 1);

        if (aline == 0) {
           // Log.i("debug", "checking orizontal " + String.valueOf(move.positionX) + "+" + String.valueOf(size));
            //sprawdz czy mozna postawic statek w danym miejscu (beta) jesli nie to zakoncz wykonywanie
            if (move.positionX + (size - 1) > 9) {
               // Log.i("debug", "statek nie moze byc stwozony horizontal ");
                return;
            }
        }
        if (aline == 1) {
          //  Log.i("debug", "checking veritcal");
            //sprawdz czy mozna postawic statek w danym miejscu (beta) jesli nie to zakoncz wykonywanie // konieczie POPRAWIĆ
            if (move.positionY + (size - 1) > 9) {
               // Log.i("debug", "statek nie moze byc stwozony vertical" + String.valueOf(statePlayer2));
                return;
            }
        }

        boolean pom_return = false;


        //sprawdzenie czy statek dotyka się z innym statkiem

        if (aline == 0) {
            for (int n = move.positionX - 1; n < move.positionX + size + 1; n++) {
                if (n >= 0 && n <= 9) {


                    if (((Field) currentPlayer.getPlayerBard().fields.get(n).get(move.positionY)).getOocupyingShip() != null) {
                       // Log.i("debug", "znaleziono styk");
                        pom_return = true;
                    }


                    if (move.positionY + 1 < 10) {
                        if (((Field) currentPlayer.getPlayerBard().fields.get(n).get(move.positionY + 1)).getOocupyingShip() != null) {
                           // Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }
                    }

                    if (move.positionY - 1 > -1) {
                        if (((Field) currentPlayer.getPlayerBard().fields.get(n).get(move.positionY - 1)).getOocupyingShip() != null) {
                          //  Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }

                    }

                }


            }
        }


        if (aline == 1) {
            for (int n = move.positionY - 1; n < move.positionY + size + 1; n++) {
                if (n >= 0 && n <= 9) {


                    if (((Field) currentPlayer.getPlayerBard().fields.get(move.positionX).get(n)).getOocupyingShip() != null) {
                       // Log.i("debug", "znaleziono styk");
                        pom_return = true;
                    }


                    if (move.positionX + 1 < 10) {
                        if (((Field) currentPlayer.getPlayerBard().fields.get(move.positionX + 1).get(n)).getOocupyingShip() != null) {
                           // Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }
                    }

                    if (move.positionX - 1 > -1) {
                        if (((Field) currentPlayer.getPlayerBard().fields.get(move.positionX - 1).get(n)).getOocupyingShip() != null) {
                           // Log.i("debug", "znaleziono styk");
                            pom_return = true;
                        }

                    }
                }


            }
        }
        if (pom_return == true)
            return;

        //dodaj statek do listy statkow gracza
        //Log.i("debug", "statek dodany do listy" + String.valueOf(player));
        currentPlayer.ships.add(new Ship(size));

        if (player==1)
        {
            ArrayList<Integer>curentShipParams=new ArrayList<>();
            curentShipParams.add(size);
            curentShipParams.add(move.positionX);
            curentShipParams.add(move.positionY);
            curentShipParams.add(aline);
            ((PlayerLocal)currentPlayer).shipsCoordsAndAlignment.add(curentShipParams);
        }
        //nadanie statku pul
        for (int i = 0; i < size; i++) {
           // Log.i("debug", "nadawanie pul");
            try {

               // Log.i("xd?", "statek dowiazany na polu" + String.valueOf(move.positionX) + " ," + String.valueOf(move.positionY));
                if (aline == 0) {
                    try {
                        currentPlayer.ships.get(currentPlayer.ships.size() - 1).addField(new Move(move.positionX + i, move.positionY, 99));
                      //  Log.i(" dodano", "place_ship:  dodano");
                    } catch (Exception e) {
                        //Log.i("nie dodano", "place_ship: nie dodano" + e.getMessage());
                    }

                }
                if (aline == 1) {
                    try {
                        currentPlayer.ships.get(currentPlayer.ships.size() - 1).addField(new Move(move.positionX, move.positionY + i, 99));
                      //  Log.i(" dodano", "place_ship:  dodano");
                    } catch (Exception e) {
                      //  Log.i("nie dodano", "place_ship: nie dodano" + e.getMessage());
                    }

                }
            } catch (Exception e) {
                //Log.i("wtf", e.getMessage());
            }
        }


        //powiąz statek z polami na planszy gracza(dodac)
        for (int i = 0; i < size; i++) {
           // Log.i("debug", "powioązano" + String.valueOf(i) + " z " + String.valueOf(size));
            try {

               // Log.i("xd?", "statek dowiazany na polu" + String.valueOf(move.positionX) + " ," + String.valueOf(move.positionY));
                if (aline == 0) {
//                    try {
//                        currentPlayer.ships.get(currentPlayer.ships.size() - 1).addField(new Move(move.positionX + i, move.positionY, 99));
//                        Log.i(" dodano", "place_ship:  dodano");
//                    }
//                    catch (Exception e)
//                    {
//                        Log.i("nie dodano", "place_ship: nie dodano"+e.getMessage());
//                    }
                    ((Field) currentPlayer.getPlayerBard().fields.get(move.positionX + i).get(move.positionY)).setOocupyingShip(currentPlayer.ships.get(currentPlayer.ships.size() - 1));
                    //currentPlayer.ships.get(currentPlayer.ships.size() - 1).addField(new Move(move.positionX + i, move.positionY, 99));
                }
                if (aline == 1) {
//                    try {
//                        currentPlayer.ships.get(currentPlayer.ships.size() - 1).addField(new Move(move.positionX, move.positionY + i, 99));
//                        Log.i(" dodano", "place_ship:  dodano");
//                   }
//                    catch (Exception e)
//                    {
//                        Log.i("nie dodano", "place_ship: nie dodano"+e.getMessage());
//                    }
                    ((Field) currentPlayer.getPlayerBard().fields.get(move.positionX).get(move.positionY + i)).setOocupyingShip(currentPlayer.ships.get(currentPlayer.ships.size() - 1));
                    //currentPlayer.ships.get(currentPlayer.ships.size() - 1).addField(new Move(move.positionX, move.positionY + i, 99));
                }
            } catch (Exception e) {
                //Log.i("wtf", e.getMessage());
            }
        }
        //usun rozmair stwozonego statku
       // Log.i("debug", "lista statkow do stworzenia zmniejszona");
        currentPlayer.shipsSizes.remove(currentPlayer.shipsSizes.size() - 1);

        //sprawdz stan rozgrywki dla gracza
        if (currentPlayer.shipsSizes.isEmpty()) {
            if (player == 1) {
                statePlayer1 = 1;
            } else if (player == 2) {
                statePlayer2 = 1;
            }
        }
       // Log.i("debug", "status rozgrywki dla gracza1:" + String.valueOf(statePlayer1));
      //  Log.i("debug", "status rozgrywki dla gracza2:" + String.valueOf(statePlayer2));

        //jesli  obaj gracze są juz w  stanie rozgrywki, ustaw stan rozgrywki na nastepny i zakoncz wykonyewanie
        if (statePlayer1 == 1 && statePlayer2 == 1) {
            state = 1;
        }
        //Log.i("debug", "status rozgrywki :" + String.valueOf(state));
        //przypisz tymczasowy obiekt do odpowiedniego gracza
        if (player == 1)
            this.player1 = currentPlayer;
        else if (player == 2) {
            this.player2 = currentPlayer;
        }

    }

    /**
     * Retrieves vaule of game's id
     * @return game's id
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves vaule of game's state
     * 0 for not yet started,
     * 1 for in  progress,
     * 2 for ended,
     * @return game's state
     */
    public int getState() {
        return state;
    }

    /**
     * Retrieves vaule of game's type
     * 0 for single-player,
     * 1 for multiplayer,

     * @return game's type
     */
    public int getType() {
        return type;
    }

    /**
     * Retrieves rest of dividing game's turn by 2, to specify whos of the 2 player turn it is
     * @return (game's turn % 2)
     */
    public int getTurn() {
        return turn % 2;
    }
    /**
     * Retrieves value of game's turn,
     * @return game's turn
     */
    public int getTurnFull() {
        return turn;
    }

    /**
     * sets id of a game
     * @param id  id of a game
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * sets State of a game
     * 0 for not yet started,
     * 1 for in  progress,
     * 2 for ended,
     * @param state state of the game
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * sets type of a game
     * 0 for single-player,
     * 1 for multiplayer,
     * @param type state of the game
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * method for manually setting turn
     * @param turn game's turn
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**Retrieves object representing player 1 in game, player 1 is always the local player
     *
     * @return object representing player1
     */
    public Player getPlayer1() {
        return player1;
    }
    /**Retrieves object representing player 2 in game, player 2 is either remote player or an AI opponent
     *
     * @return object representing player2
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Method for counting how many ships of each size player has still not placed
     * @param a an array table that  gets its element occurrences counted, in this  method it is typicaly the array of ship sizes
     * @return histogram  - an array containing number of occurrences of each value in parameter array
     */
    public static ArrayList<Integer> histogram(ArrayList<Integer> a) {
        ArrayList<Integer> histogram = new ArrayList<>();
        for (int n = 0; n < 4; n++) {
            histogram.add(0);
        }
        histogram.add(0);
        for (Integer x : a) {
            histogram.set(x - 1, (histogram.get(x - 1) + 1));
        }
       // Log.i("histogram", "histogram: jeden*" + String.valueOf(histogram.get(0)) + " dwa*" + String.valueOf(histogram.get(1)) + " trzy*" + String.valueOf(histogram.get(2)) + " cztery*" + String.valueOf(histogram.get(3)));
        return histogram;
    }


    /**
     * Method for counting how many ships of each size player still has (were not destroyed)
     * @param a an array table that  gets its element occurrences counted, in this  method it is typically the array of ships of a player
     * @return histogram  - an array containing number of occurrences of each value in parameter array
     */
    public static ArrayList<Integer> histogramInGame(ArrayList<Ship> a) {
        ArrayList<Integer> histogram = new ArrayList<>();
        for (int n = 0; n < 4; n++) {
            histogram.add(0);
        }
        histogram.add(0);
        for (Ship x : a) {
            if (x.getHealth() > 0)
                histogram.set(x.getSize() - 1, (histogram.get(x.getSize() - 1) + 1));
        }
      //  Log.i("histogram", "histogram: jeden*" + String.valueOf(histogram.get(0)) + " dwa*" + String.valueOf(histogram.get(1)) + " trzy*" + String.valueOf(histogram.get(2)) + " cztery*" + String.valueOf(histogram.get(3)));
        return histogram;
    }

}


