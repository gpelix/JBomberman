package app.jbomberman.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Observable interface needed to implement the observer observable pattern.
 * This idea is the one to provide as many default methods as possible in order
 * to avoid the burden of reimplementing each of them in the classes that
 * implement this interface
 *
 * @author gp
 */
public interface Observable
{
    /**
     * abstract method that is needed by the default methods to get a collection
     * of observers
     *
     * @return the collection containing the observers
     */
    Collection<Observer> getObservers();

    /**
     * default method to add an Observer
     *
     * @param observer an observer to be added
     */
    default void attachObserver(Observer observer)
    {
        getObservers().add(observer);
    }

    /**
     * default method to detach an Observer
     *
     * @param observer the observer to remove
     */
    default void detachObserver(Observer observer)
    {
        Collection<Observer> observers = getObservers();

        getObservers().remove(observer);
    }

    /**
     * default method to update all the observers
     */
    default void updateAllObservers()
    {
        (new ArrayList<>(getObservers())).forEach(observer -> observer.update(this));
    }
}
