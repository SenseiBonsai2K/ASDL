package it.unicam.cs.asdl2223.mp2;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Class that provides an implementation of a "dynamic" min-priority queue based
 * on a ternary heap. "Dynamic" means that the priority of an element already
 * present in the queue may be decreased, so possibly this element may become
 * the new minumum element. The elements that can be inserted may be of any
 * class implementing the interface <code>PriorityQueueElement</code>. This
 * min-priority queue does not have capacity restrictions, i.e., it is always
 * possible to insert new elements and the number of elements is unbound.
 * Duplicated elements are permitted while <code>null</code> elements are not
 * permitted.
 * 
 * @author Template: Luca Tesei, Implementation: Cristian Marinozzi -
 *         cristian.marinozzi@studenti.unicam.it
 *
 */
public class TernaryHeapMinPriorityQueue {

	/*
	 * ArrayList for representing the ternary heap. Use all positions, including
	 * position 0 (the JUnit tests will assume so). You have to adapt child/parent
	 * indexing formulas consequently.
	 */
	private ArrayList<PriorityQueueElement> heap;

	/**
	 * Create an empty queue.
	 */
	public TernaryHeapMinPriorityQueue() {
		this.heap = new ArrayList<PriorityQueueElement>();
	}

	/**
	 * Return the current size of this queue.
	 * 
	 * @return the number of elements currently in this queue.
	 */
	public int size() {
		return this.heap.size();
	}

	/**
	 * Add an element to this min-priority queue. The current priority associated
	 * with the element will be used to place it in the correct position in the
	 * ternary heap. The handle of the element will also be set accordingly.
	 * 
	 * @param element the new element to add
	 * @throws NullPointerException if the element passed is null
	 */
	public void insert(PriorityQueueElement element) {
		if (element == null)
			throw new NullPointerException("L'elemento non può essere nullo");
		// inserisco l'elemento alla fine dello heap
		this.heap.add(element);
		// imposto il sup handle pari all'ultimo indice dell'heap
		this.heap.get(this.size()-1).setHandle(this.size()-1);
		// richiamo l'heapifyUp che compara le priority dell'elemento all'indice in
		// input e del suo nodo padre.
		// se il padre ha una priority superiore del figlio allora viene effettuato uno
		// swap.
		// heapifyUp continuerà ricorsivamente il suo funzionamento.
		this.heapifyUp(this.size() - 1);
	}

	/**
	 * Returns the current minimum element of this min-priority queue without
	 * extracting it. This operation does not affect the ternary heap.
	 * 
	 * @return the current minimum element of this min-priority queue
	 * 
	 * @throws NoSuchElementException if this min-priority queue is empty
	 */
	public PriorityQueueElement minimum() {
		if (this.heap.isEmpty())
			throw new NoSuchElementException("Non ci sono elementi nella lista");
		// ritorno semplicemente l'elemento in posizione 0 che sarà sempre quello con la
		// priority piu bassa
		return this.heap.get(0);
	}

	/**
	 * Extract the current minimum element from this min-priority queue. The ternary
	 * heap will be updated accordingly.
	 * 
	 * @return the current minimum element
	 * @throws NoSuchElementException if this min-priority queue is empty
	 */
	public PriorityQueueElement extractMinimum() {
		if (this.heap.isEmpty())
			throw new NoSuchElementException("Non ci sono elementi nella lista");
		// scambio l'elemento in cima alla lista(quindi quello con la priority piu
		// bassa) con l'ultimo elemento e lo salvo in una variabile di appoggio
		this.swap(this.heap.get(0), this.heap.get(this.size() - 1));
		PriorityQueueElement result = this.heap.get(this.size() - 1);
		// rimuovo l'ultimo elemento e poi richiamo l'heapifyDown sul primo
		// elemento(teoricamente quello con la priority piu elevata)
		this.heap.remove(this.size() - 1);
		this.heapifyDown(0);
		// infine restituisco il valore dentro la variabile di appoggio che avevo
		// precedentemento inizializzato.
		return result;
	}

