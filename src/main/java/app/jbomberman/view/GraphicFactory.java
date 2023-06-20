package app.jbomberman.view;

import app.jbomberman.util.Callback;

/**
 * GraphicFactory is an abstract class representing a factory of components
 * graphic-library related
 * <p>
 * Clearly we are using the factory pattern
 *
 * @author gp
 */
public abstract class GraphicFactory
{
	/**
	 * getLoopTimer() constructs a timer from a callback
	 *
	 * @param callback the callback that must when repeatedly
	 * @return the constructed LoopTimer
	 */
	public abstract LoopTimer getLoopTimer(Callback callback);

	/**
	 * getKeyBoardController() constructs a new instance of a KeyBoardController
	 *
	 * @return the constructed keyboardController
	 */
	public abstract KeyBoardController getKeyBoardController();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
