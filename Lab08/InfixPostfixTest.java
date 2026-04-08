import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for InfixPostfix.
 * Tests are grouped by method.
 *
 * @author Ali Kablanbek
 * @version 4/7/26
 */
public class InfixPostfixTest {

    // -----------------------------------------------------------------------
    // evaluatePostfix - valid expressions
    // -----------------------------------------------------------------------

    @Test
    public void testPostfixSingleNumber() throws ExpressionFormatException {
        assertEquals(42, InfixPostfix.evaluatePostfix("42"));
    }

    @Test
    public void testPostfixAddition() throws ExpressionFormatException {
        assertEquals(7, InfixPostfix.evaluatePostfix("3 4 +"));
    }

    @Test
    public void testPostfixSubtraction() throws ExpressionFormatException {
        assertEquals(7, InfixPostfix.evaluatePostfix("10 3 -"));
    }

    @Test
    public void testPostfixMultiplication() throws ExpressionFormatException {
        assertEquals(42, InfixPostfix.evaluatePostfix("6 7 *"));
    }

    @Test
    public void testPostfixDivision() throws ExpressionFormatException {
        assertEquals(5, InfixPostfix.evaluatePostfix("20 4 /"));
    }

    @Test
    public void testPostfixExponentiation() throws ExpressionFormatException {
        assertEquals(1024, InfixPostfix.evaluatePostfix("2 10 ^"));
    }

    // 5 + (1 + 2) * 4 - 3 = 14
    @Test
    public void testPostfixMultiOperator() throws ExpressionFormatException {
        assertEquals(14, InfixPostfix.evaluatePostfix("5 1 2 + 4 * + 3 -"));
    }

    @Test
    public void testPostfixNegativeOperand() throws ExpressionFormatException {
        assertEquals(2, InfixPostfix.evaluatePostfix("-3 5 +"));
    }

    // -----------------------------------------------------------------------
    // evaluatePostfix - exceptions
    // -----------------------------------------------------------------------

