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
 
package app.jbomberman.view.graphics.javafx;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Maybe;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFXSetup is a simple class whose only aim is to create a javafx thread
 * and call the callback provided into that thread
 *
 * @author gp
 */
public class JavaFXSetup extends Application
{
	/**
	 * callback is the callback to be called in the javafx thread
	 */
	private static Maybe<Callback> callback = Maybe.nothing();

	/**
	 * start() is the javafx thread entry point. This is NOT meant to be called
	 * by any function at all in my program
	 *
	 * @param ignored the parameter is ignored
	 */
	@Override
	public void start(Stage ignored)
	{
		callback.doOrThrow(Callback::run, () -> new JBomberManException(
			"Callback is uninitialized"
		));
	}

	/**
	 * initialize() must be called by the View to initialize javafx
	 *
	 * @param callback the callback to be called inside the javafx thread
	 */
	public static void initialize(Callback callback)
	{
		JavaFXSetup.callback = Maybe.just(callback);
		launch();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
