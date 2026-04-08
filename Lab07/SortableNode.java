import java.util.ArrayList;

/**
 * A generic linked list Node that extends {@link Node} with sorting capability.
 * Requires {@code E} to implement {@code Comparable<E>} so that elements
 * can be compared and arranged in natural ascending order.
 *
 * @param <E> the element type; must implement {@code Comparable<E>}
 * @author Ali Kablanbek
 * @version 3/31/26
 */
public class SortableNode<E extends Comparable<E>> extends Node<E> {

    /**
     * Constructs a single-element SortableNode with no successor.
     *
     * @param d the value to store in this node
     */
    public SortableNode(E d) {
        super(d);
    }

    /**
     * Constructs a SortableNode linked list from an ArrayList,
     * making this node the head.
     *
     * @param list the ArrayList whose elements populate the linked list,
     *             in order from head to tail
     * @throws IllegalArgumentException if {@code list} is null or empty
     */
    public SortableNode(ArrayList<E> list) {
        super(list);
    }

    /**
     * Sorts the linked list in place in ascending natural order.
     * Uses bubble sort(hope its ok): each pass compares adjacent node values and
     * swaps them if the left value is greater than the right.
     * Passes repeat until a complete pass produces no swaps,
     * indicating the list is fully sorted.
     * Duplicate values are preserved.
     */
    public void sort() {
        if (this.next == null) return; // A single-element list is already sorted

        boolean swapped;
        do {
            swapped = false;
            Node<E> curr = this;
            while (curr.next != null) {
                if (curr.val.compareTo(curr.next.val) > 0) {
                    // Swap adjacent values
                    E temp = curr.val;
                    curr.val = curr.next.val;
                    curr.next.val = temp;
                    swapped = true;
                }
                curr = curr.next;
            }
        } while (swapped);
    }

    /**
     * Sorts the list and removes all duplicate values so that
     * each distinct value appears exactly once.
     * Calls {@link #sort()} first, which guarantees that duplicate values
     * are adjacent; then performs a single O(n) pass removing any node
     * whose value equals its successor's value.
     */
    public void uniqueSort() {
        sort(); // Duplicates become adjacent after sorting

        Node<E> curr = this;
        while (curr != null && curr.next != null) {
            if (curr.val.compareTo(curr.next.val) == 0) {
                curr.next = curr.next.next; // Bypass the duplicate node
            } else {
                curr = curr.next;
            }
        }
    }
}