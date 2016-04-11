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
    private Room northExit;
    private Room southExit;
    private Room eastExit;
    private Room westExit;
    private Room southEastExit;
    private Room northWestExit;
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
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
            northExit = north;
        if(east != null)
            eastExit = east;
        if(south != null)
            southExit = south;
        if(west != null)
            westExit = west;
        if (southEast != null)
            southEastExit = southEast;
        if (northWest != null)
            northWestExit = northWest;
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
        Room exitRoom = null;
        if (exit.equals("north")) {
            exitRoom = northExit;
        }
        else if (exit.equals("south")) {
            exitRoom = southExit;
        }
        else if (exit.equals("east")) {
            exitRoom = eastExit;
        }
        else if (exit.equals("west")) {
            exitRoom = westExit;
        }
        else if (exit.equals("southEast")) {
            exitRoom = southEastExit;
        }
        else if (exit.equals("northWest")) {
            exitRoom = northWestExit;
        }
        return exitRoom;
    }

    /**
     * Return a description of the room's exits.
     * For example: "Exits: north east west"
     *
     * @ return A description of the available exits.
     */
    public String getExitString()
    {
        String salidas = ("Exits: ");
        if(northExit != null) {
            salidas += "north ";
        }
        if(eastExit != null) {
            salidas += "east ";
        }
        if(southExit != null) {
            salidas += "south ";
        }
        if(westExit != null) {
            salidas += "west ";
        }
        if(southEastExit != null) {
            salidas += "southEast ";
        }
        if (northWestExit != null) {
            salidas += "northWest ";
        }
        return salidas;
    }
}
