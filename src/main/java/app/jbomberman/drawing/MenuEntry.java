package app.jbomberman.drawing;

import app.jbomberman.util.Callback;

/**
 * MenuEntry represents the entry for a menu. It's just text but adds also a
 * callback that must be called when the menu entry is chosen
 *
 * @author gp
 * @see Menu
 */
public class MenuEntry extends Text
{
	/**
	 * the callback that must be called when the menu entry is chosen
	 */
	private final Callback callback;

	/**
	 * MenuEntry is a public constructor that takes the text, color, and the
	 * callback to be called when chosen
	 *
	 * @param str	  the text to be shown on the entry
	 * @param color	the color of the entry
	 * @param callback the callback to be called when this entry is chosen
	 */
	public MenuEntry(String str, TextColor color, Callback callback)
	{
		super(str, color);

		this.callback = callback;
	}

	/**
	 * Constructor that takes the text to set and a callback. The color will be the
	 * one of the text
	 *
	 * @param text	 the Text from which the text and the color must be taken
	 * @param callback the callback to be called when this entry is chosen
	 */
	public MenuEntry(Text text, Callback callback)
	{
		this(text.getText(), text.getColor(), callback);
	}

	/**
	 * getText() gets the text. We substring by one because in the future we'll
	 * have to add one empty character at the beginning
	 *
	 * @return the text without the first fake character
	 */
	// TODO: ADD BETTER MENUS
	@Override
	public String getText()
	{
		return super.getText().substring(1); // WHAT THE HELL. Why substring?? TODO:
	}

	/**
	 * setSelected() selects/deselects the entry
	 *
	 * @param isSelected isSelected specifies whether this must entry must be selected
	 */
	public void setSelected(boolean isSelected)
	{
		if (isSelected)
			setColor(TextColor.Gray);
		else
			setColor(TextColor.Black);
	}

	/**
	 * callCallback() calls the callback
	 */
	public void callCallback()
	{
		callback.run();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
