/**
 * Models all truck behavior: movement, loading, and unloading.
 * Implements Schedule so SimClock drives it each hour.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class Truck implements Schedule {

    private final int id;
    private double x;
    private double y;
    private final int capacity;
    private final double speed;
    private int currentLoad;
    private TruckState state;
    private Warehouse targetWarehouse;
    private int assignedDock;
    private SimClock clock;
    private CustomLinkedList<Warehouse> warehouseList;

    private final CustomStack<Shipment> cargo;
    private final CustomPriorityQueue<ManifestEntry> manifest;

    public Truck(int id, double x, double y, int capacity) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
        this.speed = 6 - capacity; // cap5->1mph, cap4->2mph, cap3->3mph, cap2->4mph
        this.state = TruckState.IDLE;
        this.cargo = new CustomStack<>();
        this.manifest = new CustomPriorityQueue<>(16);
    }

    public void setClock(SimClock clock) { this.clock = clock; }
    public void setWarehouseList(CustomLinkedList<Warehouse> wl) { this.warehouseList = wl; }

    @Override
    public void action() {
        int hour = (clock != null) ? clock.getCurrentHour() : 0;
        switch (state) {
            case IDLE:
                if (!manifest.isEmpty()) {
                    reorderManifest();
                    targetWarehouse = manifest.peek().getSource();
                    state = TruckState.MOVING;
                }
                break;

            case MOVING:
                moveTowards(targetWarehouse.getX(), targetWarehouse.getY());
                if (x == targetWarehouse.getX() && y == targetWarehouse.getY()) {
                    targetWarehouse.truckArrived(this);
                    state = TruckState.WAITING;
                }
                break;

            case WAITING:
                // Warehouse.action() promotes this truck on its turn
                break;

            case LOADING: {
                if (!manifest.isEmpty() && manifest.peek().getSource() == targetWarehouse) {
                    ManifestEntry entry = manifest.extractMin();
                    Shipment s = entry.getShipment();
                    loadShipment(s);
                    s.markPickedUp(hour);
                    Logger.getInstance().log(hour, "TRUCK-" + id,
                        "LOADING dock=" + assignedDock + " shipment=" + s.getId()
                        + " size=" + s.getSize() + " cargo=" + currentLoad + "/" + capacity);
                    CSVWriter.getInstance().write(hour, "TRUCK", id, "LOADING",
                        "loaded shipment " + s.getId() + " size " + s.getSize()
                        + " at WH-" + targetWarehouse.getId());
                    Logger.getInstance().log(hour, "SHIPMENT-" + s.getId(),
                        "IN_TRANSIT truck=" + id + " src=" + s.getSourceWarehouseId()
                        + " dst=" + s.getDestWarehouseId() + " size=" + s.getSize());
                    CSVWriter.getInstance().write(hour, "SHIPMENT", s.getId(), "IN_TRANSIT",
                        "WH-" + s.getSourceWarehouseId() + " to WH-" + s.getDestWarehouseId());

                    boolean moreHere = !manifest.isEmpty()
                        && manifest.peek().getSource() == targetWarehouse;
                    if (!moreHere) {
                        targetWarehouse.releaseDock(assignedDock);
                        transitionAfterLoading();
                    }
                } else {
                    targetWarehouse.releaseDock(assignedDock);
                    transitionAfterLoading();
                }
                break;
            }

            case UNLOADING: {
                Shipment top = unloadTop();
                top.markDelivered(hour);
                int transit = top.getDeliveryHour() - top.getPickupHour();
                Logger.getInstance().log(hour, "TRUCK-" + id,
                    "UNLOADING dock=" + assignedDock + " shipment=" + top.getId()
                    + " dest=WH-" + top.getDestWarehouseId() + " delivered_hr=" + hour);
                CSVWriter.getInstance().write(hour, "TRUCK", id, "UNLOADING",
                    "delivered shipment " + top.getId() + " at WH-" + top.getDestWarehouseId());
                Logger.getInstance().log(hour, "SHIPMENT-" + top.getId(),
                    "DELIVERED truck=" + id + " pickup_hr=" + top.getPickupHour()
                    + " delivery_hr=" + hour + " transit=" + transit + "h");
                CSVWriter.getInstance().write(hour, "SHIPMENT", top.getId(), "DELIVERED",
                    "WH-" + top.getSourceWarehouseId() + " to WH-" + top.getDestWarehouseId()
                    + " pickup_hr=" + top.getPickupHour() + " delivery_hr=" + hour);

                boolean moreHere = !cargo.isEmpty()
                    && cargo.peek().getDestWarehouseId() == targetWarehouse.getId();
                if (!moreHere) {
                    targetWarehouse.releaseDock(assignedDock);
                    transitionAfterUnloading();
                }
                break;
            }
        }
    }

    private void transitionAfterLoading() {
        if (!cargo.isEmpty()) {
            Warehouse dst = findWarehouse(cargo.peek().getDestWarehouseId());
            if (dst != null) { targetWarehouse = dst; state = TruckState.MOVING; return; }
        }
        if (!manifest.isEmpty()) {
            reorderManifest();
            targetWarehouse = manifest.peek().getSource();
            state = TruckState.MOVING;
        } else {
            state = TruckState.IDLE;
        }
    }

    private void transitionAfterUnloading() {
        if (!manifest.isEmpty()) {
            reorderManifest();
            targetWarehouse = manifest.peek().getSource();
            state = TruckState.MOVING;
        } else if (!cargo.isEmpty()) {
            Warehouse dst = findWarehouse(cargo.peek().getDestWarehouseId());
            if (dst != null) { targetWarehouse = dst; state = TruckState.MOVING; return; }
        } else {
            state = TruckState.IDLE;
        }
    }

    @Override
    public void logStatus() {
        int hour = (clock != null) ? clock.getCurrentHour() : 0;
        Logger log = Logger.getInstance();
        CSVWriter csv = CSVWriter.getInstance();
        switch (state) {
            case MOVING:
                double dist = distanceTo(targetWarehouse.getX(), targetWarehouse.getY());
                log.log(hour, "TRUCK-" + id,
                    String.format("MOVING pos=(%.2f,%.2f) dest=(%.2f,%.2f) dist=%.2f",
                        x, y, targetWarehouse.getX(), targetWarehouse.getY(), dist));
                csv.write(hour, "TRUCK", id, "MOVING",
                    String.format("pos=(%.2f,%.2f) heading to WH-%d", x, y, targetWarehouse.getId()));
                break;
            case WAITING:
                log.log(hour, "TRUCK-" + id,
                    String.format("WAITING pos=(%.2f,%.2f)", x, y));
                csv.write(hour, "TRUCK", id, "WAITING",
                    "queued at WH-" + targetWarehouse.getId());
                break;
            case LOADING:
                log.log(hour, "TRUCK-" + id,
                    "LOADING cargo=" + currentLoad + "/" + capacity);
                csv.write(hour, "TRUCK", id, "LOADING",
                    "cargo=" + currentLoad + "/" + capacity + " at WH-" + targetWarehouse.getId());
                break;
            case UNLOADING:
                log.log(hour, "TRUCK-" + id,
                    "UNLOADING cargo=" + currentLoad + "/" + capacity);
                csv.write(hour, "TRUCK", id, "UNLOADING",
                    "unloading at WH-" + targetWarehouse.getId());
                break;
            case IDLE:
                log.log(hour, "TRUCK-" + id,
                    "IDLE manifest_remaining=" + manifest.size() + " cargo=" + currentLoad);
                csv.write(hour, "TRUCK", id, "IDLE",
                    "manifest_remaining=" + manifest.size() + " cargo=" + currentLoad);
                break;
        }
    }

    /** Moves one hour toward (tx, ty) with no overshoot. */
    public void moveTowards(double tx, double ty) {
        double dx = tx - x, dy = ty - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist <= speed) { x = tx; y = ty; }
        else { double r = speed / dist; x += dx * r; y += dy * r; }
    }

    public double distanceTo(double tx, double ty) {
        double dx = tx - x, dy = ty - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void addToManifest(ManifestEntry e) {
        e.updateDistance(x, y);
        manifest.insert(e);
    }

    /** Updates all entry distances then rebuilds the heap. */
    public void reorderManifest() {
        int sz = manifest.size();
        ManifestEntry[] entries = new ManifestEntry[sz];
        for (int i = 0; i < sz; i++) entries[i] = manifest.extractMin();
        for (ManifestEntry e : entries) { e.updateDistance(x, y); manifest.insert(e); }
    }

    public void loadShipment(Shipment s) { cargo.push(s); currentLoad += s.getSize(); }

    public Shipment unloadTop() {
        Shipment s = cargo.pop();
        currentLoad -= s.getSize();
        return s;
    }

    public boolean canAccept(Shipment s) { return (currentLoad + s.getSize()) <= capacity; }
    public boolean isDone() { return manifest.isEmpty() && cargo.isEmpty(); }
    public boolean hasCargo() { return !cargo.isEmpty(); }
    public Shipment peekCargo() { return cargo.peek(); }

    public void setState(TruckState state) { this.state = state; }
    public TruckState getState() { return state; }
    public void setAssignedDock(int dockId) { this.assignedDock = dockId; }
    public int getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getCapacity() { return capacity; }
    public double getSpeed() { return speed; }

    private Warehouse findWarehouse(int whId) {
        if (warehouseList == null) return null;
        for (Warehouse w : warehouseList) if (w.getId() == whId) return w;
        return null;
    }
}