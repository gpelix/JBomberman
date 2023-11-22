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
 
package app.jbomberman.view;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.AbstractObservable;
import app.jbomberman.util.Maybe;

import java.util.HashSet;
import java.util.Set;

/**
 * KeyBoardController is the class responsible for the handling the keyboard
 * input that comes from the user.
 * <p>
 * KeyBoardController communicates with other class the keystrokes using the
 * observer/Observable pattern.
 *
 * @author gp
 */
public abstract class KeyBoardController extends AbstractObservable
{
	/**
	 * Set of KeyCodes currently pressed
	 */
	private final Set<KeyBoardControllerKeyCode> pressedKeyCodes = new HashSet<>();

	/**
	 * updateInfo contains a reference to the most recent keyboard event
	 */
	private Maybe<KeyBoardUpdateInfo> updateInfo = Maybe.nothing();

	/**
	 * getUpdateInfo() must be called by the observer upon notification to get
	 * information about the event that triggered the notification
	 *
	 * @return the KeyBoardUpdateInfo represent the event
	 */
	public KeyBoardUpdateInfo getUpdateInfo()
	{
		return updateInfo.unwrapOrThrow(() -> new JBomberManException(
			"Called update but there are no update info"));
	}

	/**
	 * function to be called when a key pressed notification gets received
	 *
	 * @param keyCode the key code received
	 */
	protected void onPressed(KeyBoardControllerKeyCode keyCode)
	{
		pressedKeyCodes.add(keyCode);

		setUpdateInfo(new KeyBoardUpdateInfo(
			KeyBoardUpdateInfo.KeyBoardEventType.KEY_PRESSED,
			keyCode,
			pressedKeyCodes));

		updateAllObservers();
	}

	/**
	 * function to be called when a key released notification gets received
	 *
	 * @param keyCode the key code received
	 */
	protected void onReleased(KeyBoardControllerKeyCode keyCode)
	{
		pressedKeyCodes.remove(keyCode);

		setUpdateInfo(new KeyBoardUpdateInfo(
			KeyBoardUpdateInfo.KeyBoardEventType.KEY_RELEASED,
			keyCode,
			pressedKeyCodes));

		updateAllObservers();
	}

	/**
	 * setUpdateInfo sets the current update info.
	 * <p>
	 * It's meant to be called by subclasses to set the update infos
	 *
	 * @param info the info that must be set
	 */
	protected void setUpdateInfo(KeyBoardUpdateInfo info)
	{
		updateInfo = Maybe.just(info);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
