package app.jbomberman.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * CollectionUtils is a utility class that provides some useful static functions
 * to work with collections
 *
 * @author gp
 */
public class CollectionUtils
{
	/**
	 * randomPickAndRemove() is a function that picks an element from a list,
	 * and it removes it from the list
	 *
	 * @param list the list from which the element must be picked
	 * @param <T>  the type of the elements in the list
	 * @return the element picked and removed
	 */
	public static <T> T randomPickAndRemove(List<T> list)
	{
		return list.remove(RandomNumberGenerator.getInstance().nextInt(list.size()));
	}

	/**
	 * randomPick() is a function that picks a random element from a list
	 *
	 * @param list the list from which the element must be picked
	 * @param <T>  the type of the elements in the list
	 * @return the element picked
	 */
	public static <T> T randomPick(List<T> list)
	{
		return list.get(RandomNumberGenerator.getInstance().nextInt(list.size()));
	}

	/**
	 * pick() is a function that picks a generic (not necessarily random)
	 * element from a collection
	 *
	 * @param collection the collection from which the element must be picked
	 * @param <T>        the type of the elements in the Collection
	 * @return the element picked
	 */
	public static <T> Maybe<T> pick(Collection<T> collection)
	{
		Iterator<T> iterator = collection.iterator();

		return Maybe.something(iterator.hasNext() ? iterator.next() : null);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
