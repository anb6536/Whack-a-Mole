package client.gui;

import java.io.IOException;
import java.net.Socket;

import static common.WAMProtocol.*;

/**
 * The client class that handles the communication between the server and the client
 * @author Aahish Balimane
 */
public class WAMClient {

    //Data members
    private Socket socket;
    private Duplexer player;
    private WAMBoard board;
    private int ROWS;
    private int COLS;
    private int numberOfPlayers;
    private int playerNumber;
    private int moleNumberUp;
    private int moleNumberDown;
    private int score;


    /**
     * Constructor for the class
     * @param client: the client
     * @param board: the board
     * @throws IOException
     */
    public WAMClient( Socket client, WAMBoard board ) throws IOException
    {
        this.socket = client;
        this.player = new Duplexer( this.socket );
        this.board = board;
        this.score = 0;
    }

    /**
     * the run method for the thread
     */
    public void run()
    {
        while (true)
        {
            String command = player.receive();
            System.out.println(command);
            String[] input = command.split(" ");
            if(input[0].equals(WELCOME))
            {
                System.out.println(input[1] + " " + input[2]);
                this.ROWS = Integer.parseInt(input[1]);
                this.COLS = Integer.parseInt(input[2]);
                this.numberOfPlayers = Integer.parseInt(input[3]);
                this.playerNumber = Integer.parseInt(input[4]);
                this.board.boardInit(this.COLS, this.ROWS);
            }

            else if(input[0].equals(MOLE_UP))
            {
                this.moleNumberUp = Integer.parseInt(input[1]);
                int moleNumber = this.getMoleNumberUp();
                int moleCol = moleNumber%this.getCOLS();
                int moleRow = moleNumber/this.getCOLS();
                board.makeMoleUp(moleRow, moleCol);
            }

            else if(input[0].equals(MOLE_DOWN))
            {
                this.moleNumberDown = Integer.parseInt(input[1]);
                int moleNumber = this.getMoleNumberDown();
                int moleCol = moleNumber%this.getCOLS();
                int moleRow = moleNumber/this.getCOLS();
                board.makeMoleDown(moleRow, moleCol);
            }

            else if(input[0].equals(GAME_WON))
            {
                board.gameWon();
                this.player.close();
            }

            else if(input[0].equals(GAME_LOST))
            {
                board.gameLost();
                this.player.close();
            }

            else if(input[0].equals(GAME_TIED))
            {
                board.gameTied();
                this.player.close();
            }

            else if(input[0].equals(SCORE))
            {
                this.score = Integer.parseInt(input[playerNumber+1]);
            }

            else if(input[0].equals(ERROR))
            {
                this.board.error();
            }
        }
    }

    /**
     * To get the number of rows
     * @return
     */
    public int getROWS() {
        return ROWS;
    }

    /**
     * To get the number of cols
     * @return
     */
    public int getCOLS() {
        return COLS;
    }

    /**
     * Getter for the player number
     * @return
     */

    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Get the number of mole up
     * @return
     */
    public int getMoleNumberUp()
    {
        return this.moleNumberUp;
    }

    /**
     * Get the nmle number down
     * @return
     */
    public int getMoleNumberDown()
    {
        return this.moleNumberDown;
    }

    /**
     * Starts the listener
     */
    public void startListener() {
        new Thread(() -> this.run()).start();
    }

    /**
     * Get the score of the players
     * @return
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * Sends the whack message
     * @param player
     * @param mole
     */
    public void sendWHACK( int player, int mole)
    {
        this.player.send(WHACK + " " + mole + " " + player);
        this.board.makeMoleDown(mole/this.getCOLS(), mole%this.getCOLS());
    }

}
