/**
 * Generic abstract binary tree implementing the Tree interface.
 * insert() is left abstract since ordering is not defined here.
 *
 * @param <E> the type of elements stored in the tree
 * @author Ali Kablanbek
 * @version 4/21/26
 */
public abstract class BinaryTree<E> implements Tree<E> {

    /** The root node of the tree. */
    protected BinaryNode<E> root;

    /** Constructs an empty BinaryTree. */
    public BinaryTree() {
        root = null;
    }

    /**
     * Checks if the tree contains the element by checking every node.
     * Uses a recursive helper since there is no ordering guarantee.
     *
     * @param e the element to search for
     * @return true if the element is found
     */
    @Override
    public boolean contains(E e) {
        return containsHelper(root, e);
    }

    /**
     * Recursively searches every node for the target element.
     *
     * @param node the current node
     * @param e    the target element
     * @return true if found
     */
    private boolean containsHelper(BinaryNode<E> node, E e) {
        if (node == null) return false;
        if (node.getData().equals(e)) return true;
        return containsHelper(node.getLeft(), e) || containsHelper(node.getRight(), e);
    }

    /**
     * Returns a preorder traversal string: root then left then right.
     *
     * @return preorder string
     */
    @Override
    public String preOrderString() {
        StringBuilder sb = new StringBuilder();
        preOrder(root, sb);
        return sb.toString().trim();
    }

    private void preOrder(BinaryNode<E> node, StringBuilder sb) {
        if (node == null) return;
        sb.append(node.getData()).append(" ");
        preOrder(node.getLeft(), sb);
        preOrder(node.getRight(), sb);
    }

    /**
     * Returns a postorder traversal string: left then right then root.
     *
     * @return postorder string
     */
    @Override
    public String postOrderString() {
        StringBuilder sb = new StringBuilder();
        postOrder(root, sb);
        return sb.toString().trim();
    }

    private void postOrder(BinaryNode<E> node, StringBuilder sb) {
        if (node == null) return;
        postOrder(node.getLeft(), sb);
        postOrder(node.getRight(), sb);
        sb.append(node.getData()).append(" ");
    }

    /**
     * Returns an inorder traversal string: left then root then right.
     *
     * @return inorder string
     */
    @Override
    public String inOrderString() {
        StringBuilder sb = new StringBuilder();
        inOrder(root, sb);
        return sb.toString().trim();
    }

    private void inOrder(BinaryNode<E> node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.getLeft(), sb);
        sb.append(node.getData()).append(" ");
        inOrder(node.getRight(), sb);
    }

    /**
     * Clears the tree by setting root to null.
     */
    @Override
    public void empty() {
        root = null;
    }

    /**
     * Returns true if the tree has no elements.
     *
     * @return true if root is null
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }
}