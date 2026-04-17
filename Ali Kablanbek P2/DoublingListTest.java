import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * DoublingListTest.java
 *
 * JUnit 4 unit tests for DoublingList<E>.
 * Every figure in the project specification (Figures 4–11) is tested,
 * along with iterator operations, boundary conditions, and compaction.
 *
 *   @author Ali Kablanbek
 *   @version 4/15/26
 */
public class DoublingListTest {

    private DoublingList<String> list;

    @Before
    public void setUp() {
        list = new DoublingList<>();
    }

    // ============================================================
    //  Helper
    // ============================================================

    /** Adds all elements of arr to list in order. */
    private void addAll(String... arr) {
        for (String s : arr) list.add(s);
    }

    // ============================================================
    //  Empty list
    // ============================================================

    @Test
    public void testEmptySize() {
        assertEquals(0, list.size());
    }

    @Test
    public void testEmptyToStringInternal() {
        assertEquals("[]", list.toStringInternal());
    }

    // ============================================================
    //  Figure 4 — insert into empty list
    //  head -> [A] -> tail     (k=1, cap=1)
    // ============================================================

    @Test
    public void testFigure4_insertIntoEmpty() {
        list.add("A");
        assertEquals(1, list.size());
        assertEquals("[(A)]", list.toStringInternal());
    }

    // ============================================================
    //  Node capacity sequence: node0=1, node1=2, node2=4, node3=8
    // ============================================================

    @Test
    public void testNodeCapacities() {
        // After 1 add: k=1, cap=1
        addAll("A");
        assertEquals("[(A)]", list.toStringInternal());

        // After 2 adds: k=2, cap=3
        list.add("B");
        assertEquals("[(A), (B, -)]", list.toStringInternal());

        // After 3 adds: k=2, cap=3, full
        list.add("C");
        assertEquals("[(A), (B, C)]", list.toStringInternal());

        // After 4 adds: k=3, cap=7
        list.add("D");
        assertEquals("[(A), (B, C), (D, -, -, -)]", list.toStringInternal());
    }

    // ============================================================
    //  Figure 5 - insert at index when target node has room
    //  Insert X at index 3 into [A,B,C,D,E,F,G,H] (node r2 not full)
    // ============================================================

    @Test
    public void testFigure5_insertAtIndexNodeHasRoom() {
        // Build 8 elements: k=4, cap=15
        // Structure: (A)(B,-)(C,D,E,-)(F,G,H,I,-,-,-,-)
        addAll("A","B","C","D","E","F","G","H");
        // Insert X at index 2 — r2=(C,D,E,-) has room
        list.add(2, "X");
        assertEquals(9, list.size());
        assertEquals("X", list.get(2));
        assertEquals("C", list.get(3)); // original C shifted right
        assertEquals("D", list.get(4));
    }

    // ============================================================
    //  Figure 6 - leftward shift
    //  13-element list; insert X at index 9 (node r3 full, r2 has room)
    // ============================================================

    @Test
    public void testFigure6_leftwardShift() {
        // Build 13 elements into a k=4 list (cap=15)
        addAll("A","B","C","D","E","F","G","H","I","J","K","L","M");
        // k=4: (A)(B,-)(C,D,E,-)(F,G,H,I,J,K,L,M)  size=13
        // r3 is full; predecessor r2 has room (count=3, cap=4)
        list.add(9, "X");
        assertEquals(14, list.size());
        assertEquals("X", list.get(9));
    }

    // ============================================================
    //  Figure 7 - rightward shift
    //  9-element list; insert X at index 0
    //  (r0 full, all predecessors full -> rightward shift)
    // ============================================================

    @Test
    public void testFigure7_rightwardShift() {
        // 9 elements: (A)(B,-)(C,D,E,-)(F,G,H,I,-,-,-,-)  k=4
        addAll("A","B","C","D","E","F","G","H","I");
        // r0=(A) is full; no predecessor -> rightward shift
        list.add(0, "X");
        assertEquals(10, list.size());
        assertEquals("X", list.get(0));
        assertEquals("A", list.get(1));
        assertEquals("B", list.get(2));
    }

    // ============================================================
    //  Figure 8 - step-by-step appending with a partially-filled list
    // ============================================================

    @Test
    public void testFigure8_appendSteps() {
        // Start: (B)(C,-)(D,E,-,-)  k=3 cap=7 size=4
        addAll("B","C","D","E");
        assertEquals("[(B), (C, -), (D, E, -, -)]", list.toStringInternal());

        // Fig 8(b): F appended - slot in node r2
        list.add("F");
        assertEquals("[(B), (C, -), (D, E, F, -)]", list.toStringInternal());

        // Fig 8(c): G appended - slot in node r2
        list.add("G");
        assertEquals("[(B), (C, -), (D, E, F, G)]", list.toStringInternal());

        // Fig 8(d): H appended - r2 full, r1 has room -> leftward shift, H at end of r2
        list.add("H");
        assertEquals(7, list.size());
        // After leftward: (B)(C,B... wait - leftward shift at pos=size
        // pulls elements left, vacates last slot of r2; H placed there
        // Verify H is last
        assertEquals("H", list.get(6));

        // Fig 8(e): I appended - all r0..r2 full → goes to r3 (empty successor)
        list.add("I");
        assertEquals(8, list.size());
        assertEquals("I", list.get(7));
    }

