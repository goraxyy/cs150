import java.util.HashMap;

/**
 * A min-heap priority queue that extends MyPriorityQueue by maintaining a
 * private HashMap from each element to its current index in the heap.
 * This map enables O(log n) decreaseKey because the index of any element
 * can be looked up in O(1) rather than scanned for linearly.
 *
 * bubbleUp and siftDown are inherited unchanged because they operate only
 * through swap, which is overridden here to keep the map in sync via
 * Java's dynamic dispatch.
 *
 * Assumes all inserted elements are unique (HashMap requires unique keys).
 *
 * @param <T> the element type, must be Comparable
 * 
 * @author Ali Kablanbek
 * @version 4/28
 */
public class MyClassicPriorityQueue<T extends Comparable<T>> extends MyPriorityQueue<T> {

    // Maps each element to its current position in the heap ArrayList
    private HashMap<T, Integer> indexMap;

    /**
     * Constructs an empty classic priority queue.
     */
    public MyClassicPriorityQueue() {
        super();
        indexMap = new HashMap<>();
    }

    /**
     * Inserts t into the heap and records its starting index in the map
     * before bubbling up to the correct position.
     *
     * @param t the element to insert
     * @return true always, indicating the element was added
     */
    @Override
    public boolean add(T t) {
        heap.add(t);
        indexMap.put(t, heap.size() - 1);
        bubbleUp(heap.size() - 1);
        return true;
    }

    /**
     * Removes and returns the minimal element. The last element is moved
     * to the root position and its map entry is updated before sifting down.
     * The removed root is deleted from the map.
     *
     * @return the minimal element, or null if the queue is empty
     */
    @Override
    public T poll() {
        if (heap.isEmpty()) return null;
        T min = heap.get(0);
        indexMap.remove(min);
        int last = heap.size() - 1;
        // Single-element case: remove and return directly
        if (last == 0) {
            heap.remove(0);
            return min;
        }
        // Remove the last element from the end of the heap
        T lastElem = heap.remove(last);
        // Remove its old map entry then place it at the root and register index 0
        indexMap.remove(lastElem);
        heap.set(0, lastElem);
        indexMap.put(lastElem, 0);
        siftDown(0);
        return min;
    }

    /**
     * Swaps the elements at positions i and j and updates both of their
     * map entries so the map always reflects the current heap layout.
     *
     * @param i the first index
     * @param j the second index
     */
    @Override
    protected void swap(int i, int j) {
        T elemI = heap.get(i);
        T elemJ = heap.get(j);
        heap.set(i, elemJ);
        heap.set(j, elemI);
        indexMap.put(elemI, j);
        indexMap.put(elemJ, i);
    }

    /**
     * Decreases the value of target to smallerValue and returns the old value.
     * Uses the map to locate target in O(1) then bubbles up from that index
     * to restore the heap property.
     *
     * Returns null without making any changes if target is not present in the
     * queue or if smallerValue is not strictly smaller than target.
     *
     * @param target       the element whose value should be decreased
     * @param smallerValue the new smaller value to replace target with
     * @return the old value of target, or null if no change was made
     */
    public T decreaseKey(T target, T smallerValue) {
        if (!indexMap.containsKey(target)) return null;
        // Reject the update if smallerValue is not strictly less than target
        if (smallerValue.compareTo(target) >= 0) return null;
        int index = indexMap.get(target);
        T oldValue = heap.get(index);
        heap.set(index, smallerValue);
        // Remove the old key adn register the new value at the same index
        indexMap.remove(target);
        indexMap.put(smallerValue, index);
        bubbleUp(index);
        return oldValue;
    }

    /**
     * Returns the internal index map for unit testing purposes.
     *
     * @return the HashMap mapping each element to its heap index
     */
    public HashMap<T, Integer> getIndexMap() {
        return indexMap;
    }
}