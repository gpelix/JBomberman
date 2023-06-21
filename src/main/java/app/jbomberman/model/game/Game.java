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
	private final MaybeAudioHandle handle;

	public static int getLastLevel()
	{
		return LAST_LEVEL;
	}

	private final Timer timer;

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

		gameGrid = builder.getResult();

		timer = new Timer(TIME);
	}

	public void powerUpTaken()
	{
		// Should now do what the TODO:
//		replace(POWERUP_TAKEN_BACKGROUND_NOISE);
//		currentlyPlayingBackgroundNoiseName = POWERUP_TAKEN_BACKGROUND_NOISE;
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

		if (!exiting)
			handle.playIfNeeded(currentlyPlayingBackgroundNoiseName);
		// To be moved back to the gameScene
//		} else {
//			Model model = Model.getInstance();
//			if (!handle.isPlayingSomething()) {
//				if (Model.getInstance().getAccount().getCapabilities().getNumberOfLives() >= 0) {
//					if (model.getAccount().getLevel() == LAST_LEVEL + 1) {
//						doTransition(WinScene::new);
//						model.getAccount().setLevel(1);
//					} else
//						doTransition(LoadingLevelScene::new);
//				} else
//					doTransition(GameOverScene::new);
//			}
//		}

		return gameGrid.getDrawing();
	}

	public long secsToEnd()
	{
		System.out.println(timer.millisToEnd());
		return timer.millisToEnd() / 1000;
	}

	public boolean exited()
	{
		return exiting() && !handle.isPlayingSomething();
	}

	public boolean exiting()
	{
		return exiting;
	}

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

	public Vector2D getDimensions()
	{
		return gameGrid.getDimensions();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/