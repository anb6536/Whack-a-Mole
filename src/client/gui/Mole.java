package client.gui;

/**
 * The mole class that makes mole objects and make the mole up and down
 * @author Aahish Balimane
 */
public class Mole
{
    //Defining the data members
    private boolean moleIsUp;

    public Mole ()
    {
        this.moleIsUp = false;
    }

    /**
     * Tells if the mole is up
     * @return boolean
     */
    public boolean isUp()
    {
        if(moleIsUp == true)
        {
            return true;
        }

        else
        {
            return false;
        }
    }

    /**
     * Sets the mole to be up
     */
    public void setUp()
    {
        this.moleIsUp = true;
    }

    /**
     * Sets the mole to be down
     */
    public void setDown()
    {
        this.moleIsUp = false;
    }
}
