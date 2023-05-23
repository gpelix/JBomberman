
package app.jbomberman;

/**
 * JBomberManException is a generic unchecked exception that must be raised
 * by the program
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
