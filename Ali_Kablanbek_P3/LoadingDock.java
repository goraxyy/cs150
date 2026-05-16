/**
 * Tracks state of a single dock slot inside a warehouse.
 * Kept separate so each dock independently tracks its assigned truck.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class LoadingDock {

    private final int id;
    private boolean occupied;
    private Truck currentTruck;

    public LoadingDock(int id) { this.id = id; }

    public boolean isOccupied() { return occupied; }

    public void assign(Truck t) { this.currentTruck = t; this.occupied = true; }

    public void release() { this.currentTruck = null; this.occupied = false; }

    public Truck getCurrentTruck() { return currentTruck; }
    public int getId() { return id; }
}