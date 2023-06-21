package app.jbomberman.model.game;

import app.jbomberman.Properties;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;

public class Game
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

	public Game()
	{
		Model model = Model.getInstance();

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

		gameGrid = builder.getResult();

		timer = new Timer(TIME);
	}

	public void powerUpTaken()
	{
		// Should now do what the TODO:
//		replace(POWERUP_TAKEN_BACKGROUND_NOISE);
//		currentlyPlayingBackgroundNoiseName = POWERUP_TAKEN_BACKGROUND_NOISE;
	}

	public Drawing update()
	{
		return null;
	}

	public void endGame(boolean successFlag)
	{
		//((GameScene) currentBomberManScene).endGame(b);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/