package app.jbomberman.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractObservable implements Observable
{
    private final Set<Observer> observers;

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
