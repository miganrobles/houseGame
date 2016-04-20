import java.util.ArrayList;
import java.text.DecimalFormat;

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
    // Guarda los items que va cogiendo el jugador
    private ArrayList<Item> itemsCogidos;
    // Peso que puede transportar el jugador
    private float pesoPuedeLlevar;

    /**
     * Constructor for objects of class Player
     */
    public Player(Room currentRoom)
    {
        pesoPuedeLlevar = 5;
        this.currentRoom = currentRoom;
        itemsCogidos = new ArrayList<>();
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
     * Comprueba si puede coger el item y si es así lo coge
     */
    public void puedeCogerItem(Item item)
    {
        if (item.getPuedeSerCogido()) {
            float pesoItem = item.getPeso();
            if (pesoItem <= pesoPuedeLlevar) {
                pesoPuedeLlevar -= pesoItem;
                cogeItem(item);
                System.out.println();
            }
            else {
                DecimalFormat form = new DecimalFormat("#.##");
                System.out.println("El objeto pesa " + pesoItem + " kg y solo puede coger " + form.format(pesoPuedeLlevar) + " kg");
                System.out.println("Debes de posar algún objeto hasta que puedas con el\n");
            }
        }
        else {
            System.out.println("Este objeto no se puede coger");
        }
    }

    /**
     * Añade items a los que tiene el jugador
     * de los que hay en la habitación en la que se encuentra
     */
    public void cogeItem(Item item)
    {
        currentRoom.removeItem(item);
        itemsCogidos.add(item);
    }

    /**
     * Permite dejar un item de los que tiene en la habitación 
     * en la que se encuentra
     */
    public void posarItem(String nombreItem)
    {
        boolean buscando = true;
        for (int i = 0; i < itemsCogidos.size() && buscando; i++) {
            if (itemsCogidos.get(i).equals(nombreItem)) {
                buscando = false;
                currentRoom.addItem(itemsCogidos.remove(i));
            }
        }
        if (buscando) {
            System.out.println("¡¡ERROR!! , no tienes ningun/a" + nombreItem + " que puedas dejar en esta habitación");
        }
    }
}
