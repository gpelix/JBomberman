
package app.jbomberman.util;

/**
 * Threads utility class provides some static thread related functions
 *
 * @author gp
 */
public class Threads
{
	/**
	 * Some threads are never interrupted and need to sleep a lot, so
	 * catching InterruptedException makes the code ugly
	 *
	 * @param millis sleep time in milliseconds
	 */
	public static void sleep(int millis)
	{
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
		}
	}
}
