import java.util.ArrayList;

/**
 * A generic directed weighted graph.
 * @param <K> the type of key stored in each node
 * @author Ali Kablanbek
 * @version 5/5/26
 */
public class DirectedGraph<K> {

    private ArrayList<DirectedGraphNode> nodes;

    // -------------------------------------------------------------------------
    // Inner Classes
    // -------------------------------------------------------------------------

    /**
     * A node in the directed graph storing a key and its outgoing edges.
     */
    public class DirectedGraphNode {
        K key;
        ArrayList<DirectedGraphEdge> outgoing;

        /** Creates a node with the given key. */
        public DirectedGraphNode(K key) {
            this.key = key;
            this.outgoing = new ArrayList<>();
        }

        /**
         * Returns the key of the nearest neighbor reachable in one hop
         * (lowest edge weight). Returns null if no outgoing edges exist.
         */
        public K closestNeighbor() {
            if (outgoing.isEmpty()) return null;
            DirectedGraphEdge best = outgoing.get(0);
            for (DirectedGraphEdge e : outgoing) {
                if (e.weight < best.weight) best = e;
            }
            return best.end.key;
        }
    }

    /**
     * A directed weighted edge from one node to another.
     */
    public class DirectedGraphEdge {
        DirectedGraphNode start;
        DirectedGraphNode end;
        int weight;

        /** Creates an edge from start to end with the given weight. */
        public DirectedGraphEdge(DirectedGraphNode start, DirectedGraphNode end, int weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /** Creates an empty directed graph. */
    public DirectedGraph() {
        nodes = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Private Helper
    // -------------------------------------------------------------------------

    // Returns the node with the given key, or null if not found.
    private DirectedGraphNode findNode(K k) {
        for (DirectedGraphNode n : nodes) {
            if (n.key.equals(k)) return n;
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Public Operations
    // -------------------------------------------------------------------------

    /**
     * Adds a node with key k. Does nothing and returns false if k already exists.
     */
    public boolean addNode(K k) {
        if (findNode(k) != null) return false;
        nodes.add(new DirectedGraphNode(k));
        return true;
    }

    /**
     * Adds a directed edge from k1 to k2 with weight w.
     * If the edge already exists, updates its weight.
     * Returns false if either node does not exist.
     */
    public boolean addEdge(K k1, K k2, int w) {
        DirectedGraphNode n1 = findNode(k1);
        DirectedGraphNode n2 = findNode(k2);
        if (n1 == null || n2 == null) return false;
        for (DirectedGraphEdge e : n1.outgoing) {
            if (e.end.key.equals(k2)) {
                e.weight = w;
                return true;
            }
        }
        n1.outgoing.add(new DirectedGraphEdge(n1, n2, w));
        return true;
    }

    /**
     * Returns all keys reachable from k in one hop.
     */
    public ArrayList<K> getNeighbors(K k) {
        ArrayList<K> neighbors = new ArrayList<>();
        DirectedGraphNode n = findNode(k);
        if (n == null) return neighbors;
        for (DirectedGraphEdge e : n.outgoing) {
            neighbors.add(e.end.key);
        }
        return neighbors;
    }

    /**
     * Prints each node's closest neighbor in BFS order starting from k1.
     */
    public void breadthFirstClosest(K k1) {
        DirectedGraphNode start = findNode(k1);
        if (start == null) return;

        ArrayList<DirectedGraphNode> visited = new ArrayList<>();
        ArrayList<DirectedGraphNode> queue = new ArrayList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            DirectedGraphNode current = queue.remove(0);
            K closest = current.closestNeighbor();
            System.out.println(current.key + " " + closest);

            for (DirectedGraphEdge e : current.outgoing) {
                if (!visited.contains(e.end)) {
                    visited.add(e.end);
                    queue.add(e.end);
                }
            }
        }
    }
}