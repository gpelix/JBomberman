package app.jbomberman.drawing;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Panel<T extends Text>
{
    private final List<T> entries;

    private Maybe<Vector2D> topLeft = Maybe.nothing();

    @SafeVarargs
    public Panel(T... texts)
    {
        this(Arrays.stream(texts));
    }

    public Panel(Stream<T> stream)
    {
        entries = stream.collect(Collectors.toCollection(ArrayList::new));
    }

    public final List<T> getEntries()
    {
        return entries;
    }

    public final void setTopLeft(Vector2D topLeft)
    {
        this.topLeft = Maybe.just(topLeft);
    }

    protected final Maybe<Vector2D> getTopLeft()
    {
        return topLeft;
    }

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

    protected int getWidth()
    {
        throw new JBomberManException(
            "Classes that use arrangeHorizontally or any other function that call getWidth must override this method");
    }

    protected int getHeight()
    {
        throw new JBomberManException(
            "Classes that use arrangeVertically or any other function that call getHeight must override this method");
    }

    public abstract Drawing getDrawing();
}
