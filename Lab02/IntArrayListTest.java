import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class IntArrayListTest {

    @Test
    @DisplayName("constructor: initial state")
    void testConstructor() {
        IntArrayList list = new IntArrayList();
        assertEquals(0, list.size());
        assertEquals(10, list.arraySize());
        assertEquals(10, list.emptyCount());
        assertTrue(list.isEmpty());
        assertEquals("", list.toString());
    }

    @Test
    @DisplayName("add(e): append and auto-grow")
    void testAddAppendAndGrow() {
        IntArrayList list = new IntArrayList();

        for (int i = 0; i < 10; i++) list.add(i);
        assertEquals(10, list.size());
        assertEquals(10, list.arraySize());
        assertEquals(0, list.emptyCount());
        assertEquals(9, list.get(9));

        list.add(10); // triggers grow (+10)
        assertEquals(11, list.size());
        assertEquals(20, list.arraySize());
        assertEquals(9, list.emptyCount());
        assertEquals(10, list.get(10));
    }

    @Test
    @DisplayName("add(index,e): insert shifts + bounds")
    void testAddAtIndex() {
        IntArrayList list = new IntArrayList();
        list.add(1);
        list.add(3);
        list.add(4);

        list.add(1, 2); // insert in middle
        assertEquals(4, list.size());
        assertEquals("1 2 3 4", list.toString());
        assertEquals(2, list.get(1));

        list.add(0, 0); // insert at front
        assertEquals("0 1 2 3 4", list.toString());

        list.add(list.size(), 5); // insert at end
        assertEquals("0 1 2 3 4 5", list.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 99));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(list.size() + 1, 99));
    }

    @Test
    @DisplayName("get(index): valid + bounds")
    void testGet() {
        IntArrayList list = new IntArrayList();
        list.add(42);

        assertEquals(42, list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    @DisplayName("clear(): return to initial state")
    void testClear() throws Exception {
        IntArrayList list = new IntArrayList();
        list.add(1);
        list.add(2);

        list.clear();
        assertEquals(0, list.size());
        assertEquals(10, list.arraySize());
        assertTrue(list.isEmpty());
        assertEquals("", list.toString());

        // next() should now throw because list is empty
        assertThrows(Exception.class, () -> list.next()); // required behavior [web:2]
    }

    @Test
    @DisplayName("isEmpty(): true/false transitions")
    void testIsEmpty() {
        IntArrayList list = new IntArrayList();
        assertTrue(list.isEmpty());

        list.add(7);
        assertFalse(list.isEmpty());

        list.remove(0);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("remove(index): returns removed, shifts, shrink rule, bounds")
    void testRemove() {
        IntArrayList list = new IntArrayList();

        // build 21 elements -> should grow to 30 when adding 21st? actually:
        // start 10, after 11th -> 20, after 21st -> 30
        for (int i = 0; i < 21; i++) list.add(i);
        assertEquals(21, list.size());
        assertEquals(30, list.arraySize());
        assertEquals(9, list.emptyCount());

        int removed = list.remove(10);
        assertEquals(10, removed);
        assertEquals(20, list.get(10)); // shifted
        assertEquals(20, list.size());

        // remove until shrink triggers: shrink when emptyCount() > 10
        // current array 30, size 20 -> empty 10 (not >10)
        list.remove(0); // size 19 -> empty 11 => should shrink to size+10 = 29
        assertEquals(19, list.size());
        assertEquals(29, list.arraySize());
        assertEquals(10, list.emptyCount());

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(list.size()));
    }

    @Test
    @DisplayName("size(), arraySize(), emptyCount(): consistency")
    void testSizeArraySizeEmptyCount() {
        IntArrayList list = new IntArrayList();
        assertEquals(0, list.size());
        assertEquals(list.arraySize() - list.size(), list.emptyCount());

        for (int i = 0; i < 13; i++) list.add(i);
        assertEquals(13, list.size());
        assertEquals(20, list.arraySize());
        assertEquals(7, list.emptyCount());
        assertEquals(list.arraySize() - list.size(), list.emptyCount());
    }

    @Test
    @DisplayName("toString(): formatting")
    void testToString() {
        IntArrayList list = new IntArrayList();
        assertEquals("", list.toString());

        list.add(5);
        assertEquals("5", list.toString());

        list.add(6);
        list.add(-7);
        assertEquals("5 6 -7", list.toString()); // single spaces only
    }

    @Test
    @DisplayName("reset() and next(): iteration + required exception + message")
    void testResetAndNext() throws Exception {
        IntArrayList list = new IntArrayList();
        list.add(10);
        list.add(20);

        list.reset();
        assertEquals(10, list.next());
        assertEquals(20, list.next());

        Exception ex = assertThrows(Exception.class, () -> list.next()); // end reached [web:2]
        assertEquals("End of stored data is reached.", ex.getMessage());

        // reset should allow iterating again
        list.reset();
        assertEquals(10, list.next());
    }
}
