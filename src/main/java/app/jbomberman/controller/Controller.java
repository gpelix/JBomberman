
package app.jbomberman.controller;

import app.jbomberman.Settings;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.model.Model;
import app.jbomberman.util.Observer;
import app.jbomberman.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller
{
	private final View view;
	private final Model model;
	private final Logger logger;

	private final KeyBoardController keyBoardController;

	public Controller()
	{
		logger = LogManager.getLogger();

		view = new View();

		keyBoardController = new KeyBoardController();
		keyBoardController.attachToScene(view.getScene());


		model = new Model(this);

		EventLoopHandler eventLoopHandler = new EventLoopHandler(this, model::update);
		eventLoopHandler.setFps(Settings.MAX_FPS);
		eventLoopHandler.start();

		model.attachObserver(view);


		view.setTitle("JBomberMan");
		view.setIcon("icon.png");
		view.setCloseOperationCallBack(model::close);
	}

	public void close()
	{
		logger.info("Closing Controller");
		AudioPlayer.getInstance().close();
		view.close();
	}

	public void attachKeyBoardObserver(Observer observer)
	{
		keyBoardController.attachObserver(observer);
	}

	public void detachKeyBoardObserver(Observer keyBoardObserver)
	{
		keyBoardController.detachObserver(keyBoardObserver);
	}

	public static void launch()
	{
		View.initialize(Controller::new);
	}
}
