package app.jbomberman.view.scenes;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.view.View;

import java.util.function.Supplier;

/**
 * BomberManScene is one of the Scenes of the Application. It's an abstract class
 * that provides an easy way to handle what the screen is currently displaying
 *
 * @author gp
 */
public abstract class BomberManScene
{
	/**
	 * a reference to the view
	 */
	private final View view;

	/**
	 * protected constructor that sets up the reference to the view
	 *
	 * @param view the view
	 */
	protected BomberManScene(View view)
	{
		this.view = view;
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

	/**
	 * doTransition() takes a sceneConstructor and adapts that to a Supplier
	 * and finally calls doTransition(Supplier)
	 *
	 * @param sceneConstructor the constructor taking a view as parameter of the
	 *                         next scene
	 */
	protected void doTransition(SceneConstructor sceneConstructor)
	{
		doTransition(() -> sceneConstructor.makeANew(view));
	}

	/**
	 * SceneConstructor is a SAM whose only abstract method has the same signature
	 * of the constructors of the scenes
	 */
	@FunctionalInterface
	protected interface SceneConstructor
	{
		/**
		 * makeAnew will be set to the constructor of the scenes
		 *
		 * @param view the view that must be passed to the constructor of the scene
		 * @return the constructed scene
		 */
		BomberManScene makeANew(View view);
	}

	/**
	 * getView() allows subclasses to get the view
	 *
	 * @return the view
	 */
	protected View getView()
	{
		return view;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
