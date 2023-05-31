package app.jbomberman.view.scenes;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.util.Observer;
import app.jbomberman.view.AbstractView;

import java.util.function.Supplier;

/**
 * BomberManScene is one of the Scenes of the Application. It's an abstract class
 * that provides an easy way to handle what the screen is currently displaying
 *
 * @author gp
 */
public abstract class BomberManScene implements Observer
{
    private final AbstractView view;

    protected BomberManScene(AbstractView abstractView)
    {
        view = abstractView;
    }

    /**
     * update() should be called regularly by a timer like AnimationTimer in javafx
     * package. It should be used to update what gets displayed on the screen
     */
    public abstract Drawing update();

    /**
     * cleanup() must be called before transition to a new scene
     */
    public abstract void cleanup();


    /**
     * doTransition allows subclasses to make smooth painless uniform transitions
     * from one scene to another one
     *
     * @param nextSceneSupplier a supplier of a new scene
     * @implNote we pass a supplier in almost all the cases we want to defer
     * the creation and initialization of the new scene up to the moment after
     * which we did the cleanup of the previous scene
     */
    protected void doTransition(Supplier<BomberManScene> nextSceneSupplier)
    {
        cleanup();
        view.setScene(nextSceneSupplier.get());
    }
}
