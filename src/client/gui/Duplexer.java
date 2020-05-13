package client.gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Duplexer
{

    // describing the states
    private Socket socket;
    private Scanner input;
    private PrintWriter output;

    /**
     * Constructor for the duplexer
     * @param socket Socket for the connection
     * @throws IOException
     */
    public Duplexer(Socket socket) throws IOException
    {
        this.socket = socket;
        this.input = new Scanner(socket.getInputStream());
        this.output = new PrintWriter(socket.getOutputStream());
    }

    /**
     * function for sending the messages between the server and the client
     * @param in message to be sent
     */
    public void send(String in)
    {
        output.println(in);
        output.flush();
    }

    /**
     * function for receiving the message
     * @return the string message received
     */

    public String receive ()
    {
        return input.nextLine();
    }

    /**
     * closes the socket
     */
    public void close()
    {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

