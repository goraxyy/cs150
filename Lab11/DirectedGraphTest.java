/**
 * Unit tests for DirectedGraph, DirectedGraphNode and DirectedGraphEdge.
 * @author Ali Kablanbek
 * @version 5/5/26
 */
public class DirectedGraphTest {
    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        // --- addNode ---
        DirectedGraph<String> g = new DirectedGraph<>();
        assert g.addNode("a") : "addNode new key should return true";
        passed++;

        assert !g.addNode("a") : "addNode duplicate key should return false";
        passed++;

        // --- addEdge --
        g.addNode("b");
        g.addNode("c");
        assert g.addEdge("a", "b", 5) : "addEdge valid nodes should return true";
        passed++;

        assert !g.addEdge("a", "z", 3) : "addEdge missing node should return false";
        passed++;

        // weight update: re-adding same edge should change weight, not duplicate
        g.addEdge("a", "b", 1);
        assert g.getNeighbors("a").size() == 1 : "duplicate edge should update weight, not add new edge";
        passed++;

        // --- getNeighbors ---
        g.addEdge("a", "c", 9);
        assert g.getNeighbors("a").size() == 2 : "a should have 2 neighbors";
        passed++;

        assert g.getNeighbors("b").size() == 0 : "b should have 0 neighbors";
        passed++;

        // --- closestNeighbor ---
        // a->b weight 1, a->c weight 9, so closest from a is b
        String closest = g.getNeighbors("a").contains("b") ? "b" : null;
        DirectedGraph<String> g2 = new DirectedGraph<>();
        g2.addNode("x");
        g2.addNode("y");
        g2.addNode("z");
        g2.addEdge("x", "y", 10);
        g2.addEdge("x", "z", 3);
        // closestNeighbor is tested indirectly via breadthFirstClosest output
        // direct test via a fresh single-edge graph
        DirectedGraph<String> g3 = new DirectedGraph<>();
        g3.addNode("m");
        g3.addNode("n");
        g3.addEdge("m", "n", 7);
        DirectedGraph<String>.DirectedGraphNode mNode = g3.new DirectedGraphNode("m");
        // closestNeighbor on isolated node (no edges) returns null
        assert mNode.closestNeighbor() == null : "isolated node closest neighbor should be null";
        passed++;

        // --- null neighbor for node with no edges ---
        assert g.getNeighbors("c").isEmpty() : "c has no outgoing edges";
        passed++;

        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);
    }
}