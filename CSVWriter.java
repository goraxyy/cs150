import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Singleton that centralizes all writes to simulation_data.csv.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class CSVWriter {

    private static CSVWriter instance;
    private PrintWriter writer;
    private boolean closed = false;

    private CSVWriter(String filePath) {
        try {
            writer = new PrintWriter(new FileWriter(filePath, false));
            writer.println("hour,entity_type,entity_id,event,detail");
            writer.flush();
        } catch (IOException e) { throw new RuntimeException("Cannot open CSV file: " + filePath, e); }
    }

    public static CSVWriter getInstance() {
        if (instance == null) instance = new CSVWriter("simulation_data.csv");
        return instance;
    }

    public static void reset(String filePath) {
        if (instance != null) instance.close();
        instance = new CSVWriter(filePath);
    }

    public void write(int hour, String entityType, int entityId, String event, String detail) {
        if (closed) throw new IllegalStateException("CSVWriter is closed");
        writer.printf("%d,%s,%d,%s,\"%s\"%n", hour, entityType, entityId, event, detail);
        writer.flush();
    }

    public void close() {
        if (!closed) { writer.flush(); writer.close(); closed = true; }
    }
}