
package app.jbomberman.util;

/**
 * Callback is a SAM that provide a single abstact method run
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

	Callback NULL_CALLBACK = () -> {
	};
}
