
package app.jbomberman.drawing;

import app.jbomberman.JBomberManException;
import app.jbomberman.controller.KeyBoardController;
import app.jbomberman.util.Maybe;

import java.util.stream.Stream;

public class HorizontalMenu extends Menu
{

	private Maybe<Integer> width = Maybe.nothing();

	public HorizontalMenu(Stream<MenuEntry> stream)
	{
		super(stream);
		setArrangementPolicy(this::arrangeHorizontally);
	}

	public HorizontalMenu(MenuEntry... menuEntries)
	{
		super(menuEntries);
		setArrangementPolicy(this::arrangeHorizontally);
	}

	@Override
	public int getWidth()
	{
		return width.unwrapOrThrow(() -> new JBomberManException("Width is unset"));
	}

	@Override
	protected KeyBoardController.KeyBoardControllerKeyCode getNextKey()
	{
		return KeyBoardController.KeyBoardControllerKeyCode.RIGHT;
	}

	@Override
	protected KeyBoardController.KeyBoardControllerKeyCode getPrevKey()
	{
		return KeyBoardController.KeyBoardControllerKeyCode.LEFT;
	}

	public void setWidth(int width)
	{
		this.width = Maybe.just(width);
	}
}
