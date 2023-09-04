package it.unicam.cs.asdl2223.es13;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO completare gli import con eventuali classi della Java SE

/**
 * Classe che implementa l'algoritmo di Dijkstra per il calcolo dei cammini
 * minimi da una sorgente singola. L'algoritmo usa una coda con priorità
 * inefficiente (implementata con una List) che per estrarre il minimo impiega
 * O(n).
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *            le etichette dei nodi del grafo
 */
public class DijkstraShortestPathComputer<L>
        implements SingleSourceShortestPathComputer<L> {
    // ultima sorgente su cui sono stati calcolati i cammini minimi
    private GraphNode<L> lastSource;

    // il grafo su cui opera questo oggetto
    private final Graph<L> grafo;

    // flag che indica se i cammini minimi sono stati calcolati almeno una volta
    private boolean isComputed = false;

    /*
     * Contiene i nodi ancora da analizzare, la coda con priorità viene gestita
     * tramite lista e l'elemento minimo viene cercato e rimosso con costo O(n)
     */
    private List<GraphNode<L>> queue;

    /**
     * Crea un calcolatore di cammini minimi a sorgente singola per un grafo
     * diretto e pesato privo di pesi negativi.
     * 
     * @param graph
     *              il grafo su cui opera il calcolatore di cammini minimi
     * @throws NullPointerException
     *                                  se il grafo passato è nullo
     * 
     * @throws IllegalArgumentException
     *                                  se il grafo passato è vuoto
     * 
     * @throws IllegalArgumentException
     *                                  se il grafo passato non è orientato
     * 
     * @throws IllegalArgumentException
     *                                  se il grafo passato non è pesato,
     *                                  cioè esiste almeno un arco il cui
     *                                  peso è {@code Double.NaN}
     * @throws IllegalArgumentException
     *                                  se il grafo passato contiene almeno
     *                                  un peso negativo
     */
    public DijkstraShortestPathComputer(Graph<L> graph) {
        if (graph == null) {
            throw new NullPointerException();
        }
        if (graph.isEmpty() || !graph.isDirected()) {
            throw new IllegalArgumentException();
        }
        for (GraphEdge<L> edge : graph.getEdges()) {
            if (edge.getWeight() == Double.NaN || edge.getWeight() < 0) {
                throw new IllegalArgumentException();
            }
        }
        this.grafo = graph;
    }

    @Override
    public void computeShortestPathsFrom(GraphNode<L> sourceNode) {
        this.lastSource = sourceNode;
        this.initializeSingleSource(grafo, sourceNode);
        List<GraphNode<L>> pesi = new ArrayList<>();
        this.queue = new ArrayList<>(this.grafo.getNodes());
        while (!this.queue.isEmpty()) {
            GraphNode<L> u = this.extractMinNode();
            pesi.add(u);
            for (GraphNode<L> v : this.grafo.getAdjacentNodesOf(u)) {
                this.relax(u, v);
            }
        }
        this.isComputed = true;
    }

    @Override
    public boolean isComputed() {
        return this.isComputed;
    }

    @Override
    public GraphNode<L> getLastSource() {
        if (!this.isComputed)
            throw new IllegalStateException("Richiesta last source, ma non "
                    + "sono mai stati calcolati i cammini minimi");
        return this.lastSource;
    }

    @Override
    public Graph<L> getGraph() {
        return this.grafo;
    }

    @Override
    public List<GraphEdge<L>> getShortestPathTo(GraphNode<L> targetNode) {
        if (this.lastSource.equals(targetNode)) {
            return new ArrayList<>();
        }
        List<GraphEdge<L>> shortestPath = new ArrayList<>();
        // Mi prendo il nodo precedente del target
        GraphNode<L> node1 = this.grafo.getNodeOf(targetNode.getLabel()).getPrevious();
        GraphNode<L> node2 = this.grafo.getNodeOf(targetNode.getLabel());
        Set<GraphEdge<L>> edges = this.grafo.getEdges();
        do {
            GraphEdge<L> edge = this.findEdgeOf(edges, node1, node2);
            shortestPath.add(edge);
            node2 = node1;
            node1 = node2.getPrevious();
        } while (node1 != null && !node1.equals(this.lastSource));
        GraphEdge<L> last = this.findEdgeOf(edges, this.lastSource, node2);
        if(last != null) {
            shortestPath.add(last);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }

    private void initializeSingleSource(Graph<L> grafo, GraphNode<L> source) {
        for (GraphNode<L> nodo : grafo.getNodes()) {
            // Uso Integer.MAX_VALUE come +infinito
            if (nodo.equals(source)) {
                nodo.setIntegerDistance(0);
            } else {
                nodo.setIntegerDistance(Integer.MAX_VALUE);
            }
            nodo.setPrevious(null);
        }
        source.setIntegerDistance(0);
    }

    private void relax(GraphNode<L> u, GraphNode<L> v) {
        double peso = this.getWeightOfEdge(u, v);
        if (v.getIntegerDistance() > u.getIntegerDistance() + peso) {
            int newDistance = (int) (u.getIntegerDistance() + peso);
            v.setIntegerDistance(newDistance);
            v.setPrevious(u);
        }
    }

    private double getWeightOfEdge(GraphNode<L> u, GraphNode<L> v) {
        for (GraphEdge<L> arco : this.grafo.getEdges()) {
            if (arco.getNode2().equals(v)
                    && arco.getNode1().equals(u)) {
                return arco.getWeight();
            }
        }
        return 0;
    }

    private GraphNode<L> extractMinNode() {
        GraphNode<L> min = this.queue.get(0);
        for (GraphNode<L> nodo : this.queue) {
            if (nodo.getIntegerDistance() < min.getIntegerDistance()) {
                min = nodo;
            }
        }
        this.queue.remove(min);
        return min;
    }

    private GraphEdge<L> findEdgeOf(Set<GraphEdge<L>> edges, GraphNode<L> node1, GraphNode<L> node2) {
        for (GraphEdge<L> edge : edges) {
            if (edge.getNode1().equals(node1) && edge.getNode2().equals(node2)) {
                return edge;
            }
        }
        return null;
    }
}
