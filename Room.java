import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */
public class Room 
{
    private String description;
    private HashMap<String, Room> salidas;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        salidas = new HashMap<>();
    }

    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param exit define la direccion de salida
     * @param room define la habitacion a la que se accede en esa dirección.
     */
    public void setExit(String direccion, Room room) 
    {
        salidas.put(direccion, room);
    }

    /**
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Permite cambiar la descripcion de la habitación 
     */
    public void setDescription(String newDescription)
    {
        description = newDescription;
    }

    /**
     * Toma como parámetro una cadena que representa una dirección 
     * y devuelva el objeto de la clase Room
     */
    public Room getExit(String exit)
    {    
        return salidas.get(exit);
    }

    /**
     * Return a description of the room's exits.
     * For example: "Exits: north east west"
     *
     * @ return A description of the available exits.
     */
    public String getExitString()
    {
        String salidasPosibles = ("Exits: ");
        Iterator it = salidas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            salidasPosibles += e.getKey() + " ";            
        }
        return salidasPosibles;
    }
}
