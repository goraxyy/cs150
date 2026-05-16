import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for BinarySearchTree and AVLTree.
 * 
 * @author Ali Kablanbek
 * @version 4/21/26
 */
public class TreeTest {

    // --- BinarySearchTree tests ---

    @Test
    public void testInsertAndContains() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.insert(10));
        assertTrue(bst.insert(5));
        assertTrue(bst.insert(15));
        assertTrue(bst.contains(10));
        assertTrue(bst.contains(5));
        assertTrue(bst.contains(15));
        assertFalse(bst.contains(99));
    }

    @Test
    public void testNoDuplicates() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.insert(10));
        assertFalse(bst.insert(10));
    }

    @Test
    public void testIsEmpty() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        assertTrue(bst.isEmpty());
        bst.insert(1);
        assertFalse(bst.isEmpty());
    }

    @Test
    public void testEmpty() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(1);
        bst.insert(2);
        bst.empty();
        assertTrue(bst.isEmpty());
    }

    @Test
    public void testInOrderString() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        assertEquals("5 10 15", bst.inOrderString());
    }

    @Test
    public void testPreOrderString() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        assertEquals("10 5 15", bst.preOrderString());
    }

    @Test
    public void testPostOrderString() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        assertEquals("5 15 10", bst.postOrderString());
    }

    @Test
    public void testStringBST() {
        BinarySearchTree<String> bst = new BinarySearchTree<>();
        bst.insert("banana");
        bst.insert("apple");
        bst.insert("cherry");
        assertEquals("apple banana cherry", bst.inOrderString());
    }

    // --- AVLTree tests ---

    @Test
    public void testAVLInsertAndContains() {
        AVLTree<Integer> avl = new AVLTree<>();
        avl.insert(30);
        avl.insert(20);
        avl.insert(10); // triggers right rotation
        assertTrue(avl.contains(10));
        assertTrue(avl.contains(20));
        assertTrue(avl.contains(30));
    }

    @Test
    public void testAVLInOrderIsSorted() {
        AVLTree<Integer> avl = new AVLTree<>();
        int[] vals = {30, 20, 10, 40, 35, 50};
        for (int v : vals) avl.insert(v);
        assertEquals("10 20 30 35 40 50", avl.inOrderString());
    }

    @Test
    public void testAVLNoDuplicates() {
        AVLTree<Integer> avl = new AVLTree<>();
        assertTrue(avl.insert(5));
        assertFalse(avl.insert(5));
    }

    @Test
    public void testAVLEmpty() {
        AVLTree<Integer> avl = new AVLTree<>();
        avl.insert(1);
        avl.empty();
        assertTrue(avl.isEmpty());
    }

    @Test
    public void testAVLLeftRightCase() {
        AVLTree<Integer> avl = new AVLTree<>();
        avl.insert(30);
        avl.insert(10);
        avl.insert(20); // left-right double rotation
        assertTrue(avl.contains(10));
        assertTrue(avl.contains(20));
        assertTrue(avl.contains(30));
        assertEquals("10 20 30", avl.inOrderString());
    }

    @Test
    public void testAVLRightLeftCase() {
        AVLTree<Integer> avl = new AVLTree<>();
        avl.insert(10);
        avl.insert(30);
        avl.insert(20); // right-left double rotation
        assertTrue(avl.contains(10));
        assertTrue(avl.contains(20));
        assertTrue(avl.contains(30));
        assertEquals("10 20 30", avl.inOrderString());
    }
}