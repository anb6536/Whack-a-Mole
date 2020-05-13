package client.gui;

/**
 * The enum for the status of the game
 */
public enum Status {
    IN_PROGRESS, WON, LOST, TIE, ERROR;

    private String message = null;

    /**
     * To set the message of the status
     * @param msg
     */
    public void setMessage( String msg ) {
        this.message = msg;
    }

    /**
     * The tostring method of the string
     * @return: the string representation
     */
    @Override
    public String toString() {
        return super.toString() +
                this.message == null ? "" : ( '(' + this.message + ')' );
    }
}