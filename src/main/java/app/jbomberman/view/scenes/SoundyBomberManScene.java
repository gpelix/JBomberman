package app.jbomberman.view.scenes;

import app.jbomberman.audio.AudioHandle;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.util.Maybe;
import app.jbomberman.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class SoundyBomberManScene extends BomberManScene
{

	private Maybe<AudioHandle> audioHandle;

	private final Logger logger;

	protected SoundyBomberManScene(View view)
	{
		super(view);
		audioHandle = Maybe.nothing();
		logger = LogManager.getLogger(getClass());
	}

	public boolean isPlayingSomething()
	{
		return audioHandle.compute(false, AudioHandle::isPlaying);
	}

	public void stopSound()
	{
		audioHandle.doOrDo(AudioHandle::stop, () -> logger.warn(
			"Stopping threadPlayer with no sound in it"));
	}

	public void replace(String replacement)
	{
		stopSound();
		play(replacement);
	}

	public void playIfNeeded(String file)
	{
		if (!isPlayingSomething())
			play(file);
	}

	private void play(String file)
	{
		audioHandle = Maybe.just(AudioPlayer.getInstance().playSound(file));

	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
