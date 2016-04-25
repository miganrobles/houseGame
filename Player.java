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
    // Guarda la localizaci�n en la que se encuentra el jugador
    private Room currentRoom;
    // Guarda los items que va cogiendo el jugador
    private ArrayList<Item> itemsCogidos;
    // Peso que puede transportar el jugador
    private float pesoPuedeLlevar;
    // Guarda las habitaciones por las que vamos pasando para poder ir atr�s
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
     * Regresa a la habitaci�n anterior donde hemos estado
     * Si no hay ninguna anterior muestra un mensage
     */
    public void goBackRoom()
    {
        if (backRooms.empty()) {
            System.out.println("No es posible volver a la localizaci�n anterior\n");
        }
        else {
            currentRoom = backRooms.pop();
            printLocationInfo();
        }
    }
    
    /**
     * Si la orden para coger el item es correcta 
     * coge el item de la habitaci�n, si este se puede coger y 
     * si no es correcta o no se puede coger muestra un mensaje
     */
    public void cogeItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("�Que quieres coger?\n");
            return;
        }

        //Comprueba si el segundo comando es un n�mero
        int numItem;        
        try { 
            numItem = Integer.parseInt(command.getSecondWord());
        } 
        catch (NumberFormatException ex){
            System.out.println("��ERROR!! La referencia debe de ser un n�mero\n");
            return;
        }

        Item item = currentRoom.getItem(numItem);
        if (item != null) {
            puedeCogerItem(item);
        }
        else {
            System.out.println("En esta habitaci�n no se encuentra el item con esa referencia");
        }  
        System.out.println();
    }

    /**
     * Comprueba si puede coger el item y si es as� lo coge
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
            }
            else {
                DecimalFormat form = new DecimalFormat("#.##");
                System.out.println("El item pesa " + pesoItem + " kg y solo puedes coger " 
                    + form.format(pesoPuedeLlevar) + " kg");
                System.out.println("Debes de posar alg�n item hasta que puedas con �l");
            }
        }
        else {
            System.out.println("Este item no se puede coger");
        }
    }
    
    /**
     * Si la orden para posar el item es correcta y la
     * referencia del item, posa el item en la habitaci�n,
     * si no es correcta o la referencia del item no es correcta
     * muestra un mensaje avis�ndonos de ello.
     */
    public void posaItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("�Qu� quieres posar?\n");
            return;
        }

        int numItem;        
        try { 
            numItem = Integer.parseInt(command.getSecondWord());
        } 
        catch (NumberFormatException ex){
            System.out.println("��ERROR!! La referencia debe de ser un n�mero\n");
            return;
        }
        posaItem(numItem);
        System.out.println();
    }

    /**
     * Permite dejar un item de los que tiene en la habitaci�n 
     * en la que se encuentra
     */
    private void posaItem(int numRef)
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
     * Muestra la informaci�n de todos los item que el jugador lleva con sigo
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
     * Muestra la informaci�n de la habitaci�n en la que te encuentras 
     * y las posibles salidas que se pueden tomar
     */
    public void printLocationInfo()
    {
        System.out.println(currentRoom.getLongDescription());
    }    
}
