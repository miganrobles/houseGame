
/**
 * Write a description of class Item here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    // Nombre que describe el tipo de item
    private String nombre;
    // Peso del item
    private float peso;
    // Determina si el item puede ser cogido
    private boolean puedeSerCogido;
    // Número de referencia que tiene el objeto especial
    public static final int REF_OBJETO_ESPECIAL = 1;
    // Asigna un número de referencia al item según se van creando
    private static int numRef = REF_OBJETO_ESPECIAL;
    // Guarda el número de referencia del item
    private int ref;
    
    
    /**
     * Constructor for objects of class Item
     */
    public Item(String nombre, float peso, boolean puedeSerCogido)
    {
        this.nombre = nombre;
        this.peso = peso;
        this.puedeSerCogido = puedeSerCogido;
        ref = numRef;
        numRef++;
    }

    /**
     * Devuelve el nombre descriptivo del item
     */
    public String getNombre()
    {
        return nombre;
    }
    
    /**
     * Devuelve el peso del item
     */
    public float getPeso()
    {
        return peso;
    }
    
    /**
     * Devuelve una pequeña descripión con el nombre y el peso
     */
    public String getDescription()
    {
        return "Ref. " + String.format("%03d", ref)+ "   "+ nombre + ", peso: " + peso + " kg";
    }
    
    /**
     * Devuelve verdadero si el item puede ser cogido
     */
    public boolean getPuedeSerCogido()
    {
        return puedeSerCogido;
    }
    
    /**
     * Devuelve el número de referencia del item
     */
    public int getRef()
    {
        return ref;
    }
}
