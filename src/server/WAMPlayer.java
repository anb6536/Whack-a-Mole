package server;

import client.gui.Duplexer;
import common.WAMProtocol;

import java.io.IOException;
import java.net.Socket;

/**
 * The class for the individual players. This class receives the WHACK message from the client and handles it.
 * @author: Adam Islam
 * @author: Aahish Balimane
 */
public class WAMPlayer extends Duplexer implements Runnable{

    //Declaring the data members
    private Socket sock;
    WAMServer server;
    int playerScore;

    /**
     * The constructor for the class
     * @param sock: the socket
     * @param server: the server class instance
     * @throws IOException
     */
    public WAMPlayer(Socket sock, WAMServer server)throws IOException {
        super(sock);
        this.server=server;
        playerScore=0;
    }

    /**
     * Returns the score of the individual player
     * @return the score
     */
    int getPlayerScore(){return playerScore;}

    /**
     * The run method that starts the thread for the player is called in the WAMNetwork class. It receives the WHACK message from the client
     * and handles it.
     */
    @Override
    public void run() {
        while(true){
            String message = "";
            try{
                message=receive();
            }catch(Exception e){}
            String[]tokens = message.split(" ");
            if(tokens[0].equals(WAMProtocol.WHACK)){
                playerScore+=server.moleHasBeenHit(Integer.parseInt(tokens[1]));
                send(WAMProtocol.SCORE + " " + server.showScores());
            }
            else
                send(WAMProtocol.ERROR);
        }

    }
}
