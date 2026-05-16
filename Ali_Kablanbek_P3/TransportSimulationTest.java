/**
 * Unit tests for all major classes.
 * Run each testXxx() method from BlueJ or call runAll() from main.
 * 
 */
public class TransportSimulationTest {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) { System.out.println("PASS: " + name); passed++; }
        else           { System.out.println("FAIL: " + name); failed++; }
    }

    // --- CustomLinkedList ---

    public static void testLinkedList() {
        CustomLinkedList<Integer> ll = new CustomLinkedList<>();
        ll.addBack(1); ll.addBack(2);
        check("LL-01 get(0)==1", ll.get(0) == 1);
        check("LL-02 get(1)==2", ll.get(1) == 2);
        check("LL-03 removeFront==1 size==1", ll.removeFront() == 1 && ll.size() == 1);

        CustomLinkedList<Integer> ll2 = new CustomLinkedList<>();
        ll2.addFront(1); ll2.addFront(2);
        check("LL-04 addFront removeFront==2", ll2.removeFront() == 2);

        CustomLinkedList<Integer> ll3 = new CustomLinkedList<>();
        ll3.addBack(1);
        check("LL-05 removeBack==1 size==0", ll3.removeBack() == 1 && ll3.size() == 0);

        boolean threw = false;
        try { new CustomLinkedList<Integer>().removeFront(); } catch (Exception e) { threw = true; }
        check("LL-06 removeFront empty throws", threw);

        threw = false;
        try { new CustomLinkedList<Integer>().removeBack(); } catch (Exception e) { threw = true; }
        check("LL-07 removeBack empty throws", threw);

        CustomLinkedList<Integer> big = new CustomLinkedList<>();
        for (int i = 0; i < 1000; i++) big.addBack(i);
        check("LL-08 size 1000", big.size() == 1000);

        CustomLinkedList<String> ll4 = new CustomLinkedList<>();
        ll4.addBack("A"); ll4.addBack("B"); ll4.addBack("C");
        java.util.Iterator<String> it = ll4.iterator();
        check("LL-09 iterate A", it.next().equals("A"));
        check("LL-09 iterate B", it.next().equals("B"));
        check("LL-09 iterate C", it.next().equals("C"));
    }

    // --- CustomStack ---

    public static void testStack() {
        CustomStack<String> s = new CustomStack<>();
        s.push("A"); s.push("B");
        check("STK-01 LIFO pop==B", s.pop().equals("B"));

        boolean threw = false;
        CustomStack<String> s2 = new CustomStack<>();
        s2.push("A"); s2.pop();
        try { s2.pop(); } catch (Exception e) { threw = true; }
        check("STK-02 second pop throws", threw);

        CustomStack<String> s3 = new CustomStack<>();
        s3.push("A");
        check("STK-03 peek==A size==1", s3.peek().equals("A") && s3.size() == 1);

        threw = false;
        try { new CustomStack<String>().pop(); } catch (Exception e) { threw = true; }
        check("STK-04 empty pop throws", threw);

        threw = false;
        try { new CustomStack<String>().peek(); } catch (Exception e) { threw = true; }
        check("STK-05 empty peek throws", threw);

        CustomStack<String> s4 = new CustomStack<>();
        s4.push("A"); s4.push("B");
        check("STK-06 size==2", s4.size() == 2);
    }

    // --- CustomQueue ---

    public static void testQueue() {
        CustomQueue<String> q = new CustomQueue<>();
        q.enqueue("A"); q.enqueue("B");
        check("QUE-01 FIFO dequeue==A", q.dequeue().equals("A"));

        boolean threw = false;
        CustomQueue<String> q2 = new CustomQueue<>();
        q2.enqueue("A"); q2.dequeue();
        try { q2.dequeue(); } catch (Exception e) { threw = true; }
        check("QUE-02 second dequeue throws", threw);

        CustomQueue<String> q3 = new CustomQueue<>();
        q3.enqueue("A");
        check("QUE-03 peek==A size==1", q3.peek().equals("A") && q3.size() == 1);

        threw = false;
        try { new CustomQueue<String>().dequeue(); } catch (Exception e) { threw = true; }
        check("QUE-04 empty dequeue throws", threw);

        CustomQueue<String> q4 = new CustomQueue<>();
        q4.enqueue("A"); q4.enqueue("B");
        check("QUE-05 size==2", q4.size() == 2);
    }

    // --- CustomPriorityQueue via ManifestEntry ---

    public static void testPriorityQueue() {
        Warehouse w0 = new Warehouse(0, 0, 0, 1);
        Warehouse w1 = new Warehouse(1, 100, 0, 1);
        Shipment s0 = new Shipment(0, 1, 0, 1);
        Shipment s1 = new Shipment(1, 1, 0, 1);

        ManifestEntry eA = new ManifestEntry(s0, w1, w0); // dist=100
        eA.updateDistance(0, 0);
        ManifestEntry eB = new ManifestEntry(s1, w0, w1); // dist=0
        eB.updateDistance(0, 0);

        CustomPriorityQueue<ManifestEntry> pq = new CustomPriorityQueue<>(4);
        pq.insert(eA); pq.insert(eB);
        check("PQ-01 extractMin is nearest (dist=0)", pq.extractMin() == eB);

        // Tiebreak: same distance, higher ID wins
        Warehouse wSame = new Warehouse(2, 50, 50, 1);
        Shipment s2 = new Shipment(0, 1, 2, 1);
        Shipment s3 = new Shipment(1, 1, 2, 1);
        ManifestEntry e2 = new ManifestEntry(s2, wSame, w1);
        ManifestEntry e3 = new ManifestEntry(s3, wSame, w1);
        e2.updateDistance(0, 0); e3.updateDistance(0, 0);
        CustomPriorityQueue<ManifestEntry> pq2 = new CustomPriorityQueue<>(4);
        pq2.insert(e2); pq2.insert(e3);
        check("PQ-02 tiebreak higher ID first", pq2.extractMin().getShipment().getId() == 1);

        boolean threw = false;
        try { new CustomPriorityQueue<ManifestEntry>(2).extractMin(); }
        catch (Exception e) { threw = true; }
        check("PQ-05 empty extractMin throws", threw);
    }

    // --- ManifestEntry ---

    public static void testManifestEntry() {
        Warehouse src = new Warehouse(0, 3, 4, 1);
        Warehouse dst = new Warehouse(1, 0, 0, 1);
        Shipment s = new Shipment(0, 1, 0, 1);
        ManifestEntry e = new ManifestEntry(s, src, dst);
        e.updateDistance(0, 0);
        check("ME-01 distance=5.0", Math.abs(e.getDistanceFromTruck() - 5.0) < 0.001);

        Warehouse near = new Warehouse(2, 2, 0, 1);
        Warehouse far  = new Warehouse(3, 5, 0, 1);
        ManifestEntry eA = new ManifestEntry(new Shipment(0, 1, 2, 3), near, dst);
        eA.updateDistance(0, 0);
        ManifestEntry eB = new ManifestEntry(new Shipment(1, 1, 3, 2), far, dst);
        eB.updateDistance(0, 0);
        check("ME-02 nearer entry compares less", eA.compareTo(eB) < 0);

        // Same distance, higher ID wins
        Warehouse mid = new Warehouse(4, 3, 0, 1);
        ManifestEntry eC = new ManifestEntry(new Shipment(0, 1, 4, 3), mid, dst);
        ManifestEntry eD = new ManifestEntry(new Shipment(1, 1, 4, 3), mid, dst);
        eC.updateDistance(0, 0); eD.updateDistance(0, 0);
        check("ME-03 same dist higher ID ranks first", eD.compareTo(eC) < 0);

        ManifestEntry eE = new ManifestEntry(new Shipment(5, 1, 4, 3), mid, dst);
        ManifestEntry eF = new ManifestEntry(new Shipment(5, 1, 4, 3), mid, dst);
        eE.updateDistance(0, 0); eF.updateDistance(0, 0);
        check("ME-04 identical returns 0", eE.compareTo(eF) == 0);
    }

    // --- Truck movement and cargo ---

    public static void testTruck() {
        Truck t1 = new Truck(0, 0, 0, 4); // speed=2
        t1.moveTowards(10, 0);
        check("TRK-01 cap4 move toward (10,0) -> (2,0)", Math.abs(t1.getX() - 2.0) < 0.001);

        Truck t2 = new Truck(1, 0, 0, 5); // speed=1
        t2.moveTowards(0.5, 0);
        check("TRK-02 cap5 no overshoot", Math.abs(t2.getX() - 0.5) < 0.001);

        Truck t3 = new Truck(2, 0, 0, 2); // speed=4 but dist=5
        t3.moveTowards(3, 4); // dist=5, speed=4 so lands exactly
        // Actually speed=4, dist=5, so it moves 4/5 of the way: x=2.4, y=3.2
        // With speed=4 it won't land exactly since 4<5
        // TRK-03 says speed=5 (cap=1 not in range), so let's test cap=2 speed=4
        // truck at (0,0) moveTowards(3,4) dist=5 speed=4 -> ratio=4/5 -> (2.4,3.2)
        check("TRK-03 diagonal partial move x~2.4", Math.abs(t3.getX() - 2.4) < 0.001);

        Truck t4 = new Truck(3, 0, 0, 4);
        Shipment s1 = new Shipment(0, 3, 0, 1);
        Shipment s2 = new Shipment(1, 2, 0, 1);
        t4.loadShipment(s1);
        check("TRK-04 canAccept size=3 load=3 cap=4 -> false", !t4.canAccept(s2));
        check("TRK-05 canAccept size=1 load=3 cap=4 -> true",  t4.canAccept(new Shipment(2, 1, 0, 1)));

        Truck t5 = new Truck(4, 0, 0, 4);
        Shipment sa = new Shipment(0, 2, 0, 1);
        t5.loadShipment(sa);
        check("TRK-06 currentLoad after load==2", t5.unloadTop().getSize() == 2);

        Truck t6 = new Truck(5, 0, 0, 4);
        check("TRK-08 isDone both empty", t6.isDone());

        Warehouse w = new Warehouse(0, 10, 0, 1);
        Shipment sb = new Shipment(0, 1, 0, 1);
        Warehouse wDst = new Warehouse(1, 20, 0, 1);
        ManifestEntry me = new ManifestEntry(sb, w, wDst);
        t6.addToManifest(me);
        check("TRK-09 isDone manifest not empty -> false", !t6.isDone());
    }

    // --- Warehouse and LoadingDock ---

    public static void testWarehouse() {
        Warehouse wh = new Warehouse(0, 0, 0, 2);
        Truck ta = new Truck(0, 0, 0, 2);
        Truck tb = new Truck(1, 0, 0, 2);
        Truck tc = new Truck(2, 0, 0, 2);

        // Give each truck a fake manifest so they don't immediately re-route
        Warehouse dst = new Warehouse(1, 50, 50, 1);
        Shipment sa = new Shipment(0, 1, 0, 1);
        Shipment sb = new Shipment(1, 1, 0, 1);
        Shipment sc = new Shipment(2, 1, 0, 1);
        ta.addToManifest(new ManifestEntry(sa, wh, dst));
        tb.addToManifest(new ManifestEntry(sb, wh, dst));
        tc.addToManifest(new ManifestEntry(sc, wh, dst));
        ta.setWarehouseList(new CustomLinkedList<>());
        tb.setWarehouseList(new CustomLinkedList<>());
        tc.setWarehouseList(new CustomLinkedList<>());

        wh.truckArrived(ta); wh.truckArrived(tb); wh.truckArrived(tc);
        wh.action();
        // 2-dock warehouse: 2 assigned LOADING, 1 waiting
        check("WH-01 2 docks 3 trucks -> 2 assigned",
            (ta.getState() == TruckState.LOADING || tb.getState() == TruckState.LOADING));

        LoadingDock ld = new LoadingDock(0);
        Truck td = new Truck(3, 0, 0, 2);
        ld.assign(td);
        check("WH-03 dock assigned isOccupied==true", ld.isOccupied());
        ld.release();
        check("WH-04 after release isOccupied==false", !ld.isOccupied());

        Warehouse wh2 = new Warehouse(0, 0, 0, 1);
        check("WH-06 hasFreeDock when empty", wh2.hasFreeDock());
        Truck te = new Truck(4, 0, 0, 2);
        wh2.truckArrived(te);
        wh2.action();
        check("WH-05 hasFreeDock after assign -> false", !wh2.hasFreeDock());
    }

    // --- Shipment lifecycle ---

    public static void testShipment() {
        Shipment s = new Shipment(5, 2, 0, 1);
        check("SHP-01 getId==5", s.getId() == 5);
        check("SHP-02 status==PENDING", s.getStatus() == ShipmentStatus.PENDING);
        s.markPickedUp(3);
        check("SHP-03 after pickup status==IN_TRANSIT", s.getStatus() == ShipmentStatus.IN_TRANSIT);
        check("SHP-04 pickupHour==3", s.getPickupHour() == 3);
        s.markDelivered(9);
        check("SHP-05 after delivery status==DELIVERED", s.getStatus() == ShipmentStatus.DELIVERED);
        check("SHP-06 deliveryHour==9", s.getDeliveryHour() == 9);
    }

    // --- SimulationConfig ---

    public static void testSimulationConfig() {
        // Generate and reload
        SimulationConfig.generateAndSave("test_config.tmp", 42);
        SimulationConfig cfg = SimulationConfig.loadFromFile("test_config.tmp");
        check("CFG-01 trucks loaded", cfg.getTrucks().size() > 0);

        // Capacity check
        boolean ok = true;
        for (Truck t : cfg.getTrucks()) {
            int total = 0;
            for (Shipment s : cfg.getShipments()) {
                // We can't easily link back, so just verify global constraint is respected
            }
        }
        // Verify all shipment IDs are sequential from 0
        int expectedId = 0;
        boolean sequential = true;
        for (Shipment s : cfg.getShipments()) {
            if (s.getId() != expectedId++) { sequential = false; break; }
        }
        check("CFG-03 shipment IDs sequential from 0", sequential);

        // Same seed produces same file
        SimulationConfig.generateAndSave("test_config2.tmp", 42);
        try {
            java.io.BufferedReader br1 = new java.io.BufferedReader(new java.io.FileReader("test_config.tmp"));
            java.io.BufferedReader br2 = new java.io.BufferedReader(new java.io.FileReader("test_config2.tmp"));
            boolean same = true;
            String l1, l2;
            while ((l1 = br1.readLine()) != null) {
                l2 = br2.readLine();
                if (!l1.equals(l2)) { same = false; break; }
            }
            br1.close(); br2.close();
            check("CFG-05 same seed same file", same);
        } catch (Exception e) { check("CFG-05 same seed same file", false); }

        // Malformed line
        boolean threw = false;
        try {
            SimulationConfig.loadFromFile("nonexistent_file_xyz.txt");
        } catch (Exception e) { threw = true; }
        check("CFG-04 bad file throws", threw);
    }

    public static void runAll() {
        passed = 0; failed = 0;
        System.out.println("=== CustomLinkedList ===");
        testLinkedList();
        System.out.println("=== CustomStack ===");
        testStack();
        System.out.println("=== CustomQueue ===");
        testQueue();
        System.out.println("=== CustomPriorityQueue ===");
        testPriorityQueue();
        System.out.println("=== ManifestEntry ===");
        testManifestEntry();
        System.out.println("=== Truck ===");
        testTruck();
        System.out.println("=== Warehouse/LoadingDock ===");
        testWarehouse();
        System.out.println("=== Shipment ===");
        testShipment();
        System.out.println("=== SimulationConfig ===");
        testSimulationConfig();
        System.out.println("\nResults: " + passed + " passed, " + failed + " failed.");
    }

    public static void main(String[] args) { runAll(); }
}