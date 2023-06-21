package app.jbomberman.model;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawable;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.game.Game;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.GameGridBuilder;
import app.jbomberman.model.game.GameGridBuilderDirector;
import app.jbomberman.model.game.entity.BomberMan;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.util.CallbackOnce;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.KeyBoardUpdateInfo;
import app.jbomberman.view.scenes.GameOverScene;
import app.jbomberman.view.scenes.LoadingLevelScene;
import app.jbomberman.view.scenes.WinScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * it's Observable by the View
 */
public class Model implements Observable, Drawable
{
	/**
	 * normalBackgroundSound is the filename that contains the sound to be played
	 * during the game before any powerup is picked
	 */
	private static final String NORMAL_BACKGROUND_NOISE =
		Properties.getInstance().getSoundPath("03 Main BGM.wav");

	private static final long TIME = 200_000;

	private static final String POWERUP_TAKEN_BACKGROUND_NOISE =
		Properties.getInstance().getSoundPath("04 Power-Up Get.wav");

	private static final String GAME_OVER_SOUND =
		Properties.getInstance().getSoundPath("09 Miss.wav");

	private static final String WIN_SOUND =
		Properties.getInstance().getSoundPath("05 Stage Clear.wav");
	private static final int LAST_LEVEL = 50;

	private String currentlyPlayingBackgroundNoiseName;

	/**
	 * GameGrid is the gameGrid built by the builder
	 */
	private final GameGrid gameGrid;
	private boolean exiting = false;


	private final Timer timer;

	public Game(Account account)
	{
		currentlyPlayingBackgroundNoiseName = NORMAL_BACKGROUND_NOISE;

		GameGridBuilder builder = new GameGridBuilder(
			Vector2D.of(0, 2 * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL), this);

		if (account.getCapabilities().getNumberOfLives() < 0)
			account.setCapabilities(BomberManCapabilities.defaultCapabilities);

		GameGridBuilderDirector director = new GameGridBuilderDirector(
			builder, account.getLevel(), account.getCapabilities());

		director.construct();

		gameGrid = builder.getResult();

		timer = new Timer(TIME);

		// TODO:
		//gameGrid.attachObserver(this);
	}

	private Maybe<BomberMan> bomberMan = Maybe.nothing();

	public void heresBomberman(BomberMan bomberMan)
	{
		this.bomberMan = Maybe.just(bomberMan);
	}

	private final CallbackOnce func = new CallbackOnce()
	{
		@Override
		public void run()
		{
			gameGrid.timeHasEnded();
		}
	};

	public Drawing update()
	{
		if (!timer.hasStarted())
			timer.start();
		else if (timer.timeIsUp())
			func.runOnce();

		gameGrid.update();

		scoreEntry.setText(String.valueOf(Model.getInstance().getAccount().getCapabilities().getPointsEarned()));

		if (!exiting) {
			playIfNeeded(currentlyPlayingBackgroundNoiseName);
		} else {
			Model model = Model.getInstance();
			if (!isPlayingSomething()) {
				if (Model.getInstance().getAccount().getCapabilities().getNumberOfLives() >= 0) {
					if (model.getAccount().getLevel() == LAST_LEVEL + 1) {
						doTransition(WinScene::new);
						model.getAccount().setLevel(1);
					} else
						doTransition(LoadingLevelScene::new);
				} else
					doTransition(GameOverScene::new);
			}
		}

		Drawing drawing = gameGrid.getDrawing();
		timeEntry.setText("time  " + Math.max(0, timer.millisToEnd() / 1000));
		drawing.merge(topPanel.getDrawing());
		return drawing;
	}

	private final Vector2D dimensions = Vector2D.of(512, 480);

	private Maybe<Drawing> currentDraw;
	private Maybe<Game> game;
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

	private final Set<Observer> observers = new HashSet<>();

	@Override
	public Set<Observer> getObservers()
	{
		return observers;
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

	public void reset()
	{
		game = Maybe.just(new Game(getAccount()));
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

	public void reeciveKeyBoardUpdateInfo(KeyBoardUpdateInfo info)
	{
		/* do something with this TODO: */
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
