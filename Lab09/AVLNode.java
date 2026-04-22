/**
 * A node for AVL trees storing extra fields for height and parent.
 *
 * @param <E> the type of element stored in the node
 * @author Ali Kablanbek
 * @version 4/21/26
 */
public class AVLNode<E> extends BinaryNode<E> {

    /** Height of the subtree rooted at this node. */
    protected int height;

    /** Reference to the parent node. */
    protected AVLNode<E> parent;

    /**
     * Constructs an AVLNode with the given data.
     * Default height is 0 and parent is null.
     *
     * @param data the element to store
     */
    public AVLNode(E data) {
        super(data);
        this.height = 0;
        this.parent = null;
    }

    /** Returns the height of this node. */
    public int getHeight() {
        return height;
    }

    /** Sets the height of this node. */
    public void setHeight(int height) {
        this.height = height;
    }

    /** Returns the parent of this node. */
    public AVLNode<E> getParent() {
        return parent;
    }

    /** Sets the parent of this node. */
    public void setParent(AVLNode<E> parent) {
        this.parent = parent;
    }
}