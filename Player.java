import java.util.ArrayList;

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
    // Guarda los items que va cojiendo el jugador
    private ArrayList<Item> itemsCojidos;
    // Peso que puede transportar el jugador
    private float pesoPuedeLlevar;

    /**
     * Constructor for objects of class Player
     */
    public Player(Room currentRoom)
    {
        pesoPuedeLlevar = 10;
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
    
    /**
     * Comprueba si puede cojer el item según su peso
     */
    public void puedeConItem(Item item)
    {
        float pesoItem = item.getPeso();
        if (pesoItem <= pesoPuedeLlevar) {
            pesoPuedeLlevar -= pesoItem;
            cojeItem(item);
            currentRoom.removeItem(item);
        }
        else {
            System.out.println("El objeto pesa " + pesoItem + " y solo puede cojer " + pesoPuedeLlevar);
            System.out.println("Si quiere cojer este objeto, debe de posar otro antes para que pueda con él");
        }
    }

    /**
     * Añade items a los que tiene el jugador
     * de los que hay en la habitación en la que se encuentra
     */
    public void cojeItem(Item item)
    {
        itemsCojidos.add(item);
    }
    
    /**
     * Permite dejar un item de los que tiene en la habitación 
     * en la que se encuentra
     */
    public void posarItem(String nombreItem)
    {
        boolean buscando = true;
        for (int i = 0; i < itemsCojidos.size() && buscando; i++) {
            if (itemsCojidos.get(i).equals(nombreItem)) {
                buscando = false;
                currentRoom.addItem(itemsCojidos.remove(i));
            }
        }
        if (buscando) {
            System.out.println("¡¡ERROR!! , no tienes ningun/a" + nombreItem + " que puedas dejar en esta habitación");
        }
    }
}
