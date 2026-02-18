
/**
 * Represents a VIP customer review with a customer name and their latest review.
 * This class has identical functionality to CustomerReview but is a separate type.
 * Two VIPCustomerReview objects are considered equal if they have the same customer name.
 *
 * @author Ali Kablanbek
 * @version 2.17.2026
 */
public class VIPCustomerReview {
    private String customerName;
    private String latestReview;
    
    /**
     * Creates a new VIPCustomerReview with the specified customer name and review text.
     * 
     * @param customerName the name of the VIP customer who wrote the review
     * @param latestReview the text content of the VIP customer's latest review
     */
    public VIPCustomerReview(String customerName, String latestReview) {
        this.customerName = customerName;
        this.latestReview = latestReview;
    }
    
    /**
     * Gets the VIP customer name.
     * 
     * @return the name of the VIP customer who wrote this review
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /**
     * Gets the latest review text.
     * 
     * @return the text content of the VIP customer's latest review
     */
    public String getLatestReview() {
        return latestReview;
    }
    
    /**
     * Checks if this VIPCustomerReview is equal to another object.
     * Two VIPCustomerReview objects are considered equal if they have the same customer name,
     * regardless of the review text content. Note that a VIPCustomerReview will never be
     * equal to a CustomerReview, even if they have the same customer name.
     * 
     * @param obj the object to compare with this VIPCustomerReview
     * @return true if obj is a VIPCustomerReview with the same customer name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VIPCustomerReview other = (VIPCustomerReview) obj;
        return customerName.equals(other.customerName);
    }
    
    /**
     * Returns the hash code for this VIPCustomerReview.
     * The hash code is computed based on the customer name only, which is
     * consistent with the equals() method implementation.
     * 
     * @return the hash code value for this VIPCustomerReview
     */
    @Override
    public int hashCode() {
        return customerName.hashCode();
    }
    
    /**
     * Returns a string representation of this VIPCustomerReview.
     * The format is: "Name: [customerName] Review: [latestReview]"
     * For example: "Name: Bob Review: It was good"
     * 
     * @return a formatted string containing the VIP customer name and review text
     */
    @Override
    public String toString() {
        return "Name: " + customerName + " Review: " + latestReview;
    }
}