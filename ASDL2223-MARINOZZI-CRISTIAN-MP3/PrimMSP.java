package it.unicam.cs.asdl2223.mp3;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * <p>
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * <p>
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 *
 * @param <L> tipo delle etichette dei nodi del grafo
 * @author Luca Tesei (template)
 * Implementation: Marinozzi Cristian - cristian.marinozzi@studenti.unicam.it
 */
public class PrimMSP<L> {

    private List<GraphNode<L>> priorityQueue;
    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un
     * insieme dei nodi già visitati
     */

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMSP() {
        priorityQueue = new ArrayList<>();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     *
     * @param g un grafo non orientato, pesato, con pesi non negativi
     * @param s il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *          dell'albero di copertura minimo. Tale nodo sarà la radice
     *          dell'albero di copertura trovato
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     * con pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {
        //Come prima cosa verifico che il grafo ed il nodo non siano nulli, poi che il grafo in input
        //contenga il nodo sempre in input ed infine che il grafo sia non orientato.
        //Se una di questa condizioni è vera throwo.
        if (g == null || s == null) throw new NullPointerException("Grafo o nodo null");
        if (g.getNode(s) == null) throw new IllegalArgumentException("Il nodo non appartiene al grafo");
        if (g.isDirected()) throw new IllegalArgumentException("Grafo orientato");
        //Successivamente imposto il floatingpointDistance, il previous ed il colore di ogni nodo.
        for (GraphNode<L> node : g.getNodes()) {
            if (!node.equals(s)) {
                node.setFloatingPointDistance(Double.POSITIVE_INFINITY);
            } else {
                //il nodo corrisponde ad s ed imposto il suo floatingPointDistance a 0
                node.setFloatingPointDistance(0);
            }
            priorityQueue.add(node);
            node.setColor(GraphNode.COLOR_WHITE);
            node.setPrevious(null);
        }
        //Finchè la priorty non è vuota richiamo il getAndRemoveMin (trova il minimo nodo e
        //lo rimuove per poi restituirlo) e cambio il colore del nodo che restituisce(min)
        while (!priorityQueue.isEmpty()) {
            GraphNode<L> min = getAndRemoveMin();
            min.setColor(GraphNode.COLOR_BLACK);
            //Poi creo un ciclo che oltre a verificare le varie eccezioni
            //che si potrebbero presentare dovute al peso degli archi...
            for (GraphNode<L> node : g.getAdjacentNodesOf(min)) {
                if (!g.getEdge(min, node).hasWeight()) throw new IllegalArgumentException();
                if (g.getEdge(min, node).getWeight() < 0) throw new IllegalArgumentException();
                //...verifica se node fa parte della priority queue e se il suo
                // floatingPointDistance è maggiore del peso dell'arco tra min e node.
                // Nel caso la condizione risulti vera aggiorno il floatingDistance
                // con il peso dell'arco e coloro il nodo di nero.
                if (priorityQueue.contains(node) && g.getEdge(min, node).getWeight() < node.getFloatingPointDistance()) {
                    node.setFloatingPointDistance(g.getEdge(min, node).getWeight());
                    node.setPrevious(min);
                    node.setColor(GraphNode.COLOR_BLACK);
                }
            }
        }
    }

    private GraphNode<L> getAndRemoveMin() {
        GraphNode<L> min = priorityQueue.get(0);
        for (int i = 1; i < priorityQueue.size(); i++) {
            if (priorityQueue.get(i).getFloatingPointDistance() < min.getFloatingPointDistance())
                min = priorityQueue.get(i);
        }
        priorityQueue.remove(min);
        return min;
    }
}
