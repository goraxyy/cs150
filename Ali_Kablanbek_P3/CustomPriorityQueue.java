import java.util.NoSuchElementException;

/**
 * Min-heap backed by a resizable array.
 * Used for the truck manifest ordered by distance then shipment ID.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class CustomPriorityQueue<T extends Comparable<T>> {

    private Object[] heap;
    private int size;
    private int capacity;

    public CustomPriorityQueue(int initialCapacity) {
        this.capacity = initialCapacity;
        this.heap = new Object[capacity];
    }

    public void insert(T item) {
        if (size == capacity) resize();
        heap[size] = item;
        siftUp(size);
        size++;
    }

    @SuppressWarnings("unchecked")
    public T extractMin() {
        if (size == 0) throw new NoSuchElementException("Priority queue is empty");
        T min = (T) heap[0];
        size--;
        heap[0] = heap[size];
        heap[size] = null;
        if (size > 0) siftDown(0);
        return min;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (size == 0) throw new NoSuchElementException("Priority queue is empty");
        return (T) heap[0];
    }

    /** Rebuilds heap from scratch after item priorities change. */
    public void rebuildHeap() {
        for (int i = size / 2 - 1; i >= 0; i--) siftDown(i);
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    @SuppressWarnings("unchecked")
    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (((T) heap[i]).compareTo((T) heap[parent]) < 0) { swap(i, parent); i = parent; }
            else break;
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int i) {
        while (true) {
            int left = 2 * i + 1, right = 2 * i + 2, smallest = i;
            if (left < size && ((T) heap[left]).compareTo((T) heap[smallest]) < 0) smallest = left;
            if (right < size && ((T) heap[right]).compareTo((T) heap[smallest]) < 0) smallest = right;
            if (smallest != i) { swap(i, smallest); i = smallest; }
            else break;
        }
    }

    private void swap(int a, int b) {
        Object tmp = heap[a]; heap[a] = heap[b]; heap[b] = tmp;
    }

    private void resize() {
        capacity *= 2;
        Object[] bigger = new Object[capacity];
        for (int i = 0; i < size; i++) bigger[i] = heap[i];
        heap = bigger;
    }
}