    // ============================================================
    //  Figure 9 - expansion when size == cap
    // ============================================================

    @Test
    public void testFigure9_expansion() {
        // Fill k=3 to cap=7
        addAll("F","I","J","M","P","Q","R");
        assertEquals(7, list.size());
        assertEquals("[(F), (I, J), (M, P, Q, R)]", list.toStringInternal());

        // Add T - triggers expansion to k=4
        list.add("T");
        assertEquals(8, list.size());
        assertEquals("T", list.get(7));
        // New node has capacity 8; T is at index 0 of it
        assertTrue(list.toStringInternal().endsWith(
            "(T, -, -, -, -, -, -, -)]"));
    }

    // ============================================================
    //  Figure 10 - deletion within a node
    // ============================================================

    @Test
    public void testFigure10_deletion() {
        // Build A B C D E F G H I  (9 elements, k=4)
        addAll("A","B","C","D","E","F","G","H","I");

        // Delete index 4 (E) - Fig 10(b)
        assertEquals("E", list.remove(4));
        assertEquals(8, list.size());
        assertEquals("F", list.get(4));  // F slides left

        // Delete index 1 (B) - Fig 10(c)
        assertEquals("B", list.remove(1));
        assertEquals(7, list.size());
        assertEquals("C", list.get(1));  // C slides left
    }

    // ============================================================
    //  Figure 11 - compaction after removal
    //  k=3, remove until size <= 2^(3-2)-1 = 1
    // ============================================================

    @Test
    public void testFigure11_compaction() {
        // Build F C A D - (F)(C,-)(A,D,-,-)  k=3 cap=7
        addAll("F","C","A","D");
        // Remove until compaction fires (size <= 1)
        list.remove(3); // remove D -> size=3  (3 > 1, no compact)
        list.remove(2); // remove A -> size=2  (2 > 1, no compact)
        list.remove(1); // remove C -> size=1  (1 <= 1, COMPACT)
        assertEquals(1, list.size());
        // After compaction: k=2 nodes, node0=(F), node1=(-, -)
        assertEquals("[(F), (-, -)]", list.toStringInternal());
    }

    // ============================================================
    //  Compaction - verify elements preserved
    // ============================================================

    @Test
    public void testCompactionPreservesElements() {
        addAll("A","B","C","D","E","F","G");  // k=3 cap=7 full
        list.add("H");   // expands to k=4
        // Remove 6 elements to force compaction
        for (int i = 0; i < 6; i++) list.remove(0);
        assertEquals(2, list.size());
        assertEquals("G", list.get(0));
        assertEquals("H", list.get(1));
    }

    // ============================================================
    //  NullPointerException tests
    // ============================================================

    @Test(expected = NullPointerException.class)
    public void testAddNullThrows() { list.add(null); }

    @Test(expected = NullPointerException.class)
    public void testAddAtIndexNullThrows() { list.add(0, null); }

