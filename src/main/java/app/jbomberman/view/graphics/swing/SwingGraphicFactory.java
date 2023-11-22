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
 
package app.jbomberman.view.graphics.swing;

import app.jbomberman.util.Callback;
import app.jbomberman.view.FileChooser;
import app.jbomberman.view.GraphicFactory;
import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.LoopTimer;

/**
 * SwingGraphicFactory is the concrete implementation of the GraphicFactory for
 * java swing
 *
 * @author gp
 */
public class SwingGraphicFactory extends GraphicFactory
{
	/**
	 * getLoopTimer() constructs a timer from a callback
	 *
	 * @param callback the callback that must when repeatedly
	 * @return the constructed LoopTimer
	 */
	@Override
	public LoopTimer getLoopTimer(Callback callback)
	{
		return new SwingLoopTimer(callback);
	}

	/**
	 * getKeyBoardController() constructs a new instance of a KeyBoardController
	 *
	 * @return the constructed keyboardController
	 */
	@Override
	public KeyBoardController getKeyBoardController()
	{
		return new SwingKeyBoardController();
	}

	/**
	 * getFileChooser() gets a FileChooser for java swing
	 *
	 * @return the java swing file chooser
	 */
	public FileChooser getFileChooser()
	{
		return new SwingFileChooser();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
