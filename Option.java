/**
 * Enumeration class Option - write a description of the enum class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public enum Option
{
    GO("andare"), QUIT("smettere"), HELP("aiuto"), LOOK("guarda"), EAT("mangiare"), BACK("indietro"), TAKE("prendere"), 
    DROP("far_cadere"), ITEMS("elementi"), UNKNOWN("");
    
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

