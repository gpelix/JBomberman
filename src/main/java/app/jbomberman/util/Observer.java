package app.jbomberman.util;

/**
 * Observer interface to implement the Observer Observable design pattern
 * As one possibility described by the Gang of Four books, we chose to give
 * a lot of responsibility to the Observer that must query
 * the observable to get more information about the event that triggered the
 * update. Of course, it must downcast and
 * then call the functions provided by the public interface
 * of the specific class implementing the Observable
 * interface
 *
 * @author gp
 */
public interface Observer
{
    /**
     * The method that must be called to cause an update of the observer.
     *
     * @param observable the observable that called the method update.
     */
    void update(Observable observable);
}
