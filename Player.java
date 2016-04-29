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
    // Guarda la habitación donde coge el objeto especial
    private Room roomEspecial;
    // Pesso máximo que puede llevar el jugador
    public static final int PESO_MAXIMO_PUEDE_COJER = 5;

    /**
     * Constructor for objects of class Player
     */
    public Player(Room currentRoom)
    {
        pesoPuedeLlevar = PESO_MAXIMO_PUEDE_COJER;
        this.currentRoom = currentRoom;
        itemsCogidos = new ArrayList<>();
        backRooms = new Stack<>();
        roomEspecial = null;
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
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    public void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?\n");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!\n");
        }
        else {
            backRooms.push(currentRoom);
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    /**
     * Regresa a la habitación anterior donde hemos estado
     * Si no hay ninguna anterior muestra un mensage
     */
    public void goBackRoom()
    {
        if (backRooms.empty()) {
            System.out.println("No es posible volver a la localización anterior\n");
        }
        else {
            currentRoom = backRooms.pop();
            printLocationInfo();
        }
    }

    /**
     * Si la orden para coger el item es correcta 
     * coge el item de la habitación, si este se puede coger y 
     * si no es correcta o no se puede coger muestra un mensaje
     */
    public void cogeItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("¿Que quieres coger?\n");
            return;
        }

        //Comprueba si el segundo comando es un número
        int numItem;        
        try { 
            numItem = Integer.parseInt(command.getSecondWord());
        } 
        catch (NumberFormatException ex){
            System.out.println("¡¡ERROR!! La referencia debe de ser un número\n");
            return;
        }

        Item item = currentRoom.getItem(numItem);
        if (item != null) {
            puedeCogerItem(item);
        }
        else {
            System.out.println("En esta habitación no se encuentra el item con esa referencia");
        }  
        System.out.println();
    }

    /**
     * Comprueba si puede coger el item y si es así lo coge
     */
    private void puedeCogerItem(Item item)
    {
        if (item.getPuedeSerCogido()) {
            float pesoItem = item.getPeso();
            if (pesoItem <= pesoPuedeLlevar) {
                pesoPuedeLlevar -= pesoItem;
                currentRoom.removeItem(item);
                itemsCogidos.add(item);
                System.out.println("Cogido item:   " + item.getDescription());
                if (item.getRef() == 1) {
                    roomEspecial = currentRoom;
                }
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
     * Si la orden para posar el item es correcta y la
     * referencia del item, posa el item en la habitación,
     * si no es correcta o la referencia del item no es correcta
     * muestra un mensaje avisándonos de ello.
     */
    public Item posaItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("¿Qué quieres posar?\n");
            return null;
        }

        int numItem;        
        try { 
            numItem = Integer.parseInt(command.getSecondWord());
        } 
        catch (NumberFormatException ex){
            System.out.println("¡¡ERROR!! La referencia debe de ser un número\n");
            return null;
        }
        Item item = posaItem(numItem);
        System.out.println();
        return item;
    }

    /**
     * Permite dejar un item de los que tiene en la habitación 
     * en la que se encuentra
     */
    private Item posaItem(int numRef)
    {
        Item item = null;
        boolean buscando = true;
        int numeroItems = itemsCogidos.size();
        for (int i = 0; i < numeroItems && buscando; i++) {
            if (itemsCogidos.get(i).getRef() == numRef) {
                buscando = false;
                pesoPuedeLlevar += itemsCogidos.get(i).getPeso();
                System.out.println("Posado item:   " + itemsCogidos.get(i).getDescription());
                if (numRef == 1) {
                    currentRoom = roomEspecial;
                    roomEspecial = null;
                    item = itemsCogidos.remove(i);
                }
                else {
                    item = itemsCogidos.remove(i);
                    currentRoom.addItem(item);
                } 
            }
        }
        if (buscando) {
            System.out.println("No tienes el item con referencia " + numRef);
        }
        return item;
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

    /**
     * Muestra la información de la habitación en la que te encuentras 
     * y las posibles salidas que se pueden tomar
     */
    public void printLocationInfo()
    {
        System.out.println(currentRoom.getLongDescription());
    }    
}
