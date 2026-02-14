public class IntArrayList {
    private int[] data;
    private int size;
    private int cursor;

    /*
     * Creates an empty IntArrayList with an internal array capacity of 10.
     */
    public IntArrayList() {
        data = new int[10];
        size = 0;
        cursor = 0;
    }

    /*
     * Appends an integer to the end of the list.
     * Grows the internal array by 10 when the array becomes full.
     *
     * @param e The integer to append.
     */
    public void add(int e) {
        if (size == data.length) {
            int[] bigger = new int[data.length + 10];
            for (int i = 0; i < size; i++) bigger[i] = data[i];
            data = bigger;
        }
        data[size] = e;
        size++;
    }

    /*
     * Inserts an integer at the specified index, shifting elements to the right.
     * Grows the internal array by 10 when the array becomes full.
     *
     * @param index The position to insert at (0..size).
     * @param e The integer to insert.
     */
    public void add(int index, int e) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();

        if (size == data.length) {
            int[] bigger = new int[data.length + 10];
            for (int i = 0; i < size; i++) bigger[i] = data[i];
            data = bigger;
        }

        for (int i = size; i > index; i--) data[i] = data[i - 1];
        data[index] = e;
        size++;
    }

    /*
     * Returns the integer stored at the specified index.
     *
     * @param index The position to retrieve (0..size-1).
     * @return The integer at the given index.
     */
    public int get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return data[index];
    }

    /*
     * Clears the list back to its initial state (size 0, capacity 10, cursor 0).
     */
    public void clear() {
        data = new int[10];
        size = 0;
        cursor = 0;
    }

    /*
     * Checks whether the list contains no elements.
     *
     * @return true if the list is empty; false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
     * Removes and returns the element at the specified index, shifting elements left.
     * Shrinks the internal array when empty slots become greater than 10, keeping
     * exactly 10 empty slots (capacity becomes size + 10).
     *
     * @param index The position to remove (0..size-1).
     * @return The removed integer value.
     */
    public int remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        int removed = data[index];

        for (int i = index; i < size - 1; i++) data[i] = data[i + 1];
        size--;

        if (emptyCount() > 10) {
            int[] smaller = new int[size + 10];
            for (int i = 0; i < size; i++) smaller[i] = data[i];
            data = smaller;
        }

        if (cursor > size) cursor = size;
        return removed;
    }

    /*
     * Returns the number of elements currently stored in the list.
     *
     * @return The logical size of the list.
     */
    public int size() {
        return size;
    }

    /*
     * Returns the length of the internal array (the current capacity).
     *
     * @return The internal array length.
     */
    public int arraySize() {
        return data.length;
    }

    /*
     * Returns the number of unused (empty) slots in the internal array.
     *
     * @return The number of empty slots (arraySize - size).
     */
    public int emptyCount() {
        return data.length - size;
    }

    /*
     * Returns the stored integers as a single string, separated by one space.
     * Returns "" if the list is empty.
     *
     * @return A space-separated string of the stored integers.
     */
    public String toString() {
        if (size == 0) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(data[0]);
        for (int i = 1; i < size; i++) sb.append(' ').append(data[i]);
        return sb.toString();
    }

    /*
     * Resets the internal cursor used by next() back to the beginning (0).
     */
    public void reset() {
        cursor = 0;
    }

    /*
     * Returns the element at the current cursor position and then increments
     * the cursor. If the cursor is at or beyond the end of the list, throws
     * an Exception with the required message.
     *
     * @return The next integer in iteration order.
     * @throws Exception If the end of stored data is reached.
     */
    public int next() throws Exception {
        if (cursor >= size) throw new Exception("End of stored data is reached.");
        int val = data[cursor];
        cursor++;
        return val;
    }
}