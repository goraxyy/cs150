/**
 * Central clock object that drives the simulation.
 * Iterates over all Schedule objects using custom linked lists.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class SimClock {

    private int currentHour;
    private final CustomLinkedList<Truck> trucks;
    private final CustomLinkedList<Warehouse> warehouses;

    public SimClock(CustomLinkedList<Truck> trucks, CustomLinkedList<Warehouse> warehouses) {
        this.trucks = trucks;
        this.warehouses = warehouses;
        this.currentHour = 0;
        for (Truck t : trucks) t.setClock(this);
        for (Warehouse w : warehouses) w.setClock(this);
    }

    /** Runs until all trucks have completed their manifests. */
    public void run() {
        System.out.println("Simulation starting.");
        while (!isSimulationComplete()) tick();
        System.out.println("Simulation complete at hour " + currentHour + ".");
        Logger.getInstance().close();
        CSVWriter.getInstance().close();
    }

    /**
     * Advances one hour. Warehouses act first so docks are assigned
     * before trucks try to load or unload.
     */
    public void tick() {
        currentHour++;
        for (Warehouse w : warehouses) { w.action(); w.logStatus(); }
        for (Truck t : trucks) { t.action(); t.logStatus(); }
    }

    /** Returns true when every truck has an empty manifest and empty cargo. */
    public boolean isSimulationComplete() {
        for (Truck t : trucks) if (!t.isDone()) return false;
        return true;
    }

    public int getCurrentHour() { return currentHour; }
}