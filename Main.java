import java.io.File;

/**
 * Entry point for the transport simulation.
 * Usage: java Main [seed]
 * If config.txt exists it is reused; otherwise a new world is generated.
 * @author Ali Kablanbek
 * @version 5/15
 */
public class Main {

    public static void main(String[] args) {
        long seed = 42;
        if (args.length > 0) {
            try { seed = Long.parseLong(args[0]); }
            catch (NumberFormatException e) {
                System.err.println("Invalid seed, using default 42.");
            }
        }

        String configPath = "config.txt";
        if (!new File(configPath).exists()) {
            System.out.println("Generating new world with seed " + seed + "...");
            SimulationConfig.generateAndSave(configPath, seed);
        } else {
            System.out.println("Loading existing config from " + configPath);
        }

        SimulationConfig cfg = SimulationConfig.loadFromFile(configPath);
        Logger.reset("simulation.log");
        CSVWriter.reset("simulation_data.csv");

        SimClock clock = new SimClock(cfg.getTrucks(), cfg.getWarehouses());
        clock.run();
    }
}