package app.jbomberman.model;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawable;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.util.AbstractObservable;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Model extends AbstractObservable implements Drawable
{
	private final Vector2D dimensions = Vector2D.of(512, 480);

	private Maybe<Drawing> currentDraw;
	private Account account;
	private boolean isDefaultAccount = true;

	private final Logger logger;
	private static Controller controller;

	private final Timer frameTimer;

	private static final int FPS = Properties.getInstance().getMaxFps();

	private Model()
	{
		currentDraw = Maybe.nothing();
		account = AccountBuilder.getInstance().getDefaultAccount("Unknown");

		logger = LogManager.getLogger(getClass());

		frameTimer = new Timer(1000 / FPS);
		frameTimer.start();
	}


	public static void setController(Controller controller)
	{
		Model.controller = controller;
	}

	public static Controller getController()
	{
		if (controller == null)
			throw new JBomberManException("Controller is unset");

		return controller;

	}

	@Override
	public Drawing getDrawing()
	{
		return currentDraw.unwrapOrThrow(() ->
			new JBomberManException("Drawing is nothing"));
	}

	public void update()
	{
//		if (!frameTimer.timeIsUp())
//			return;
//		currentDraw = Maybe.just(currentBomberManScene.update());
//		updateAllObservers(); // tell the whole world that we are ready to give them a Drawing
	}

	public void setAccount(Account account)
	{
		this.account = account;
		isDefaultAccount = false;
	}

	public Vector2D getDimensions()
	{
		return dimensions;
	}

	public Account getAccount()
	{
		return account;
	}

	public void close()
	{
		if (!isDefaultAccount)
			AccountBuilder.getInstance().savePlayer(account);
		logger.info("Closing model");
		controller.close();
	}

	private static Model instance;

	public static Model getInstance()
	{
		if (instance == null)
			instance = new Model();
		return instance;
	}

	public void endGame(boolean b)
	{
		//((GameScene) currentBomberManScene).endGame(b);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
