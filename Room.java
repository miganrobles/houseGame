import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

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
    // Almacena todos los items que puedan existir en la habitación
    private ArrayList<Item> items;
    // Guarda el personje que está en la habitación
    private Personaje personaje;
    
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
        items = new ArrayList<>();
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
     * Toma como parámetro una cadena que representa una dirección 
     * y devuelva el item de la clase Room
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

    /**
     * Return a long description of this room, of the form:
     *     You are in the 'name of room'
     *     Exits: north west southwest
     * @return A description of the room, including exits.
     */
    public String getLongDescription()
    {
        return "Te encuentras en " + description + "\n" + nombrePersonaje()
        + "\n" + itemsDescription() + "\n" + getExitString() + "\n";
    }
    
    /**
     * Añade items a la habitación
     */
    public void addItem(Item item)
    {
        items.add(item);
    }
    
    /**
     * Elimina el item cuando es cogido
     */
    public void removeItem(Item item)
    {
        items.remove(item);
    }
    
    /**
     * Devuelve el item si se encuentra en la habitación
     */
    public Item getItem(int ref)
    {
        Item item = null;
        int cantidadItems = items.size();
        for (int index = 0; index < items.size() && item == null; index++) {
            if (items.get(index).getRef() == ref) {
                item = items.get(index);
            }
        }
        return item;
    }
    
    /**
     * Añade un personaje a la habitacion
     */
    public void setPersonaje(Personaje personaje)
    {
        this.personaje = personaje;
    }
    
    /**
     * Devuelve todos los items que hay en la habitación
     */
    public ArrayList<Item> getItems()
    {
        return items;
    }
    
    /**
     * Devuelve el nombre del personaje que se va ha mostrar en la descripción
     */
    private String nombrePersonaje()
    {
        String nombre = "no hay";
        if (personaje != null) {
            nombre = personaje.getNombre();
        }
        return "Personaje: " + nombre;
    }
    
    /**
     * Devuelve los items que hay en la habitación
     * para mostrarlos en la descripción de la habitación
     */
    private String itemsDescription()
    {
        String descripcionItems = "";
        for (Item item : items) {
            descripcionItems += "\n" + item.getDescription();
        }
        if (descripcionItems.isEmpty()) {
            descripcionItems = " no hay items";
        }
        return "Items existentes:" + descripcionItems + "\n";
    }
}