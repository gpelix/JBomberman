package app.jbomberman.view;

import app.jbomberman.util.Callback;

public abstract class AbstractGraphicFactory
{
    public abstract AbstractGraphicLoop buildLoopTimer(Callback callback);
}
