/**
 * A generic binary search tree extending BinaryTree.
 * Requires that E implements Comparable for natural ordering.
 *
 * @param <E> the type of elements which must be Comparable
 * @author Ali Kablanbek
 * @version 4/21/26
 */
public class BinarySearchTree<E extends Comparable<E>> extends BinaryTree<E> {

    /**
     * Inserts the element into the BST using natural ordering.
     * Duplicates are rejected.
     *
     * @param e the element to insert
     * @return true if inserted successfully
     */
    @Override
    public boolean insert(E e) {
        if (root == null) {
            root = new BinaryNode<>(e);
            return true;
        }
        return insertHelper(root, e);
    }

    /**
     * Recursively finds the correct position and inserts the element.
     *
     * @param node the current node
     * @param e    the element to insert
     * @return true if inserted
     */
    private boolean insertHelper(BinaryNode<E> node, E e) {
        int cmp = e.compareTo(node.getData());
        if (cmp == 0) {
            return false; // duplicate
        } else if (cmp < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new BinaryNode<>(e));
                return true;
            }
            return insertHelper(node.getLeft(), e);
        } else {
            if (node.getRight() == null) {
                node.setRight(new BinaryNode<>(e));
                return true;
            }
            return insertHelper(node.getRight(), e);
        }
    }

    /**
     * Efficiently searches for an element using BST ordering.
     * Overrides the linear search from BinaryTree.
     *
     * @param e the element to find
     * @return true if the element is in the tree
     */
    @Override
    public boolean contains(E e) {
        return binarySearch(root, e);
    }

    /**
     * Recursively performs binary search using natural ordering.
     *
     * @param node the current node
     * @param e    the target element
     * @return true if found
     */
    private boolean binarySearch(BinaryNode<E> node, E e) {
        if (node == null) return false;
        int cmp = e.compareTo(node.getData());
        if (cmp == 0) return true;
        if (cmp < 0) return binarySearch(node.getLeft(), e);
        return binarySearch(node.getRight(), e);
    }
}