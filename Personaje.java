
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
    // Habitación en la que se encuentra
    private Room currentRoom;
    // Item que pertence al personaje
    private Item item;
    // Numero máximo de veces que responde cuando el jugador habla con el
    public static final int MUMERO_MAXIMO_DE_RESPUESTAS = 5;
    // Numero de veces que va ha responder
    private int numRespuestas;
    // Número de respuesta que debe enviar al jugador
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
     * Devuelve la habitación en la que se encuentra el personaje
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Cambia la habitación en la que se encuentra el personaje
     */
    public void setCurrentRoom(Room newRoom)
    {
        currentRoom = newRoom;    
    }

    /**
     * Asigna un item al personaje que será el que pedirá al jugador
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
                System.out.println("Pósalo en la habitación y cuando lo hayas hecho deberás decir textualmente 'item posado'\n");
                respuesta++;
                charlar = false;
            }
            else if (frase.equalsIgnoreCase("no")) {
                System.out.println("Entonces lo siento pero no te podré dar ninguna información\n");
                charlar = false;
            }
            else {
                System.out.println("Solo debes de decir 'si' o 'no'\n");
            }
        }
        else if (respuesta == 3) {
            if (frase.equalsIgnoreCase("item posado")) {
                if (currentRoom.getItem(item.getRef()) == null) {
                    System.out.println("No has posado " + item.getNombre() + " iténtalo de nuevo\n");
                }
                else {
                    respuesta++;
                }
                charlar = false;            
            }
            else {
                System.out.println("Llegado a este punto sólo deberás decir 'item posado'" + 
                    " para que yo pueda comprobarlo\n");
            }
        }
        else {
            respuesta++;
            System.out.println("Ya te he dado la toda la información, como sigas insistiendo te acabaré echando de la habitación\n");
        }
        numRespuestas--; 
        System.out.println("Te quedan " + numRespuestas + " intentos\n");
        return charlar;
    }

    /**
     * Instrucciones inciales que deberá seguir el jugador 
     * para que el personaje le acabe dando la información sobre el padre
     */
    private void intruccioneIniciales()
    {
        System.out.println("Hola tato, me traes " + item.getNombre() + ", revisa tu mochila y luego deverás " +
            "decirme solo 'si' en caso de que la tengas o 'no' en caso que no."  
            + "\nSólo te responderé como máximo " + MUMERO_MAXIMO_DE_RESPUESTAS + " veces, si te pasas te echo de la habitación\n");
    }

    /**
     * Devuelve el número de veces que se puede hablar con el personaje
     */
    public int getNumRespuestas()
    {
        return numRespuestas;
    }

    /**
     * Devuelve el número de la respuesta que va ha dar
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
