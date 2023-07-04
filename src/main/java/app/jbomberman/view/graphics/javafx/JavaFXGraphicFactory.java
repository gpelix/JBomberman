package app.jbomberman.view.graphics.javafx;

import app.jbomberman.util.Callback;
import app.jbomberman.view.FileChooser;
import app.jbomberman.view.GraphicFactory;
import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.LoopTimer;

/**
 * JavaFxGraphicFactory is the concrete implementation of the GraphicFactory for
 * javafx
 *
 * @author gp
 */
public class JavaFXGraphicFactory extends GraphicFactory
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
		return new JavaFXLoopTimer(callback);
	}

	/**
	 * getKeyBoardController() constructs a new instance of a KeyBoardController
	 *
	 * @return the constructed keyboardController
	 */
	@Override
	public KeyBoardController getKeyBoardController()
	{

		return new JavaFXKeyBoardController();
	}

	public FileChooser getFileChooser(JavaFXView view)
	{
		return new JavaFXFileChooser(view.getStage());
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
