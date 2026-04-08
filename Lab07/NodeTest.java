import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

/**
 * JUnit 4 unit tests for {@link Node} and {@link SortableNode}.
 * Tests are organized by method!! Each method section covers both
 * Integer and String as the generic type parameter E.
 *
 * @author Ali Kablanbek
 * @version 3/31/26
 */
public class NodeTest {

    // =========================================================
    // Helpers
    // =========================================================

    /** Builds an ArrayList of Integers from varargs for concise test setup. */
    private ArrayList<Integer> intList(Integer... vals) {
        ArrayList<Integer> list = new ArrayList<>();
        for (Integer v : vals) list.add(v);
        return list;
    }

    /** Builds an ArrayList of Strings from varargs for concise test setup. */
    private ArrayList<String> strList(String... vals) {
        ArrayList<String> list = new ArrayList<>();
        for (String v : vals) list.add(v);
        return list;
    }

    // =========================================================
    // toString
    // =========================================================

    /** Integer list of multiple elements is comma-and-space separated. */
    @Test
    public void testToStringInteger() {
        Node<Integer> n = new Node<>(intList(1, 2, 3, 4, 5));
        assertEquals("1, 2, 3, 4, 5", n.toString());
    }

    /** Single-element Integer list produces no comma. */
    @Test
    public void testToStringSingleInteger() {
        Node<Integer> n = new Node<>(intList(7));
        assertEquals("7", n.toString());
    }

    /** String list of multiple elements is comma-and-space separated. */
    @Test
    public void testToStringString() {
        Node<String> n = new Node<>(strList("a", "b", "c"));
        assertEquals("a, b, c", n.toString());
    }

    /** Single-element String list produces no comma. */
    @Test
    public void testToStringSingleString() {
        Node<String> n = new Node<>("hello");
        assertEquals("hello", n.toString());
    }

    // =========================================================
    // addToBack
    // =========================================================

    /** addToBack appends an Integer to the tail of a multi-node list. */
    @Test
    public void testAddToBackInteger() {
        Node<Integer> n = new Node<>(intList(1, 2));
        n.addToBack(3);
        assertEquals("1, 2, 3", n.toString());
    }

    /** addToBack on a single-Integer node creates a two-node list. */
    @Test
    public void testAddToBackIntegerSingleNode() {
        Node<Integer> n = new Node<>(10);
        n.addToBack(20);
        assertEquals("10, 20", n.toString());
    }

    /** addToBack appends a String to the tail of a multi-node list. */
    @Test
    public void testAddToBackString() {
        Node<String> n = new Node<>(strList("x", "y"));
        n.addToBack("z");
        assertEquals("x, y, z", n.toString());
    }

    /** addToBack on a single-String node creates a two-node list. */
    @Test
    public void testAddToBackStringSingleNode() {
        Node<String> n = new Node<>("a");
        n.addToBack("b");
        assertEquals("a, b", n.toString());
    }

    // =========================================================
    // addToFront
    // =========================================================

    /** addToFront prepends an Integer; the head reference remains the same object. */
    @Test
    public void testAddToFrontInteger() {
        Node<Integer> n = new Node<>(intList(2, 3));
        n.addToFront(1);
        assertEquals("1, 2, 3", n.toString());
    }

    /** Successive Integer addToFront calls produce the correct final order. */
    @Test
    public void testAddToFrontIntegerMultipleTimes() {
        Node<Integer> n = new Node<>(3);
        n.addToFront(2);
        n.addToFront(1);
        assertEquals("1, 2, 3", n.toString());
    }

    /** addToFront prepends a String; the head reference remains the same object. */
    @Test
    public void testAddToFrontString() {
        Node<String> n = new Node<>(strList("b", "c"));
        n.addToFront("a");
        assertEquals("a, b, c", n.toString());
    }

    /** Successive String addToFront calls produce the correct final order. */
    @Test
    public void testAddToFrontStringMultipleTimes() {
        Node<String> n = new Node<>("c");
        n.addToFront("b");
        n.addToFront("a");
        assertEquals("a, b, c", n.toString());
    }

    // =========================================================
    // rotate
    // =========================================================

    /** Integer rotate(2) matches the lab example: [1,2,3,4,5] -> [4,5,1,2,3]. */
    @Test
    public void testRotateIntegerLabExample() {
        Node<Integer> n = new Node<>(intList(1, 2, 3, 4, 5));
        n.rotate(2);
        assertEquals("4, 5, 1, 2, 3", n.toString());
    }

    /** rotate(0) leaves an Integer list unchanged. */
    @Test
    public void testRotateIntegerZero() {
        Node<Integer> n = new Node<>(intList(1, 2, 3));
        n.rotate(0);
        assertEquals("1, 2, 3", n.toString());
    }

