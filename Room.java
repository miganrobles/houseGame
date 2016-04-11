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
     * @param north The north exit.
     * @param east The east east.
     * @param south The south exit.
     * @param west The west exit.
     */
    public void setExits(Room north, Room east, Room south, Room west, Room southEast, Room northWest) 
    {
        if(north != null)
            salidas.put("north", north);
        if(east != null)
            salidas.put("east", east);
        if(south != null)
            salidas.put("south", south);
        if(west != null)
            salidas.put("west", west);
        if (southEast != null)
            salidas.put("southEast", southEast);
        if (northWest != null)
            salidas.put("nortWest", northWest);
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
            if (e.getValue() != null) {
                salidasPosibles += e.getKey() + " ";
            }
        }
        return salidasPosibles;
    }
}
