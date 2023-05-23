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

public class Drawing
{
    // The outer integer is the layer
    private final Map<Integer, List<DrawObject>> drawObjects;

    private Maybe<Vector2D> dimensions = Maybe.nothing();

    public Drawing()
    {
        drawObjects = new HashMap<>();
    }

    public Drawing(DrawObject... drawObjects)
    {
        this(Arrays.stream(drawObjects));
    }

    public Drawing(Stream<DrawObject> stream)
    {
        this();
        drawObjects.put(0, stream.collect(Collectors.toList()));
    }

    public Drawing(Drawing... drawings)
    {
        this();

        Arrays.stream(drawings).forEach(this::merge);
    }

    public void add(DrawObject drawObject, int layer)
    {
        drawObjects.computeIfAbsent(layer, ignored -> new ArrayList<>()
        {
            {
                add(drawObject);
            }
        });
    }

    public Map<Integer, List<DrawObject>> getDrawObjects()
    {
        return drawObjects;
    }

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

    public void merge(Drawing otherDrawing)
    {
        merge(otherDrawing, Function.identity());
    }

    public void setDimensions(Vector2D dimensions)
    {
        this.dimensions = Maybe.just(dimensions);
    }

    public Maybe<Vector2D> getDimensions()
    {
        return dimensions;
    }

    @Override
    public String toString()
    {
        return Arrays.toString(drawObjects.values().toArray());
    }
}
