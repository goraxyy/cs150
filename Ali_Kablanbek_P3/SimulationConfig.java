import java.io.*;
import java.util.Random;

/**
 * Generates a random world and saves it, or reads an existing config file.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class SimulationConfig {

    private CustomLinkedList<Truck> trucks;
    private CustomLinkedList<Warehouse> warehouses;
    private CustomLinkedList<Shipment> shipments;

    private SimulationConfig() {
        trucks = new CustomLinkedList<>();
        warehouses = new CustomLinkedList<>();
        shipments = new CustomLinkedList<>();
    }

    /** Generates a world with the given seed and writes it to filePath. */
    public static void generateAndSave(String filePath, long seed) {
        Random rng = new Random(seed);
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath, false))) {
            out.println("# Transport Simulation Configuration");
            out.println("# Generated with seed " + seed);
            out.println("#");
            out.println("# Format:");
            out.println("# WAREHOUSE id x y docks");
            out.println("# TRUCK id x y capacity");
            out.println("# SHIPMENT id size src_wh dst_wh truck_id");
            out.println("#");

            int W = 3 + rng.nextInt(6);  // 3..8
            int T = 3 + rng.nextInt(8);  // 3..10

            double[] wx = new double[W], wy = new double[W];
            int[] wdocks = new int[W];
            for (int i = 0; i < W; i++) {
                wx[i] = Math.round(rng.nextDouble() * 1000.0) / 10.0;
                wy[i] = Math.round(rng.nextDouble() * 1000.0) / 10.0;
                wdocks[i] = 1 + rng.nextInt(3);
                out.printf("WAREHOUSE %d %.2f %.2f %d%n", i, wx[i], wy[i], wdocks[i]);
            }
            out.println("#");

            int[] tcap = new int[T];
            for (int i = 0; i < T; i++) {
                double tx = Math.round(rng.nextDouble() * 1000.0) / 10.0;
                double ty = Math.round(rng.nextDouble() * 1000.0) / 10.0;
                tcap[i] = 2 + rng.nextInt(4);  // 2..5
                out.printf("TRUCK %d %.2f %.2f %d%n", i, tx, ty, tcap[i]);
            }
            out.println("#");

            int shipmentId = 0;
            for (int t = 0; t < T; t++) {
                int usedCap = 0;
                int M = 2 + rng.nextInt(4);  // 2..5 pickups
                for (int j = 0; j < M; j++) {
                    int maxSize = Math.min(3, tcap[t] - usedCap);
                    if (maxSize < 1) break;
                    int size = 1 + rng.nextInt(maxSize);
                    int src = rng.nextInt(W);
                    int dst;
                    do { dst = rng.nextInt(W); } while (dst == src && W > 1);
                    out.printf("SHIPMENT %d %d %d %d %d%n", shipmentId, size, src, dst, t);
                    usedCap += size;
                    shipmentId++;
                    if (usedCap >= tcap[t]) break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot write config file: " + filePath, e);
        }
    }

    /** Reads a config file and constructs all simulation objects. */
    public static SimulationConfig loadFromFile(String filePath) {
        SimulationConfig cfg = new SimulationConfig();
        int lineNum = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\s+");
                switch (parts[0]) {
                    case "WAREHOUSE":
                        if (parts.length < 5)
                            throw new RuntimeException("Malformed WAREHOUSE at line " + lineNum);
                        cfg.warehouses.addBack(new Warehouse(
                            Integer.parseInt(parts[1]),
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            Integer.parseInt(parts[4])));
                        break;
                    case "TRUCK":
                        if (parts.length < 5)
                            throw new RuntimeException("Malformed TRUCK at line " + lineNum);
                        cfg.trucks.addBack(new Truck(
                            Integer.parseInt(parts[1]),
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            Integer.parseInt(parts[4])));
                        break;
                    case "SHIPMENT": {
                        if (parts.length < 6)
                            throw new RuntimeException("Malformed SHIPMENT at line " + lineNum);
                        int sid = Integer.parseInt(parts[1]);
                        int sz  = Integer.parseInt(parts[2]);
                        int src = Integer.parseInt(parts[3]);
                        int dst = Integer.parseInt(parts[4]);
                        int tid = Integer.parseInt(parts[5]);
                        Shipment s = new Shipment(sid, sz, src, dst);
                        cfg.shipments.addBack(s);
                        Truck truck = null;
                        Warehouse srcWh = null, dstWh = null;
                        for (Truck t : cfg.trucks) if (t.getId() == tid) { truck = t; break; }
                        for (Warehouse w : cfg.warehouses) {
                            if (w.getId() == src) srcWh = w;
                            if (w.getId() == dst) dstWh = w;
                        }
                        if (truck == null || srcWh == null || dstWh == null)
                            throw new RuntimeException("Unknown reference at line " + lineNum);
                        truck.addToManifest(new ManifestEntry(s, srcWh, dstWh));
                        truck.setWarehouseList(cfg.warehouses);
                        break;
                    }
                    default:
                        throw new RuntimeException("Unrecognized token '" + parts[0]
                            + "' at line " + lineNum);
                }
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Bad number at line " + lineNum + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read config file: " + filePath, e);
        }
        return cfg;
    }

    public CustomLinkedList<Truck> getTrucks() { return trucks; }
    public CustomLinkedList<Warehouse> getWarehouses() { return warehouses; }
    public CustomLinkedList<Shipment> getShipments() { return shipments; }
}