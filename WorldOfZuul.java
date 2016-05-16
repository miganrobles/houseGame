
/**
 * Write a description of class WorldOfZuul here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WorldOfZuul
{
    // Almacena nuestro juego
    private static Game game; 
    
    /**
     * Este método es ejecutable sin necesidad de crear un objeto
     */
    public static void main(String[] args) 
    {
        game = new Game();
        game.play();
    }
}
