/**
 * Thrown when an expression string is not in valid infix or postfix format.
 *
 * @author Ali Kablanbek
 * @version 4/7/26
 */
public class ExpressionFormatException extends Exception {

    /**
     * Constructs an ExpressionFormatException with the given detail message.
     *
     * @param message description of the formatting problem
     */
    public ExpressionFormatException(String message) {
        super(message);
    }
}