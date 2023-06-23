package app.jbomberman.view.scenes;

import app.jbomberman.audio.MaybeAudioHandle;
import app.jbomberman.view.View;

/**
 * SoundBomberManScene is the parent of many scenes that keeps an audio handle
 * to have a simple handling of sounds
 *
 * @author gp
 */
public abstract class SoundyBomberManScene extends BomberManScene
{
	/**
	 * audioHandle is a MaybeAudioHandle that will be used to handle the sound
	 */
	private final MaybeAudioHandle audioHandle;

	/**
	 * protected constructor builds a SoundyBomberManScene from a View
	 *
	 * @param view the View
	 */
	protected SoundyBomberManScene(View view)
	{
		super(view);
		audioHandle = new MaybeAudioHandle();
	}

	/**
	 * getAudioHandle() is a protected function to get the underlying audioHandle
	 *
	 * @return the audioHandle
	 */
	protected MaybeAudioHandle getAudioHandle()
	{
		return audioHandle;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
