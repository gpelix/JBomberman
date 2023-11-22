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
 * HorizontalMenu is a class that models a horizontal menu.
 * All it does is setting the correct arrangement policy
 * (which is arrangeHorizontally) and overriding the (get/set)Height() methods
 *
 * @author gp
 */
public class HorizontalMenu extends Menu
{
	/**
	 * width is the width of the horizontal menu
	 */
	private Maybe<Integer> width = Maybe.nothing();

	/**
	 * Constructor from Stream of MenuEntry's
	 *
	 * @param stream the stream containing the MenuEntry's
	 */
	public HorizontalMenu(Stream<MenuEntry> stream)
	{
		super(stream);
		setArrangementPolicy(this::arrangeHorizontally);
	}

	/**
	 * Constructor from varargs
	 *
	 * @param menuEntries the list of the MenuEntry's
	 */
	public HorizontalMenu(MenuEntry... menuEntries)
	{
		super(menuEntries);
		setArrangementPolicy(this::arrangeHorizontally);
	}

	/**
	 * getWidth() returns the width of the horizontal menu
	 *
	 * @return the width of the horizontal menu
	 * @throws JBomberManException if the width is unset
	 */
	@Override
	public final int getWidth()
	{
		return width.unwrapOrThrow(() -> new JBomberManException("Width is unset"));
	}

	/**
	 * getNextKey() returns the key that must be pressed to select the next entry in
	 * the menu
	 *
	 * @return the key that must be pressed to select the next entry in the menu
	 */
	@Override
	protected KeyBoardControllerKeyCode getNextKey()
	{
		return KeyBoardControllerKeyCode.RIGHT;
	}

	/**
	 * getPrevKey() returns the key that must be pressed to select the previous entry in
	 * the menu
	 *
	 * @return the key that must be pressed to select the previous entry in the menu
	 */
	@Override
	protected KeyBoardControllerKeyCode getPrevKey()
	{
		return KeyBoardControllerKeyCode.LEFT;
	}

	/**
	 * setWidth() sets the width of the horizonral menu
	 *
	 * @param width the width of the menu
	 */
	public void setWidth(int width)
	{
		this.width = Maybe.just(width);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
