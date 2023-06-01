package app.jbomberman.view;

import app.jbomberman.util.Observable;

/**
 * KeyBoardController is the class responsible for the handling the keyboard
 * input that comes from the user.
 * <p>
 * KeyBoardController communicates with other class the keystrokes using the
 * observer/Observable pattern.
 *
 * @author gp
 */
public abstract class KeyBoardController implements Observable
{

    /**
     * getUpdateInfo() must be called by the observer upon notification to get
     * information about the event that triggered the notification
     *
     * @return the KeyBoardUpdateInfo represent the event
     */
    public abstract KeyBoardUpdateInfo getUpdateInfo();
}
