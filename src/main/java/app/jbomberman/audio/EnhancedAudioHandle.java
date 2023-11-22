/*
 * Copyright (c) 2023, Gabriele Pelissetto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement:
 *      This product includes software developed by Gabriele Pelissetto
 *
 * 4. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
package app.jbomberman.audio;

import app.jbomberman.util.Maybe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * EnhancedAudioHandle is an AudioHandle that can be resumed,
 * stopped and restarted
 *
 * @author gp
 */
public class EnhancedAudioHandle
{
	/**
	 * audioHandle may contain the AudioHandle
	 */
	private Maybe<AudioHandle> audioHandle;

	/**
	 * Logger is the logger of the EnhancedAudioHandle
	 */
	private final Logger logger;

	/**
	 * public constructor of EnhancedAudioHandle from an AudioHandle
	 *
	 * @param audioHandle the underlying AudioHandle
	 */
	public EnhancedAudioHandle(AudioHandle audioHandle)
	{
		this();
		this.audioHandle = Maybe.just(audioHandle);
	}

	/**
	 * public constructor from that sets up an empty audioHandle
	 */
	public EnhancedAudioHandle()
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
	 * stop the sound
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
