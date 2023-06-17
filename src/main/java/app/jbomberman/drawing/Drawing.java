package app.jbomberman.drawing;

import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    // The outer integer is the layer
    /**
     * a map containing the draw objects. In the outer map, there are the layers,
     * with the lowest numbers being the objects in the background.
     */
    private final Map<Integer, List<DrawObject>> drawObjects;

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
        drawObjects = new HashMap<>();
    }

    /**
     * Construct a drawing from a list of drawObjects
     *
     * @param drawObjects the list of drawObjects
     */
    public Drawing(DrawObject... drawObjects)
    {
        this(Arrays.stream(drawObjects));
    }

    /**
     * Construct a drawing from a Stream of drawObjects
     *
     * @param stream the stram of drawObjects
     */
    public Drawing(Stream<DrawObject> stream)
    {
        this();
        drawObjects.put(0, stream.collect(Collectors.toList()));
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
     * @param layer      the layer to which it must be added
     */
    public void add(DrawObject drawObject, int layer)
    {
        drawObjects.computeIfAbsent(layer, ignored -> new ArrayList<>()
        {
            {
                add(drawObject);
            }
        });
    }

    /**
     * getDrawObjects() gets the map of drawObjects
     *
     * @return the layer to list of drawObjects
     */
    public Map<Integer, List<DrawObject>> getDrawObjects()
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
        otherDrawing.drawObjects.forEach((idx, drawObjectsSet) ->
            drawObjects.merge(
                toNewLayerFunction.apply(idx),
                drawObjectsSet,
                (oldSet, newSet) -> new ArrayList<>()
                {
                    {
                        addAll(oldSet);
                        addAll(newSet);
                    }
                }));
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
        return Arrays.toString(drawObjects.values().toArray());
    }
}
