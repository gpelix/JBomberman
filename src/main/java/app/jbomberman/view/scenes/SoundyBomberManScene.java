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
 
package app.jbomberman.view.scenes;

import app.jbomberman.audio.EnhancedAudioHandle;
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
	 * audioHandle is a EnhancedAudioHandle that will be used to handle the sound
	 */
	private final EnhancedAudioHandle audioHandle;

	/**
	 * protected constructor builds a SoundyBomberManScene from a View
	 *
	 * @param view the View
	 */
	protected SoundyBomberManScene(View view)
	{
		super(view);
		audioHandle = new EnhancedAudioHandle();
	}

	/**
	 * getAudioHandle() is a protected function to get the underlying audioHandle
	 *
	 * @return the audioHandle
	 */
	protected EnhancedAudioHandle getAudioHandle()
	{
		return audioHandle;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
