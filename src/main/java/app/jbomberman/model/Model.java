
package app.jbomberman.model;

import app.jbomberman.JBomberManException;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.scenes.BomberManScene;
import app.jbomberman.model.scenes.StartDisplay;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class Model implements Observable
{
	private BomberManScene currentBomberManScene;

	private Maybe<Vector2D> dimensions = Maybe.nothing();

	private Maybe<Drawing> currentDraw;

	private Account account;
	private boolean isDefaultAccount = true;

	private final Logger logger;

	private final Controller controller;

	public Model(Controller controller)
	{
		currentBomberManScene = new StartDisplay(controller, this);
		currentBomberManScene.setup();
		this.controller = controller;

		currentDraw = Maybe.nothing();
		account = AccountBuilder.getInstance().getDefaultAccount("Unknown");

		logger = LogManager.getLogger();
	}

	private final Set<Observer> observers = new HashSet<>();

	@Override
	public Set<Observer> getObservers()
	{
		return observers;
	}

	public Drawing getDraw()
	{
		return currentDraw.unwrapOrThrow(() ->
			new JBomberManException("Drawing is nothing"));
	}

	public void update()
	{
		currentDraw = Maybe.just(currentBomberManScene.update());
		updateAllObservers();
	}

	public void setScene(BomberManScene newScene)
	{
		currentBomberManScene = newScene;
	}

	public void setDimensions(Vector2D dimensions)
	{
		this.dimensions = Maybe.just(dimensions);
	}

	public Maybe<Vector2D> getDimensions()
	{
		return dimensions;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public void setAccount(Account account)
	{
		this.account = account;
		isDefaultAccount = false;
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
}
