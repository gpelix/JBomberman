package app.jbomberman.view;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.AbstractObservable;
import app.jbomberman.util.Maybe;

import java.util.HashSet;
import java.util.Set;

/**
 * KeyBoardController is the class responsible for the handling the keyboard
 * input that comes from the user.
 * <p>
 * KeyBoardController communicates with other class the keystrokes using the
 * observer/Observable pattern.
 *
 * @author gp
 */
public abstract class KeyBoardController extends AbstractObservable
{

	/**
	 * Set of KeyCodes currently pressed
	 */
	private final Set<KeyBoardControllerKeyCode> pressedKeyCodes = new HashSet<>();

	/**
	 * updateInfo contains a reference to the most recent keyboard event
	 */
	private Maybe<KeyBoardUpdateInfo> updateInfo = Maybe.nothing();

	/**
	 * getUpdateInfo() must be called by the observer upon notification to get
	 * information about the event that triggered the notification
	 *
	 * @return the KeyBoardUpdateInfo represent the event
	 */
	public KeyBoardUpdateInfo getUpdateInfo()
	{
		return updateInfo.unwrapOrThrow(() -> new JBomberManException(
			"Called update but there are no update info"));
	}

	/**
	 * function to be called when a key pressed notification gets received
	 *
	 * @param keyCode the key code received
	 */
	protected void onPressed(KeyBoardControllerKeyCode keyCode)
	{
		pressedKeyCodes.add(keyCode);

		setUpdateInfo(new KeyBoardUpdateInfo(
			KeyBoardUpdateInfo.KeyBoardEventType.KEY_PRESSED,
			keyCode,
			pressedKeyCodes));

		updateAllObservers();
	}

	/**
	 * function to be called when a key released notification gets received
	 *
	 * @param keyCode the key code received
	 */
	protected void onReleased(KeyBoardControllerKeyCode keyCode)
	{
		pressedKeyCodes.remove(keyCode);

		setUpdateInfo(new KeyBoardUpdateInfo(
			KeyBoardUpdateInfo.KeyBoardEventType.KEY_RELEASED,
			keyCode,
			pressedKeyCodes));

		updateAllObservers();
	}

	/**
	 * setUpdateInfo sets the current update info.
	 * <p>
	 * It's meant to be called by subclasses to set the update infos
	 *
	 * @param info the info that must be set
	 */
	protected void setUpdateInfo(KeyBoardUpdateInfo info)
	{
		updateInfo = Maybe.just(info);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
