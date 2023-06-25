package app.jbomberman.model;

/**
 * PlayerBuilderException is the exception that we throw when something related
 * to the creation of an account fails
 *
 * @author gp
 */
public class PlayerBuilderException extends IllegalArgumentException
{
	/**
	 * public constructor that receives a String and will be used to initialize
	 * the superclass
	 *
	 * @param str the message to be displayed on error
	 */
	public PlayerBuilderException(String str)
	{
		super(str);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
