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
import app.jbomberman.util.Maybe;
import app.jbomberman.view.KeyBoardControllerKeyCode;

import java.util.stream.Stream;

/**
 * VerticalMenu is a class that models a vertical menu.
 * All it does is setting the correct arrangement policy
 * (which is arrangeVertically) and overriding the (get/set)Width() methods
 *
 * @author gp
 */
public class VerticalMenu extends Menu
{
	/**
	 * height is the height of the VerticalMenu. It will be used to spread the
	 * entries to fill the whole height
	 */
	private Maybe<Integer> height = Maybe.nothing();

	/**
	 * Constructor from Stream of MenuEntry's
	 *
	 * @param stream the stream containing the MenuEntry's
	 */
	public VerticalMenu(Stream<MenuEntry> stream)
	{
		super(stream);
		setArrangementPolicy(this::arrangeVertically);
	}

	/**
	 * Constructor from varargs
	 *
	 * @param menuEntries the list of the MenuEntry's
	 */
	public VerticalMenu(MenuEntry... menuEntries)
	{
		super(menuEntries);
		setArrangementPolicy(this::arrangeVertically);
	}

	/**
	 * getNextKey() returns the key that must be pressed to select the next entry in
	 * the menu
	 *
	 * @return the key that must be pressed to select the next entry in the menu
	 */
	@Override
	protected final KeyBoardControllerKeyCode getNextKey()
	{
		return KeyBoardControllerKeyCode.DOWN;
	}

	/**
	 * getPrevKey() returns the key that must be pressed to select the previous entry in
	 * the menu
	 *
	 * @return the key that must be pressed to select the previous entry in the menu
	 */
	@Override
	protected final KeyBoardControllerKeyCode getPrevKey()
	{
		return KeyBoardControllerKeyCode.UP;
	}

	/**
	 * getHeight() returns the height of the vertical menu
	 *
	 * @return the height of the vertical menu
	 * @throws JBomberManException if the height is unset
	 */
	@Override
	protected final int getHeight()
	{
		return height.unwrapOrThrow(() -> new JBomberManException("heihgt is unset"));
	}

	/**
	 * setHeight() sets the hight of the vertical menu
	 *
	 * @param height the height of the menu
	 */
	public void setHeight(int height)
	{
		this.height = Maybe.just(height);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
