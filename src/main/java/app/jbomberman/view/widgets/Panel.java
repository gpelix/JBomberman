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
import app.jbomberman.drawing.Drawable;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Panel class contains some Text's and lays them out texts appropriately.
 * We keep the arrangement policies here because they are not menu-specific
 *
 * @param <T> the type of the text that this panel will be showing
 * @author gp
 * specific
 * @see Text
 */
/*~*/abstract class Panel<T extends Text> implements Drawable
{
	/**
	 * entries is the list of texts that will have to be arranged
	 */
	private final List<T> entries;

	/**
	 * topLeft corner of the panel
	 */
	private Maybe<Vector2D> topLeft = Maybe.nothing();

	/**
	 * Sets up a panel from a list of texts
	 *
	 * @param texts the list of text that will be used to intialize the Panel
	 */
	@SafeVarargs
	public Panel(T... texts)
	{
		this(Arrays.stream(texts));
	}

	/**
	 * Constructor that initializes a panel from a Stream of texts
	 *
	 * @param stream the stream that will be used to initialize the panel
	 */
	public Panel(Stream<T> stream)
	{
		entries = stream.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * getEntries() gets the list of entries
	 *
	 * @return the list of entries
	 */
	public final List<T> getEntries()
	{
		return Collections.unmodifiableList(entries);
	}

	/**
	 * setTopLeft() sets the topleft corner of the Panel
	 *
	 * @param topLeft the top left coordinate
	 */
	public final void setTopLeft(Vector2D topLeft)
	{
		this.topLeft = Maybe.just(topLeft);
	}

	/**
	 * getTopLeft gets the topLeft corner
	 *
	 * @return the topLeft corner
	 */
	protected final Maybe<Vector2D> getTopLeft()
	{
		return topLeft;
	}

	/**
	 * arrangeHorizontally() arranges the texts horizontally. It's one of the
	 * two policies available. You can also add more policies
	 *
	 * @return the Drawing representing the Panel
	 */
	protected final Drawing arrangeHorizontally()
	{
		Drawing returnDrawing = new Drawing();

		Maybe<Vector2D> topLeft = getTopLeft();

		if (topLeft.isJust()) {
			Vector2D topLeftUnwrapped = topLeft.fromJust();

			for (int i = 0; i < entries.size(); i++) {
				T entry = entries.get(i);
				entry.setPosition(
					topLeftUnwrapped.sum(Vector2D.of((int) (((double) i + 0.5) * getWidth() / entries.size()),
						0)));
			}
		} else {
			throw new JBomberManException("Unset topLeft");
		}

		entries.forEach(text -> returnDrawing.merge(text.getDrawing()));

		return returnDrawing;
	}

	/**
	 * arrangeVertically() must be called to arrange the texts vertically. It's
	 * one of the two possible policies
	 *
	 * @return a drawing representing the texts arranged vertically
	 */
	protected final Drawing arrangeVertically()
	{
		Drawing returnDrawing = new Drawing();

		Maybe<Vector2D> topLeft = getTopLeft();

		if (topLeft.isJust()) {
			Vector2D topLeftUnwrapped = topLeft.fromJust();

			for (int i = 0; i < entries.size(); i++) {
				T entry = entries.get(i);
				entry.setPosition(
					topLeftUnwrapped.sum(Vector2D.of(0,
						(int) (((double) i + 0.5) * getHeight() / entries.size()))));
			}
		} else
			throw new JBomberManException("Unset center");

		entries.forEach(text -> returnDrawing.merge(text.getDrawing()));

		return returnDrawing;
	}

	/**
	 * getWidth() must be overridden in subclasses that want the elements to be
	 * arranged horizontally.
	 * <p>
	 * Note that being this class is package/visible and, being getWidth() protected
	 * (its visibility might be augmented in inherited classes), there is almost no chance
	 * for code to call this function when not overridden
	 *
	 * @return the width of the panel
	 * @throws JBomberManException if this function is not overridden in subclasses
	 */
	protected int getWidth()
	{
		throw new JBomberManException(
			"Classes that use arrangeHorizontally or any other function that call getWidth must override this method");
	}

	/**
	 * getHeight() must be overridden in subclasses that want the elements to be arranged
	 * vertically
	 *
	 * @return the height of the panel
	 * @throws JBomberManException if this function is not overridden in subclasses
	 */
	protected int getHeight()
	{
		throw new JBomberManException(
			"Classes that use arrangeVertically or any other function that call getHeight must override this method");
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
