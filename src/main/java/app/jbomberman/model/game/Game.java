package app.jbomberman.model.game;

import app.jbomberman.Properties;
import app.jbomberman.audio.MaybeAudioHandle;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.util.CallbackOnce;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;

/**
 * Game is the class representing a game. Its duties are creating an instance
 * of GameGrid using the builder and then start the game
 *
 * @author gp
 */
public class Game
{
	/**
	 * normalBackgroundSound is the filename that contains the sound to be played
	 * during the game before any powerup is picked
	 */
	private static final String NORMAL_BACKGROUND_NOISE =
		Properties.getInstance().getSoundPath("03 Main BGM.wav");

	/**
	 * TIME is the time that a game will last in milliseconds
	 */
	private static final long TIME = 200_000;

	/**
	 * POWERUP_TAKEN_BACKGROUND_NOISE is the sound that will be played as soon
	 * as a powerup gets taken by bomberman
	 */
	private static final String POWERUP_TAKEN_BACKGROUND_NOISE =
		Properties.getInstance().getSoundPath("04 Power-Up Get.wav");

	/**
	 * GAME_OVER_SOUND is the sound to be played when there is a game over
	 */
	private static final String GAME_OVER_SOUND =
		Properties.getInstance().getSoundPath("09 Miss.wav");

	/**
	 * WIN_SOUND is the sound to be played when the player has won
	 */
	private static final String WIN_SOUND =
		Properties.getInstance().getSoundPath("05 Stage Clear.wav");

	/**
	 * currentlyPlayingBackgroundNoiseName is the name of the background sound that
	 * is currently playing. This variable is needed as we first play the
	 * NORMAL_BACKGROUND_SOUND and then if a powerup gets taken, we switch to the
	 * POWERUP_TAKEN_BACKGROUND_NOISE.
	 * So we keep the name of the file containing the background sound currently
	 * playing in this String
	 */
	private String currentlyPlayingBackgroundNoiseName;

	/**
	 * GameGrid is the gameGrid built by the builder
	 */
	private final GameGrid gameGrid;

	/**
	 * exiting is a flag that is set to true only if bomberman has won or has been
	 * distroyed somehow (or by an enemy or hit by an explosion)
	 */
	private boolean exiting = false;

	/**
	 * handle is the MaybeAudioHandle that will be responsible for playing the
	 * background noise sound
	 */
	private final MaybeAudioHandle handle;

	/**
	 * timer is the timer that will ring at the end of the game
	 */
	private final Timer timer;

	/**
	 * public constructor that should be called by the Model when a new game
	 * gets requested
	 */
	public Game()
	{
		Model model = Model.getInstance();

		handle = new MaybeAudioHandle();

		int levelNumber = model.getAccount().getLevel();

		currentlyPlayingBackgroundNoiseName = NORMAL_BACKGROUND_NOISE;

		GameGridBuilder builder = new GameGridBuilder(
			Vector2D.of(0, 2 * FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL));


		if (model.getAccount().getCapabilities().getNumberOfLives() < 0)
			model.getAccount().setCapabilities(BomberManCapabilities.defaultCapabilities);

		GameGridBuilderDirector director =
			new GameGridBuilderDirector(builder, levelNumber,
				model.getAccount().getCapabilities());

		director.construct();

		gameGrid = builder.getGrid();

		timer = new Timer(TIME);
	}

	/**
	 * powerUpTaken is a function meant to be called by a Powerup when it has
	 * been taken
	 */
	public void powerUpTaken()
	{
		handle.replace(POWERUP_TAKEN_BACKGROUND_NOISE);
		currentlyPlayingBackgroundNoiseName = POWERUP_TAKEN_BACKGROUND_NOISE;
	}

	/**
	 * Callback to call when time is up, but just one time
	 */
	private final CallbackOnce func = new CallbackOnce()
	{
		@Override
		public void run()
		{
			gameGrid.doWhenTimeHasEnded();
		}
	};

	/**
	 * update is the method that must be called to update the game and get a
	 * drawing of it
	 *
	 * @return the drawing of the game
	 */
	public Drawing update()
	{
		if (!timer.hasStarted())
			timer.start();
		else if (timer.timeIsUp())
			func.runOnce();

		gameGrid.update();

		if (!exiting)
			handle.playIfNeeded(currentlyPlayingBackgroundNoiseName);

		return gameGrid.getDrawing();
	}

	/**
	 * secsToEnd() returns the number of seconds to the end of the game
	 *
	 * @return the seconds to the end of the game
	 */
	public long secsToEnd()
	{
		return timer.millisToEnd() / 1000;
	}

	/**
	 * exited() returns true if and only is the game has ended (so we are in the
	 * exiting state) and the playing of the shutdown music has ended
	 *
	 * @return whether the game has ended (so we are in the
	 * exiting state) and the playing of the shutdown music has ended
	 */
	public boolean exited()
	{
		return exiting() && !handle.isPlayingSomething();
	}

	/**
	 * exiting() returns the exiting flag
	 *
	 * @return the exiting flag
	 */
	public boolean exiting()
	{
		return exiting;
	}

	/**
	 * endGame() must be called by entities to start the shutdown process of the
	 * game
	 *
	 * @param victory it must be set to true if there was a victory or false
	 *                otherwise
	 */
	public void endGame(boolean victory)
	{
		if (victory) {
			handle.replace(WIN_SOUND);
			Model.getInstance().getAccount().incrementLevel();
		} else {
			handle.replace(GAME_OVER_SOUND);
		}

		exiting = true;
	}

	/**
	 * getDimensions() returns the dimensions of the underlying gameGrid in model
	 * coordinates
	 *
	 * @return the dimension of the underlying gameGrid
	 */
	public Vector2D getDimensions()
	{
		return gameGrid.getDimensions();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/