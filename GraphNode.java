import java.util.ArrayList;

/**
 * A graph node for each node in a grpah; also keeps track of the node's neighbors
 */
public class GraphNode {
    int vertex; // the source vector
    ArrayList<Integer> adjacent;

    /**
     * Creates an object of type GraphNode
     * @param vertex the source vertex
     */
    public GraphNode(int vertex) {
        this.vertex = vertex;
        adjacent = new ArrayList<>();
    }

    /**
     * Adds a vertex into a list of neighbors
     * @param neighbor the adjacent vertex to be added
     */
    public void addNeighbor(int neighbor) {
        adjacent.add(neighbor);
    }

    /**
     * Gets a list of neighbors (of the source vector's neighbors)
     * @return the list of neighbors
     */
    public ArrayList<Integer> getNeighbors() {
        return adjacent;
    }
}
