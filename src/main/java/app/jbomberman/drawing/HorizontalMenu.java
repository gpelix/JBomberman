package app.jbomberman.drawing;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Maybe;
import app.jbomberman.view.KeyBoardControllerKeyCode;

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
    protected KeyBoardControllerKeyCode getNextKey()
    {
        return KeyBoardControllerKeyCode.RIGHT;
    }

    @Override
    protected KeyBoardControllerKeyCode getPrevKey()
    {
        return KeyBoardControllerKeyCode.LEFT;
    }

    public void setWidth(int width)
    {
        this.width = Maybe.just(width);
    }
}