    @Test(expected = ExpressionFormatException.class)
    public void testPostfixDivisionByZero() throws ExpressionFormatException {
        InfixPostfix.evaluatePostfix("5 0 /");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testPostfixInvalidToken() throws ExpressionFormatException {
        InfixPostfix.evaluatePostfix("3 abc +");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testPostfixTooManyOperands() throws ExpressionFormatException {
        InfixPostfix.evaluatePostfix("3 4 5");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testPostfixOperatorWithNoOperands() throws ExpressionFormatException {
        InfixPostfix.evaluatePostfix("+");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testPostfixOperatorWithOneOperand() throws ExpressionFormatException {
        InfixPostfix.evaluatePostfix("5 +");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testPostfixEmpty() throws ExpressionFormatException {
        InfixPostfix.evaluatePostfix("   ");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testPostfixNull() throws ExpressionFormatException {
        InfixPostfix.evaluatePostfix(null);
    }

    // -----------------------------------------------------------------------
    // simpleInfixToPostfix - valid conversions
    // -----------------------------------------------------------------------

    @Test
    public void testSimpleInfixSingleOperand() throws ExpressionFormatException {
        assertEquals("7", InfixPostfix.simpleInfixToPostfix("7"));
    }

    @Test
    public void testSimpleInfixAddition() throws ExpressionFormatException {
        assertEquals("3 4 +", InfixPostfix.simpleInfixToPostfix("3 + 4"));
    }

    @Test
    public void testSimpleInfixSubtraction() throws ExpressionFormatException {
        assertEquals("10 3 -", InfixPostfix.simpleInfixToPostfix("10 - 3"));
    }

    // 3 + 4 * 5 -> higher precedence of * handled correctly
    @Test
    public void testSimpleInfixPrecedence() throws ExpressionFormatException {
        assertEquals("3 4 5 * +", InfixPostfix.simpleInfixToPostfix("3 + 4 * 5"));
    }

    // 6 - 3 - 1 is left-associative so it groups as (6-3)-1
    @Test
    public void testSimpleInfixLeftAssociativity() throws ExpressionFormatException {
        assertEquals("6 3 - 1 -", InfixPostfix.simpleInfixToPostfix("6 - 3 - 1"));
    }

    @Test
    public void testSimpleInfixLongerChain() throws ExpressionFormatException {
        assertEquals("1 2 3 * + 4 2 / -",
            InfixPostfix.simpleInfixToPostfix("1 + 2 * 3 - 4 / 2"));
    }

    // -----------------------------------------------------------------------
    // simpleInfixToPostfix - exceptions
    // -----------------------------------------------------------------------

    @Test(expected = ExpressionFormatException.class)
    public void testSimpleInfixRejectsLeftParen() throws ExpressionFormatException {
        InfixPostfix.simpleInfixToPostfix("( 3 + 4 )");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testSimpleInfixRejectsRightParen() throws ExpressionFormatException {
        InfixPostfix.simpleInfixToPostfix("3 + 4 )");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testSimpleInfixRejectsExponent() throws ExpressionFormatException {
        InfixPostfix.simpleInfixToPostfix("2 ^ 3");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testSimpleInfixInvalidToken() throws ExpressionFormatException {
        InfixPostfix.simpleInfixToPostfix("3 + x");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testSimpleInfixConsecutiveOperators() throws ExpressionFormatException {
        InfixPostfix.simpleInfixToPostfix("3 + + 4");
    }

    // -----------------------------------------------------------------------
    // infixToPostfix - valid conversions
    // -----------------------------------------------------------------------

    // Parentheses override precedence: (3 + 4) * 5
    @Test
    public void testFullInfixParentheses() throws ExpressionFormatException {
        assertEquals("3 4 + 5 *",
            InfixPostfix.infixToPostfix("( 3 + 4 ) * 5"));
    }

    @Test
    public void testFullInfixNestedParens() throws ExpressionFormatException {
        assertEquals("2 3 + 4 1 - *",
            InfixPostfix.infixToPostfix("( ( 2 + 3 ) * ( 4 - 1 ) )"));
    }

    // 2 ^ 3 ^ 2 is right-associative so it means 2^(3^2) = 2^9 = 512
    @Test
    public void testFullInfixExponentRightAssoc() throws ExpressionFormatException {
        assertEquals("2 3 2 ^ ^",
            InfixPostfix.infixToPostfix("2 ^ 3 ^ 2"));
    }

    @Test
    public void testFullInfixMixedPrecAndParens() throws ExpressionFormatException {
        assertEquals("3 4 2 1 - * +",
            InfixPostfix.infixToPostfix("3 + 4 * ( 2 - 1 )"));
    }

    // -----------------------------------------------------------------------
    // infixToPostfix - exceptions
    // -----------------------------------------------------------------------

    @Test(expected = ExpressionFormatException.class)
    public void testFullInfixUnmatchedLeft() throws ExpressionFormatException {
        InfixPostfix.infixToPostfix("( 3 + 4");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testFullInfixUnmatchedRight() throws ExpressionFormatException {
        InfixPostfix.infixToPostfix("3 + 4 )");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testFullInfixTrailingOperator() throws ExpressionFormatException {
        InfixPostfix.infixToPostfix("3 + 4 +");
    }

    // -----------------------------------------------------------------------
    // evaluateInfix - end-to-end pipeline
    // -----------------------------------------------------------------------

    @Test
    public void testEvalInfixSimpleAdd() throws ExpressionFormatException {
        assertEquals(7, InfixPostfix.evaluateInfix("3 + 4"));
    }

    @Test
    public void testEvalInfixWithParens() throws ExpressionFormatException {
        assertEquals(14, InfixPostfix.evaluateInfix("( 3 + 4 ) * 2"));
    }

    // 2^(3^2) = 2^9 = 512
    @Test
    public void testEvalInfixExponentRightAssoc() throws ExpressionFormatException {
        assertEquals(512, InfixPostfix.evaluateInfix("2 ^ 3 ^ 2"));
    }

    // Division before subtraction: 10 - 4/2 = 8
    @Test
    public void testEvalInfixPrecedence() throws ExpressionFormatException {
        assertEquals(8, InfixPostfix.evaluateInfix("10 - 4 / 2"));
    }

    @Test(expected = ExpressionFormatException.class)
    public void testEvalInfixDivisionByZero() throws ExpressionFormatException {
        InfixPostfix.evaluateInfix("5 / ( 3 - 3 )");
    }

    @Test(expected = ExpressionFormatException.class)
    public void testEvalInfixBadToken() throws ExpressionFormatException {
        InfixPostfix.evaluateInfix("3 + abc");
    }
}