package app.jbomberman.view;

import app.jbomberman.util.Callback;

public abstract class GraphicFactory
{
    public abstract LoopTimer getLoopTimer(Callback callback);
}
