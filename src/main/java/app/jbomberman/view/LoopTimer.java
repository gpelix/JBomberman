package app.jbomberman.view;

/**
 * LoopTimer is a timer that calls a function repeatedly.
 * <p>
 * The classes implementing LoopTimer rely on graphic-library specific looptimers.
 * In this way, for example, the function will be called in a JavaFx thread when
 * we use JavaFx
 * <p>
 * it might seem strange that we want clients to use LoopTimer without
 * even knowing what concrete timer we are using.
 * But this will be accomplished through the use of the GraphicFactory.
 *
 * @author gp
 */
public abstract class LoopTimer
{
	/**
	 * start() is called to start the LoopTimer
	 */
	public abstract void start();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
