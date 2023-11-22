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
 
package app.jbomberman.drawing;

import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Drawing is a class representing a drawing, that is a set of DrawObjects
 *
 * @author gp
 */
public class Drawing
{
	/**
	 * drawObjects is the list of DrawObjects in the Drawing
	 */
	private List<LayeredDrawObject> drawObjects;

	/**
	 * dimensions are the dimensions of the Drawing. It's a Maybe because it might
	 * be unset
	 */
	private Maybe<Vector2D> dimensions = Maybe.nothing();

	/**
	 * Construct an empty drawing
	 */
	public Drawing()
	{
		drawObjects = new ArrayList<>();
	}

	/**
	 * Construct a drawing from a list of drawObjects
	 *
	 * @param drawObjects the list of drawObjects
	 */
	public Drawing(LayeredDrawObject... drawObjects)
	{
		this(Arrays.stream(drawObjects));
	}

	/**
	 * Construct a drawing from a Stream of drawObjects
	 *
	 * @param stream the stram of drawObjects
	 */
	public Drawing(Stream<LayeredDrawObject> stream)
	{
		drawObjects = stream.collect(Collectors.toList());
	}

	/**
	 * Construct a drawing from many drawings, merging keeping the layers intact
	 *
	 * @param drawings the drawings that will be merged in the costructed drawing
	 */
	public Drawing(Drawing... drawings)
	{
		this();

		Arrays.stream(drawings).forEach(this::merge);
	}

	/**
	 * add() adds a DrawObject to the specified layer
	 *
	 * @param drawObject the DrawObject to add
	 */
	public void add(LayeredDrawObject drawObject)
	{
		drawObjects.add(drawObject);
	}

	/**
	 * getDrawObjects() gets the map of drawObjects
	 *
	 * @return the layer to list of drawObjects
	 */
	public List<LayeredDrawObject> getDrawObjects()
	{
		return drawObjects;
	}

	/**
	 * merge() merges a drawing into this. Before adding the drawObject to their
	 * layer, it applies the function toNewLayerFunction
	 *
	 * @param otherDrawing       the drawing that will be merged in this
	 * @param toNewLayerFunction the function that must be used to remap the
	 *                           layers of otherDrawing to their new layer
	 */
	public void merge(Drawing otherDrawing,
										Function<Integer, Integer> toNewLayerFunction)
	{
		otherDrawing.drawObjects.forEach(layeredDrawObject ->
			drawObjects.add(new LayeredDrawObject(Vector2D.of(layeredDrawObject.getX(), layeredDrawObject.getY()),
				layeredDrawObject.getImage(),
				toNewLayerFunction.apply(layeredDrawObject.getLayer()))));
	}

	/**
	 * merge() is a wrapper around merge() that doesn't do any remapping of the
	 * layers of otherDrawing
	 *
	 * @param otherDrawing the other Drawing to merge in this
	 */
	public void merge(Drawing otherDrawing)
	{
		merge(otherDrawing, Function.identity());
	}

	/**
	 * setDimensions() sets the dimensions of the Drawing
	 *
	 * @param dimensions a vector2D keeping the dimensions of the Drawing
	 */
	public void setDimensions(Vector2D dimensions)
	{
		this.dimensions = Maybe.just(dimensions);
	}

	/**
	 * getDimensions() gets the dimensions of the drawing
	 *
	 * @return the dimensions of the drawing
	 */
	public Maybe<Vector2D> getDimensions()
	{
		return dimensions;
	}

	/**
	 * toString() returns a representation of the Drawing used for debugging
	 *
	 * @return a representation of the Drawing used for debugging
	 */
	@Override
	public String toString()
	{
		return drawObjects.toString();
	}

	/**
	 * getMaxLayer() gets the maximum layer
	 *
	 * @return the maximum layer in the Drawing
	 */
	public int getMaxLayer()
	{
		return drawObjects.stream().mapToInt(LayeredDrawObject::getLayer).max().orElse(-1);
	}

	/**
	 * numberOfItems() gets the number of drawObjects in the Drawing
	 *
	 * @return the number of DrawObjects in the Drawing
	 */
	public int numberOfItems()
	{
		return drawObjects.size();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
