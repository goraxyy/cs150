import java.util.NoSuchElementException;

/**
 * FIFO queue built on CustomLinkedList.
 * Used for the warehouse dock arrival queue.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class CustomQueue<T> {

    private final CustomLinkedList<T> list = new CustomLinkedList<>();

    public void enqueue(T item) { list.addBack(item); }

    public T dequeue() {
        if (list.isEmpty()) throw new NoSuchElementException("Queue is empty");
        return list.removeFront();
    }

    public T peek() {
        if (list.isEmpty()) throw new NoSuchElementException("Queue is empty");
        return list.get(0);
    }

    public boolean isEmpty() { return list.isEmpty(); }
    public int size() { return list.size(); }
}