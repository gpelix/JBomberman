
package app.jbomberman.util;

import java.util.ArrayList;
import java.util.Collection;

public interface Observable
{
    Collection<Observer> getObservers();

    default void attachObserver(Observer observer)
    {
        getObservers().add(observer);
    }

    default void detachObserver(Observer observer)
    {
        getObservers().remove(observer);
    }

    default void updateAllObservers()
    {
        (new ArrayList<>(getObservers())).forEach(observer -> observer.update(this));
    }
}
