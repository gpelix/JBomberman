package app.jbomberman.controller;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.model.Model;
import app.jbomberman.util.Observer;
import app.jbomberman.view.View;
import javafx.animation.AnimationTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller is the controller class in the MVC pattern on which the program
 * is based. The controller is responsible for the communication between the graphic
 * layer and the model in the application. It also sets up the View
 *
 * @author gp
 * @see Model
 * @see View
 */
public class Controller
{
    /**
     * view is the View in our program
     */
    private final View view;
    /**
     * logger is an instance of a logger for the Controller
     */
    private final Logger logger;

    private final KeyBoardController keyBoardController;

    /**
     * Controller() is the only constructor of the controller
     */
    public Controller()
    {
        logger = LogManager.getLogger(getClass());

        view = new View();

        keyBoardController = new KeyBoardController();
        keyBoardController.attachToScene(view.getScene());


        Model.setController(this);
        Model.getInstance().setup();
        Model.getInstance().attachObserver(view);

        new AnimationTimer()
        {
            @Override
            public void handle(long ignored)
            {
                Model.getInstance().update();
            }
        }.start();


        view.setTitle(Properties.getInstance().getApplicationName());
        view.setIcon(Properties.getInstance().getIconFileName());
        view.setCloseOperationCallBack(Model.getInstance()::close);
    }

    /**
     * close() function must be used during the shutdown process of the
     * application
     *
     * @implNote it closes the view and stops the AudioPlayer. Not closing the
     * audio player will prevent the JVM from exiting
     */
    public void close()
    {
        logger.info("Closing Controller");
        AudioPlayer.getInstance().close();
        view.close();
    }

    /**
     * attachKeyBoardObserver() must be used to attach an observer to the
     * keyBoardController
     *
     * @param observer Observer of the controller
     * @see Observer
     */
    public void attachKeyBoardObserver(Observer observer)
    {
        keyBoardController.attachObserver(observer);
    }

    /**
     * detachKeyBoardObserver() must be used to detach an observer from the
     * keyBoardController
     *
     * @param keyBoardObserver the observer of the KeyDoardController
     * @see Observer
     */
    public void detachKeyBoardObserver(Observer keyBoardObserver)
    {
        keyBoardController.detachObserver(keyBoardObserver);
    }

    /**
     * launch() is the function that must be called during the bootstrap process
     * The callback passed to the view is the constructor of the controller
     * itself
     */
    public static void launch()
    {
        View.initialize(Controller::new);
    }
}
