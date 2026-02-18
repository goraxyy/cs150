
/**
 * Unit tests for CustomerReview, VIPCustomerReview, and CustomerReviewArrayList
 * 
 * @author  ALi Kablanbek
 * @version 2.17.2026
 */

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CustomerReviewTest {
    private CustomerReview review1;
    private CustomerReview review2;
    private CustomerReview review3;
    private VIPCustomerReview vipReview1;
    private VIPCustomerReview vipReview2;
    private CustomerReviewArrayList list1;
    private CustomerReviewArrayList list2;
    
    /**
     * Sets up test fixtures before each test method.
     * Creates sample CustomerReview, VIPCustomerReview, and CustomerReviewArrayList objects
     * to be used across multiple test cases.
     */
    @Before
    public void setUp() {
        review1 = new CustomerReview("Bob", "It was good");
        review2 = new CustomerReview("Bob", "It was excellent");
        review3 = new CustomerReview("Alice", "Great product");
        vipReview1 = new VIPCustomerReview("Bob", "It was good");
        vipReview2 = new VIPCustomerReview("Charlie", "Amazing");
        
        list1 = new CustomerReviewArrayList();
        list2 = new CustomerReviewArrayList();
    }
    
    /**
     * Tests that the CustomerReview getter methods return the correct values.
     */
    @Test
    public void testCustomerReviewGetters() {
        assertEquals("Bob", review1.getCustomerName());
        assertEquals("It was good", review1.getLatestReview());
    }
    
    /**
     * Tests that the CustomerReview toString() method returns the correct format.
     */
    @Test
    public void testCustomerReviewToString() {
        assertEquals("Name: Bob Review: It was good", review1.toString());
    }
    
    /**
     * Tests that two CustomerReview objects with the same customer name are equal,
     * even if they have different review text.
     */
    @Test
    public void testCustomerReviewEqualsSameName() {
        assertTrue(review1.equals(review2));
    }
    
    /**
     * Tests that two CustomerReview objects with different customer names are not equal.
     */
    @Test
    public void testCustomerReviewEqualsDifferentName() {
        assertFalse(review1.equals(review3));
    }
    
    /**
     * Tests that two CustomerReview objects with the same customer name have the same hash code.
     */
    @Test
    public void testCustomerReviewHashCodeSameName() {
        assertEquals(review1.hashCode(), review2.hashCode());
    }
    
    /**
     * Tests that two CustomerReview objects with different customer names have different hash codes.
     */
    @Test
    public void testCustomerReviewHashCodeDifferentName() {
        assertNotEquals(review1.hashCode(), review3.hashCode());
    }
    
    /**
     * Tests that the VIPCustomerReview getter methods return the correct values.
     */
    @Test
    public void testVIPCustomerReviewGetters() {
        assertEquals("Bob", vipReview1.getCustomerName());
        assertEquals("It was good", vipReview1.getLatestReview());
    }
    
    /**
     * Tests that the VIPCustomerReview toString() method returns the correct format.
     */
    @Test
    public void testVIPCustomerReviewToString() {
        assertEquals("Name: Bob Review: It was good", vipReview1.toString());
    }
    
    /**
     * CRITICAL TEST: Verifies that a CustomerReview and VIPCustomerReview with the same
     * customer name are NOT considered equal, as they are different types.
     */
    @Test
    public void testCustomerReviewNotEqualToVIPCustomerReview() {
        assertFalse(review1.equals(vipReview1));
        assertFalse(vipReview1.equals(review1));
    }
    
    /**
     * Tests that two VIPCustomerReview objects with the same customer name are equal,
     * and that VIPCustomerReview objects with different names are not equal.
     */
    @Test
    public void testVIPCustomerReviewEquals() {
        VIPCustomerReview vip3 = new VIPCustomerReview("Bob", "Different review");
        assertTrue(vipReview1.equals(vip3));
        assertFalse(vipReview1.equals(vipReview2));
    }
    
    /**
     * Tests that the add() and get() methods work correctly, and that size() returns
     * the correct number of elements.
     */
    @Test
    public void testArrayListAddAndGet() {
        list1.add(review1);
        list1.add(review3);
        assertEquals(2, list1.size());
        assertEquals(review1, list1.get(0));
        assertEquals(review3, list1.get(1));
    }
    
    /**
     * Tests that the remove() method correctly removes and returns an element,
     * and updates the size accordingly.
     */
    @Test
    public void testArrayListRemove() {
        list1.add(review1);
        list1.add(review3);
        CustomerReview removed = list1.remove(0);
        assertEquals(review1, removed);
        assertEquals(1, list1.size());
    }
    
    /**
     * Tests that isEmpty() returns true for an empty list and false after adding an element.
     */
    @Test
    public void testArrayListIsEmpty() {
        assertTrue(list1.isEmpty());
        list1.add(review1);
        assertFalse(list1.isEmpty());
    }
    
    /**
     * Tests that two CustomerReviewArrayList objects are equal when they contain
     * CustomerReview objects with the same customer names in the same order.
     */
    @Test
    public void testArrayListEquals() {
        list1.add(review1);
        list1.add(review3);
        list2.add(new CustomerReview("Bob", "It was good"));
        list2.add(new CustomerReview("Alice", "Great product"));
        assertTrue(list1.equals(list2));
    }
    
    /**
     * Tests that two CustomerReviewArrayList objects with different reviews are not equal.
     */
    @Test
    public void testArrayListNotEquals() {
        list1.add(review1);
        list2.add(review3);
        assertFalse(list1.equals(list2));
    }
    
    /**
     * Tests that two CustomerReviewArrayList objects containing the same reviews
     * produce the same hash code.
     */
    @Test
    public void testArrayListHashCode() {
        list1.add(review1);
        list1.add(review3);
        list2.add(review1);
        list2.add(review3);
        assertEquals(list1.hashCode(), list2.hashCode());
    }
    
    /**
     * Tests that the toString() method produces a string containing the review information.
     */
    @Test
    public void testArrayListToString() {
        list1.add(review1);
        String result = list1.toString();
        assertTrue(result.contains("Name: Bob Review: It was good"));
    }
    
    /**
     * Tests that equals() returns false when comparing with null for all classes.
     */
    @Test
    public void testNullEquality() {
        assertFalse(review1.equals(null));
        assertFalse(vipReview1.equals(null));
        assertFalse(list1.equals(null));
    }
    
    /**
     * Tests that equals() returns true when an object is compared with itself (reflexivity).
     */
    @Test
    public void testSelfEquality() {
        assertTrue(review1.equals(review1));
        assertTrue(vipReview1.equals(vipReview1));
        assertTrue(list1.equals(list1));
    }
}