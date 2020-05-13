package server;

import common.WAMProtocol;

import java.util.ArrayList;
import java.util.Random;

/**
 * The mole class that sets the mole up or down randomly. This is also a thread that is started in the WAMNetwork class
 * @author Aahish Balimane
 */
public class ServerMole extends Thread implements WAMProtocol
{
    //Declaring the data members
    private WAMPlayer[] players;
    private WAMServer server;

    /**
     * The constructor for the class
     * @param players: the array of all the players
     * @param server: the instance of the server
     */
    public ServerMole(WAMPlayer[] players, WAMServer server)
    {
        this.players = players;
        this.server = server;
    }

    /**
     * the run method of the thread that generates a random number and sends the mole up message to all the players and then waits for some time
     * during which the server waits for the whack message and then after some time sends the mole down message.
     */
    public void run()
    {
        while(this.server.flag)
        {
            Random random = new Random();
            int mole = random.nextInt(server.getCol()*server.getRow());
            for (WAMPlayer player : players)
            {
                player.send(MOLE_UP + " " + mole);
            }
            server.getMolesUp().add(mole);
            try {
                sleep(random.nextInt(3)*1000);
            } catch (InterruptedException e) {
                System.err.println(e);;
            }
            for (WAMPlayer player : players)
            {
                player.send(MOLE_DOWN + " " + mole);
            }
            server.getMolesUp().remove((Object)mole);
        }

    }
}
