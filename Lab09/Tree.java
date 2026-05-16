
/**
 * Generic Tree interface defining common tree operations.
 *
 * @param <E> the type of elements stored in the tree
 * @author ALi Kablanbek
 * @version 4/21/26
 */
public interface Tree<E> {

    /**
     * Inserts the given element into the tree.
     * Duplicate keys are not allowed.
     *
     * @param e the element to insert
     * @return true if insertion was successful
     */
    boolean insert(E e);

    /**
     * Checks whether the tree contains the given element.
     *
     * @param e the element to search for
     * @return true if the element is found
     */
    boolean contains(E e);

    /**
     * Returns a string of elements using preorder traversal.
     *
     * @return preorder string representation
     */
    String preOrderString();

    /**
     * Returns a string of elements using postorder traversal.
     *
     * @return postorder string representation
     */
    String postOrderString();

    /**
     * Returns a string of elements using inorder traversal.
     *
     * @return inorder string representation
     */
    String inOrderString();

    /**
     * Removes all elements from the tree.
     */
    void empty();

    /**
     * Returns true if the tree has no elements.
     *
     * @return true if empty
     */
    boolean isEmpty();
}