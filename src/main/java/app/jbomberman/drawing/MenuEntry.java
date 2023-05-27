package app.jbomberman.drawing;

import app.jbomberman.util.Callback;

public class MenuEntry extends Text
{

    private final Callback callback;

    public MenuEntry(String str, boolean black, Callback callback)
    {
        super(str, black);

        this.callback = callback;
    }

    public MenuEntry(Text text, Callback callback)
    {
        this(text.getText(), text.getColor(), callback);
    }

    @Override
    public String getText()
    {
        return super.getText().substring(1); // WHAT THE HELL. Why substring?? TODO:
    }

    public void setSelected(boolean isSelected)
    {
        if (isSelected)
            setColor(false);
        else
            setColor(true);
    }

    public void callCallback()
    {
        callback.run();
    }
}
