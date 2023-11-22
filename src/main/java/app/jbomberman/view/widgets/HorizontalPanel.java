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

import app.jbomberman.drawing.Drawing;

/**
 * HorizontalPanel is simply a panel with arrangeHorizontally as arrangementPolicy
 *
 * @param <T> the type of the element in the HorizontalPanel
 */
public class HorizontalPanel<T extends Text> extends Panel<T>
{
	/**
	 * width is the width of the horizontal panel
	 */
	private int width;

	/**
	 * Constructor from varargs
	 *
	 * @param entries the list of the texts
	 */
	@SafeVarargs
	public HorizontalPanel(T... entries)
	{
		super(entries);
		width = 0;
	}

	/**
	 * getDrawing() is a function that gets the current drawing
	 *
	 * @return the current drawing depicting the menu
	 */
	@Override
	public Drawing getDrawing()
	{
		return arrangeHorizontally();
	}

	/**
	 * getWidth() returns the width of the horizontal menu
	 *
	 * @return the width of the horizontal menu
	 */
	@Override
	public int getWidth()
	{
		return width;
	}

	/**
	 * setWidth() sets the width of the horizonral menu
	 *
	 * @param width the width of the menu
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
