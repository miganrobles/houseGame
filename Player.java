import java.util.ArrayList;

/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player
{
    // Guarda la localizaci�n en la que se encuentra el jugador
    private Room currentRoom;
    // Guarda los items que va cojiendo el jugador
    private ArrayList<Item> itemsCojidos;

    /**
     * Constructor for objects of class Player
     */
    public Player(Room currentRoom)
    {
        this.currentRoom = currentRoom;
    }

    /**
     * Devuelve la localizaci�n en la que se encuentra el jugador
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Establece la localizaci�n actual donde se encuentra el jugador
     */
    public void setCurrentRoom(Room newRoom)
    {
        currentRoom = newRoom;
    }

    /**
     * A�ade items a los que tiene el jugador
     * de los que hay en la habitaci�n en la que se encuentra
     */
    public void cojeItem(Item item)
    {
        itemsCojidos.add(item);
    }
}
