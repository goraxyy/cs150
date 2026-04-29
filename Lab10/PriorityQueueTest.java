import org.junit.Test;
import static org.junit.Assert.*;
import java.util.PriorityQueue;

/**
 * Unit tests for WeightedElement, MyPriorityQueue and MyClassicPriorityQueue.
 *
 * Each MyPriorityQueue test uses a java.util.PriorityQueue as an oracle to
 * verify that poll order and peek behavior match the standard library.
 * Tests are grouped by class and cover edge cases as well as general cases
 * using WeightedElement<Integer,Integer> and WeightedElement<String,String>.
 * 
 * @author Ali Kablanbek
 * @version 4/28
 */
public class PriorityQueueTest {

    // -----------------------------------------------------------------------
    // WeightedElement tests
    // -----------------------------------------------------------------------

    /** Getters should return the exact values passed to the constructor. */
    @Test
    public void testWeightedElementGetters() {
        WeightedElement<String, Integer> we = new WeightedElement<>("hello", 42);
        assertEquals("hello", we.getElement());
        assertEquals(Integer.valueOf(42), we.getWeight());
    }

    /** compareTo for Integer weight: ordering should match Integer.compareTo. */
    @Test
    public void testWeightedElementCompareToIntInt() {
        WeightedElement<Integer, Integer> a = new WeightedElement<>(1, 5);
        WeightedElement<Integer, Integer> b = new WeightedElement<>(2, 3);
        WeightedElement<Integer, Integer> c = new WeightedElement<>(3, 5);
        assertTrue("5 > 3 so a should be greater than b", a.compareTo(b) > 0);
        assertTrue("3 < 5 so b should be less than a", b.compareTo(a) < 0);
        assertEquals("equal weights should yield 0", 0, a.compareTo(c));
    }

    /** compareTo for String weight: ordering should match String.compareTo. */
    @Test
    public void testWeightedElementCompareToStringString() {
        WeightedElement<String, String> a = new WeightedElement<>("dog", "banana");
        WeightedElement<String, String> b = new WeightedElement<>("cat", "apple");
        // "banana" > "apple" lexicographically
        assertTrue(a.compareTo(b) > 0);
        assertTrue(b.compareTo(a) < 0);
    }

    /** equals should be based on both element and weight fields. */
    @Test
    public void testWeightedElementEquals() {
        WeightedElement<Integer, Integer> a = new WeightedElement<>(1, 10);
        WeightedElement<Integer, Integer> b = new WeightedElement<>(1, 10);
        WeightedElement<Integer, Integer> c = new WeightedElement<>(2, 10);
        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    /** hashCode must be consistent with equals for HashMap usage. */
    @Test
    public void testWeightedElementHashCode() {
        WeightedElement<Integer, Integer> a = new WeightedElement<>(1, 10);
        WeightedElement<Integer, Integer> b = new WeightedElement<>(1, 10);
        assertEquals(a.hashCode(), b.hashCode());
    }

    // -----------------------------------------------------------------------
    // MyPriorityQueue edge case tests
    // -----------------------------------------------------------------------

    /** An empty queue should return null for peek and poll and report isHeap true. */
    @Test
    public void testEmptyQueueBehavior() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        assertNull(pq.peek());
        assertNull(pq.poll());
        assertTrue(pq.isHeap());
        assertTrue(pq.isEmpty());
        assertEquals(0, pq.size());
    }

