
package app.jbomberman.drawing;

import app.jbomberman.controller.KeyBoardController;
import app.jbomberman.util.Maybe;

import java.util.stream.Stream;

public class VerticalMenu extends Menu
{
	private Maybe<Integer> height = Maybe.nothing();


	public VerticalMenu(Stream<MenuEntry> stream)
	{
		super(stream);
		setArrangementPolicy(this::arrangeVertically);
	}

	public VerticalMenu(MenuEntry... menuEntries)
	{
		super(menuEntries);
		setArrangementPolicy(this::arrangeVertically);
	}

	@Override
	protected final KeyBoardController.KeyBoardControllerKeyCode getNextKey()
	{
		return KeyBoardController.KeyBoardControllerKeyCode.DOWN;
	}

	@Override
	protected final KeyBoardController.KeyBoardControllerKeyCode getPrevKey()
	{
		return KeyBoardController.KeyBoardControllerKeyCode.UP;
	}

	@Override
	protected final int getHeight()
	{
		return height.unwrap();
	}

	public void setHeight(int height)
	{
		this.height = Maybe.just(height);
	}
}