    @Test(expected = NullPointerException.class)
    public void testIteratorAddNullThrows() {
        list.add("A");
        list.listIterator(0).add(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIteratorSetNullThrows() {
        list.add("A");
        ListIterator<String> it = list.listIterator(0);
        it.next();
        it.set(null);
    }

    // ============================================================
    //  IndexOutOfBoundsException tests
    // ============================================================

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex()  { list.add(-1, "A"); }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddTooLargeIndex()  { list.add(1, "A"); }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveNegativeIndex() { list.remove(-1); }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOnEmptyList() { list.remove(0); }

    // ============================================================
    //  DoublingIterator - forward traversal
    // ============================================================

    @Test
    public void testDoublingIteratorForward() {
        addAll("A","B","C","D","E");
        StringBuilder sb = new StringBuilder();
        for (String s : list) sb.append(s);
        assertEquals("ABCDE", sb.toString());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDoublingIteratorNextPastEnd() {
        list.add("A");
        Iterator<String> it = list.iterator();
        it.next();
        it.next();  // throws
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoublingIteratorRemoveThrows() {
        list.add("A");
        Iterator<String> it = list.iterator();
        it.next();
        it.remove();  // throws
    }

    // ============================================================
    //  DoublingListIterator - forward traversal
    // ============================================================

    @Test
    public void testListIteratorForward() {
        addAll("A","B","C","D");
        ListIterator<String> it = list.listIterator(0);
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) sb.append(it.next());
        assertEquals("ABCD", sb.toString());
    }

    // ============================================================
    //  DoublingListIterator - backward traversal
    // ============================================================

    @Test
    public void testListIteratorBackward() {
        addAll("A","B","C","D");
        ListIterator<String> it = list.listIterator(list.size());
        StringBuilder sb = new StringBuilder();
        while (it.hasPrevious()) sb.append(it.previous());
        assertEquals("DCBA", sb.toString());
    }

    // ============================================================
    //  DoublingListIterator - nextIndex / previousIndex
    // ============================================================

    @Test
    public void testListIteratorIndices() {
        addAll("A","B","C");
        ListIterator<String> it = list.listIterator(1);
        assertEquals(1, it.nextIndex());
        assertEquals(0, it.previousIndex());
        it.next();
        assertEquals(2, it.nextIndex());
        assertEquals(1, it.previousIndex());
    }

    // ============================================================
    //  DoublingListIterator - set() after next
    // ============================================================

    @Test
    public void testListIteratorSetAfterNext() {
        addAll("A","B","C");
        ListIterator<String> it = list.listIterator(0);
        it.next();       // returns A, lastRet=0
        it.set("Z");
        assertEquals("Z", list.get(0));
        assertEquals("B", list.get(1));
    }

    // ============================================================
    //  DoublingListIterator - set() after previous
    // ============================================================

    @Test
    public void testListIteratorSetAfterPrevious() {
        addAll("A","B","C");
        ListIterator<String> it = list.listIterator(2);
        it.previous();   // returns B, lastRet=1
        it.set("Y");
        assertEquals("Y", list.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testListIteratorSetWithoutMove() {
        list.add("A");
        list.listIterator(0).set("Z");  // no next/previous → throws
    }

    // ============================================================
    //  DoublingListIterator - add()
    // ============================================================

    @Test
    public void testListIteratorAdd() {
        addAll("A","C");
        ListIterator<String> it = list.listIterator(1);
        it.add("B");
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
        assertEquals(2, it.nextIndex());  // cursor past inserted element
    }

    // ============================================================
    //  DoublingListIterator - remove() after next
    // ============================================================

    @Test
    public void testListIteratorRemoveAfterNext() {
        addAll("A","B","C");
        ListIterator<String> it = list.listIterator(0);
        it.next();       // returns A
        it.remove();     // removes A
        assertEquals(2, list.size());
        assertEquals("B", list.get(0));
        assertEquals(0, it.nextIndex());
    }

    // ============================================================
    //  DoublingListIterator - remove() after previous
    // ============================================================

    @Test
    public void testListIteratorRemoveAfterPrevious() {
        addAll("A","B","C");
        ListIterator<String> it = list.listIterator(2);
        it.previous();   // returns B, lastRet=1
        it.remove();     // removes B
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
        assertEquals(1, it.nextIndex());
    }

    @Test(expected = IllegalStateException.class)
    public void testListIteratorRemoveWithoutMove() {
        list.add("A");
        list.listIterator(0).remove();  // throws
    }

    // ============================================================
    //  toStringInternal with cursor (spec example)
    // ============================================================

    @Test
    public void testToStringInternalWithCursor_specExample() {
        // Build 8-element list: (A)(B,-)(C,D,E,-)(F,G,H,-,-,-,-,-)
        addAll("A","B","C","D","E","F","G","H");
        ListIterator<String> it = list.listIterator(3);
        // nextIndex=3, cursor is before D in node r2
        String s = list.toStringInternal(it);
        // Verify cursor marker appears before D
        assertTrue("Cursor '|' expected before D", s.contains("| D"));
    }

    @Test
    public void testToStringInternalCursorAtStart() {
        addAll("A","B","C");
        ListIterator<String> it = list.listIterator(0);
        String s = list.toStringInternal(it);
        assertTrue("Cursor at start should precede A", s.contains("| A"));
    }

    // ============================================================
    //  get() and contains()
    // ============================================================

    @Test
    public void testGet() {
        addAll("X","Y","Z");
        assertEquals("X", list.get(0));
        assertEquals("Y", list.get(1));
        assertEquals("Z", list.get(2));
    }

    @Test
    public void testContains() {
        addAll("A","B","C");
        assertTrue(list.contains("B"));
        assertFalse(list.contains("Z"));
    }

    // ============================================================
    //  indexOf() and lastIndexOf()
    // ============================================================

    @Test
    public void testIndexOf() {
        addAll("A","B","C","B");
        assertEquals(1,  list.indexOf("B"));
        assertEquals(3,  list.lastIndexOf("B"));
        assertEquals(-1, list.indexOf("Z"));
    }

    // ============================================================
    //  Stress test - large list add and remove
    // ============================================================

    @Test
    public void testLargeList() {
        int n = 64;
        for (int i = 0; i < n; i++) list.add("E" + i);
        assertEquals(n, list.size());
        for (int i = 0; i < n; i++) assertEquals("E" + i, list.remove(0));
        assertEquals(0, list.size());
        assertEquals("[]", list.toStringInternal());
    }

    // ============================================================
    //  Single-element list remove -> resets to empty
    // ============================================================

    @Test
    public void testRemoveSingleElement() {
        list.add("A");
        list.remove(0);
        assertEquals(0, list.size());
        assertEquals("[]", list.toStringInternal());
    }
}