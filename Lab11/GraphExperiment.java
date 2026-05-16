import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Reads a graph from a file and prints each node's closest neighbor
 * in BFS order starting from the first node listed.
 * @author Ali Kablanbek
 * @version 5/5/26
 */
public class GraphExperiment {

    /**
     * Entry point. Expects a filename as the first command-line argument.
     * File format: first line lists node names, subsequent lines list edges as "src dst weight"
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java GraphExperiment <filename>");
            return;
        }

        DirectedGraph<String> graph = new DirectedGraph<>();
        String firstNode = null;

        try {
            Scanner sc = new Scanner(new File(args[0]));

            if (sc.hasNextLine()) {
                String[] nodeNames = sc.nextLine().trim().split("\\s+");
                for (String name : nodeNames) {
                    graph.addNode(name);
                }
                if (nodeNames.length > 0) firstNode = nodeNames[0];
            }

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                if (parts.length == 3) {
                    graph.addEdge(parts[0], parts[1], Integer.parseInt(parts[2]));
                }
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + args[0]);
            return;
        }

        if (firstNode != null) {
            graph.breadthFirstClosest(firstNode);
        }
    }
}