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

import app.jbomberman.util.Callback;

/**
 * MenuEntry represents the entry for a menu. It's just text but adds also a
 * callback that must be called when the menu entry is chosen
 *
 * @author gp
 * @see Menu
 */
public class MenuEntry extends Text
{
	/**
	 * the callback that must be called when the menu entry is chosen
	 */
	private final Callback callback;

	/**
	 * MenuEntry is a public constructor that takes the text, color, and the
	 * callback to be called when chosen
	 *
	 * @param str      the text to be shown on the entry
	 * @param color    the color of the entry
	 * @param callback the callback to be called when this entry is chosen
	 */
	public MenuEntry(String str, TextColor color, Callback callback)
	{
		super(str, color);

		this.callback = callback;
	}

	/**
	 * Constructor that takes the text to set and a callback. The color will be the
	 * one of the text
	 *
	 * @param text     the Text from which the text and the color must be taken
	 * @param callback the callback to be called when this entry is chosen
	 */
	public MenuEntry(Text text, Callback callback)
	{
		this(text.getText(), text.getColor(), callback);
	}

	/**
	 * getText() gets the text. We substring by one because in the future we'll
	 * have to add one empty character at the beginning
	 *
	 * @return the text without the first fake character
	 */
	@Override
	public String getText()
	{
		return super.getText().substring(1);
	}

	/**
	 * setSelected() selects/deselects the entry
	 *
	 * @param isSelected isSelected specifies whether this must entry must be selected
	 */
	public void setSelected(boolean isSelected)
	{
		if (isSelected)
			setColor(TextColor.Gray);
		else
			setColor(TextColor.Black);
	}

	/**
	 * callCallback() calls the callback
	 */
	public void callCallback()
	{
		callback.run();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
