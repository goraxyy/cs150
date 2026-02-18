
/**
 * Represents a customer review with a customer name and their latest review
 * Two CustomerReview objects are considered equal if they have the same customer name
 * 
 * @author Ali Kablanbek
 * @version 2.17.2026
 *
 */

public class CustomerReview {
    private String customerName;
    private String latestReview;
    
    /**
     * Creates a new CustomerReview with the specified customer name and review text.
     * 
     * @param customerName the name of the customer who wrote the review
     * @param latestReview the text content of the customer's latest review
     */
    public CustomerReview(String customerName, String latestReview) {
        this.customerName = customerName;
        this.latestReview = latestReview;
    }
    
    /**
     * Gets the customer name.
     * 
     * @return the name of the customer who wrote this review
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /**
     * Gets the latest review text.
     * 
     * @return the text content of the customer's latest review
     */
    public String getLatestReview() {
        return latestReview;
    }
    
    /**
     * Checks if this CustomerReview is equal to another object.
     * Two CustomerReview objects are considered equal if they have the same customer name,
     * regardless of the review text content.
     * 
     * @param obj the object to compare with this CustomerReview
     * @return true if obj is a CustomerReview with the same customer name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomerReview other = (CustomerReview) obj;
        return customerName.equals(other.customerName);
    }
    
    /**
     * Returns the hash code for this CustomerReview.
     * The hash code is computed based on the customer name only, which is
     * consistent with the equals() method implementation.
     * 
     * @return the hash code value for this CustomerReview
     */
    @Override
    public int hashCode() {
        return customerName.hashCode();
    }
    
    /**
     * Returns a string representation of this CustomerReview.
     * The format is: "Name: [customerName] Review: [latestReview]"
     * For example: "Name: Bob Review: It was good"
     * 
     * @return a formatted string containing the customer name and review text
     */
    @Override
    public String toString() {
        return "Name: " + customerName + " Review: " + latestReview;
    }
}