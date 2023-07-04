package app.jbomberman.view;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.scenes.BomberManScene;
import app.jbomberman.view.scenes.StartDisplay;

/**
 * View class plays the role of the View in the MVC pattern we used to build
 * the application.
 *
 * @author gp
 */
public abstract class View
{
	/**
	 * currentBomberManScene is the current Scene displayed on the View
	 */
	private BomberManScene currentBomberManScene;

	/**
	 * setup() must be called to effectively start the view. It goes onto the
	 * first scene
	 */
	public void setup()
	{
		currentBomberManScene = new StartDisplay(this);
	}

	/**
	 * setScene() sets the next Scene
	 *
	 * @param newScene the next Scene
	 */
	public void setScene(BomberManScene newScene)
	{
		currentBomberManScene = newScene;
	}

	/**
	 * GraphicFactory is the factory of abstract graphical components
	 */
	private final GraphicFactory factory;

	/**
	 * keyBoardController is the keyboard controller that must be listening for
	 * keyboard events and notify observers. There is just one keyBoardController
	 * as there is just one window at the moment
	 */
	private final KeyBoardController keyBoardController;

	/**
	 * public constructor that initializes a View from a GraphicFactory and keyBoardController
	 *
	 * @param factory the abstract components factory
	 */
	protected View(GraphicFactory factory)
	{
		this.factory = factory;
		this.keyBoardController = factory.getKeyBoardController();
	}

	/**
	 * getGraphicFactory returns the graphic factory
	 *
	 * @return the graphic factory
	 */
	public GraphicFactory getGraphicFactory()
	{
		return factory;
	}

	/**
	 * getKeyBoardController gets the keyBoardController
	 *
	 * @return the keyboard controller
	 */
	public KeyBoardController getKeyBoardController()
	{
		return keyBoardController;
	}

	/**
	 * Given a drawing, draw() centers it in the middle of the screen and displays
	 * it.
	 *
	 * @param drawing the drawing to be displayed
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
	 * @param callBack the callback to call when the window gets closed
	 */
	public abstract void setCloseOperationCallBack(Callback callBack);

	/**
	 * update() listens for update requests by the model. In that case, it draws
	 * the drawing
	 *
	 * @param observable the {@link Observable} that called the method update.
	 */
	// TODO: Should this really be disabled
//	@Override
//	public void update(Observable observable)
//	{
//		if (observable instanceof Model model)
//			draw(model.getDrawing());
//		else
//			throw new JBomberManException("not implemented");
//	}

	/**
	 * getDrawingDimensions() must be called by classes inheriting View, to get the
	 * dimensions of a drawing
	 *
	 * @param drawing the drawing whose dimensions must be calculated
	 * @return the dimensions of the drawing
	 */
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

	/**
	 * update() updates the view calling update() on the currentBomberManScene
	 */
	public void update()
	{
		draw(currentBomberManScene.update());
	}

	/**
	 * getFileChooser() must be used to get the FileChooser
	 *
	 * @return the FileChooser
	 */
	public abstract FileChooser getFileChooser();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
