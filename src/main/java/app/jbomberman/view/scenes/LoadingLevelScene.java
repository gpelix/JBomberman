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
import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;
import app.jbomberman.view.widgets.Text;

/**
 * LoadingLevelScene is the scene loaded before starting the gameplay
 * Its only duty is the one of displaying a little tiny text telling which
 * level the player is going to play and playing a sound
 *
 * @author gp
 */
public class LoadingLevelScene extends SoundyBomberManScene
{
	/**
	 * loadingLevelSceneSound is the sound to be played during this scene
	 */
	private static final String LOADING_LEVEL_SCENE_SOUND =
		Properties.getInstance().getSoundPath("02 Stage Start.wav");

	/**
	 * drawing is the Drawing of the LoadingLevelScene
	 */
	private final Drawing drawing;

	/**
	 * public constructor builds a LoadingLevelScene from a View
	 *
	 * @param view the View
	 */
	public LoadingLevelScene(View view)
	{
		super(view);

		AudioPlayer.getInstance().stopAll();

		Model model = Model.getInstance();

		Vector2D dimensions = model.getDimensions();

		Text stageText = new Text("Stage " + model.getAccount().getLevel(), Text.TextColor.Black, dimensions.div(2));
		Text loadingText = new Text("Loading...", Text.TextColor.Black,
			dimensions.sub(
				Vector2D.of(dimensions.getX() / 2,
					FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL * 2)));


		drawing = new Drawing(loadingText.getDrawing(), stageText.getDrawing());
		drawing.setDimensions(dimensions);

		getAudioHandle().playIfNeeded(LOADING_LEVEL_SCENE_SOUND);
	}

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 * <p>
	 * if the handle is playing nothing, it transitions to a new GameScene
	 */
	@Override
	public Drawing update()
	{
		if (!getAudioHandle().isPlayingSomething())
			doTransition(GameScene::new);

		return drawing;
	}

	/**
	 * cleanup() must be called before transition to a new scene
	 * <p>
	 * stop all sounds
	 */
	@Override
	public void cleanup()
	{
		// This should not be necessary
		AudioPlayer.getInstance().stopAll();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
