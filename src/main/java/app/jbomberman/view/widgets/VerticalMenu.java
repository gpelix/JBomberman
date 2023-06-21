package app.jbomberman.view.widgets;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Maybe;
import app.jbomberman.view.KeyBoardControllerKeyCode;

import java.util.stream.Stream;

/**
 * VerticalMenu is a class that models a vertical menu.
 *
 * @author gp
 * @implNote All it does is setting the correct arrangement policy
 * (which is arrangeVertically) and overriding the (get/set)Width() methods
 */
public class VerticalMenu extends Menu
{
	/**
	 * height is the height of the VerticalMenu. It will be used to spread the
	 * entries to fill the whole height
	 */
	private Maybe<Integer> height = Maybe.nothing();

	/**
	 * Constructor from Stream of MenuEntry's
	 *
	 * @param stream the stream containing the MenuEntry's
	 */
	public VerticalMenu(Stream<MenuEntry> stream)
	{
		super(stream);
		setArrangementPolicy(this::arrangeVertically);
	}

	/**
	 * Constructor from varargs
	 *
	 * @param menuEntries the list of the MenuEntry's
	 */
	public VerticalMenu(MenuEntry... menuEntries)
	{
		super(menuEntries);
		setArrangementPolicy(this::arrangeVertically);
	}

	/**
	 * getNextKey() returns the key that must be pressed to select the next entry in
	 * the menu
	 *
	 * @return the key that must be pressed to select the next entry in the menu
	 */
	@Override
	protected final KeyBoardControllerKeyCode getNextKey()
	{
		return KeyBoardControllerKeyCode.DOWN;
	}

	/**
	 * getPrevKey() returns the key that must be pressed to select the previous entry in
	 * the menu
	 *
	 * @return the key that must be pressed to select the previous entry in the menu
	 */
	@Override
	protected final KeyBoardControllerKeyCode getPrevKey()
	{
		return KeyBoardControllerKeyCode.UP;
	}

	/**
	 * getHeight() returns the height of the vertical menu
	 *
	 * @return the height of the vertical menu
	 * @throws JBomberManException if the height is unset
	 */
	@Override
	protected final int getHeight()
	{
		return height.unwrapOrThrow(() -> new JBomberManException("heihgt is unset"));
	}

	/**
	 * setHeight() sets the hight of the vertical menu
	 *
	 * @param height the height of the menu
	 */
	public void setHeight(int height)
	{
		this.height = Maybe.just(height);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
