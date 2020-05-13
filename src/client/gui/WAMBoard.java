package client.gui;

import java.util.LinkedList;
import java.util.List;

/**
 * The class for the board
 * @author Aahish Balimane
 */
public class WAMBoard
{
    //Data members
    private Mole[][] board;
    private List<Observer<WAMBoard>> observers;
    private Status status;

    /**
     * Constructor for the class
     */
    public WAMBoard()
    {
        this.status = Status.IN_PROGRESS;
        this.observers = new LinkedList<>();
    }

    /**
     * Initializes the board of the game
     * @param col: the col number
     * @param row: the row number
     */
    public void boardInit(int col, int row)
    {
        this.board = new Mole[row][col];
        for(int i=0; i<row; i++)
        {
            for (int j=0; j<col; j++)
            {
                board[i][j] = new Mole();
            }
        }
    }

    /**
     * Adds the board to the observer
     * @param observer the board
     */
    public void addObserver(Observer<WAMBoard> observer) {
        this.observers.add(observer);
    }

    /** when the model changes, the observers are notified via their update() method */
    private void alertObservers() {
        for (Observer<WAMBoard> obs: this.observers ) {
            obs.update(this);
        }
    }

    /**
     * Makes the mole up
     * @param moleRow: the mole row number
     * @param moleCol: the mole col number
     */
    public void makeMoleUp(int moleRow, int moleCol)
    {
        board[moleRow][moleCol].setUp();
        alertObservers();
    }

    /**
     * Makes the mole dowm
     * @param moleRow: the mole row
     * @param moleCol: the mole col
     */
    public void makeMoleDown(int moleRow, int moleCol)
    {
        board[moleRow][moleCol].setDown();
        alertObservers();
    }

    /**
     * Get the mole at a given row col
     * @param row: the row
     * @param col: the col
     * @return: the mole at the position
     */
    public Mole getMole(int row, int col)
    {
        return board[row][col];
    }

    /**
     * Sets the status as won
     */
    public void gameWon() {
        this.status = Status.WON;
        alertObservers();
    }

    /**
     * Sets the status as lost
     */
    public void gameLost() {
        this.status = Status.LOST;
        alertObservers();
    }

    /**
     * Sets the game as ties
     */
    public void gameTied() {
        this.status = Status.TIE;
        alertObservers();
    }

    /**
     * To get the status of the game
     * @return
     */
    public Status getStatus()
    {
        return this.status;
    }


    /**
     * Sets the status when there is an error
     */
    public void error()
    {
        this.status = Status.ERROR;
        alertObservers();
    }

}
