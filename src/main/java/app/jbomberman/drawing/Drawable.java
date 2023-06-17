package app.jbomberman.drawing;

/**
 * Drawable is a simple interface used to define a standard way of calling function
 * to get a Drawing
 *
 * @author gp
 */
public interface Drawable
{
    /**
     * getDrawing is the method that must be called to get the drawing of the
     * Drawable
     *
     * @return a drawing describing the Drawable
     */
    Drawing getDrawing();
}
