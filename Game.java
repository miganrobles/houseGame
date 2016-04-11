import java.util.Random;

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
    private Room currentRoom;
    private int numeroIntentos;   
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        numeroIntentos = 2;
    }

    /**
     * Situa al padre en una habitación cogida al azahar
     */
    private void colocarPadre(Room[] rooms)
    {
        Random aleatorio = new Random();
        int habitacionColocamosAlPadre = aleatorio.nextInt(rooms.length);
        String[] descriptionTroceada = rooms[habitacionColocamosAlPadre].getDescription().split(","); 
        String description = descriptionTroceada[0];
        description += "\nDonde te crees que vas, arranca para la cama echando mistos";
        rooms[habitacionColocamosAlPadre].setDescription(description);
    }

    /**
     *  Comienza el juego desde el principio cuando te pilla tu padre
     *  si te quedan más intentos
     */
    private void masIntentos()
    {
        System.out.println();
        System.out.println();
        System.out.println("Te quedan " + numeroIntentos + " intentos");
        System.out.println("Comienzas de nuevo");
        System.out.println();
        System.out.println();

        createRooms();// Comienzas de nuevo
        printLocationInfo();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room salida, salon, cocina, salaDeInvitados, roomHermana, roomPadres,
        roomHijo, roomHermano;

        // create the rooms
        salida = new Room("en la puerta, ya puedes salir de parranda");
        salon = new Room("en el salón, la noche es tuya");
        cocina = new Room("en la cocina, coge los hielos");
        salaDeInvitados = new Room("en la sala de invitados, valla noche que te espera");
        roomHermana = new Room("en la habitación de tu hermana, no hagas ruido que está durmiendo");
        roomPadres = new Room("en la habitación de tus padres, pero ojo que el viejo está de guardia");
        roomHermano = new Room("en la habitación de tu hermano el llorón, que no se despierte que la lias");

        roomHijo = new Room("echándote gomina para el pelo, hoy triunfas");

        // creamos un array de habitaciones para colocar al padre en una al azahar
        Room[] habitacionesPuedeEstarPadre = new Room[7]; 

        habitacionesPuedeEstarPadre[0] = salon;
        habitacionesPuedeEstarPadre[1] = cocina;
        habitacionesPuedeEstarPadre[2] = salaDeInvitados;
        habitacionesPuedeEstarPadre[3] = roomHermana;
        habitacionesPuedeEstarPadre[4] = roomPadres;
        habitacionesPuedeEstarPadre[5] = roomHermano;
        habitacionesPuedeEstarPadre[6] = salida;

        colocarPadre(habitacionesPuedeEstarPadre);        

        // initialise room exits
        salida.setExits(salaDeInvitados, salon, null, null);
        salon.setExits(cocina, null, null, salida);
        cocina.setExits(roomHermano, null, salon, salaDeInvitados);
        salaDeInvitados.setExits(roomHijo, cocina, salida, roomHermana);
        roomHermana.setExits(roomPadres, salaDeInvitados, null, null);
        roomPadres.setExits(null, roomHijo, roomHermana, null);
        roomHijo.setExits(null, roomHermano, salaDeInvitados, roomPadres);
        roomHermano.setExits(null, null, cocina, roomHijo);

        currentRoom = roomHijo;  // start game outside
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
            if (numeroIntentos == 0) {
                finished = true;
            }
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

        return wantToQuit;
    }

    // implementations of user commands:

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
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
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
        Room nextRoom = null;
        if(direction.equals("north")) {
            nextRoom = currentRoom.northExit;
        }
        if(direction.equals("east")) {
            nextRoom = currentRoom.eastExit;
        }
        if(direction.equals("south")) {
            nextRoom = currentRoom.southExit;
        }
        if(direction.equals("west")) {
            nextRoom = currentRoom.westExit;
        }

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else if ((nextRoom.getDescription().contains("Donde te crees que vas, arranca para la cama echando mistos"))) {
            numeroIntentos--;// Te pilla tu padre          
            if (numeroIntentos > 0) { // Si te quedan más intentos
                System.out.println("Te encuentras " + nextRoom.getDescription());
                masIntentos();
            }
            else {
                System.out.println();
                System.out.println("Te pillé de nuevo");
                System.out.println("Se te acabaron las oprotunidades, te quedas sin ¡¡ ESPICHA !! todo el més");
            }
        }
        else if (nextRoom.getDescription().equals("en la puerta, ya puedes salir de parranda")) {
            numeroIntentos = 0;
            System.out.println("Te encuentras " + nextRoom.getDescription());
            System.out.println();
            System.out.println("Pásalo en grande que la noche es joven");
        }
        else {
            currentRoom = nextRoom;
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
     * Muestra la habitación en la que te encuentras 
     * y las posibles salidas que se pueden tomar
     */
    private void printLocationInfo()
    {
        System.out.println("Te encuentras " + currentRoom.getDescription());
        System.out.print("Exits: ");
        if(currentRoom.northExit != null) {
            System.out.print("north ");
        }
        if(currentRoom.eastExit != null) {
            System.out.print("east ");
        }
        if(currentRoom.southExit != null) {
            System.out.print("south ");
        }
        if(currentRoom.westExit != null) {
            System.out.print("west ");
        }
        System.out.println();
    }
}
