package app.jbomberman.view.scenes;

import app.jbomberman.audio.MaybeAudioHandle;
import app.jbomberman.view.View;

public abstract class SoundyBomberManScene extends BomberManScene
{

	private MaybeAudioHandle audioHandle;

	protected SoundyBomberManScene(View view)
	{
		super(view);
		audioHandle = new MaybeAudioHandle();
	}

	public MaybeAudioHandle getAudioHandle()
	{
		return audioHandle;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
