import java.util.Random;
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
    private Player player;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
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
        String[] nombresItems = {"refrescos", "cartera", "llaves", "pizzas", "bocadillos",
                "hielos", "mochila", "portatil", "altavoces", "radioCD"};
        float[] pesoItems = {2.5F, 0.3F, 0.2F, 2, 2, 1.2F, 1.8F, 1.5F, 1.3F, 1.7F};
        ArrayList<Boolean> puedeCoger = new ArrayList<>(Arrays.asList(true, true,
                    true, true, true, true, true, false, false, false));

        Collections.shuffle(puedeCoger);
        Random alternativo = new Random();
        // Podemos coger los 7 primeros items y los otros no
        int numeroRooms = rooms.size();
        for(int i = 0; i < nombresItems.length; i++) {
            rooms.get(alternativo.nextInt(numeroRooms)).addItem(new Item(nombresItems[i], pesoItems[i], puedeCoger.get(i)));
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
        System.out.println("Type 'aiuto' if you need help.");
        System.out.println();
        player.printLocationInfo();
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
            System.out.println("I don't know what you mean...\n");
            return false;
        }

        Option commandWord = command.getCommandWord();
        switch (commandWord) {
            case HELP: 
            printHelp();
            break;
            
            case GO: 
            player.goRoom(command);
            break;
            
            case QUIT: 
            wantToQuit = quit(command);
            break;
            
            case LOOK:
            player.printLocationInfo();
            break;
            
            case EAT:
            System.out.println("You have eaten now and you are not hungry any more\n");
            break;
            
            case BACK:
            player.goBackRoom();
            break;
            
            case TAKE:
            player.cogeItem(command);
            break;
            
            case DROP:
            player.posaItem(command);
            break;
            
            case ITEMS:
            player.mostrarItems();
            break;
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
}