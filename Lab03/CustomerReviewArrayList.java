
/**
 * A specialized ArrayList that stores CustomerReview objects.
 * Provides additional functionality for managing customer reviews.
 *
 * @author Ali Kablanbek
 * @version 2.17.2026
 */

import java.util.ArrayList;

public class CustomerReviewArrayList {
    private ArrayList<CustomerReview> reviews;
    
    /**
     * Creates a new empty CustomerReviewArrayList.
     * The internal ArrayList is initialized but contains no reviews.
     */
    public CustomerReviewArrayList() {
        reviews = new ArrayList<CustomerReview>();
    }
    
    /**
     * Adds a CustomerReview to the end of this list.
     * 
     * @param review the CustomerReview object to be added to the list
     */
    public void add(CustomerReview review) {
        reviews.add(review);
    }
    
    /**
     * Gets the CustomerReview at the specified index.
     * 
     * @param index the index of the CustomerReview to retrieve
     * @return the CustomerReview at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public CustomerReview get(int index) {
        return reviews.get(index);
    }
    
    /**
     * Returns the number of CustomerReview objects in this list.
     * 
     * @return the number of reviews currently stored in the list
     */
    public int size() {
        return reviews.size();
    }
    
    /**
     * Removes the CustomerReview at the specified index from this list.
     * All subsequent reviews are shifted to the left (their indices decrease by one).
     * 
     * @param index the index of the CustomerReview to be removed
     * @return the CustomerReview that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public CustomerReview remove(int index) {
        return reviews.remove(index);
    }
    
    /**
     * Checks if this list contains no CustomerReview objects.
     * 
     * @return true if this list contains no reviews, false otherwise
     */
    public boolean isEmpty() {
        return reviews.isEmpty();
    }
    
    /**
     * Checks if this CustomerReviewArrayList is equal to another object.
     * Two CustomerReviewArrayList objects are equal if they contain the same
     * number of reviews and all corresponding CustomerReview objects are equal
     * according to their equals() method.
     * 
     * @param obj the object to compare with this CustomerReviewArrayList
     * @return true if both lists contain equal reviews in the same order, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomerReviewArrayList other = (CustomerReviewArrayList) obj;
        
        if (reviews.size() != other.reviews.size()) return false;
        
        for (int i = 0; i < reviews.size(); i++) {
            if (!reviews.get(i).equals(other.reviews.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns the hash code for this CustomerReviewArrayList.
     * The hash code is computed as the sum of all hash codes of the
     * CustomerReview objects contained in the list.
     * 
     * @return the sum of hash codes of all reviews in this list
     */
    @Override
    public int hashCode() {
        int sum = 0;
        for (CustomerReview review : reviews) {
            sum += review.hashCode();
        }
        return sum;
    }
    
    /**
     * Returns a string representation of this CustomerReviewArrayList.
     * The format includes all CustomerReview objects, each on a separate line,
     * enclosed in brackets with proper indentation.
     * 
     * @return a formatted string containing all customer reviews in the list
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CustomerReviewArrayList [\n");
        for (CustomerReview review : reviews) {
            sb.append("  ").append(review.toString()).append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
}