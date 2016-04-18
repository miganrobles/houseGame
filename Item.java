
/**
 * Write a description of class Item here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    // Nombre que describe el tipo de objeto
    private String nombre;
    // Peso del objeto
    private float peso;

    /**
     * Constructor for objects of class Item
     */
    public Item(String nombre, float peso)
    {
        this.nombre = nombre;
        this.peso = peso;
    }

    /**
     * Devuleve el nombre descriptivo del objeto
     */
    public String getNombre()
    {
        return nombre;
    }
    
    /**
     * Devuelve el peso del objeto
     */
    public float getPeso()
    {
        return peso;
    }
    public String getDescription()
    {
        return nombre + ", peso: " + peso + " kg";
    }
}
