import java.util.ArrayList;

/**
 * A generic singly-linked list Node.
 * Each Node stores a value and a reference to the next Node.
 * The Node itself represents the full list beginning at this head node;
 * traversing {@code next} pointers reaches every subsequent element.
 *
 * @param <E> the type of elements stored in this linked list
 * @author Ali Kablanbek
 * @version 3/31/26
 */
public class Node<E> {

    protected E val;
    protected Node<E> next;

    /**
     * Constructs a single-element Node with no successor.
     *
     * @param d the value to store in this node
     */
    public Node(E d) {
        val = d;
        next = null;
    }

    /**
     * Constructs a linked list from an ArrayList, making this Node the head.
     * The first element of {@code list} is stored in this node;
     * each subsequent element becomes its own node chained via {@code next}.
     *
     * @param list the ArrayList whose elements populate the linked list,
     *             in order from head to tail
     * @throws IllegalArgumentException if {@code list} is null or empty
     */
    public Node(ArrayList<E> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty.");
        }
        val = list.get(0);
        next = null;
        Node<E> current = this;
        for (int i = 1; i < list.size(); i++) {
            current.next = new Node<E>(list.get(i));
            current = current.next;
        }
    }

    /**
     * Returns a comma-and-space-separated String of every value in the list,
     * starting from this head node and traversing to the tail.
     * A single-element list returns just that element with no comma.
     * ex: a list [1, 2, 3] returns {@code "1, 2, 3"}
     *
     * @return a String representation of the linked list
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> current = this;
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        return sb.toString();
    }

    /**
     * Appends a new node holding {@code value} to the tail of the linked list.
     * Traverses from this head node to the last node,then sets its
     * {@code next} pointer to the new node.
     *
     * @param value the value to add at the back of the list
     */
    public void addToBack(E value) {
        Node<E> current = this;
        while (current.next != null) {
            current = current.next;
        }
        current.next = new Node<E>(value);
    }

    /**
     * Prepends {@code value} to the front of the linked list.
     * Because {@code this} cannot be reassigned, the current head's value is
     * copied into a new node that takes over the second position, and
     * {@code value} is written directly into this node.
     * The head node's identity (object reference) never changes.
     *
     * @param value the value to add at the front of the list
     */
    public void addToFront(E value) {
        // Shift the current head value into a new second node
        Node<E> copy = new Node<E>(this.val);
        copy.next = this.next;
        // Overwrite this node with the new front value
        this.val = value;
        this.next = copy;
    }

    /**
     * Rotates the linked list to the right by {@code k} positions in place.
     * The last {@code k % length} elements wrap around to become the new front.
     * Example: [1, 2, 3, 4, 5] rotated right by 2 becomes [4, 5, 1, 2, 3].
     * Rotating by a multiple of the list length leaves the list unchanged.
     * Because {@code this} cannot be reassigned, values are collected into a
     * temporary array and written back to nodes in rotated order.
     *
     * @param k the number of positions to rotate right; must be non-negative
     * @throws IllegalArgumentException if {@code k} is negative
     */
    public void rotate(int k) {
        if (k < 0) {
            throw new IllegalArgumentException("Rotation number must be non-negative.");
        }
        if (k == 0) return;

        // Measure list length
        int length = 0;
        Node<E> curr = this;
        while (curr != null) {
            length++;
            curr = curr.next;
        }

        // Eliminate full-cycle rotations
        k = k % length;
        if (k == 0) return;

        // Snapshot all values
        @SuppressWarnings("unchecked")
        E[] values = (E[]) new Object[length];
        curr = this;
        for (int i = 0; i < length; i++) {
            values[i] = curr.val;
            curr = curr.next;
        }

        // Write back in right-rotated order:
        // node at position i receives the value formerly at (i - k + length) % length
        curr = this;
        for (int i = 0; i < length; i++) {
            curr.val = values[(i - k + length) % length];
            curr = curr.next;
        }
    }
}