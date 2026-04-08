import java.util.Stack;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Provides static methods for evaluating and converting arithmetic expressions
 * in postfix (RPN) and infix notation.
 * All tokens in an expression must be separated by single spaces.
 * Supported operators are: + - * / ^ ( )
 * Operands must be integers. The ^ operator is right-associative.
 *
 * @author Ali Kablanbek
 * @version 4/7/26
 */
public class InfixPostfix {

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Returns the precedence level of an operator.
     * Returns -1 if the token is not a recognised operator.
     *
     * @param op the operator token
     * @return precedence level (1, 2 or 3) or -1 if not an operator
     */
    private static int precedence(String op) {
        switch (op) {
            case "+": case "-": return 1;
            case "*": case "/": return 2;
            case "^":           return 3;
            default:            return -1;
        }
    }

    /**
     * Returns true if the token is one of the five supported operators.
     *
     * @param token the token to test
     * @return true if the token is + - * / or ^
     */
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
               token.equals("*") || token.equals("/") ||
               token.equals("^");
    }

    /**
     * Returns true if the token can be parsed as an integer.
     *
     * @param token the token to test
     * @return true if the token is a valid integer literal
     */
    private static boolean isInteger(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Appends a token to the output buffer, inserting a leading space when needed.
     *
     * @param sb    the output buffer
     * @param token the token to append
     */
    private static void appendToken(StringBuilder sb, String token) {
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(token);
    }

    // -----------------------------------------------------------------------
    // Part 1 - evaluatePostfix
    // -----------------------------------------------------------------------

    /**
     * Evaluates a postfix expression and returns the integer result.
     * Tokens must be separated by spaces.
     * Example: "5 1 2 + 4 * + 3 -" evaluates to 14.
     *
     * @param expression space-separated postfix expression
     * @return integer result of the expression
     * @throws ExpressionFormatException if the expression contains invalid tokens,
     *         has the wrong number of operands or causes division by zero
     */
    public static int evaluatePostfix(String expression) throws ExpressionFormatException {

        if (expression == null || expression.trim().isEmpty()) {
            throw new ExpressionFormatException("Expression must not be empty.");
        }

        Stack<Integer> stack = new Stack<>();
        String[] tokens = expression.trim().split("\\s+");

        for (String token : tokens) {
            if (isInteger(token)) {
                stack.push(Integer.parseInt(token));

            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new ExpressionFormatException(
                        "Not enough operands for operator '" + token + "'.");
                }

                int b = stack.pop(); // right operand
                int a = stack.pop(); // left operand

                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/":
                        try {
                            stack.push(a / b);
                        } catch (ArithmeticException e) {
                            throw new ExpressionFormatException(
                                "Division by zero: " + a + " / " + b);
                        }
                        break;
                    case "^":
                        stack.push((int) Math.pow(a, b));
                        break;
                }

            } else {
                throw new ExpressionFormatException(
                    "Invalid token '" + token + "': expected an integer or operator.");
            }
        }

        if (stack.isEmpty()) {
            throw new ExpressionFormatException("Expression produced no result.");
        }

        if (stack.size() > 1) {
            throw new ExpressionFormatException(
                "Too many operands: " + stack.size() + " values remain on the stack.");
        }

        return stack.pop();
    }

    // -----------------------------------------------------------------------
    // Part 2 - simpleInfixToPostfix
    // -----------------------------------------------------------------------

    /**
     * Converts a simple infix expression to postfix notation.
     * Parentheses and the ^ operator are not allowed.
     * Tokens must be separated by spaces.
     * Example: "3 + 4 * 5" converts to "3 4 5 * +".
     *
     * @param expression space-separated simple infix expression
     * @return equivalent postfix expression
     * @throws ExpressionFormatException if the expression is invalid or contains
     *         parentheses or exponentiation
     */
    public static String simpleInfixToPostfix(String expression) throws ExpressionFormatException {

        if (expression == null || expression.trim().isEmpty()) {
            throw new ExpressionFormatException("Expression must not be empty.");
        }

        // Reject disallowed tokens before doing any work
        for (String token : expression.trim().split("\\s+")) {
            if (token.equals("(") || token.equals(")")) {
                throw new ExpressionFormatException(
                    "Parentheses are not allowed in a simple infix expression.");
            }
            if (token.equals("^")) {
                throw new ExpressionFormatException(
                    "Exponentiation is not allowed in a simple infix expression.");
            }
        }

        return infixToPostfix(expression);
    }

    // -----------------------------------------------------------------------
    // Part 3 - infixToPostfix
    // -----------------------------------------------------------------------

    /**
     * Converts a full infix expression to postfix notation using the Shunting-Yard
     * algorithm. Handles + - * / ^ and parentheses.
     * The ^ operator is right-associative.
     * Tokens must be separated by spaces.
     * Example: "( 3 + 4 ) * 5" converts to "3 4 + 5 *".
     *
     * @param expression space-separated infix expression
     * @return equivalent postfix expression
     * @throws ExpressionFormatException if the expression has mismatched parentheses,
     *         unknown tokens or incorrect operand placement
     */
    public static String infixToPostfix(String expression) throws ExpressionFormatException {

        if (expression == null || expression.trim().isEmpty()) {
            throw new ExpressionFormatException("Expression must not be empty.");
        }

        Stack<String> opStack = new Stack<>();
        StringBuilder output = new StringBuilder();
        String[] tokens = expression.trim().split("\\s+");

        // Track whether we expect an operand or operator next
        boolean expectOperand = true;

        for (String token : tokens) {

            if (isInteger(token)) {
                if (!expectOperand) {
                    throw new ExpressionFormatException(
                        "Unexpected operand '" + token + "': an operator was expected.");
                }
                appendToken(output, token);
                expectOperand = false;

            } else if (isOperator(token)) {
                if (expectOperand) {
                    throw new ExpressionFormatException(
                        "Unexpected operator '" + token + "': an operand was expected.");
                }

                // Pop operators of greater (or equal for left-assoc) precedence
                while (!opStack.isEmpty() && isOperator(opStack.peek())) {
                    int topPrec  = precedence(opStack.peek());
                    int thisPrec = precedence(token);
                    // ^ is right-associative: only pop if strictly greater
                    boolean shouldPop = token.equals("^")
                        ? topPrec > thisPrec
                        : topPrec >= thisPrec;
                    if (!shouldPop) break;
                    appendToken(output, opStack.pop());
                }

                opStack.push(token);
                expectOperand = true;

            } else if (token.equals("(")) {
                if (!expectOperand) {
                    throw new ExpressionFormatException(
                        "Unexpected '(': an operator was expected.");
                }
                opStack.push(token);

            } else if (token.equals(")")) {
                if (expectOperand) {
                    throw new ExpressionFormatException(
                        "Unexpected ')': an operand was expected.");
                }

                boolean foundLeft = false;
                while (!opStack.isEmpty()) {
                    String top = opStack.pop();
                    if (top.equals("(")) {
                        foundLeft = true;
                        break;
                    }
                    appendToken(output, top);
                }

                if (!foundLeft) {
                    throw new ExpressionFormatException(
                        "Mismatched parentheses: ')' has no matching '('.");
                }
                expectOperand = false;

            } else {
                throw new ExpressionFormatException(
                    "Invalid token '" + token + "': expected an integer, operator or parenthesis.");
            }
        }

        if (expectOperand) {
            throw new ExpressionFormatException(
                "Expression ends unexpectedly: a final operand was expected.");
        }

        // Drain remaining operators
        while (!opStack.isEmpty()) {
            String op = opStack.pop();
            if (op.equals("(")) {
                throw new ExpressionFormatException(
                    "Mismatched parentheses: '(' was never closed.");
            }
            appendToken(output, op);
        }

        return output.toString();
    }

    // -----------------------------------------------------------------------
    // Part 4 - evaluateInfix and main
    // -----------------------------------------------------------------------

    /**
     * Evaluates a full infix expression by converting it to postfix first
     * then evaluating the result.
     * Tokens must be separated by spaces.
     *
     * @param expression space-separated infix expression
     * @return integer result of the expression
     * @throws ExpressionFormatException if conversion or evaluation fails
     */
    public static int evaluateInfix(String expression) throws ExpressionFormatException {
        String postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    /**
     * Reads infix expressions line by line from an input file, evaluates each one
     * with evaluateInfix() and writes the results to an output file.
     * Each successful output line takes the form: {@code expression = result}
     * If a line causes an ExpressionFormatException the output line is:
     * {@code expression -- ERROR: message}
     * Usage: java InfixPostfix inputFile outputFile
     *
     * @param args args[0] is the input file path and args[1] is the output file path
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java InfixPostfix <inputFile> <outputFile>");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
             BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    writer.write(line);
                } else {
                    try {
                        int result = evaluateInfix(line.trim());
                        writer.write(line + " = " + result);
                    } catch (ExpressionFormatException e) {
                        writer.write(line + " -- ERROR: " + e.getMessage());
                    }
                }
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}