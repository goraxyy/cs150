/**
 * Represents a node in a binary tree.
 *
 * @param <E> the type of element stored in the node
 * @author Ali Kablanbek
 * @version 4/21/26
 */
public class BinaryNode<E> {

    /** The data stored in this node. */
    protected E data;

    /** Left and right child references. */
    protected BinaryNode<E> left;
    protected BinaryNode<E> right;

    /**
     * Constructs a BinaryNode with the given data.
     *
     * @param data the element to store
     */
    public BinaryNode(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    /** Returns the data stored in this node. */
    public E getData() {
        return data;
    }

    /** Sets the data for this node. */
    public void setData(E data) {
        this.data = data;
    }

    /** Returns the left child. */
    public BinaryNode<E> getLeft() {
        return left;
    }

    /** Sets the left child. */
    public void setLeft(BinaryNode<E> left) {
        this.left = left;
    }

    /** Returns the right child. */
    public BinaryNode<E> getRight() {
        return right;
    }

    /** Sets the right child. */
    public void setRight(BinaryNode<E> right) {
        this.right = right;
    }
}