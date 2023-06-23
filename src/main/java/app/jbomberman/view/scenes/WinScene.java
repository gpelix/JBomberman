package app.jbomberman.view.scenes;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.view.View;
import app.jbomberman.view.widgets.Text;

/**
 * WinScene is the scene to display when there is a Victory
 *
 * @author gp
 */
public class WinScene extends SoundyBomberManScene
{
	/**
	 * winText is the text to display during this scene
	 */
	private final Text winText;

	/**
	 * VICTORY_SOUND is the sound to play during the WinScene
	 */
	private static final String VICTORY_SOUND = Properties.getInstance().getSoundPath("08 Ending.wav");

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 *
	 * @implNote It checks if the audioHandle is palying something. In that case,
	 * it does a transition to StartDisplay scene.
	 */
	@Override
	public Drawing update()
	{
		if (!getAudioHandle().isPlayingSomething())
			doTransition(StartDisplay::new);

		Drawing a = winText.getDrawing();
		a.setDimensions(Model.getInstance().getDimensions());

		return a;
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
	 * public constructor builds a WinScene from a View
	 *
	 * @param view the View
	 */
	public WinScene(View view)
	{
		super(view);

		AudioPlayer.getInstance().stopAll();
		getAudioHandle().playIfNeeded(VICTORY_SOUND);

		winText = new Text("You have won", Text.TextColor.Black);

		winText.setPosition(
			Model.getInstance().getDimensions().div(2)
		);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
