/**
 * A warehouse with 1-3 loading docks.
 * Implements Schedule so SimClock calls action() each hour.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class Warehouse implements Schedule {

    private final int id;
    private final double x;
    private final double y;
    private final LoadingDock[] docks;
    private final CustomQueue<Truck> arrivalQueue;
    private SimClock clock;

    public Warehouse(int id, double x, double y, int numDocks) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.docks = new LoadingDock[numDocks];
        for (int i = 0; i < numDocks; i++) docks[i] = new LoadingDock(i);
        this.arrivalQueue = new CustomQueue<>();
    }

    public void setClock(SimClock clock) { this.clock = clock; }

    /**
     * Each hour: assign a waiting truck to each free dock.
     * Decide LOADING vs UNLOADING based on top cargo destination.
     */
    @Override
    public void action() {
        for (LoadingDock dock : docks) {
            if (dock.isOccupied() || arrivalQueue.isEmpty()) continue;
            Truck truck = arrivalQueue.dequeue();
            dock.assign(truck);
            truck.setAssignedDock(dock.getId());
            if (truck.hasCargo() && truck.peekCargo().getDestWarehouseId() == this.id) {
                truck.setState(TruckState.UNLOADING);
            } else {
                truck.setState(TruckState.LOADING);
            }
        }
    }

    /** Writes dock and queue state to log and CSV. */
    @Override
    public void logStatus() {
        int hour = (clock != null) ? clock.getCurrentHour() : 0;
        Logger log = Logger.getInstance();
        CSVWriter csv = CSVWriter.getInstance();

        // Snapshot queue IDs without destroying it
        int qsize = arrivalQueue.size();
        Truck[] tmp = new Truck[qsize];
        for (int i = 0; i < qsize; i++) tmp[i] = arrivalQueue.dequeue();
        StringBuilder queueStr = new StringBuilder("[");
        for (int i = 0; i < qsize; i++) {
            if (i > 0) queueStr.append(",");
            queueStr.append(tmp[i].getId());
            arrivalQueue.enqueue(tmp[i]);
        }
        queueStr.append("]");

        log.log(hour, "WAREHOUSE-" + id, "QUEUE length=" + qsize + " trucks=" + queueStr);
        csv.write(hour, "WAREHOUSE", id, "QUEUE", "length=" + qsize + " trucks=" + queueStr);

        StringBuilder dockStr = new StringBuilder();
        for (LoadingDock dock : docks) {
            if (dockStr.length() > 0) dockStr.append(" ");
            dockStr.append("dock=").append(dock.getId()).append(" ");
            if (dock.isOccupied()) dockStr.append("truck=").append(dock.getCurrentTruck().getId());
            else dockStr.append("FREE");
        }
        log.log(hour, "WAREHOUSE-" + id, "DOCK_BUSY " + dockStr);
        csv.write(hour, "WAREHOUSE", id, "DOCK_STATUS", dockStr.toString());
    }

    public void truckArrived(Truck t) { arrivalQueue.enqueue(t); }

    public boolean hasFreeDock() {
        for (LoadingDock dock : docks) if (!dock.isOccupied()) return true;
        return false;
    }

    public void releaseDock(int dockId) { docks[dockId].release(); }

    public int getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getNumDocks() { return docks.length; }
}