
package app.jbomberman.util;

/**
 * Unwrappable is an interface that specifies a method unwrap.
 *
 * @param <T> type of inner object to unwrap
 * @author gp
 */
@FunctionalInterface
public interface Unwrappable<T>
{
	/**
	 * unwrap() do the unwrapping
	 *
	 * @return the object that this interface is wrapping
	 */
	T unwrap();
}
