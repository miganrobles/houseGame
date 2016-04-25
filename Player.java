import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.Stack;

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
    // Guarda las habitaciones por las que vamos pasando para poder ir atrás
    private Stack<Room> backRooms;

    /**
     * Constructor for objects of class Player
     */
    public Player(Room currentRoom)
    {
        pesoPuedeLlevar = 5;
        this.currentRoom = currentRoom;
        itemsCogidos = new ArrayList<>();
        backRooms = new Stack<>();
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
     * Va a una nueva habitación y guarda
     * la habitación actual por si queremos
     * retroceder en nuestro recorrido
     */
    public void goNewRoom(Room newRoom)
    {        
        backRooms.push(currentRoom);
        currentRoom = newRoom;
    }

    /**
     * Devuelve true si no hay ninguna habitacíon donde retroceder
     */
    public boolean backRoomsIsEmpty()
    {
        return backRooms.empty();
    }

    /**
     * Regresa a la última habitación anterior donde hemos estado
     */
    public void goBackRoom()
    {
        currentRoom = backRooms.pop();
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
                System.out.println("Cogido item:   " + item.getDescription());
            }
            else {
                DecimalFormat form = new DecimalFormat("#.##");
                System.out.println("El item pesa " + pesoItem + " kg y solo puedes coger " 
                    + form.format(pesoPuedeLlevar) + " kg");
                System.out.println("Debes de posar algún item hasta que puedas con él");
            }
        }
        else {
            System.out.println("Este item no se puede coger");
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
    public void posarItem(int numRef)
    {
        boolean buscando = true;
        int numeroItems = itemsCogidos.size();
        for (int i = 0; i < numeroItems && buscando; i++) {
            if (itemsCogidos.get(i).getRef() == numRef) {
                buscando = false;
                pesoPuedeLlevar += itemsCogidos.get(i).getPeso();
                System.out.println("Posado item:   " + itemsCogidos.get(i).getDescription());
                currentRoom.addItem(itemsCogidos.remove(i));
            }
        }
        if (buscando) {
            System.out.println("No tienes el item con referencia " + numRef);
        }
    }

    /**
     * Muestra la información de todos los item que el jugador lleva con sigo
     */
    public void mostrarItems()
    {
        System.out.println("Items que llevas encima: ");
        for (Item item : itemsCogidos) {
            System.out.println(item.getDescription());
        }
        System.out.println();
    }
}
