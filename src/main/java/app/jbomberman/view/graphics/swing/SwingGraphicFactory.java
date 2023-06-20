package app.jbomberman.view.graphics.swing;

import app.jbomberman.util.Callback;
import app.jbomberman.view.GraphicFactory;
import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.LoopTimer;

/**
 * SwingGraphicFactory is the concrete implementation of the GraphicFactory for
 * java swing
 *
 * @author gp
 */
public class SwingGraphicFactory extends GraphicFactory
{
	/**
	 * getLoopTimer() constructs a timer from a callback
	 *
	 * @param callback the callback that must when repeatedly
	 * @return the constructed LoopTimer
	 */
	@Override
	public LoopTimer getLoopTimer(Callback callback)
	{
		return new SwingLoopTimer(callback);
	}

	/**
	 * getKeyBoardController() constructs a new instance of a KeyBoardController
	 *
	 * @return the constructed keyboardController
	 */
	@Override
	public KeyBoardController getKeyBoardController()
	{
		return new SwingKeyBoardController();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
