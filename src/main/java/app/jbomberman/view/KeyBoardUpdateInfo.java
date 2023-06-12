package app.jbomberman.view;

import com.google.common.collect.ImmutableSet;

/**
 * KeyBoardUpdateInfo is a class meant to be accessed by the clients/observers
 * to gather information about the keyboard state.
 *
 * @author gp
 */
public class KeyBoardUpdateInfo
{
    /**
     * KeyBoardEventType is an enum that says whether the event was caused by
     * the release or by pressing a key
     */
    public enum KeyBoardEventType
    {
        KEY_PRESSED,
        KEY_RELEASED
    }

    /**
     * keyBoardEventType is the KeyBoardEventType that describes the event
     */
    private final KeyBoardEventType keyBoardEventType;
    /**
     * keyCode is the KeyBoardControllerKeyCode of the key that triggered the
     * event
     */
    private final KeyBoardControllerKeyCode keyCode;
    /**
     * currentlyPressed is a reference to the set of currently pressed keys
     */
    private final ImmutableSet<KeyBoardControllerKeyCode> currentlyPressed;

    /**
     * KeyBoardUpdateInfo() is used to initialize a KeyBoardUpdateInfo from
     * the KeyBoardController
     *
     * @param keyBoardEventType the type of KeyBoardEvent
     * @param keyCode           the code of the key that triggered the event
     * @param pressedKeyCodes   the set of currently pressed codes
     */
    /*~*/
    public KeyBoardUpdateInfo(KeyBoardEventType keyBoardEventType,
                              KeyBoardControllerKeyCode keyCode,
                              ImmutableSet<KeyBoardControllerKeyCode> pressedKeyCodes)
    {
        this.keyBoardEventType = keyBoardEventType;
        this.keyCode = keyCode;
        currentlyPressed = pressedKeyCodes;
    }

    /**
     * getKeyBoardEventType() return the keyBoardEventType
     *
     * @return the keyBoardEventType
     */
    public KeyBoardEventType getKeyBoardEventType()
    {
        return keyBoardEventType;
    }

    /**
     * getKeyCode() returns the keyCode
     *
     * @return the keyCode
     */
    public KeyBoardControllerKeyCode getKeyCode()
    {
        return keyCode;
    }

    /**
     * getCurrentlyPressed() gets the currentlyPressed
     *
     * @return the currently pressed key codes
     */
    public ImmutableSet<KeyBoardControllerKeyCode> getCurrentlyPressed()
    {
        return currentlyPressed;
    }
}