import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Singleton that centralizes all writes to simulation.log.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class Logger {

    private static Logger instance;
    private PrintWriter writer;
    private boolean closed = false;

    private Logger(String filePath) {
        try { writer = new PrintWriter(new FileWriter(filePath, false)); }
        catch (IOException e) { throw new RuntimeException("Cannot open log file: " + filePath, e); }
    }

    public static Logger getInstance() {
        if (instance == null) instance = new Logger("simulation.log");
        return instance;
    }

    /** Reinitializes the singleton (used for testing or new runs). */
    public static void reset(String filePath) {
        if (instance != null) instance.close();
        instance = new Logger(filePath);
    }

    /** Format: [HOUR HHHH] [entity] msg */
    public void log(int hour, String entity, String msg) {
        if (closed) throw new IllegalStateException("Logger is closed");
        writer.printf("[HOUR %04d] [%s] %s%n", hour, entity, msg);
        writer.flush();
    }

    public void close() {
        if (!closed) { writer.flush(); writer.close(); closed = true; }
    }
}