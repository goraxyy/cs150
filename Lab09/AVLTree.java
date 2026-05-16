/**
 * An AVL tree extending BinarySearchTree.
 * Overrides insert() to maintain the AVL balance property after each insertion.
 *
 * @param <E> the type of elements which must be Comparable
 * @author Ali Kablanbek
 * @version 4/21/26
 */
public class AVLTree<E extends Comparable<E>> extends BinarySearchTree<E> {

    /**
     * Inserts the element and rebalances the tree as needed.
     *
     * @param e the element to insert
     * @return true if insertion was successful
     */
    @Override
    public boolean insert(E e) {
        if (root == null) {
            root = new AVLNode<>(e);
            return true;
        }
        boolean inserted = avlInsert((AVLNode<E>) root, null, e);
        return inserted;
    }

    /**
     * Recursively inserts the element and rebalances on the way back up.
     *
     * @param node   the current node
     * @param parent the parent of the current node
     * @param e      the element to insert
     * @return true if the element was inserted
     */
    private boolean avlInsert(AVLNode<E> node, AVLNode<E> parent, E e) {
        int cmp = e.compareTo(node.getData());
        if (cmp == 0) return false;

        if (cmp < 0) {
            if (node.getLeft() == null) {
                AVLNode<E> newNode = new AVLNode<>(e);
                node.setLeft(newNode);
                newNode.setParent(node);
            } else {
                if (!avlInsert((AVLNode<E>) node.getLeft(), node, e)) return false;
            }
        } else {
            if (node.getRight() == null) {
                AVLNode<E> newNode = new AVLNode<>(e);
                node.setRight(newNode);
                newNode.setParent(node);
            } else {
                if (!avlInsert((AVLNode<E>) node.getRight(), node, e)) return false;
            }
        }

        updateHeight(node);
        rebalance(node, parent);
        return true;
    }

    /**
     * Updates the height of a node based on its children's heights.
     *
     * @param node the node to update
     */
    private void updateHeight(AVLNode<E> node) {
        int leftH  = height((AVLNode<E>) node.getLeft());
        int rightH = height((AVLNode<E>) node.getRight());
        node.setHeight(1 + Math.max(leftH, rightH));
    }

    /**
     * Returns the height of a node or -1 if the node is null.
     *
     * @param node the node to measure
     * @return the height
     */
    private int height(AVLNode<E> node) {
        return (node == null) ? -1 : node.getHeight();
    }

    /**
     * Returns the balance factor of a node (left height minus right height).
     *
     * @param node the node to evaluate
     * @return balance factor
     */
    private int balanceFactor(AVLNode<E> node) {
        return height((AVLNode<E>) node.getLeft()) - height((AVLNode<E>) node.getRight());
    }

    /**
     * Rebalances the node if the balance factor is outside [-1, 1].
     * Performs the appropriate single or double rotation.
     *
     * @param node   the node to rebalance
     * @param parent the parent of the node
     */
    private void rebalance(AVLNode<E> node, AVLNode<E> parent) {
        int bf = balanceFactor(node);

        if (bf > 1) {
            // left heavy
            if (balanceFactor((AVLNode<E>) node.getLeft()) < 0) {
                // left-right case
                node.setLeft(rotateLeft((AVLNode<E>) node.getLeft()));
            }
            AVLNode<E> newSubRoot = rotateRight(node);
            replaceChild(parent, node, newSubRoot);
        } else if (bf < -1) {
            // right heavy
            if (balanceFactor((AVLNode<E>) node.getRight()) > 0) {
                // right-left case
                node.setRight(rotateRight((AVLNode<E>) node.getRight()));
            }
            AVLNode<E> newSubRoot = rotateLeft(node);
            replaceChild(parent, node, newSubRoot);
        }
    }

    /**
     * Performs a right rotation around the given node.
     *
     * @param y the node to rotate around
     * @return the new subroot after rotation
     */
    private AVLNode<E> rotateRight(AVLNode<E> y) {
        AVLNode<E> x = (AVLNode<E>) y.getLeft();
        y.setLeft(x.getRight());
        x.setRight(y);
        if (y.getLeft() != null) ((AVLNode<E>) y.getLeft()).setParent(y);
        x.setParent(y.getParent());
        y.setParent(x);
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /**
     * Performs a left rotation around the given node.
     *
     * @param x the node to rotate around
     * @return the new subroot after rotation
     */
    private AVLNode<E> rotateLeft(AVLNode<E> x) {
        AVLNode<E> y = (AVLNode<E>) x.getRight();
        x.setRight(y.getLeft());
        y.setLeft(x);
        if (x.getRight() != null) ((AVLNode<E>) x.getRight()).setParent(x);
        y.setParent(x.getParent());
        x.setParent(y);
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    /**
     * Replaces a child reference in the parent node.
     * If parent is null the child is the root so root is updated.
     *
     * @param parent   the parent node
     * @param oldChild the child being replaced
     * @param newChild the new child to set
     */
    private void replaceChild(AVLNode<E> parent, AVLNode<E> oldChild, AVLNode<E> newChild) {
        if (parent == null) {
            root = newChild;
            if (newChild != null) newChild.setParent(null);
        } else if (parent.getLeft() == oldChild) {
            parent.setLeft(newChild);
            if (newChild != null) newChild.setParent(parent);
        } else {
            parent.setRight(newChild);
            if (newChild != null) newChild.setParent(parent);
        }
    }
}