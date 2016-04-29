/**
 * Enumeration class Option - write a description of the enum class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public enum Option
{
    GO("go"), QUIT("quit"), HELP("help"), LOOK("look"), EAT("eat"), BACK("back"), TAKE("take"), 
    DROP("drop"), ITEMS("items"), UNKNOWN("");
    
    private String comando;
    
    /**
     * Constructor de la clase Option
     */
    Option (String comando) 
    {
        this.comando = comando;
    }
    
    /**
     * Devuelve un String con nombre del comando
     */
    public String getComando()
    {
        return comando;
    }
}

