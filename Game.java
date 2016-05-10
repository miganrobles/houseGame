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
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Player player;
    // Guarda las habitaciones para ir recolocando a los personajes
    private ArrayList<Room> rooms;
    // Atributo para el personaje del padre
    private Personaje padre;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        rooms = new ArrayList<>();
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

        salida = new Room("la salida"); rooms.add(salida);       
        salon = new Room("el sal�n"); rooms.add(salon);        
        cocina = new Room("la cocina"); rooms.add(cocina);       
        salaDeInvitados = new Room("la sala de invitados"); rooms.add(salaDeInvitados);       
        roomHermana = new Room("la habitaci�n de tu hermana"); rooms.add(roomHermana);        
        roomPadres = new Room("la habitaci�n de tus padres"); rooms.add(roomPadres);        
        roomHermano = new Room("la habitaci�n de tu hermano el llor�n"); rooms.add(roomHermano); 
        roomHijo = new Room("tu habitaci�n"); rooms.add(roomHijo); 

        // Establecemos las salidas que tiene cada habitaci�n
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

        // Creamos el jugados y lo situamos en su habitaci�n
        player = new Player(roomHijo);
        // Creamos al padre y lo colocamos en una habitaci�n
        padre = new Personaje("Padre",salaDeInvitados);    
        // Colocamos los items en las habitaciones
        colocarItems(salida);

        // Creamos a los hermanos y los colocamos en habitaciones al azar
        colocarHermanos(salida);
    }

    /**
     * Metodo que crea y coloca a los hermanos en el juego
     */
    private void colocarHermanos(Room room) 
    {        
        Room roomSituadoHermano = azarRoom(room);
        Personaje hermano = new Personaje("hermano", roomSituadoHermano);
        roomSituadoHermano.setPersonaje(hermano);
        do {
            roomSituadoHermano = azarRoom(room);
        } while (hermano.getCurrentRoom() == roomSituadoHermano);
        Personaje hermana = new Personaje("hermana", roomSituadoHermano);
        roomSituadoHermano.setPersonaje(hermana);
        // Les asignamos los items a cada uno
        asignarItemHermanos(hermano, hermana);
        asignarItemHermanos(hermana, hermano);
    }

    /**
     * Asigna de forma aleatoria un item a cada hermano
     */
    private void asignarItemHermanos(Personaje personaje, Personaje hermano)
    {
        boolean asignadoItem = false;
        Random aleatorio = new Random();
        while (!asignadoItem) {
            Room room = azarRoom(personaje.getCurrentRoom());
            if ((!room.getItems().isEmpty())) {
                Item item = room.getItems().get(aleatorio.nextInt(room.getItems().size()));
                if ((hermano.getItem() != item) && (item.getRef() != Item.REF_OBJETO_ESPECIAL)
                && item.getPuedeSerCogido()) {
                    personaje.setItem(item);
                    asignadoItem = true;
                }
            }         
        }
    }

    /*
     *  Metodo que coloca los items por las habitaciones
     *  de manera aleatoria por las habitaciones
     */
    private void colocarItems(Room room)
    {
        // Creamos los items que vamos a colocar en las habitaciones
        // y los vamos a�adiendo escojiendo las habitaciones al azahar
        String[] nombresItems = {"los refrescos", " la cartera", "las llaves", "las pizzas", "la bolsa de bocadillos",
                "los hielos", " la mochila", "el portatil", " los altavoces", "el equipo de m�sica"};
        float[] pesoItems = {2.5F, 0.3F, 0.2F, 2, 2, 1.2F, 1.8F, 1.5F, 1.3F, 1.7F};
        // Se podr�n coger solo 7 items
        ArrayList<Boolean> puedeCoger = new ArrayList<>(Arrays.asList(true, true,
                    true, true, true, true, true, false, false, false));
        Collections.shuffle(puedeCoger);

        // A�adimos el objeto especial
        azarRoom(room).addItem(new Item("Objeto especial", 1, true));
        for(int i = 0; i < nombresItems.length; i++) {
            azarRoom(room).addItem(new Item(nombresItems[i], pesoItems[i], puedeCoger.get(i)));
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
        System.out.println("Type '" + Option.HELP.getComando() + "' if you need help.\n");
        System.out.println("��� OJO !!!  No cojas la salida 'south', ahora mismo hay se encuentra tu padre\n");
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
            recolocarEspecial(player.posaItem(command));
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

    /**
     * Devuelve una habitaci�n al azar
     * Nunca ser� donde esta el jugador ni las pasadas como param�tros
     */
    private Room azarRoom(Room room)
    {
        Random alternativo = new Random();
        int numeroRooms = rooms.size();
        Room azarRoom = null;
        do {
            azarRoom = rooms.get(alternativo.nextInt(numeroRooms));
        } while(azarRoom == player.getCurrentRoom() || azarRoom == room);
        return azarRoom;
    }

    /**
     * Coloca el item especial en una habitaci�n al azar
     */
    private void recolocarEspecial(Item item)
    {
        if (item != null) {
            if (item.getRef() == Item.REF_OBJETO_ESPECIAL) {
                azarRoom(rooms.get(0)).addItem(item);
            }
        }
    }
}