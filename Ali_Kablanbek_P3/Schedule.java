/**
 * Implemented by Truck and Warehouse.
 * Allows SimClock to drive all entities uniformly each hour.
 * 
 * @author Ali Kablanbek
 * @version 5/15
 */
public interface Schedule {
    void action();
    void logStatus();
}