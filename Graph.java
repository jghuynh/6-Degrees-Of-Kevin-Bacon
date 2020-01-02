import java.util.*;

/**
 * A Graph class that also keeps track of predecessors
 */
public class Graph {

    private ArrayList<GraphNode> vertices;
    private HashMap<Integer, Integer> currentToPred = new HashMap<>();
    private LinkedList<Integer> path = new LinkedList<>();

    /**
     * Creates an object of type Graph and creates an ArrayList with vertices,
     * each vertex has its own neighbors List
     * @param numVertices the number of vertices to create in the Graph
     */
    private Graph(int numVertices) {
        vertices = new ArrayList<GraphNode>();
        for (int index = 0; index < numVertices; index ++) {
            vertices.add(new GraphNode(index));
        }
    }

    /**
     * Adds an edge between 2 vertices
     * @param v1 one of the vertices
     * @param v2 one of the vertices
     */
    private void addEdge(long v1, long v2) {
        // check for illegal conditions
        if ( v1 == v2) {
            return;
        }
        if (edgeExist((int) v1, (int) v2)) {
            return;
        }
        GraphNode vertex1 = vertices.get((int) v1);
        GraphNode vertex2 = vertices.get((int) v2);
        vertex1.addNeighbor( (int) v2);
        vertex2.addNeighbor( (int) v1);
    }

    /**
     * Checks of an edge/link already exists between 2 given vertices
     * @param v1 one vertex
     * @param v2 another vertex
     * @return true if an edge exist within these 2 vertices; false if no such edge exist
     */
    private boolean edgeExist(int v1, int v2) {
        GraphNode vertex1 = vertices.get(v1);
        ListIterator<Integer> itr = vertex1.getNeighbors().listIterator();
        while (itr.hasNext()) {
            int aneighbor = itr.next();
            if (aneighbor == v2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates an Queue of the shortest distance between start Vertex and all the other vertices
     * @param startVertex the starting vertex
     * @param destination the destination vertex
     * @return true if there is a path between start Vertex and destination
     */
    private boolean shortestReach (int startVertex, int destination) {
        Queue<Integer> queue = new LinkedList<Integer>();
        // keeps a hashset of all actors visited
        HashSet<Integer> visited = new HashSet<>();

        visited.add(startVertex);
        currentToPred.put(startVertex, null);
        queue.add(startVertex);

        while(!queue.isEmpty()) {
            int parentValue = queue.poll();
            Iterator<Integer> itrVertex = vertices.get(parentValue).getNeighbors().iterator();
            while (itrVertex.hasNext()) {
                int neighbor = itrVertex.next();
                if (!visited.contains(neighbor)) {

                    // update visited and currentToPred
                    visited.add(neighbor);
                    currentToPred.put(neighbor, parentValue);

                     ((LinkedList<Integer>) queue).addLast(neighbor);
                    if (neighbor == destination) {
                        path.addFirst(destination);
                        int location = destination;
                        while (currentToPred.get(location) != null) {
                            parentValue = currentToPred.get(location);
                            path.addFirst(parentValue);
                            location = parentValue;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Finds the shortest path between a source vertex and destination vertex
     * @param source the starting vertex
     * @param destination the end vertex
     */
    public LinkedList<Integer> printShortestDistance(int source, int destination) {
        if (!shortestReach(source, destination)) {
            System.out.println("Cannot find path between source = " + source + " and destination = " + destination);
            return null;
        }
        return path;
    }

    public static void main(String[] args) {
        Graph myGraph = new Graph(7);
        myGraph.addEdge(0, 1);
        myGraph.addEdge(0, 4);
        myGraph.addEdge(0, 2);
        myGraph.addEdge(1, 2);
        myGraph.addEdge(2, 4);
        myGraph.addEdge(3, 1);
        myGraph.addEdge(3, 6);
        myGraph.addEdge(3, 5);
        myGraph.addEdge(4, 5);

        myGraph.printShortestDistance(1, 5);
    }
}