    /** A queue with one element should behave correctly for all operations. */
    @Test
    public void testSingleElement() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(42);
        assertEquals(Integer.valueOf(42), pq.peek());
        assertTrue(pq.isHeap());
        assertEquals(1, pq.size());
        assertEquals(Integer.valueOf(42), pq.poll());
        assertTrue(pq.isEmpty());
        assertNull(pq.poll());
    }

    /** peek should not change the size or the next poll result. */
    @Test
    public void testPeekDoesNotRemove() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        pq.add(5);
        pq.add(3);
        pq.add(7);
        Integer first = pq.peek();
        assertEquals(first, pq.peek());
        assertEquals(3, pq.size());
    }

    /** add must return true and the heap property must hold after every insertion. */
    @Test
    public void testAddReturnValueAndIsHeap() {
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();
        int[] values = {9, 4, 7, 1, 8, 3, 6, 2, 5};
        for (int v : values) {
            assertTrue(pq.add(v));
            assertTrue(pq.isHeap());
        }
    }

    // -----------------------------------------------------------------------
    // MyPriorityQueue vs java.util.PriorityQueue: Integer oracle tests
    // -----------------------------------------------------------------------

    /** poll order and peek must match java.util.PriorityQueue for a general Integer sequence. */
    @Test
    public void testMatchesJavaPQIntegers() {
        MyPriorityQueue<Integer> myPQ = new MyPriorityQueue<>();
        PriorityQueue<Integer> javaPQ = new PriorityQueue<>();
        int[] values = {5, 3, 8, 1, 9, 2, 7, 4, 6};
        for (int v : values) {
            myPQ.add(v);
            javaPQ.add(v);
        }
        assertEquals(javaPQ.peek(), myPQ.peek());
        while (!javaPQ.isEmpty()) {
            assertEquals(javaPQ.poll(), myPQ.poll());
            assertTrue(myPQ.isHeap());
        }
        assertNull(myPQ.poll());
    }

    /** Duplicate values must be handled correctly and match the oracle. */
    @Test
    public void testDuplicateValues() {
        MyPriorityQueue<Integer> myPQ = new MyPriorityQueue<>();
        PriorityQueue<Integer> javaPQ = new PriorityQueue<>();
        int[] values = {3, 3, 1, 1, 2, 2};
        for (int v : values) {
            myPQ.add(v);
            javaPQ.add(v);
        }
        while (!javaPQ.isEmpty()) {
            assertEquals(javaPQ.poll(), myPQ.poll());
            assertTrue(myPQ.isHeap());
        }
    }

    // -----------------------------------------------------------------------
    // MyPriorityQueue vs java.util.PriorityQueue: WeightedElement<Integer,Integer>
    // -----------------------------------------------------------------------

    /** Poll order by weight must match the oracle for WeightedElement<Integer,Integer>. */
    @Test
    public void testWeightedIntIntMatchesJavaPQ() {
        MyPriorityQueue<WeightedElement<Integer, Integer>> myPQ = new MyPriorityQueue<>();
        PriorityQueue<WeightedElement<Integer, Integer>> javaPQ = new PriorityQueue<>();
        int[][] pairs = {{1, 5}, {2, 3}, {3, 8}, {4, 1}, {5, 9}, {6, 2}};
        for (int[] pair : pairs) {
            myPQ.add(new WeightedElement<>(pair[0], pair[1]));
            javaPQ.add(new WeightedElement<>(pair[0], pair[1]));
            assertTrue(myPQ.isHeap());
        }
        while (!javaPQ.isEmpty()) {
            assertEquals(javaPQ.poll().getWeight(), myPQ.poll().getWeight());
        }
    }

    /** Equal weights with different elements: all elements should still drain correctly. */
    @Test
    public void testWeightedIntIntEqualWeights() {
        MyPriorityQueue<WeightedElement<Integer, Integer>> myPQ = new MyPriorityQueue<>();
        PriorityQueue<WeightedElement<Integer, Integer>> javaPQ = new PriorityQueue<>();
        int[][] pairs = {{1, 5}, {2, 5}, {3, 5}};
        for (int[] pair : pairs) {
            myPQ.add(new WeightedElement<>(pair[0], pair[1]));
            javaPQ.add(new WeightedElement<>(pair[0], pair[1]));
        }
        while (!javaPQ.isEmpty()) {
            assertEquals(javaPQ.poll().getWeight(), myPQ.poll().getWeight());
            assertTrue(myPQ.isHeap());
        }
    }

    // -----------------------------------------------------------------------
    // MyPriorityQueue vs java.util.PriorityQueue: WeightedElement<String,String>
    // -----------------------------------------------------------------------

    /** Poll order by String weight must match the oracle for WeightedElement<String,String>. */
    @Test
    public void testWeightedStringStringMatchesJavaPQ() {
        MyPriorityQueue<WeightedElement<String, String>> myPQ = new MyPriorityQueue<>();
        PriorityQueue<WeightedElement<String, String>> javaPQ = new PriorityQueue<>();
        String[][] pairs = {
            {"apple", "mango"},
            {"cat",   "ant"},
            {"dog",   "cat"},
            {"egg",   "fig"}
        };
        for (String[] pair : pairs) {
            myPQ.add(new WeightedElement<>(pair[0], pair[1]));
            javaPQ.add(new WeightedElement<>(pair[0], pair[1]));
            assertTrue(myPQ.isHeap());
        }
        while (!javaPQ.isEmpty()) {
            assertEquals(javaPQ.poll().getWeight(), myPQ.poll().getWeight());
        }
    }

    /** Single-element WeightedElement<String,String> edge case. */
    @Test
    public void testWeightedStringStringSingleElement() {
        MyPriorityQueue<WeightedElement<String, String>> pq = new MyPriorityQueue<>();
        WeightedElement<String, String> we = new WeightedElement<>("only", "z");
        pq.add(we);
        assertEquals("z", pq.peek().getWeight());
        assertEquals("z", pq.poll().getWeight());
        assertNull(pq.poll());
    }

    // -----------------------------------------------------------------------
    // MyClassicPriorityQueue: basic operation tests
    // -----------------------------------------------------------------------

    /** An empty classic queue should return null and have an empty map. */
    @Test
    public void testClassicPQEmptyBehavior() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        assertNull(pq.peek());
        assertNull(pq.poll());
        assertTrue(pq.isHeap());
        assertTrue(pq.getIndexMap().isEmpty());
    }

    /** A single-element classic queue should add the element to the map at index 0. */
    @Test
    public void testClassicPQSingleElement() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(7);
        assertEquals(Integer.valueOf(7), pq.peek());
        assertEquals(Integer.valueOf(0), pq.getIndexMap().get(7));
        assertEquals(Integer.valueOf(7), pq.poll());
        assertTrue(pq.getIndexMap().isEmpty());
    }

    /** poll should return elements in ascending order. */
    @Test
    public void testClassicPQPollOrder() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(5);
        pq.add(3);
        pq.add(8);
        pq.add(1);
        assertEquals(Integer.valueOf(1), pq.poll());
        assertEquals(Integer.valueOf(3), pq.poll());
        assertEquals(Integer.valueOf(5), pq.poll());
        assertEquals(Integer.valueOf(8), pq.poll());
        assertNull(pq.poll());
    }

    /** Every element in the heap should have its index correctly recorded in the map. */
    @Test
    public void testClassicPQMapIndicesConsistentAfterAdd() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(10);
        pq.add(5);
        pq.add(20);
        pq.add(1);
        for (int i = 0; i < pq.heap.size(); i++) {
            Integer elem = pq.heap.get(i);
            assertEquals(Integer.valueOf(i), pq.getIndexMap().get(elem));
        }
    }

    /** The map should remain consistent after a poll operation */
    @Test
    public void testClassicPQMapIndicesConsistentAfterPoll() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        int[] values = {15, 7, 3, 12, 9};
        for (int v : values) pq.add(v);
        pq.poll();
        for (int i = 0; i < pq.heap.size(); i++) {
            Integer elem = pq.heap.get(i);
            assertEquals(Integer.valueOf(i), pq.getIndexMap().get(elem));
        }
    }

    /** Poll order of MyClassicPriorityQueue must match java.util.PriorityQueue. */
    @Test
    public void testClassicPQMatchesJavaPQ() {
        MyClassicPriorityQueue<Integer> myPQ = new MyClassicPriorityQueue<>();
        PriorityQueue<Integer> javaPQ = new PriorityQueue<>();
        int[] values = {15, 7, 3, 12, 9, 1, 6};
        for (int v : values) {
            myPQ.add(v);
            javaPQ.add(v);
        }
        while (!javaPQ.isEmpty()) {
            assertEquals(javaPQ.poll(), myPQ.poll());
            assertTrue(myPQ.isHeap());
        }
    }

    // -----------------------------------------------------------------------
    // decreaseKey tests
    // -----------------------------------------------------------------------

    /** decreaseKey on a leaf element should restore heap property and update poll order. */
    @Test
    public void testDecreaseKeyLeafElement() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(10);
        pq.add(20);
        pq.add(30);
        Integer old = pq.decreaseKey(30, 5);
        assertEquals(Integer.valueOf(30), old);
        assertTrue(pq.isHeap());
        assertEquals(Integer.valueOf(5), pq.poll());
    }

    /** decreaseKey on the current root element should still return the correct old value. */
    @Test
    public void testDecreaseKeyRootElement() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(10);
        pq.add(20);
        pq.add(30);
        Integer old = pq.decreaseKey(10, 1);
        assertEquals(Integer.valueOf(10), old);
        assertTrue(pq.isHeap());
        assertEquals(Integer.valueOf(1), pq.poll());
    }

    /** decreaseKey on a mid-heap element should bubble it up correctly. */
    @Test
    public void testDecreaseKeyMiddleElement() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(1);
        pq.add(10);
        pq.add(20);
        pq.add(15);
        pq.add(30);
        Integer old = pq.decreaseKey(20, 2);
        assertEquals(Integer.valueOf(20), old);
        assertTrue(pq.isHeap());
        // 2 should be polled right after the existing root 1
        pq.poll();
        assertEquals(Integer.valueOf(2), pq.poll());
    }

    /** decreaseKey should return null without any change when smallerValue is larger. */
    @Test
    public void testDecreaseKeyLargerValueReturnsNull() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(10);
        pq.add(20);
        assertNull(pq.decreaseKey(10, 15));
        assertTrue(pq.isHeap());
        assertEquals(Integer.valueOf(10), pq.peek());
    }

    /** decreaseKey should return null when smallerValue equals target. */
    @Test
    public void testDecreaseKeyEqualValueReturnsNull() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(10);
        pq.add(20);
        assertNull(pq.decreaseKey(10, 10));
    }

    /** decreaseKey should return null when target is not present in the queue. */
    @Test
    public void testDecreaseKeyNotInQueueReturnsNull() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(10);
        assertNull(pq.decreaseKey(99, 5));
    }

    /** After decreaseKey the old key must be removed from the map and the new key added. */
    @Test
    public void testDecreaseKeyMapUpdatedCorrectly() {
        MyClassicPriorityQueue<Integer> pq = new MyClassicPriorityQueue<>();
        pq.add(10);
        pq.add(20);
        pq.add(30);
        pq.decreaseKey(20, 1);
        // Old key must be gone, new key must be present
        assertFalse(pq.getIndexMap().containsKey(20));
        assertTrue(pq.getIndexMap().containsKey(1));
        // All remaining heap indices must be consistent
        for (int i = 0; i < pq.heap.size(); i++) {
            assertEquals(Integer.valueOf(i), pq.getIndexMap().get(pq.heap.get(i)));
        }
    }

    // -----------------------------------------------------------------------
    // MyClassicPriorityQueue with WeightedElement<Integer,Integer>
    // -----------------------------------------------------------------------

    /** decreaseKey should work correctly for WeightedElement<Integer,Integer>. */
    @Test
    public void testClassicDecreaseKeyWeightedIntInt() {
        MyClassicPriorityQueue<WeightedElement<Integer, Integer>> pq = new MyClassicPriorityQueue<>();
        WeightedElement<Integer, Integer> a = new WeightedElement<>(1, 10);
        WeightedElement<Integer, Integer> b = new WeightedElement<>(2, 5);
        WeightedElement<Integer, Integer> c = new WeightedElement<>(3, 20);
        pq.add(a);
        pq.add(b);
        pq.add(c);
        WeightedElement<Integer, Integer> smaller = new WeightedElement<>(3, 1);
        WeightedElement<Integer, Integer> old = pq.decreaseKey(c, smaller);
        assertEquals(c, old);
        assertTrue(pq.isHeap());
        assertEquals(smaller, pq.poll());
    }

    /** Poll order for WeightedElement<Integer,Integer> in classic queue must match oracle. */
    @Test
    public void testClassicWeightedIntIntMatchesJavaPQ() {
        MyClassicPriorityQueue<WeightedElement<Integer, Integer>> myPQ = new MyClassicPriorityQueue<>();
        PriorityQueue<WeightedElement<Integer, Integer>> javaPQ = new PriorityQueue<>();
        int[][] pairs = {{1, 5}, {2, 3}, {3, 8}, {4, 1}, {5, 9}};
        for (int[] pair : pairs) {
            myPQ.add(new WeightedElement<>(pair[0], pair[1]));
            javaPQ.add(new WeightedElement<>(pair[0], pair[1]));
        }
        while (!javaPQ.isEmpty()) {
            assertEquals(javaPQ.poll().getWeight(), myPQ.poll().getWeight());
            assertTrue(myPQ.isHeap());
        }
    }

    // -----------------------------------------------------------------------
    // MyClassicPriorityQueue with WeightedElement<String,String>
    // -----------------------------------------------------------------------

    /** Poll order for WeightedElement<String,String> in classic queue must match oracle. */
    @Test
    public void testClassicWeightedStringStringMatchesJavaPQ() {
        MyClassicPriorityQueue<WeightedElement<String, String>> myPQ = new MyClassicPriorityQueue<>();
        PriorityQueue<WeightedElement<String, String>> javaPQ = new PriorityQueue<>();
        String[][] pairs = {
            {"apple", "mango"},
            {"cat",   "ant"},
            {"dog",   "cat"},
            {"egg",   "fig"}
        };
        for (String[] pair : pairs) {
            myPQ.add(new WeightedElement<>(pair[0], pair[1]));
            javaPQ.add(new WeightedElement<>(pair[0], pair[1]));
        }
        while (!javaPQ.isEmpty()) {
            assertEquals(javaPQ.poll().getWeight(), myPQ.poll().getWeight());
            assertTrue(myPQ.isHeap());
        }
    }

    /** decreaseKey on WeightedElement<String,String> with a larger weight must return null. */
    @Test
    public void testClassicDecreaseKeyWeightedStringStringRejected() {
        MyClassicPriorityQueue<WeightedElement<String, String>> pq = new MyClassicPriorityQueue<>();
        WeightedElement<String, String> a = new WeightedElement<>("cat", "apple");
        pq.add(a);
        WeightedElement<String, String> bigger = new WeightedElement<>("cat", "zebra");
        assertNull(pq.decreaseKey(a, bigger));
        assertEquals(a, pq.peek());
    }
}