	/**
	 * Decrease the priority associated to an element of this min-priority queue.
	 * The position of the element in the ternary heap must be changed accordingly.
	 * The changed element may become the minimum element. The handle of the element
	 * will also be changed accordingly.
	 * 
	 * @param element     the element whose priority will be decreased, it must
	 *                    currently be inside this min-priority queue
	 * @param newPriority the new priority to assign to the element
	 * 
	 * @throws NoSuchElementException   if the element is not currently present in
	 *                                  this min-priority queue
	 * @throws IllegalArgumentException if the specified newPriority is not strictly
	 *                                  less than the current priority of the
	 *                                  element
	 */
	public void decreasePriority(PriorityQueueElement element, double newPriority) {
		if (!this.heap.contains(element))
			throw new NoSuchElementException(element + "non è presente nella lista");
		if (newPriority >= element.getPriority())
			throw new IllegalArgumentException("la nuova priorità dell'elemento deve essere inferiore alla precedente");
		// imposto la nuova priorità dell'elemento
		element.setPriority(newPriority);
		// poi richiamo l'heapifyUp sull'elemento appena modificato.
		// l'heapifyUp andrà a comparare le priority dell'elemento e del suo Nodo padre
		// se quella del padre è maggiore allora verrà swappato con l'elemento e cosi
		// vià.
		this.heapifyUp(element.getHandle());
	}

	/**
	 * Erase all the elements from this min-priority queue. After this operation
	 * this min-priority queue is empty.
	 */
	public void clear() {
		this.heap.clear();
	}
	
	//se la priorita dell'elemento alla posizione i è minore di quella del padre, i due elementi vengono swappati.
	//successivamente viene richiamato ricorsivamente il metodo sul precedente elemento padre e cosi via.
	private void heapifyUp(int i) {
		if (i > 0 && this.heap.get(i).getPriority() < this.heap.get(parent(i)).getPriority()) {
			//se entriamo in questa condizione significa che l'elemento figlio è più piccolo del padre
			//quindi swappo i due e continuo la computazione dell'heapifyUp sull'elemento piu piccolo(nuovo padre)
			this.swap(this.heap.get(i), this.heap.get(parent(i)));
			this.heapifyUp(parent(i));
		}
	}
	
	//individua l'elemento con la priorità minore tra un elemento alla posizione i e tutti i suoi figli.
	//una volta trovato l'elemento con la priorità minore(se l'elemento mionore è il padre non succede nulla)
	//questo viene swappato con il padre. Poi viene richiamato ricorsivamente l'heapifyDown sull'elemento piu piccolo trovato.
	private void heapifyDown(int i) {
		int left = left(i);
		int center = center(i);
		int right = right(i);
		int smallest = i;
		if (left < this.size() && this.heap.get(left).getPriority() < this.heap.get(i).getPriority())
			smallest = left;
		if (center < this.size() && this.heap.get(center).getPriority() < this.heap.get(smallest).getPriority())
			smallest = center;
		if (right < this.size() && this.heap.get(right).getPriority() < this.heap.get(smallest).getPriority())
			smallest = right;
		if (smallest != i) {
			//arrivati a questo punto abbiamo sicuramente trovato un elemento piu piccolo del padre
			//quindi swappo il padre con il figlio piu piccolo e continuo la computazione dell'heapifyDown finchè serve
			swap(this.heap.get(i), this.heap.get(smallest));
			this.heapifyDown(smallest);
		}
	}
	
	//Un metodo che scambia prima la posizione dei due elementi nello heap.
	//poi scambia i loro handle
	private void swap(PriorityQueueElement sonElement, PriorityQueueElement dadElement) {
		//variabili di comodo per rendere piu comprensibile e leggibile il codice
		int sonHandle = sonElement.getHandle();
		int dadHandle = dadElement.getHandle();
		//sostituisco il figlio con il padre
		this.heap.set(sonHandle, dadElement);
		//sostituisco il padre con il figlio
		this.heap.set(dadHandle, sonElement);
		//a questo punto il dadHandle rappresenta la posizione dell'elemento
		//figlio quindi imposto l'handle del figlio uguale al dadHandle
		this.heap.get(dadHandle).setHandle(dadHandle);
		//allo stesso modo il sonHandle rappresenta la posizione dell'elemento
		//padre quindi imposto l'handle del padre uguale al sonHandle
		this.heap.get(sonHandle).setHandle(sonHandle);
	}

	// individua il figlio sinistro di un elemento alla posizione i
	private int left(int i) {
		return (3 * i + 1);
	}

	// individua il figlio centrale di un elemento alla posizione i
	private int center(int i) {
		return (3 * i + 2);
	}

	// individua il figlio destro di un elemento alla posizione i
	private int right(int i) {
		return (3 * i + 3);
	}

	// individua il padre di un elemento alla posizione i
	private int parent(int i) {
		if (i == 0)
			return 0;
		return (i - 1) / 3;
	}

	/*
	 * This method is only for JUnit testing purposes.
	 */
	protected ArrayList<PriorityQueueElement> getTernaryHeap() {
		return this.heap;
	}

}
