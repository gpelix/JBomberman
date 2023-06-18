package app.jbomberman.view;

import app.jbomberman.JBomberManException;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Vector2D;

/**
 * View class plays the role of the View in the MVC pattern we used to build
 * the application
 *
 * @author gp
 */
public abstract class View implements Observer
{
    private final GraphicFactory factory;

    private final KeyBoardController keyBoardController;

    protected View(GraphicFactory factory, KeyBoardController keyBoardController)
    {
        this.factory = factory;
        this.keyBoardController = keyBoardController;
    }

    public GraphicFactory getGraphicFactory()
    {
        return factory;
    }

    public KeyBoardController getKeyBoardController()
    {
        return keyBoardController;
    }

    /**
     * Given a drawing, draw() centers it in the middle of the screen and displays
     * it.
     *
     * @param drawing the drawing to be displayed
     * @implNote This approach is kind of slow as every time we have to redraw everything.
     * However, with some benchmarks we saw that the total amount of time needed
     * to draw the screen is less than the time that the model needs to update
     * himself. So using this approach is not such a bad choice
     */
    public abstract void draw(Drawing drawing);

    /**
     * setTitle() sets the title to the window
     *
     * @param title the title that we want to set to the window
     */
    public abstract void setTitle(String title);

    /**
     * setIcon() sets the icon to be displayed for this application
     *
     * @param path the path of the icon to display
     */
    public abstract void setIcon(String path);

    /**
     * setCloseOperationCallBack() is needed if we want to add a callback to be
     * called when the application gets closed by the user
     *
     * @param callBack the callback to call on close of the window
     */
    public abstract void setCloseOperationCallBack(Callback callBack);

    /**
     * update() listens for update requests by the model. In that case, it draws
     * the drawing
     *
     * @param observable the observable that called the method update.
     */
    @Override
    public void update(Observable observable)
    {
        if (observable instanceof Model model)
            draw(model.getDrawing());
        else
            throw new JBomberManException("not implemented");
    }

    protected Vector2D getDrawingDimensions(Drawing drawing)
    {
        return drawing.getDimensions().orElse(() ->
            drawing.getDrawObjects().stream()
                .reduce(
                    Vector2D.getIdentity(),
                    (acc, drawObj) -> acc.elementWiseApply(drawObj.sum(drawObj.getDimensions()),
                        Math::max),
                    (product1, product2) ->
                        product1.elementWiseApply(product2, Math::max)));
    }

    /**
     * close() is needed to close the view and shut down the program
     */
    public abstract void close();
}