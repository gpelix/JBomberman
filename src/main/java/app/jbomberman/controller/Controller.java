package app.jbomberman.controller;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.images.javafx.JavaFXImageBuilder;
import app.jbomberman.images.swing.SwingImageBuilder;
import app.jbomberman.model.Model;
import app.jbomberman.util.Observer;
import app.jbomberman.view.View;
import app.jbomberman.view.graphics.javafx.JavaFXView;
import app.jbomberman.view.graphics.swing.SwingView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller is the controller class in the MVC pattern on which the program
 * is based. The controller is responsible for the communication between the graphic
 * layer and the model in the application. It also sets up the View and the View
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

	/**
	 * Controller() is the only constructor of the controller. It must not be called
	 * by other classes, as, to do the startup, the static function Controller.launch()
	 * must be called
	 */
	private Controller(View view)
	{
		logger = LogManager.getLogger(getClass());

		this.view = view;

		Model.setController(this);
		Model.getInstance().setup();
		Model.getInstance().attachObserver(view);

		view.getGraphicFactory().getLoopTimer(Model.getInstance()::update).start();

		view.setTitle(Properties.getInstance().getApplicationName());
		view.setCloseOperationCallBack(Model.getInstance()::close);
		view.setIcon(Properties.getInstance().getIconFileName());
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
		view.getKeyBoardController().attachObserver(observer);
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
		view.getKeyBoardController().detachObserver(keyBoardObserver);
	}

	/**
	 * launch() is the function that must be called during the bootstrap process
	 * to initialize the Graphic library and then initialize the controller
	 */
	public static void launch()
	{
		if (Properties.getInstance().getGraphicLibrary() == Properties.GraphicLibrary.JAVAFX) {
			FlightWeightImagesFactory.initializeImageBuilder(new JavaFXImageBuilder());
			JavaFXView.initialize(() -> new Controller(new JavaFXView()));
		} else {
			FlightWeightImagesFactory.initializeImageBuilder(new SwingImageBuilder());
			new Controller(new SwingView());
		}
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
