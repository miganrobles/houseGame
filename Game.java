import java.util.Random;
import java.util.Stack;

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
     
            // create the rooms
        salida = new Room("la salida");
        salida.addItem(new Item("un paraguas",1));
        salon = new Room("el salón");
        salon.addItem(new Item("una botella de coca-cola", 1));
        salon.addItem(new Item("una bolsa con botellas" , 10));
        cocina = new Room("la cocina");
        cocina.addItem(new Item("los hielos", 1));
        salaDeInvitados = new Room("la sala de invitados");
        salaDeInvitados.addItem(new Item("una bolsa con bocadillos", 7));
        roomHermana = new Room("la habitación de tu hermana");
        roomPadres = new Room("la habitación de tus padres");
        roomHermano = new Room("la habitación de tu hermano el llorón");

        roomHijo = new Room("tu habitación");
        roomHijo.addItem(new Item("la cartera", 0.5F));

        final String n = "north";
        final String e = "east";
        final String s = "south";
        final String w = "west";
        final String sE = "southEast";
        final String nW = "northWest";
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

        player = new Player(roomHijo);
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