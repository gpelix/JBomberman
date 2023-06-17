package app.jbomberman.drawing;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Vector2D;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Panel class contains some Text's and lays them out texts appropriately.
 *
 * @param <T> the type of the text that this panel will be showing
 * @author gp
 * @implNote we keep the arrangement policies here because they are not menu-specific
 * specific
 * @see Text
 */
/*~*/abstract class Panel<T extends Text> implements Drawable
{
    /**
     * entris is the list of texts that will have to be arranged
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
    public final ImmutableList<T> getEntries()
    {
        return ImmutableList.copyOf(entries);
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
