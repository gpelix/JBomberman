package app.jbomberman.controller;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.model.Model;
import app.jbomberman.util.Observer;
import app.jbomberman.view.AbstractView;
import app.jbomberman.view.scenes.StartDisplay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller is the controller class in the MVC pattern on which the program
 * is based. The controller is responsible for the communication between the graphic
 * layer and the model in the application. It also sets up the View
 *
 * @author gp
 * @see Model
 * @see AbstractView
 */
public class Controller
{
    private final AbstractView abstractView;

    /**
     * logger is an instance of a logger for the Controller
     */
    private final Logger logger;

    /**
     * Controller() is the only constructor of the controller
     */
    public Controller(AbstractView view)
    {
        abstractView = view;

        logger = LogManager.getLogger(getClass());
        view.setScene(new StartDisplay());

        Model.setController(this);
        Model.getInstance().setup();
        Model.getInstance().attachObserver(view);

        abstractView.getAbstractGraphicFactory().buildLoopTimer(abstractView::update).start();


        abstractView.setTitle(Properties.getInstance().getApplicationName());
        abstractView.setIcon(Properties.getInstance().getIconFileName());
        abstractView.setCloseOperationCallBack(Model.getInstance()::close);
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
        abstractView.close();
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
        abstractView.getInputController().attachObserver(observer);
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
        abstractView.getInputController().detachObserver(keyBoardObserver);
    }
}
