package app.jbomberman.view;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.AbstractObservable;
import app.jbomberman.util.Maybe;
import com.google.common.collect.ImmutableSet;

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

	protected void onPressed(KeyBoardControllerKeyCode keyCode)
	{
		pressedKeyCodes.add(keyCode);

		setUpdateInfo(new KeyBoardUpdateInfo(
			KeyBoardUpdateInfo.KeyBoardEventType.KEY_PRESSED,
			keyCode,
			ImmutableSet.copyOf(pressedKeyCodes.iterator())));

		updateAllObservers();
	}

	protected void onReleased(KeyBoardControllerKeyCode keyCode)
	{
		pressedKeyCodes.remove(keyCode);

		setUpdateInfo(new KeyBoardUpdateInfo(
			KeyBoardUpdateInfo.KeyBoardEventType.KEY_RELEASED,
			keyCode,
			ImmutableSet.copyOf(pressedKeyCodes.iterator())));

		updateAllObservers();
	}

	protected void setUpdateInfo(KeyBoardUpdateInfo info)
	{
		updateInfo = Maybe.just(info);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
