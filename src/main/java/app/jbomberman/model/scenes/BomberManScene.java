
package app.jbomberman.model.scenes;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;

/**
 * BomberManScene is one of the Scenes of the Application. It's an abstract class
 * that provides an easy way to handle what the screen is currently displaying
 */
public abstract class BomberManScene
{
    private final Model model;

    protected BomberManScene(Model model)
    {
        this.model = model;
    }

    /**
     * update() should be called regularly by a timer like AnimationTimer in javafx
     * package. It should be used to update what gets displayed on the screen
     */
    public abstract Drawing update();

    /**
     * cleanup before transition to a new scene
     */
    public abstract void cleanup();

    public abstract void setup();

    protected void doTransition(BomberManScene nextScene)
    {
        cleanup();
        nextScene.setup();
        model.setScene(nextScene);
    }

    protected Model getModel()
    {
        return model;
    }
}
