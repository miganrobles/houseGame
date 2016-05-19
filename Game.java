import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
     * Crea los personajes del juego y coloca los items
     */
    private void createRooms()
    {
        Room salida, salon, cocina, salaDeInvitados, roomHermana, roomPadres,
        roomHijo, roomHermano;      

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
        // Creamos al padre y lo colocamos en una habitación
        padre = new Personaje("Padre",salaDeInvitados);    
        // Colocamos los items en las habitaciones
        colocarItems(salida);

        // Creamos a los hermanos y los colocamos en habitaciones al azar
        colocarHermanos(salida);
    }

    /**
     * Metodo que crea y coloca a los hermanos en el juego, siempre en habitaciones
     * distintas y nunca ni en la habitación en la que se encuentra el jugador
     * inicialmente ni en la salida
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
     * Asigna de forma aleatoria un item a cada hermano, el item nunca será el mismo que 
     * tiene el hermano, ni el objeto especial y siempre será uno que pueda ser cojido
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
     *  Metodo que coloca los items de manera aleatoria por todas las habitaciones
     *  excepto en la habitación donde se encuentra el jugador inicialmente ni en la salida
     */
    private void colocarItems(Room room)
    {
        // Creamos los items que vamos a colocar en las habitaciones
        // y los vamos añadiendo escojiendo las habitaciones al azahar
        String[] nombresItems = {"los refrescos", " la cartera", "las llaves", "las pizzas", "la bolsa de bocadillos",
                "los hielos", " la mochila", "el portatil", " los altavoces", "el equipo de música"};
        float[] pesoItems = {2.5F, 0.3F, 0.2F, 2, 2, 1.2F, 1.8F, 1.5F, 1.3F, 1.7F};
        // Se podrán coger solo 7 items
        ArrayList<Boolean> puedeCoger = new ArrayList<>(Arrays.asList(true, true,
                    true, true, true, true, true, false, false, false));
        Collections.shuffle(puedeCoger);

        // Añadimos el objeto especial
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
        System.out.println("¡¡¡ OJO !!!  No cojas la salida 'south', ahora mismo hay se encuentra tu padre\n");
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
            return accionMovimiento(player.goRoom(command));

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
            return accionMovimiento(player.goBackRoom());

            case TAKE:
            player.cogeItem(command);
            break;

            case DROP:
            recolocarEspecial(player.posaItem(command));
            break;

            case ITEMS:
            player.mostrarItems();
            break;

            case TALK:
            mostrarSituacionPadre(player.talk());
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
     * Devuelve una habitación al azar
     * Nunca será donde esta el jugador ni la pasada como paramétro
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
     * Coloca el item especial en una habitación al azar
     */
    private void recolocarEspecial(Item item)
    {
        if (item != null) {
            if (item.getRef() == Item.REF_OBJETO_ESPECIAL) {
                azarRoom(rooms.get(0)).addItem(item);
            }
        }
    }

    /**
     * Recoloca al padre en una de las habitaciones al azar que 
     * no puedrá ser en la que está el jugador actualmente
     */
    private void recolocarPadre()
    {
        padre.setCurrentRoom(azarRoom(null));
    }

    /**
     * Despues de realizar un movimiento, este método procesa la opciones devueltas
     */
    private boolean accionMovimiento(int valor)
    {
        boolean finalizarJuego = false;
        if (player.getCurrentRoom() == padre.getCurrentRoom()) {
            System.out.println("\n¡¡¡ TE PILLO TU PADRE !!!");
            // Ponemos un tiempo de espera para darle emoción
            try
            {
                Thread.sleep(3000);
            } 
            catch (InterruptedException e)
            {
                // ignoring exception at the moment
            }
            Item item = player.posaItem(Item.REF_OBJETO_ESPECIAL);
            if (item != null) {
                System.out.println("Has tenido suerte, tenías el objeto especial y te has salvado\n");
                recolocarEspecial(item);
                player.printLocationInfo();
            }
            else {                
                System.out.println("-----GAME OVER-----");
                try
                {
                    Thread.sleep(3000);
                } 
                catch (InterruptedException e)
                {
                    // ignoring exception at the moment
                }
                finalizarJuego = true;
            }
        }
        else if (player.getCurrentRoom().getDescription().equalsIgnoreCase("la salida")) {
            System.out.println("¡¡¡FELICIDADES!!!");
            System.out.println("\nLo has logrado, estás en la salida y ya puedes salir de fiesta");
            System.out.println("-----GAME OVER-----");
            try
            {
                Thread.sleep(3000);
            } 
            catch (InterruptedException e)
            {
                // ignoring exception at the moment
            }
            finalizarJuego = true;
        }
        else if (valor == 0) {
            recolocarPadre();
        }
        return finalizarJuego;
    }

    /**
     * Muestra la situación del padre si después de hablar con el personaje si este le ha
     * llegado a dar la respuesta 4 que se supone que es donde le dice la salida que no
     * debe de tomar o la situación del padre
     */
    private void mostrarSituacionPadre(Personaje personaje)
    {
        if (personaje != null && personaje.getRespuesta() == 4) {
            int numItem = personaje.getItem().getRef();
            if (personaje.getCurrentRoom().getItem(numItem) != null) {
                HashMap<String, Room> salidas = personaje.getCurrentRoom().roomsExits();
                Iterator it = salidas.entrySet().iterator();
                boolean buscando = true;
                while (it.hasNext() && buscando) {
                    Map.Entry e = (Map.Entry)it.next();
                    if (padre.getCurrentRoom() == e.getValue()) {
                        System.out.println("No salgas por la salida " + e.getKey() + " que está papá\n");
                        buscando = false;
                    }           
                }
                if (buscando) {
                    System.out.println("Papá ahora mismo está en " + padre.getCurrentRoom().getDescription() + "\n");
                }
            }
        }
    }
}