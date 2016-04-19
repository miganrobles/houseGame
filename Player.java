
/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player
{
    // Guarda la localización en la que se encuentra el jugador
    private Room currentRoom;

    /**
     * Constructor for objects of class Player
     */
    public Player(Room currentRoom)
    {
        this.currentRoom = currentRoom;
    }
    
    /**
     * Devuelve la localización en la que se encuentra el jugador
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }
    
    /**
     * Establece la localización actual donde se encuentra el jugador
     */
    public void setCurrentRoom(Room newRoom)
    {
        currentRoom = newRoom;
    }
}
