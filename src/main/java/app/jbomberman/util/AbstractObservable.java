package app.jbomberman.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * One common problem when using the Observable interface is that we have to
 * define in the class implementing that interface the method getObservers and
 * to define a set called observers. This pattern is so common that we defined
 * AbstractObservable to solve this problem. Classes that do not inherit anything
 * and must implement the Observable interface can directly extend the
 * AbstractObservable class
 *
 * @author gp
 */
public abstract class AbstractObservable implements Observable
{
	/**
	 * the set of Observers
	 */
	private final Set<Observer> observers;

	/**
	 * Constructor to create the set of observers
	 */
	protected AbstractObservable()
	{
		observers = new HashSet<>();
	}

	@Override
	public Collection<Observer> getObservers()
	{
		return observers;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
