
/**
 * Write a description of class Personaje here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Personaje
{
    // Nombre que describe al personaje
    private String nombre ;
    // Habitaci�n en la que se encuentra
    private Room currentRoom;
    // Item que pertence al personaje
    private Item item;
    // Numero m�ximo de veces que responde cuando el jugador habla con el
    public static final int MUMERO_MAXIMO_DE_RESPUESTAS = 5;
    // Numero de veces que va ha responder
    private int numRespuestas;
    // N�mero de respuesta que debe enviar al jugador
    private int respuesta;

    /**
     * Constructor for objects of class Personaje
     */
    public Personaje(String nombre, Room currentRoom)
    {
        this.nombre = nombre;
        this.currentRoom = currentRoom;
        item = null;
        numRespuestas = MUMERO_MAXIMO_DE_RESPUESTAS;
        respuesta = 1;
    }

    /**
     * Devuelve el nombre del personaje
     */
    public String getNombre()
    {
        return nombre;
    }

    /**
     * Devuelve el item de personaje
     */
    public Item getItem()
    {
        return item;
    }

    /**
     * Devuelve la habitaci�n en la que se encuentra el personaje
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Cambia la habitaci�n en la que se encuentra el personaje
     */
    public void setCurrentRoom(Room newRoom)
    {
        currentRoom = newRoom;    
    }

    /**
     * Asigna un item al personaje que ser� el que pedir� al jugador
     */
    public void setItem(Item item)
    {
        this.item = item;
    }

    /**
     * Permite contestar al jugador
     */
    public boolean charlar(String frase)
    {
        boolean charlar = true;
        if (respuesta == 1) {
            intruccioneIniciales();
            respuesta++;
            charlar = false;
        }
        else if (respuesta == 2) {
            if (frase.equalsIgnoreCase("si")) {
                System.out.println("P�salo en la habitaci�n y cuando lo hayas hecho deber�s decir textualmente 'item posado'\n");
                respuesta++;
                charlar = false;
            }
            else if (frase.equalsIgnoreCase("no")) {
                System.out.println("Entonces lo siento pero no te podr� dar ninguna informaci�n\n");
                charlar = false;
            }
            else {
                System.out.println("Solo debes de decir 'si' o 'no'\n");
            }
        }
        else if (respuesta == 3) {
            if (frase.equalsIgnoreCase("item posado")) {
                if (currentRoom.getItem(item.getRef()) == null) {
                    System.out.println("No has posado " + item.getNombre() + " it�ntalo de nuevo\n");
                }
                else {
                    respuesta++;
                }
                charlar = false;            
            }
            else {
                System.out.println("Llegado a este punto s�lo deber�s decir 'item posado'" + 
                    " para que yo pueda comprobarlo\n");
            }
        }
        else {
            respuesta++;
            System.out.println("Ya te he dado la toda la informaci�n, como sigas insistiendo te acabar� echando de la habitaci�n\n");
        }
        numRespuestas--; 
        System.out.println("Te quedan " + numRespuestas + " intentos\n");
        return charlar;
    }

    /**
     * Instrucciones inciales que deber� seguir el jugador 
     * para que el personaje le acabe dando la informaci�n sobre el padre
     */
    private void intruccioneIniciales()
    {
        System.out.println("Hola tato, me traes " + item.getNombre() + ", revisa tu mochila y luego dever�s " +
            "decirme solo 'si' en caso de que la tengas o 'no' en caso que no."  
            + "\nS�lo te responder� como m�ximo " + MUMERO_MAXIMO_DE_RESPUESTAS + " veces, si te pasas te echo de la habitaci�n\n");
    }

    /**
     * Devuelve el n�mero de veces que se puede hablar con el personaje
     */
    public int getNumRespuestas()
    {
        return numRespuestas;
    }

    /**
     * Devuelve el n�mero de la respuesta que va ha dar
     */
    public int getRespuesta()
    {
        return respuesta;
    }
    
    /**
     * Incrementa la respuesta que va ha dar
     */
    public void incrementaRespuesta()
    {
        respuesta++;
    }
}
