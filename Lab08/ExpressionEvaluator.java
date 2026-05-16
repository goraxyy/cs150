/**
 * Defines the contract for parsing and evaluating arithmetic expressions
 * in both postfix and infix notation.
 *
 * @author Ali Kablanbek
 * @version 4/7/26
 */
public interface ExpressionEvaluator {

    /**
     * Evaluates a space-separated postfix expression and returns the result.
     *
     * @param  expression  the postfix expression to evaluate
     * @return             the integer result
     * @throws ExpressionFormatException  if the expression is invalid or causes division by zero
     */
    int evaluatePostfix(String expression) throws ExpressionFormatException;

    /**
     * Converts a simple infix expression (no parentheses or exponentiation) to postfix.
     *
     * @param  expression  the simple infix expression to convert
     * @return             the equivalent postfix expression
     * @throws ExpressionFormatException  if the expression is invalid or contains disallowed tokens
     */
    String simpleInfixToPostfix(String expression) throws ExpressionFormatException;

    /**
     * Converts a full infix expression (with parentheses and exponentiation) to postfix.
     *
     * @param  expression  the infix expression to convert
     * @return             the equivalent postfix expression
     * @throws ExpressionFormatException  if the expression is invalid
     */
    String infixToPostfix(String expression) throws ExpressionFormatException;

    /**
     * Evaluates a full infix expression and returns the integer result.
     *
     * @param  expression  the infix expression to evaluate
     * @return             the integer result
     * @throws ExpressionFormatException  if the expression is invalid or causes division by zero
     */
    int evaluateInfix(String expression) throws ExpressionFormatException;
}