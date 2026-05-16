import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generic doubly-linked list. Backbone for CustomStack and CustomQueue.
 * Both addFront and addBack are O(1).
 * @author Ali Kablanbek
 * @version 5/15
 */
public class CustomLinkedList<T> implements Iterable<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    public void addFront(T data) {
        Node<T> node = new Node<>(data);
        if (head == null) { head = tail = node; }
        else { node.next = head; head.prev = node; head = node; }
        size++;
    }

    public void addBack(T data) {
        Node<T> node = new Node<>(data);
        if (tail == null) { head = tail = node; }
        else { node.prev = tail; tail.next = node; tail = node; }
        size++;
    }

    public T removeFront() {
        if (head == null) throw new NoSuchElementException("List is empty");
        T data = head.data;
        if (head == tail) { head = tail = null; }
        else { head = head.next; head.prev = null; }
        size--;
        return data;
    }

    public T removeBack() {
        if (tail == null) throw new NoSuchElementException("List is empty");
        T data = tail.data;
        if (head == tail) { head = tail = null; }
        else { tail = tail.prev; tail.next = null; }
        size--;
        return data;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        Node<T> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur.data;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> cur = head;
            public boolean hasNext() { return cur != null; }
            public T next() {
                if (cur == null) throw new NoSuchElementException();
                T data = cur.data;
                cur = cur.next;
                return data;
            }
        };
    }
}