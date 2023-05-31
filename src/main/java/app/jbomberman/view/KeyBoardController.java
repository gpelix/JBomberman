package app.jbomberman.view;

import app.jbomberman.util.Observable;

public abstract class KeyBoardController implements Observable
{
    public abstract KeyBoardUpdateInfo getUpdateInfo();
}
