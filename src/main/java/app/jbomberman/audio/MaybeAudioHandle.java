package app.jbomberman.audio;

import app.jbomberman.util.Maybe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * MaybeAudioHandle is an AudioHandle that can be resumed, stopped and restarted
 *
 * @author gp
 */
public class MaybeAudioHandle
{
	/**
	 * audioHandle may contain the AudioHandle
	 */
	private Maybe<AudioHandle> audioHandle;

	/**
	 * Logger is the logger of the MaybeAudioHandle
	 */
	private final Logger logger;

	/**
	 * public constructor of MaybeAudioHandle from an AudioHandle
	 *
	 * @param audioHandle the underlying AudioHandle
	 */
	public MaybeAudioHandle(AudioHandle audioHandle)
	{
		this();
		this.audioHandle = Maybe.just(audioHandle);
	}

	/**
	 * public constructor from that sets up an empty audioHandle
	 */
	public MaybeAudioHandle()
	{
		this.audioHandle = Maybe.nothing();
		logger = LogManager.getLogger(getClass());
	}

	/**
	 * isPlayingSomething() returns true is there is a valid audioHandle and is
	 * playing something
	 *
	 * @return whether the AudioHandle is something and is playing something
	 */
	public boolean isPlayingSomething()
	{
		return audioHandle.compute(false, AudioHandle::isPlaying);
	}

	/**
	 *
	 */
	public void stopSound()
	{
		audioHandle.doOrDo(AudioHandle::stop, () -> logger.warn(
			"Stopping threadPlayer with no sound in it"));
	}

	/**
	 * replace() stops the playing of the previous sound and starts playing the
	 * replacementSound
	 *
	 * @param replacementSound the sound to substitute
	 */
	public void replace(String replacementSound)
	{
		stopSound();
		play(replacementSound);
	}

	/**
	 * playIfNeeded() plays the audio file only if the AudioHandle is not playing
	 * anything
	 *
	 * @param file the audio file to be eventually played
	 */
	public void playIfNeeded(String file)
	{
		if (!isPlayingSomething())
			play(file);
	}

	/**
	 * play() plays an audio file
	 *
	 * @param file the audio file to be played
	 */
	private void play(String file)
	{
		audioHandle = Maybe.just(AudioPlayer.getInstance().playSound(file));
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
