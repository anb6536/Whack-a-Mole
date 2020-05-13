package server;

import client.gui.WAMBoard;
import client.gui.WAMClient;
import common.WAMProtocol;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 * The server class that creates the WAM Players and implements the server side of the game
 * @author: Aahish Balimane
 * @author: Adam Islam
 */
public class WAMServer implements WAMProtocol,Runnable{

    //Defining the data members
    private ServerSocket serverSocket;
    private WAMBoard board;
    private int row;
    private int col;
    private int numPlayers;
    private int gameTime;
    private WAMPlayer[] allPlayers;
    private ArrayList<Integer> molesUp;
    public boolean flag;

    /**
     * The constructor for the class
     * @param port: the port number
     * @param row: the number of rows
     * @param col: the number of columns
     * @param numPlayers: the total number of players
     * @param gameTime: the total game time
     * @throws IOException
     */
    public WAMServer(int port, int row, int col, int numPlayers, int gameTime) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IOException(e);
        }
        this.row=row;
        this.col=col;
        this.numPlayers=numPlayers;
        this.gameTime=gameTime;
        this.allPlayers=new WAMPlayer[numPlayers];
        this.flag = true;
        this.molesUp = new ArrayList<>();
    }


    /**
     * The run method of the thread that handles the game. It start with accepting the clients and then handles the up and down of the
     * mole and whack message that the WAMPlayer receives
     */
    @Override
    public void run() {
        try {
            for (int i = 0; i < numPlayers; i++) {
                Socket socket = serverSocket.accept();
                WAMPlayer player = new WAMPlayer(socket, this);
                player.send(WAMProtocol.WELCOME + " " + row + " " + col + " " + numPlayers + " " + i);
                allPlayers[i] = player;
                Thread thread = new Thread(player);
                thread.start();
                System.out.println("Player " + (i + 1) + " is here.");

            }
        } catch (IOException e) {
            System.out.println("Didn't run");
        }

        ServerMole mole = new ServerMole(allPlayers,this);
        mole.start();
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis()-start <= gameTime*1000)
        {

        }
        flag = false;

        for (int i=0; i<row*col; i++)
        {
            for (WAMPlayer player : allPlayers)
            {
                player.send(MOLE_DOWN + " " + i);
            }
        }
        String scores = showScores();
        String[] indScores = scores.split(" ");
        if (indScores.length == 1)
        {
            allPlayers[0].send(GAME_WON);
        }
        else if (indScores.length == 2)
        {
            if (Integer.parseInt(indScores[0]) > Integer.parseInt(indScores[1]))
            {
                allPlayers[0].send(GAME_WON);
                allPlayers[1].send(GAME_LOST);
            }
            else if (Integer.parseInt(indScores[0]) < Integer.parseInt(indScores[1]))
            {
                allPlayers[1].send(GAME_WON);
                allPlayers[0].send(GAME_LOST);
            }
            else if (Integer.parseInt(indScores[0]) == Integer.parseInt(indScores[1]))
            {
                allPlayers[0].send(GAME_TIED);
                allPlayers[1].send(GAME_TIED);
            }
        }
        else if(indScores.length == 3)
        {
            if ((Integer.parseInt(indScores[0]) == Integer.parseInt(indScores[1])) && (Integer.parseInt(indScores[0]) == Integer.parseInt(indScores[2])))
            {
                allPlayers[0].send(GAME_TIED);
                allPlayers[1].send(GAME_TIED);
                allPlayers[2].send(GAME_TIED);
            }
            else if ((Integer.parseInt(indScores[0]) >= Integer.parseInt(indScores[1])) && (Integer.parseInt(indScores[0]) >= Integer.parseInt(indScores[2])))
            {
                allPlayers[0].send(GAME_WON);
                allPlayers[1].send(GAME_LOST);
                allPlayers[2].send(GAME_LOST);
            }

            else if ((Integer.parseInt(indScores[1]) >= Integer.parseInt(indScores[0])) && (Integer.parseInt(indScores[1]) >= Integer.parseInt(indScores[2])))
            {
                allPlayers[1].send(GAME_WON);
                allPlayers[0].send(GAME_LOST);
                allPlayers[2].send(GAME_LOST);
            }

            else if ((Integer.parseInt(indScores[2]) >= Integer.parseInt(indScores[1])) && (Integer.parseInt(indScores[2]) >= Integer.parseInt(indScores[0])))
            {
                allPlayers[2].send(GAME_WON);
                allPlayers[1].send(GAME_LOST);
                allPlayers[0].send(GAME_LOST);
            }
        }
    }

    /**
     * Returns the array list of the mole numbers that are up
     * @return: the arraylist of moles
     */
    public ArrayList<Integer> getMolesUp() {
        return molesUp;
    }

    /**
     * This method is called by the WAMPlayer class when it receives the WHACK message from the client. It checks if the mole number is up or not
     * and then accordingly returns the points it has to give to the player
     * @param moleNumber: the mole that has been whacked
     * @return: the point that needs to be awarded to the player
     */
    public int moleHasBeenHit(int moleNumber ){
        if (molesUp.contains(moleNumber))
        {
            for (WAMPlayer player : allPlayers)
            {
                player.send(MOLE_DOWN + " " + moleNumber);
            }
            return 2;
        }
        else
        {
            return -1;
        }
    }


    /**
     * The method returns a string of the scores of all the players seperated by spaces
     * @return: the scores in the form of a single string
     */
    public String showScores(){
        String out = "";
        for (int i=0; i<allPlayers.length;i++){
            int score = allPlayers[i].getPlayerScore();
            if ( i == 0)
            {
                out += score;
            }
            else
            {
                out+=" " + score;
            }

        }
        return out;
    }

    /**
     * Returns the number of rows
     * @return: the number of rows
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the number of columns
     * @return: the number of columns
     */
    public int getCol() {
        return col;
    }

    /**
     * The main method that makes a new server object, taps all the arguments and calls the run method to start the game on the network side
     * @param args: the Command line arguments
     * @throws IOException
     */
    public static void main(String[] args)throws IOException{
        if (args.length != 5) {
            System.out.println("Wrong number of arguments");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        int row = Integer.parseInt(args[1]);
        int col = Integer.parseInt(args[2]);
        int numPlayers = Integer.parseInt(args[3]);
        int gameTime = Integer.parseInt(args[4]);

        WAMServer server = new WAMServer(port,row,col,numPlayers,gameTime);

        server.run();

    }
}
