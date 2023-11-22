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
 
package app.jbomberman.view.widgets;

import app.jbomberman.JBomberManException;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawable;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.KeyBoardControllerKeyCode;
import app.jbomberman.view.KeyBoardUpdateInfo;

/**
 * TypingBox is a box that will display the text typed
 * TypingBox implements the observer interface as it has to listen for
 * key-pressed events. So it listens to the KeyBoardController
 *
 * @author gp
 */
public class TypingBox implements Observer, Drawable
{
	/**
	 * Text contains the currently typed text
	 */
	private final Text text;

	/**
	 * controller is a reference to the Controller. Using this reference, we attach
	 * and detach this to and from the keyBoardController
	 */
	private final Controller controller;

	/**
	 * construct a TypingBox
	 *
	 * @param controller         the controller that handles forwards the keyboar event
	 * @param color              the color of the TypingBox
	 * @param coordinateOfCenter the center of the TypingBox
	 */
	public TypingBox(Controller controller, Text.TextColor color, Vector2D coordinateOfCenter)
	{
		text = new Text(color, coordinateOfCenter);
		this.controller = controller;
		controller.attachKeyBoardObserver(this);
	}

	/**
	 * destruct() must be called to detach this from the keyboardcontroller
	 */
	public void destruct()
	{
		controller.detachKeyBoardObserver(this);
	}

	/**
	 * update() must listen from keyboard events like the pressing of the arrows
	 *
	 * @param observable the observable
	 * @throws JBomberManException if the observable is not a KeyBoardController
	 */
	@Override
	public void update(Observable observable)
	{
		if (observable instanceof KeyBoardController keyBoardController) {
			KeyBoardUpdateInfo updateInfo =
				keyBoardController.getUpdateInfo();

			if (updateInfo.getKeyBoardEventType() == KeyBoardUpdateInfo.KeyBoardEventType.KEY_RELEASED)
				return;

			KeyBoardControllerKeyCode code = updateInfo.getKeyCode();

			if (code == KeyBoardControllerKeyCode.BACK_SPACE &&
				text.length() != 0)
				text.pop();

			if (code != KeyBoardControllerKeyCode.OTHER
				&& code.isPrintable())
			{
				text.append(updateInfo.getKeyCode().getRepr().fromJust().toString());
			}
		} else
			throw new JBomberManException("Unable to handle notification coming from `" + observable + "'");
	}

	/**
	 * getText() gets the currently typed text
	 *
	 * @return the currently typed text
	 */
	public String getText()
	{
		return text.getText();
	}

	/**
	 * getDrawing() returns the current drawing of the typing box
	 *
	 * @return the current drawing
	 */
	@Override
	public Drawing getDrawing()
	{
		return text.getDrawing();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
