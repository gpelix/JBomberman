package app.jbomberman.view.scenes;

import app.jbomberman.Properties;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.HorizontalPanel;
import app.jbomberman.drawing.Text;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.GameGridBuilder;
import app.jbomberman.model.game.GameGridBuilderDirector;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.util.CallbackOnce;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.KeyBoardControllerKeyCode;
import app.jbomberman.view.KeyBoardUpdateInfo;
import app.jbomberman.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * GameScene represents a game
 *
 * @author gp
 */
public class GameScene extends SoundyBomberManScene implements Observable, Observer
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

	private final HorizontalPanel<Text> topPanel;

	private final Text timeEntry;

	private final Text scoreEntry;

	private final Timer timer;

	public GameScene(View view)
	{
		super(view);

		Model model = Model.getInstance();

		int levelNumber = model.getAccount().getLevel();

		currentlyPlayingBackgroundNoiseName = NORMAL_BACKGROUND_NOISE;

		GameGridBuilder builder = new GameGridBuilder(
			Vector2D.of(0, 2 * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL), this);

		Text lives = new Text("left  " + model.getAccount().getCapabilities().getNumberOfLives(), Text.TextColor.Black);
		timeEntry = new Text("", Text.TextColor.Black);
		scoreEntry = new Text("", Text.TextColor.Black);

		topPanel = new HorizontalPanel<>(
			timeEntry,
			scoreEntry,
			lives);

		if (model.getAccount().getCapabilities().getNumberOfLives() < 0)
			model.getAccount().setCapabilities(BomberManCapabilities.defaultCapabilities);

		GameGridBuilderDirector director =
			new GameGridBuilderDirector(builder, levelNumber,
				model.getAccount().getCapabilities());

		director.construct();

		gameGrid = builder.getResult();

		timer = new Timer(TIME);

		gameGrid.attachObserver(this);

		initialize();
	}

	private final CallbackOnce func = new CallbackOnce()
	{
		@Override
		public void run()
		{
			gameGrid.timeHasEnded();
		}
	};

	@Override
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

	/**
	 * resume() is a function called by the PauseScene to resume the game
	 */
	public void resume()
	{
		initialize();
	}

	/**
	 * initialize() is a utility function that gets called from both resume and setup.
	 * We use this function to avoid code duplication
	 */
	private void initialize()
	{
		Model.getController().attachKeyBoardObserver(this); // Listen for ESC

		topPanel.setTopLeft(Vector2D.of(0, 0));
		topPanel.setWidth(gameGrid.getDimensions().multiply(Entity.SIZE_IN_MODEL).getX());
	}

	@Override
	public void cleanup()
	{
		Model.getController().detachKeyBoardObserver(this);
	}

	private Maybe<KeyBoardUpdateInfo> keyBoardUpdateInfo =
		Maybe.nothing();


	@Override
	public void update(Observable observable)
	{
		if (observable instanceof KeyBoardController keyBoardController) {
			KeyBoardUpdateInfo keyBoardUpdateInfo =
				keyBoardController.getUpdateInfo();

			if (keyBoardUpdateInfo.getKeyBoardEventType() == KeyBoardUpdateInfo.KeyBoardEventType.KEY_PRESSED
				&& keyBoardUpdateInfo.getKeyCode() == KeyBoardControllerKeyCode.ESC)
			{
				doTransition(() -> new PauseScene(getView(), this));
			} else {
				if (!exiting) {
					// Forward it to bomberman
					this.keyBoardUpdateInfo = Maybe.just(keyBoardUpdateInfo);
					updateAllObservers();
				}
			}
		} else if (observable instanceof GameGrid) { // That's telling me a powerup has been taken
			replace(POWERUP_TAKEN_BACKGROUND_NOISE);
			currentlyPlayingBackgroundNoiseName = POWERUP_TAKEN_BACKGROUND_NOISE;
		}
	}

	// Called by bomberman or whatever
	public Maybe<KeyBoardUpdateInfo> getKeyBoardUpdateInfo()
	{
		return keyBoardUpdateInfo;
	}

	// Just BomberMan character for now. Why the hell is this. I'd say we move
	// the observation to bomberman directly
	private final List<Observer> observers = new ArrayList<>();

	@Override
	public Collection<Observer> getObservers()
	{
		return observers;
	}

	public void endGame(boolean victory)
	{
		if (victory) {
			replace(WIN_SOUND);
			Model.getInstance().getAccount().incrementLevel();
		} else {
			replace(GAME_OVER_SOUND);
		}

		exiting = true;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
