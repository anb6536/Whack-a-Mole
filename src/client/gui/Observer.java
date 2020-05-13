package client.gui;

/**
 * Observer class that reflects the changes made in both the boards
 * @param <T> generic type
 */
public interface Observer<T>
{
    void update(T type);
}
