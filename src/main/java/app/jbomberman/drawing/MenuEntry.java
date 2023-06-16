package app.jbomberman.drawing;

import app.jbomberman.util.Callback;

public class MenuEntry extends Text
{

    private final Callback callback;

    public MenuEntry(String str, TextColor color, Callback callback)
    {
        super(str, color);

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
            setColor(TextColor.Gray);
        else
            setColor(TextColor.Black);
    }

    public void callCallback()
    {
        callback.run();
    }
}