    /** rotate(length) is a full cycle and leaves an Integer list unchanged. */
    @Test
    public void testRotateIntegerFullCycle() {
        Node<Integer> n = new Node<>(intList(1, 2, 3));
        n.rotate(3);
        assertEquals("1, 2, 3", n.toString());
    }

    /** rotate(1) moves only the last Integer element to the front. */
    @Test
    public void testRotateIntegerByOne() {
        Node<Integer> n = new Node<>(intList(1, 2, 3, 4));
        n.rotate(1);
        assertEquals("4, 1, 2, 3", n.toString());
    }

    /** Negative k throws IllegalArgumentException for Integer list. */
    @Test(expected = IllegalArgumentException.class)
    public void testRotateIntegerNegativeThrows() {
        Node<Integer> n = new Node<>(intList(1, 2, 3));
        n.rotate(-1);
    }

    /** The IllegalArgumentException carries the required message. */
    @Test
    public void testRotateNegativeMessage() {
        Node<Integer> n = new Node<>(intList(1, 2));
        try {
            n.rotate(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Rotation number must be non-negative.", e.getMessage());
        }
    }

    /** String rotate(1) moves the last String element to the front. */
    @Test
    public void testRotateString() {
        Node<String> n = new Node<>(strList("a", "b", "c", "d"));
        n.rotate(1);
        assertEquals("d, a, b, c", n.toString());
    }

    /** rotate(0) leaves a String list unchanged. */
    @Test
    public void testRotateStringZero() {
        Node<String> n = new Node<>(strList("x", "y", "z"));
        n.rotate(0);
        assertEquals("x, y, z", n.toString());
    }

    // =========================================================
    // sort  (SortableNode only)
    // =========================================================

    /** sort() arranges Integers in ascending order, keeping duplicates. */
    @Test
    public void testSortInteger() {
        SortableNode<Integer> n = new SortableNode<>(intList(3, 1, 4, 1, 5, 9, 2, 6));
        n.sort();
        assertEquals("1, 1, 2, 3, 4, 5, 6, 9", n.toString());
    }

    /** sort() on an already-sorted Integer list leaves it unchanged. */
    @Test
    public void testSortIntegerAlreadySorted() {
        SortableNode<Integer> n = new SortableNode<>(intList(1, 2, 3));
        n.sort();
        assertEquals("1, 2, 3", n.toString());
    }

    /** sort() on a single Integer node produces no change. */
    @Test
    public void testSortIntegerSingleElement() {
        SortableNode<Integer> n = new SortableNode<>(intList(42));
        n.sort();
        assertEquals("42", n.toString());
    }

    /** sort() arranges Strings in lexicographic ascending order. */
    @Test
    public void testSortString() {
        SortableNode<String> n = new SortableNode<>(strList("banana", "apple", "cherry"));
        n.sort();
        assertEquals("apple, banana, cherry", n.toString());
    }

    /** sort() on a single String node produces no change. */
    @Test
    public void testSortStringSingleElement() {
        SortableNode<String> n = new SortableNode<>(strList("only"));
        n.sort();
        assertEquals("only", n.toString());
    }

    // =========================================================
    // uniqueSort  (SortableNode only)
    // =========================================================

    /** uniqueSort() sorts Integers and removes all duplicate values. */
    @Test
    public void testUniqueSortInteger() {
        SortableNode<Integer> n = new SortableNode<>(intList(3, 1, 4, 1, 5));
        n.uniqueSort();
        assertEquals("1, 3, 4, 5", n.toString());
    }

    /** uniqueSort() when all Integer values are equal leaves exactly one node. */
    @Test
    public void testUniqueSortIntegerAllSame() {
        SortableNode<Integer> n = new SortableNode<>(intList(7, 7, 7));
        n.uniqueSort();
        assertEquals("7", n.toString());
    }

    /** uniqueSort() on an Integer list with no duplicates just sorts it. */
    @Test
    public void testUniqueSortIntegerNoDuplicates() {
        SortableNode<Integer> n = new SortableNode<>(intList(5, 3, 1));
        n.uniqueSort();
        assertEquals("1, 3, 5", n.toString());
    }

    /** uniqueSort() sorts Strings and removes all duplicate values. */
    @Test
    public void testUniqueSortString() {
        SortableNode<String> n = new SortableNode<>(
                strList("banana", "apple", "apple", "cherry", "banana"));
        n.uniqueSort();
        assertEquals("apple, banana, cherry", n.toString());
    }

    /** uniqueSort() on a String list with no duplicates just sorts it. */
    @Test
    public void testUniqueSortStringNoDuplicates() {
        SortableNode<String> n = new SortableNode<>(strList("dog", "cat", "bird"));
        n.uniqueSort();
        assertEquals("bird, cat, dog", n.toString());
    }
}