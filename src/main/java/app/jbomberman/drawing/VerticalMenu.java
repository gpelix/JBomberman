package app.jbomberman.drawing;

import app.jbomberman.util.Maybe;
import app.jbomberman.view.KeyBoardControllerKeyCode;

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
    protected final KeyBoardControllerKeyCode getNextKey()
    {
        return KeyBoardControllerKeyCode.DOWN;
    }

    @Override
    protected final KeyBoardControllerKeyCode getPrevKey()
    {
        return KeyBoardControllerKeyCode.UP;
    }

    @Override
    protected final int getHeight()
    {
        return height.fromJust();
    }

    public void setHeight(int height)
    {
        this.height = Maybe.just(height);
    }
}
