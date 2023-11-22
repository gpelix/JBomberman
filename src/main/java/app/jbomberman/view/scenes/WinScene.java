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
	 * <p>
	 * It checks if the audioHandle is playing something. In that case,
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
	 * <p>
	 * It does nothing
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
