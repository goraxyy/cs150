import java.util.Objects;

/**
 * A generic container pairing an element of type E with a weight of type W.
 * W must be Comparable so that two WeightedElements can be ordered by weight.
 * This class implements Comparable by delegating entirely to W's compareTo method.
 *
 * @param <E> the type of the element stored
 * @param <W> the type of the weight, must itself be Comparable
 * 
 * @author Ali Kablanbek
 * @version 4/28
 */
public class WeightedElement<E, W extends Comparable<W>> implements Comparable<WeightedElement<E, W>> {

    private E element;
    private W weight;

    /**
     * Constructs a WeightedElement with the given element and weight.
     *
     * @param element the element to store
     * @param weight  the weight associated with this element
     */
    public WeightedElement(E element, W weight) {
        this.element = element;
        this.weight = weight;
    }

    /**
     * Returns the element stored in this WeightedElement.
     *
     * @return the element
     */
    public E getElement() {
        return element;
    }

    /**
     * Returns the weight stored in this WeightedElement.
     *
     * @return the weight
     */
    public W getWeight() {
        return weight;
    }

    /**
     * Compares this WeightedElement to another by weight only.
     * Relies on W's compareTo so that the ordering matches W's natural ordering.
     *
     * @param other the other WeightedElement to compare against
     * @return a negative integer if this weight is less than other's weight, zero if equal, a positive integer if greater
     */
    @Override
    public int compareTo(WeightedElement<E, W> other) {
        return this.weight.compareTo(other.weight);
    }

    /**
     * Two WeightedElements are equal when both their element and weight are equal.
     * This is needed for correct HashMap key behavior in MyClassicPriorityQueue.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WeightedElement)) return false;
        WeightedElement<?, ?> other = (WeightedElement<?, ?>) obj;
        return Objects.equals(element, other.element) && Objects.equals(weight, other.weight);
    }

    /**
     * Hash code is derived from both element and weight to be consistent with equals.
     */
    @Override
    public int hashCode() {
        return Objects.hash(element, weight);
    }

    /**
     * Returns a human-readable string in the form (element, weight).
     */
    @Override
    public String toString() {
        return "(" + element + ", " + weight + ")";
    }
}