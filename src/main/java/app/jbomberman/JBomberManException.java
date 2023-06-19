package app.jbomberman;

/**
 * JBomberManException is a generic unchecked exception that must be raised
 * by the program if something weird has occurred
 *
 * @author gp
 */
public class JBomberManException extends RuntimeException
{
	/**
	 * Construct a JBomberManException with a specific message
	 *
	 * @param str the message for this Exception
	 */
	public JBomberManException(String str)
	{
		super(str);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
