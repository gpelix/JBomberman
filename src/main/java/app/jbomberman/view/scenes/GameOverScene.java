package app.jbomberman.view.scenes;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.util.Maybe;
import app.jbomberman.view.View;
import app.jbomberman.view.widgets.Text;

/**
 * GameOverScene is the scene to display when there is a GameOver
 *
 * @author gp
 */
public class GameOverScene extends SoundyBomberManScene
{
	/**
	 * gameOverText is the text to display when there is a gameOver
	 */
	private final Maybe<Text> gameOverText;

	/**
	 * GAME_OVER_SOUND is the sound to play during the GameOverScene
	 */
	private static final String GAME_OVER_SOUND = Properties.getInstance().getSoundPath("10 Game Over.wav");

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 *
	 * @implNote We check if the audioHandle is playing something and, if not,
	 * we go onto another scene (StartDisplay)
	 */
	@Override
	public Drawing update()
	{
		if (!getAudioHandle().isPlayingSomething()) {
			Model.getInstance().getAccount().setLevel(1);
			doTransition(StartDisplay::new);
		}

		return gameOverText.fromJust().getDrawing();
	}

	/**
	 * cleanup() must be called before transition to a new scene
	 *
	 * @implNote does nothing
	 */
	@Override
	public void cleanup()
	{
		// Do nothing
	}

	/**
	 * public constructor builds a GameOverScene from a View
	 *
	 * @param view the View
	 */
	public GameOverScene(View view)
	{
		super(view);

		AudioPlayer.getInstance().stopAll();
		getAudioHandle().playIfNeeded(GAME_OVER_SOUND);

		gameOverText = Maybe.just(new Text("Game over", Text.TextColor.Black));

		gameOverText.fromJust().setPosition(
			Model.getInstance().getDimensions().div(2)
		);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
