package app.jbomberman.model.game;

import app.jbomberman.JBomberManException;

/**
 * GamePlayException is the exception to be raised if something does really
 * wrong during the execution of the game
 *
 * @author gp
 */
public class GamePlayException extends JBomberManException
{
	/**
	 * public constructor that receives a String and will be used to initialize
	 * the superclass
	 *
	 * @param str the message to be displayed on error
	 */
	public GamePlayException(String str)
	{
		super(str);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
