package app.jbomberman;

import app.jbomberman.controller.Controller;
import app.jbomberman.view.graphics.javafx.JavaFXView;

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
        JavaFXView.initialize(() -> new Controller(new JavaFXView()));
    }
}
