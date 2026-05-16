/**
 * Immutable record of a single shipment and its routing info.
 * Tracks its own lifecycle for logging and CSV output.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class Shipment {

    private final int id;
    private final int size;
    private final int sourceWarehouseId;
    private final int destWarehouseId;
    private ShipmentStatus status;
    private int pickupHour;
    private int deliveryHour;

    public Shipment(int id, int size, int srcId, int dstId) {
        this.id = id;
        this.size = size;
        this.sourceWarehouseId = srcId;
        this.destWarehouseId = dstId;
        this.status = ShipmentStatus.PENDING;
    }

    public void markPickedUp(int hour) {
        this.status = ShipmentStatus.IN_TRANSIT;
        this.pickupHour = hour;
    }

    public void markDelivered(int hour) {
        this.status = ShipmentStatus.DELIVERED;
        this.deliveryHour = hour;
    }

    /** Writes current shipment state to log and CSV. */
    public void logStatus(int hour) {
        Logger log = Logger.getInstance();
        CSVWriter csv = CSVWriter.getInstance();
        if (status == ShipmentStatus.IN_TRANSIT) {
            log.log(hour, "SHIPMENT-" + id,
                "IN_TRANSIT truck=? src=" + sourceWarehouseId + " dst=" + destWarehouseId + " size=" + size);
            csv.write(hour, "SHIPMENT", id, "IN_TRANSIT",
                "WH-" + sourceWarehouseId + " to WH-" + destWarehouseId + " size=" + size);
        } else if (status == ShipmentStatus.DELIVERED) {
            int transit = deliveryHour - pickupHour;
            log.log(hour, "SHIPMENT-" + id,
                "DELIVERED pickup_hr=" + pickupHour + " delivery_hr=" + deliveryHour + " transit=" + transit + "h");
            csv.write(hour, "SHIPMENT", id, "DELIVERED",
                "WH-" + sourceWarehouseId + " to WH-" + destWarehouseId
                + " pickup_hr=" + pickupHour + " delivery_hr=" + deliveryHour);
        } else {
            log.log(hour, "SHIPMENT-" + id, "PENDING src=" + sourceWarehouseId + " dst=" + destWarehouseId);
            csv.write(hour, "SHIPMENT", id, "PENDING", "awaiting pickup at WH-" + sourceWarehouseId);
        }
    }

    public int getId() { return id; }
    public int getSize() { return size; }
    public ShipmentStatus getStatus() { return status; }
    public int getPickupHour() { return pickupHour; }
    public int getDeliveryHour() { return deliveryHour; }
    public int getSourceWarehouseId() { return sourceWarehouseId; }
    public int getDestWarehouseId() { return destWarehouseId; }
}