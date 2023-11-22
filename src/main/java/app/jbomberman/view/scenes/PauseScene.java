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

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;
import app.jbomberman.view.widgets.HorizontalMenu;
import app.jbomberman.view.widgets.MenuEntry;
import app.jbomberman.view.widgets.Text;

/**
 * PauseScene is the scene to display when a game gets suspended
 *
 * @author gp
 */
public class PauseScene extends BomberManScene
{
	/**
	 * menu keeps the HorizontalMenu that has two entries, exit and resume
	 */
	private final HorizontalMenu menu;

	/**
	 * public constructor builds a PauseScene from a View
	 *
	 * @param view      the View
	 * @param gameScene the GameScene
	 */
	public PauseScene(View view, GameScene gameScene)
	{
		super(view);

		AudioPlayer.getInstance().pausePlaying();

		menu = new HorizontalMenu(
			new MenuEntry("resume", Text.TextColor.Black, () -> {
				doTransition(() -> gameScene);
				gameScene.resume();
			}),
			new MenuEntry("exit", Text.TextColor.Black, () -> {
				AudioPlayer.getInstance().stopAll();
				doTransition(StartDisplay::new);
			})
		);

		Vector2D modelDims = Model.getInstance().getDimensions();

		menu.setWidth(modelDims.getX());
		menu.setTopLeft(Vector2D.of(0, 0));

		Controller.controller.attachKeyBoardObserver(menu);
	}

	/**
	 * cleanup() must be called before transition to a new scene
	 * <p>
	 * resume playing and detach menu from keyboardcontroller
	 */
	@Override
	public void cleanup()
	{
		AudioPlayer.getInstance().resumePlaying();
		Controller.controller.detachKeyBoardObserver(menu);
	}

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 * <p>
	 * simply return the drawing of the menu
	 */
	@Override
	public Drawing update()
	{
		return menu.getDrawing();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
