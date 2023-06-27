package app.jbomberman.view.widgets;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Maybe;
import app.jbomberman.view.KeyBoardControllerKeyCode;

import java.util.stream.Stream;

/**
 * HorizontalMenu is a class that models a horizontal menu.
 * All it does is setting the correct arrangement policy
 * (which is arrangeHorizontally) and overriding the (get/set)Height() methods
 *
 * @author gp
 */
public class HorizontalMenu extends Menu
{
	/**
	 * width is the width of the horizontal menu
	 */
	private Maybe<Integer> width = Maybe.nothing();

	/**
	 * Constructor from Stream of MenuEntry's
	 *
	 * @param stream the stream containing the MenuEntry's
	 */
	public HorizontalMenu(Stream<MenuEntry> stream)
	{
		super(stream);
		setArrangementPolicy(this::arrangeHorizontally);
	}

	/**
	 * Constructor from varargs
	 *
	 * @param menuEntries the list of the MenuEntry's
	 */
	public HorizontalMenu(MenuEntry... menuEntries)
	{
		super(menuEntries);
		setArrangementPolicy(this::arrangeHorizontally);
	}

	/**
	 * getWidth() returns the width of the horizontal menu
	 *
	 * @return the width of the horizontal menu
	 * @throws JBomberManException if the width is unset
	 */
	@Override
	public final int getWidth()
	{
		return width.unwrapOrThrow(() -> new JBomberManException("Width is unset"));
	}

	/**
	 * getNextKey() returns the key that must be pressed to select the next entry in
	 * the menu
	 *
	 * @return the key that must be pressed to select the next entry in the menu
	 */
	@Override
	protected KeyBoardControllerKeyCode getNextKey()
	{
		return KeyBoardControllerKeyCode.RIGHT;
	}

	/**
	 * getPrevKey() returns the key that must be pressed to select the previous entry in
	 * the menu
	 *
	 * @return the key that must be pressed to select the previous entry in the menu
	 */
	@Override
	protected KeyBoardControllerKeyCode getPrevKey()
	{
		return KeyBoardControllerKeyCode.LEFT;
	}

	/**
	 * setWidth() sets the width of the horizonral menu
	 *
	 * @param width the width of the menu
	 */
	public void setWidth(int width)
	{
		this.width = Maybe.just(width);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
