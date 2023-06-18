package app.jbomberman;

/**
 * JbomberMan is the Main class of the program. It contains the main
 * method and so that's the entry point.
 *
 * @author gp
 */
public class JBomberMan
{

    /**
     * Entry point of the program
     *
     * @param args command line parameters
     */
    public static void main(String[] args)
    {
//        AudioManager.getInstance().play(Resources.getResourceAsStream("sounds/01 Title Screen.wav"));
//        Threads.sleep(10000);
        Controller.launch();
    }
}