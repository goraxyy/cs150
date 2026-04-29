import java.util.ArrayList;

/**
 * A generic min-heap priority queue backed by an ArrayList.
 * T must be Comparable so elements can be ordered naturally.
 * The heap is stored in a zero-indexed array layout where the parent
 * of index i is at (i - 1) / 2 and the children of index i are at
 * 2i + 1 (left) and 2i + 2 (right).
 *
 * @param <T> the element type, must be Comparable
 * 
 * @author Ali Kablanbek
 * @version 4/28
 */
public class MyPriorityQueue<T extends Comparable<T>> {

    // The ArrayList used as the backing store for the heap
    protected ArrayList<T> heap;

    /**
     * Constructs an empty priority queue.
     */
    public MyPriorityQueue() {
        heap = new ArrayList<>();
    }

    /**
     * Inserts t at the end of the heap then bubbles it up to restore the heap property.
     *
     * @param t the element to insert
     * @return true always, indicating the element was added
     */
    public boolean add(T t) {
        heap.add(t);
        bubbleUp(heap.size() - 1);
        return true;
    }

    /**
     * Moves the element at the given index upward until its parent is no larger
     * than it, restoring the heap property above the index.
     *
     * @param index the starting index of the element to bubble up
     */
    protected void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            // If this element is smaller than its parent, swap upward
            if (heap.get(index).compareTo(heap.get(parent)) < 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Removes and returns the minimal element (the root of the heap).
     * The last element is moved to the root and sifted down to restore the heap property.
     *
     * @return the minimal element, or null if the queue is empty
     */
    public T poll() {
        if (heap.isEmpty()) return null;
        T min = heap.get(0);
        int last = heap.size() - 1;
        // If only one element remains just remove and return it
        if (last == 0) {
            heap.remove(0);
            return min;
        }
        // Move the last element to the root and sift it down
        heap.set(0, heap.remove(last));
        siftDown(0);
        return min;
    }

    /**
     * Moves the element at the given index downward until both of its
     * children are no smaller than it, restoring the heap property below
     * the index.
     *
     * @param index the starting index of the element to sift down
     */
    protected void siftDown(int index) {
        int size = heap.size();
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;
            // Check whether the left child is smaller than the current smalest
            if (left < size && heap.get(left).compareTo(heap.get(smallest)) < 0) {
                smallest = left;
            }
            // Check whether the right child is smaller than the current smallest
            if (right < size && heap.get(right).compareTo(heap.get(smallest)) < 0) {
                smallest = right;
            }
            // Swap with the smaller child and continue, or stop if already correct
            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    /**
     * Returns the minimal element without removing it from the heap.
     *
     * @return the minimal element, or null if the queue is empty
     */
    public T peek() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    /**
     * Iteratively verifies the heap ordering property for every element
     * by checking that each node is no larger than its left and right children.
     *
     * @return true if the heap property holds for every node, false otherwise
     */
    public boolean isHeap() {
        int size = heap.size();
        for (int i = 0; i < size; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < size && heap.get(i).compareTo(heap.get(left)) > 0) {
                return false;
            }
            if (right < size && heap.get(i).compareTo(heap.get(right)) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Swaps the elements at positions i and j in the heap.
     * Subclasses override this to also update any auxiliary data structures.
     *
     * @param i the first index
     * @param j the second index
     */
    protected void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Returns the number of elements currently in the heap.
     *
     * @return the size of the heap
     */
    public int size() {
        return heap.size();
    }

    /**
     * Returns true if the heap contains no elements.
     *
     * @return true if empty,false otherwise
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }
}