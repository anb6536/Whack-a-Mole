package server;

import client.gui.Mole;

public class WAMGame implements Runnable{

    private int row;
    private int col;
    private int gameTime;
    private WAMServer server;
    private Mole[] board;



    public WAMGame(int row, int col, int gameTime, WAMServer server) {
        this.row=row;
        this.col=col;
        this.gameTime=gameTime;
        this.server=server;
        int length = row*col;
        board = new Mole[length];
        for (int i=0; i<length;i++){
            board[i] = new Mole();
        }

    }

    @Override
    public void run() {
        try{
            Thread.sleep(gameTime*1000);
        }catch (InterruptedException ie) {}
        server.run();

    }

}
