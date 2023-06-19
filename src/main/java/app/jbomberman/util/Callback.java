package app.jbomberman.util;

/**
 * Callback is a SAM that provide a single abstract method run
 *
 * @author gp
 */
@FunctionalInterface
public interface Callback
{
	/**
	 * run() is the single abstract method
	 */
	void run();
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
