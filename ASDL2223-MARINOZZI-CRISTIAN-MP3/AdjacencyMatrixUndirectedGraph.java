/**
 *
 */
package it.unicam.cs.asdl2223.mp3;

import java.util.*;

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * <p>
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * <p>
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * <p>
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * <p>
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 *
 * @author Luca Tesei (template)
 * Implementation: Marinozzi Cristian - cristian.marinozzi@studenti.unicam.it
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        // Restituisco semplicemente la dimensione dell'HashMap
        return this.nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        // Restituisco l'insieme di tutti gli archi nel grafo
        return this.getEdges().size();
    }

    @Override
    public void clear() {
        // Pulisco completamente sia la matrice che l'HashMap
        nodesIndex.clear();
        matrix.clear();
    }

    @Override
    public boolean isDirected() {
        // Data la consegna della classe questo grafo sarà sempre NON orientato
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        // Verifico che il nodo in input non sia nullo o non sia gia presente nel grafo
        if (node == null) throw new NullPointerException("Il nodo da aggiungere non deve essere nullo");
        if (nodesIndex.containsKey(node)) return false;
        // A questo punto il nodo non è presente nel grafo e quindi lo aggiungo all'HashMap nella prima posizione libera
        int firstFreePosition = nodesIndex.size();
        nodesIndex.put(node, firstFreePosition);
        // Ora aggiungo il nodo alla matrice iniziando dall'ultima riga
        matrix.add(new ArrayList<GraphEdge<L>>());
        for (int i = 0; i < nodesIndex.size(); i++) {
            matrix.get(firstFreePosition).add(null);
        }
        // Infine aggiungo una colonna per ogni riga aggiunta precedentemente
        for (int i = 0; i < firstFreePosition; i++) {
            matrix.get(i).add(null);
        }
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        // se il label in input è nullo lancio la NullPointerException
        // altrimenti richiamo l'addNode che, se serve, lancierà le eccezioni necessarie
        if (label == null)
            throw new NullPointerException("il label non deve essere nullo");
        else
            return addNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        // Se il nodo in input è nullo lancio la NullPointer
        if (node == null) throw new NullPointerException("Il nodo da rimuovere non deve essere nullo");
        // Se il nodo non è presente nell'HashMap allora lancio l'IllegalArgument
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Il nodo da rimuovere non è nel grafo");
        // In tutti gli altri casi prendo l'indice del nodo e lo rimuovo per poi aggiornare gli indici
        int nodeIndex = nodesIndex.get(node);
        nodesIndex.remove(node);
        for (Map.Entry<GraphNode<L>, Integer> entryNode : nodesIndex.entrySet()) {
            if (entryNode.getValue() > nodeIndex)
                nodesIndex.put(entryNode.getKey(), entryNode.getValue() - 1);
        }
        // Successivamente rimuovo la riga del nodo e rimuovo anche la colonna
        // corrispondente al nodo rimosso per ogni altra riga rimasta
        matrix.remove(nodeIndex);
        for (ArrayList<GraphEdge<L>> row : matrix) {
            row.remove(nodeIndex);
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        // Modifico solamente l'eccezione
        if (label == null) throw new NullPointerException("Il label in input non deve essere nullo");
            // Per il resto si occuperà di tutto il removeNode
        else removeNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        // verifico che l'indice in input sia nel range giusto altrimenti lancio l'eccezione.
        // se l'indice è corretto si occuperà di tutto il resto il removeNode.
        if (i < 0 || i >= nodeCount())
            throw new IndexOutOfBoundsException("L'indice in input è furoi range");
        else
            removeNode(getNode(i));
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        // Verifico che il nodo non sia nullo
        if (node == null) throw new NullPointerException("Il nodo in input non deve essere nullo");
        // se non lo è scorro ogni elemento dell'HashMap e se trovo il nodo in input lo restituisco
        for (GraphNode<L> checkNode : nodesIndex.keySet()) {
            if (node.equals(checkNode)) return checkNode;
        }
        // se arriviamo qui siginifica che il nodo non è stato trovato e restituisco null
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        // Modifico solamente l'eccezione
        if (label == null) throw new NullPointerException("Il label non deve essere nullo");
        // Per il resto si occuperà di tutto il getNode
        return getNode(new GraphNode<L>(label));
    }

    @Override
    public GraphNode<L> getNode(int i) {
        // verifico che l'indice in input sia nel range giusto altrimenti lancio l'eccezione.
        if (i < 0 || i >= nodeCount())
            throw new IndexOutOfBoundsException("L'indice in input è fuori range");
        // se non è nullo accedo all'HashMap e vado alla ricerca del nodo in input
        for (Map.Entry<GraphNode<L>, Integer> entryNode : nodesIndex.entrySet()) {
            //se lo trovo lo restituisco
            if (entryNode.getValue() == i) return entryNode.getKey();
        }
        //altrimenti restituisco null
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        // verifico che il nodo non sia nullo e che sia presente nel grafo
        if (node == null) throw new NullPointerException("Il node in input non deve essere null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Il nodo deve essere nel grafo");
        // se passa il controllo restituisco l'indice del nodo
        return nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        // Modifico solo le eccezioni
        if (label == null) throw new NullPointerException("il label in input non deve essere null");
        // Per tutto il resto se ne occuperà il metodo getNodeIndexOf
        return getNodeIndexOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // Verifico le varie eccezioni
        if (edge == null) throw new NullPointerException("l'arco in input non deve essere null");
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2())) throw new
                IllegalArgumentException("i nodi dell'arco devono essere nel grafo");
        if (isDirected() != edge.isDirected())
            throw new IllegalArgumentException("Se l'arco è orientato anche il grafo deve esserlo");
        // uso delle variabili di appoggio per la posizione dei due nodi
        int indexNode1 = nodesIndex.get(edge.getNode1());
        int indexNode2 = nodesIndex.get(edge.getNode2());
        //Controllo se l'arco già esiste nel grafo
        if (edge.equals(matrix.get(indexNode1).get(indexNode2))) {
            return false;
        }
        // Arrivato qui so che questo arco non esiste e lo aggiungo
        matrix.get(indexNode1).set(indexNode2, edge);
        matrix.get(indexNode2).set(indexNode1, edge);
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // verifico che uno dei due nodi ( o entrambi ) non siano null
        if (node1 == null || node2 == null) throw new NullPointerException("i nodi non devono essere null");
        // Tutto il resto verrà svolto da addEdge
        return addEdge(new GraphEdge<>(node1, node2, isDirected()));
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        // verifico che uno dei due nodi ( o entrambi ) non siano null
        if (node1 == null || node2 == null) throw new NullPointerException("i nodi non devono essere null");
        // Tutto il resto verrà svolto da addEdge
        return addEdge(new GraphEdge<>(node1, node2, isDirected(), weight));
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        // verifico che uno dei due label ( o entrambi ) non siano null
        if (label1 == null || label2 == null)
            throw new NullPointerException("i label non devono essere null");
        GraphNode<L> firstNode = new GraphNode<>(label1);
        GraphNode<L> secondNode = new GraphNode<>(label2);
        // Tutto il resto verrà svolto da addEdge
        return addEdge(new GraphEdge<>(firstNode, secondNode, isDirected()));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        // verifico che uno dei due label ( o entrambi ) non siano null
        if (label1 == null || label2 == null)
            throw new NullPointerException("i label non devono essere null");
        GraphNode<L> firstNode = new GraphNode<>(label1);
        GraphNode<L> secondNode = new GraphNode<>(label2);
        // Tutto il resto verrà svolto da addEdge
        return addEdge(new GraphEdge<>(firstNode, secondNode, isDirected(), weight));
    }

    @Override
    public boolean addEdge(int i, int j) {
        // tutte le eccezioni in questo metodo verranno lanciate dai metodi richiamati
        GraphNode<L> firstNode = getNode(i);
        GraphNode<L> secondNode = getNode(j);
        return addEdge(new GraphEdge<L>(firstNode, secondNode, isDirected()));
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        // tutte le eccezioni in questo metodo verranno lanciate dai metodi richiamati
        GraphNode<L> firstNode = getNode(i);
        GraphNode<L> secondNode = getNode(j);
        return addEdge(new GraphEdge<L>(firstNode, secondNode, isDirected(), weight));
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        // Verifico che l'arco non sia nullo e che sia presente nel grafo
        // poi verifico che i nodi collegati dall'arco esistano nel grafo
        if (edge == null) throw new NullPointerException("L'arco in input non deve essere nullo");
        if (getEdge(edge) == null) throw new IllegalArgumentException("l'arco in input non è nel grafo");
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("i nodi devono appartenere al grafo");
        //poi prendo gli indici dei nodi collegati dall'arco e sostituisco con null le celle corrispondenti
        int firstIndex = getNodeIndexOf(edge.getNode1());
        int secondIndex = getNodeIndexOf(edge.getNode2());
        matrix.get(firstIndex).set(secondIndex, null);
        matrix.get(secondIndex).set(firstIndex, null);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // Modifico solamente l'eccezione
        if (node1 == null || node2 == null) throw new NullPointerException("i nodi in input non devono essere null");
        //Tutto il resto viene controllato da removeEdge
        removeEdge(new GraphEdge<L>(node1, node2, isDirected()));
    }

    @Override
    public void removeEdge(L label1, L label2) {
        // Modifico solamente l'eccezione
        if (label1 == null || label2 == null)
            throw new NullPointerException("I label in input non devono essere null");
        GraphNode<L> firstNode = new GraphNode<>(label1);
        GraphNode<L> secondNode = new GraphNode<>(label2);
        //Tutto il resto viene controllato da removeEdge
        removeEdge(new GraphEdge<L>(firstNode, secondNode, isDirected()));
    }

    @Override
    public void removeEdge(int i, int j) {
        // tutte le eccezioni in questo metodo verranno lanciate dai metodi richiamati
        GraphNode<L> node1 = getNode(i);
        GraphNode<L> node2 = getNode(j);
        removeEdge(new GraphEdge<L>(node1, node2, isDirected()));
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        // Verifico che l'arco non sia null e che i nodi che collega esistano nel grafo
        if (edge == null) throw new NullPointerException("l'arco non deve essere null");
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()))
            throw new IllegalArgumentException("i nodi devono essere presenti nel grafo");
        int firstIndex = getNodeIndexOf(edge.getNode1());
        int secondIndex = getNodeIndexOf(edge.getNode2());
        // Se il nodo è contenuto nella matrice posso restituire un arco qualsiasi dato che la matrice <è specchiata
        if (edge.equals(matrix.get(firstIndex).get(secondIndex)) || edge.equals(matrix.get(secondIndex).get(firstIndex))) {
            return matrix.get(firstIndex).get(secondIndex);
        }
        //Se arrivo qui nessun arco è presente e restituisco null
        return null;
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // Modifico solamente l'eccezione
        if (node1 == null || node2 == null) throw new NullPointerException("i nodi in input non devono essere null");
        //Tutto il resto viene controllato da getEdge
        return getEdge(new GraphEdge<L>(node1, node2, isDirected()));
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        // Modifico solamente l'eccezione
        if (label1 == null || label2 == null) throw new NullPointerException("I label in input non devono essere null");
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        //Tutto il resto viene controllato da getEdge
        return getEdge(new GraphEdge<L>(node1, node2, isDirected()));
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        // tutte le eccezioni in questo metodo verranno lanciate dai metodi richiamati
        GraphNode<L> node1 = getNode(i);
        GraphNode<L> node2 = getNode(j);
        return getEdge(new GraphEdge<L>(node1, node2, isDirected()));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // verifico prima di tutto che il nodo non sia nullo e che sia contenuto nell'HashMap
        if (node == null) throw new NullPointerException("il nodo non deve essere nullo");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Il nodo non esiste");
        // imposto una variabile di appoggio per memorizzare il risultato futuro
        Set<GraphNode<L>> tmp = new HashSet<GraphNode<L>>();
        int nodeIndex = getNodeIndexOf(node);
        // Verifico che l'indice del nodo in input contenga un valore diverso da null
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(nodeIndex).get(i) != null) {
                // se è diverso da null,controllo se
                if (nodeIndex == getNodeIndexOf(matrix.get(nodeIndex).get(i).getNode1())) {
                    tmp.add(matrix.get(nodeIndex).get(i).getNode2());
                } else {
                    tmp.add(matrix.get(nodeIndex).get(i).getNode1());
                }
            }
        }
        return tmp;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        // se il label è nullo throwo
        if (label == null) throw new NullPointerException("Il label non deve essere null");
        // Per tutto il resto se ne occuperà getAdjacentNodesOf
        return getAdjacentNodesOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        return getAdjacentNodesOf(getNode(i));
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        // verifico prima di tutto che il nodo non sia nullo e che sia contenuto nell'HashMap
        if (node == null) throw new NullPointerException("Il nodo in input non deve essere nullo");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo non presente");
        // imposto una variabile di appoggio per memorizzare il risultato futuro
        Set<GraphEdge<L>> tmp = new HashSet<GraphEdge<L>>();
        int nodeIndex = getNodeIndexOf(node);
        // poi aggiungo gli archi != null presenti sulla riga del nodo
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(nodeIndex).get(i) != null) tmp.add(matrix.get(nodeIndex).get(i));
        }
        return tmp;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        // Modifico solamente l'eccezione
        if (label == null) throw new NullPointerException("Il label in input non deve essere nullo");
        // Per tutto il resto ci penserà getEdgesOf
        return getEdgesOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        // Modifico solamente l'eccezione
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("L'indice in input è fuori range");
        // Per tutto il resto ci penserà getEdgesOf
        return getEdgesOf(getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // creo una variabile di appoggio per memorizzare l'arco
        Set<GraphEdge<L>> tmp = new HashSet<GraphEdge<L>>();
        for (int j = 0; j < matrix.size(); j++) {
            // Controllo che ogni arco all'interno della matrice sia diverso da null
            for (ArrayList<GraphEdge<L>> graphEdges : matrix) {
                //in caso positivo lo aggiungo alla variabile di appoggio e vado avanti
                if (graphEdges.get(j) != null) tmp.add(graphEdges.get(j));
            }
        }
        //infine restituisco tutti gli archi trovati
        return tmp;
    }
}
