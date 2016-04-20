import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Stack<Room> backRooms;
    private Player player;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        backRooms = new Stack<>();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room salida, salon, cocina, salaDeInvitados, roomHermana, roomPadres,
        roomHijo, roomHermano;  

        // creamos las habitaciones y las guardamos en un ArrayList
        ArrayList<Room> rooms = new ArrayList<>();    

        salida = new Room("la salida"); rooms.add(salida);       
        salon = new Room("el salón"); rooms.add(salon);        
        cocina = new Room("la cocina"); rooms.add(cocina);       
        salaDeInvitados = new Room("la sala de invitados"); rooms.add(salaDeInvitados);       
        roomHermana = new Room("la habitación de tu hermana"); rooms.add(roomHermana);        
        roomPadres = new Room("la habitación de tus padres"); rooms.add(roomPadres);        
        roomHermano = new Room("la habitación de tu hermano el llorón"); rooms.add(roomHermano); 
        roomHijo = new Room("tu habitación"); rooms.add(roomHijo); 

        // Establecemos las salidas que tiene cada habitación
        final String n = "north", e = "east", s = "south", w = "west",
        sE = "southEast", nW = "northWest";

        // initialise room exits
        salida.setExit(n, salaDeInvitados);
        salida.setExit(e, salon);

        salon.setExit(n, cocina);
        salon.setExit(w, salida);
        salon.setExit(nW, salaDeInvitados);

        cocina.setExit(n, roomHermano);
        cocina.setExit(s, salon);
        cocina.setExit(w, salaDeInvitados);
        cocina.setExit(nW, roomHijo);

        salaDeInvitados.setExit(n,roomHijo );
        salaDeInvitados.setExit(e, cocina);
        salaDeInvitados.setExit(s, salida);
        salaDeInvitados.setExit(w, roomHermana);
        salaDeInvitados.setExit(sE, salon);
        salaDeInvitados.setExit(nW, roomPadres);

        roomHermana.setExit(n, roomPadres);
        roomHermana.setExit(e, salaDeInvitados);

        roomPadres.setExit(e, roomHijo);
        roomPadres.setExit(s, roomHermana);
        roomPadres.setExit(sE, salaDeInvitados);

        roomHermano.setExit(s, cocina);
        roomHermano.setExit(w, roomHijo);

        roomHijo.setExit(e, roomHermano);
        roomHijo.setExit(s, salaDeInvitados);
        roomHijo.setExit(w, roomPadres);
        roomHijo.setExit(sE, cocina);

        // Creamos el jugados y lo situamos en su habitación
        player = new Player(roomHijo);

        // Creamos los items que vamos a colocar en las habitaciones
        // y los vamos añadiendo escojiendo las habitaciones al azahar
        String[] nombresObjetos = {"refrescos", "cartera", "llaves", "pizzas", "bocadillos",
                        "hielos", "mochila", "portatil", "altavoces", "radioCD"};
        float[] pesoObjetos = {2.5F, 0.3F, 0.2F, 2, 2, 1.2F, 1.8F, 1.5F, 1.3F, 1.7F};
        ArrayList<Boolean> puedeCoger = new ArrayList<>(Arrays.asList(true, true,
                            true, true, true, true, true, false, false, false));
        
        Collections.shuffle(puedeCoger);
        Random alternativo = new Random();
        // Podemos coger los 7 primeros objetos y los otros no
        int numeroRooms = rooms.size();
        for(int i = 0; i < nombresObjetos.length; i++) {
            rooms.get(alternativo.nextInt(numeroRooms)).addItem(new Item(nombresObjetos[i], pesoObjetos[i], puedeCoger.get(i)));
        } 
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);         
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the jungle");
        System.out.println("World of jungle is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            printLocationInfo();
        }
        else if (commandWord.equals("eat")) {
            System.out.println("You have eaten now and you are not hungry any more\n");
        }
        else if (commandWord.equals("back")) {
            if (backRooms.empty()) {
                System.out.println("No es posible volver a la localización anterior\n");
            }
            else {
                player.setCurrentRoom(backRooms.pop());
                printLocationInfo();
            }
        }
        else if (commandWord.equals("take")) {
            cogeItem(command);
        }
        else if (commandWord.equals("drop")) {
            posaItem(command);
        }
        return wantToQuit;
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("the party is in the university.");
        System.out.println();
        parser.muestraComandos();

    }
    
    /** 
     * Si la orden para coger el objeto es correcta 
     * coge el objeto de la habitación, si este se puede coger y 
     * si no es correcta o no se puede coger muestra un mensaje de error
     */
    private void cogeItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Debes introducir el nombre del objeto que quieres coger");
            return;
        }
        
        String nombreItem = command.getSecondWord();
        Item item = player.getCurrentRoom().getItem(nombreItem); 
        if (item != null) {
            player.puedeCogerItem(item);
        }
        else {
            System.out.println("En esta habitación no hay ningún/a " + nombreItem);
        }  
        System.out.println();
    }
    
    /**
     * Posa un objeto de los que lleva en la habitación donde se encuentra
     */
    private void posaItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Debes introducir el nombre del objeto que quieres posar");
            return;
        }
        player.posarItem(command.getSecondWord());
        System.out.println();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room actualRoom = player.getCurrentRoom();
        Room nextRoom = actualRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            backRooms.push(actualRoom);
            player.setCurrentRoom(nextRoom);
            printLocationInfo();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Muestra la información de la habitación en la que te encuentras 
     * y las posibles salidas que se pueden tomar
     */
    private void printLocationInfo()
    {
        System.out.println(player.getCurrentRoom().getLongDescription());
    }
}