/**
 * One pickup job in a truck's manifest.
 * Orders by distance ascending then shipment ID descending.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class ManifestEntry implements Comparable<ManifestEntry> {

    private final Shipment shipment;
    private final Warehouse source;
    private final Warehouse destination;
    private double distanceFromTruck;

    public ManifestEntry(Shipment shipment, Warehouse src, Warehouse dst) {
        this.shipment = shipment;
        this.source = src;
        this.destination = dst;
    }

    /** Recomputes distanceFromTruck from the truck's current position. */
    public void updateDistance(double tx, double ty) {
        double dx = source.getX() - tx;
        double dy = source.getY() - ty;
        distanceFromTruck = Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Distance ascending; on tie, higher shipment ID wins (newer = higher priority).
     */
    @Override
    public int compareTo(ManifestEntry other) {
        if (this.distanceFromTruck < other.distanceFromTruck) return -1;
        if (this.distanceFromTruck > other.distanceFromTruck) return 1;
        if (this.shipment.getId() > other.shipment.getId()) return -1;
        if (this.shipment.getId() < other.shipment.getId()) return 1;
        return 0;
    }

    public Shipment getShipment() { return shipment; }
    public Warehouse getSource() { return source; }
    public Warehouse getDestination() { return destination; }
    public double getDistanceFromTruck() { return distanceFromTruck; }
}