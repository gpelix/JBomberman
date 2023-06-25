package app.jbomberman.model;

import app.jbomberman.JBomberManException;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.game.Game;
import app.jbomberman.util.AbstractObservable;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.KeyBoardUpdateInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Model is the Model in the MVC pattern that our application uses.
 * <p>
 * It's responsible for the creation and destruction of the game
 *
 * @author gp
 */
public class Model extends AbstractObservable
{
	/**
	 * dimensions is the dimensions of the Model
	 */
	private final Vector2D dimensions = Vector2D.of(512, 480);

	/**
	 * account is the current logged in account
	 */
	private Account account;

	/**
	 * isDefaultAccount is a boolean that is set to false if a log in has been done
	 */
	private boolean isDefaultAccount = true;

	/**
	 * logger is the logger for the Model
	 */
	private final Logger logger;

	/**
	 * Controller is a reference to the controller
	 */
	private static Controller controller;

	/**
	 * game is the current game. It's possible that no game has been started
	 */
	private Maybe<Game> game = Maybe.nothing();

	/**
	 * Model is the private constructor of the model. It's private as we use
	 * the singleton pattern, and we want it to be initialized only if needed by
	 * the getInstance() method
	 */
	private Model()
	{
		account = AccountBuilder.getInstance().getDefaultAccount("Unknown");

		logger = LogManager.getLogger(getClass());
	}

	/**
	 * getGame() gets the current game
	 *
	 * @return the game
	 */
	public Game getGame()
	{
		return game.unwrapOrThrow(
			() -> new JBomberManException("Getting unset game"));
	}

	/**
	 * resetGame() resets the game creating a new one
	 */
	public void resetGame()
	{
		detachAllObservers();
		game = Maybe.just(new Game());
	}

	/**
	 * setController sets the controller
	 *
	 * @param controller the controller to set. We don't want to pass to the getInstance()
	 *                   method the Controller every time we need the Model
	 */
	public static void setController(Controller controller)
	{
		Model.controller = controller;
	}

	/**
	 * update() gets the drawing
	 *
	 * @return the drawing
	 */
	public Drawing update()
	{
		return getGame().update();
	}

	/**
	 * setAccount() sets the account
	 *
	 * @param account the account to set
	 */
	public void setAccount(Account account)
	{
		this.account = account;
		isDefaultAccount = false;
	}

	/**
	 * getDimensions() gets the dimensions of the Model
	 *
	 * @return the dimensions of the Model
	 */
	public Vector2D getDimensions()
	{
		return dimensions;
	}

	/**
	 * getAccount() gets the current logged-in account
	 *
	 * @return the current logged-in account
	 */
	public Account getAccount()
	{
		return account;
	}

	/**
	 * close() closes the model
	 */
	public void close()
	{
		if (!isDefaultAccount)
			AccountBuilder.getInstance().savePlayer(account);
		logger.info("Closing model");
		controller.close();
	}

	/**
	 * instance is the only unique instance of the Model
	 */
	private static Model instance;

	/**
	 * getInstance() gets the unique instance of the Model
	 *
	 * @return the unique instance of the Model
	 */
	public static Model getInstance()
	{
		if (instance == null)
			instance = new Model();
		return instance;
	}

	/**
	 * keyBoardUpdateInfo keeps the KeyBoardUpdateInfo
	 */
	private Maybe<KeyBoardUpdateInfo> keyBoardUpdateInfo = Maybe.nothing();

	/**
	 * getKeyBoardUpdateInfo() gets the keyBoardUpdateInfo
	 *
	 * @return the keyBoardUpdateInfo
	 */
	public Maybe<KeyBoardUpdateInfo> getKeyBoardUpdateInfo()
	{
		return keyBoardUpdateInfo;
	}

	/**
	 * keyBoardEvent must be called by the View to propagate a keyboard event to
	 * the Model
	 *
	 * @param updateInfo the keyboard update info
	 * @implNote We don't want the mode to directly observe the KeyBoardController
	 * as we must allow the View to filter the keyboardevents
	 */
	public void keyBoardEvent(KeyBoardUpdateInfo updateInfo)
	{
		keyBoardUpdateInfo = Maybe.just(updateInfo);
		updateAllObservers();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
