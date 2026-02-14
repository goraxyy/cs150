import static org.junit.jupiter.api.Assertions.*;
// did not need aftereach and beforeeach
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class Part1Test {

    @Test
    @DisplayName("equals")
    void testEquals() {
        String a = "hello";
        String b = new String("hello");
        String c = "Hello";

        assertTrue(a.equals(b));          // same content
        assertFalse(a.equals(c));         // case-sensitive
        assertFalse(a.equals(null));      // equals(null) is false
    }

    @Test
    @DisplayName("concat")
    void testConcat() {
        assertEquals("ab", "a".concat("b"));
        assertEquals("a", "a".concat(""));              // empty append
        assertEquals("b", "".concat("b"));              // empty base
        assertThrows(NullPointerException.class, () -> "a".concat(null)); // invalid
    }

    @Test
    @DisplayName("compareTo")
    void testCompareTo() {
        assertEquals(0, "abc".compareTo("abc"));
        assertTrue("abc".compareTo("abd") < 0);
        assertTrue("abd".compareTo("abc") > 0);
        assertThrows(NullPointerException.class, () -> "a".compareTo(null)); // invalid
    }

    @Test
    @DisplayName("charAt")
    void testCharAt() {
        String s = "cat";

        assertEquals('c', s.charAt(0));
        assertEquals('a', s.charAt(1));
        assertEquals('t', s.charAt(2));
        assertThrows(StringIndexOutOfBoundsException.class, () -> s.charAt(3));  // invalid
        assertThrows(StringIndexOutOfBoundsException.class, () -> s.charAt(-1)); // invalid
    }

    @Test
    @DisplayName("indexOf")
    void testIndexOf() {
        String s = "banana";

        assertEquals(0, s.indexOf("ba"));     // at beginning
        assertEquals(1, s.indexOf("ana"));    // first occurrence
        assertEquals(-1, s.indexOf("xyz"));   // not found
        assertEquals(0, s.indexOf(""));       // empty string found at 0 by spec behavior
        assertThrows(NullPointerException.class, () -> s.indexOf((String) null)); // invalid
    }

    @Test
    @DisplayName("substring")
    void testSubstring() {
        String s = "abcdef";

        assertEquals("bcd", s.substring(1, 4)); // end exclusive
        assertEquals("def", s.substring(3));
        assertEquals("", s.substring(2, 2));    // empty ok
        assertThrows(StringIndexOutOfBoundsException.class, () -> s.substring(-1, 2));
        assertThrows(StringIndexOutOfBoundsException.class, () -> s.substring(4, 2));
        assertThrows(StringIndexOutOfBoundsException.class, () -> s.substring(2, 99));
    }

    @Test
    @DisplayName("split")
    void testSplit() {
        assertArrayEquals(new String[]{"a", "b", "c"}, "a,b,c".split(","));
        assertArrayEquals(new String[]{"a", "b", "c"}, "a.b.c".split("\\.")); // regex '.' must be escaped
        assertArrayEquals(new String[]{"a", "b"}, "a,b,".split(",")); // trailing empty dropped by default [web:9]
        assertArrayEquals(new String[]{"a", "b", ""}, "a,b,".split(",", -1)); // keep trailing empty with negative limit [web:6]
        assertThrows(NullPointerException.class, () -> "a,b".split(null)); // invalid
    }
}