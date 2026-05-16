import java.util.NoSuchElementException;

/**
 * LIFO stack built on CustomLinkedList.
 * Used for truck cargo so the last shipment loaded is delivered first.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class CustomStack<T> {

    private final CustomLinkedList<T> list = new CustomLinkedList<>();

    public void push(T item) { list.addFront(item); }

    public T pop() {
        if (list.isEmpty()) throw new NoSuchElementException("Stack is empty");
        return list.removeFront();
    }

    public T peek() {
        if (list.isEmpty()) throw new NoSuchElementException("Stack is empty");
        return list.get(0);
    }

    public boolean isEmpty() { return list.isEmpty(); }
    public int size() { return list.size(); }
}