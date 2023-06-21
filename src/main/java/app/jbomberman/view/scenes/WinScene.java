package app.jbomberman.view.scenes;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.Text;
import app.jbomberman.model.Model;
import app.jbomberman.view.View;

public class WinScene extends SoundyBomberManScene
{
	private final Text winText;

	private static final String GAME_OVER_SOUND = Properties.getInstance().getSoundPath("08 Ending.wav");

	@Override
	public Drawing update()
	{
		if (!getAudioHandle().isPlayingSomething())
			doTransition(StartDisplay::new);

		Drawing a = winText.getDrawing();
		a.setDimensions(Model.getInstance().getDimensions());

		return a;
	}

	@Override
	public void cleanup()
	{
		// Do nothing
	}

	public WinScene(View view)
	{
		super(view);

		AudioPlayer.getInstance().stopAll();
		getAudioHandle().playIfNeeded(GAME_OVER_SOUND);

		winText = new Text("You have won", Text.TextColor.Black);

		winText.setPosition(
			Model.getInstance().getDimensions().div(2)
		);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
