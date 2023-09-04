package it.unicam.cs.asdl2223.es8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 * @param <E> il tipo degli elementi dello heap, che devono avere un ordinamento
 *            naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

	/*
	 * L'array che serve come base per lo heap
	 */
	private ArrayList<E> heap;

	/**
	 * Costruisce uno heap vuoto.
	 */
	public MaxHeap() {
		this.heap = new ArrayList<E>();
	}

	/**
	 * Restituisce il numero di elementi nello heap.
	 * 
	 * @return il numero di elementi nello heap
	 */
	public int size() {
		return this.heap.size();
	}

	/**
	 * Determina se lo heap è vuoto.
	 * 
	 * @return true se lo heap è vuoto.
	 */
	public boolean isEmpty() {
		return this.heap.isEmpty();
	}

	/**
	 * Costruisce uno heap a partire da una lista di elementi.
	 * 
	 * @param list lista di elementi
	 * @throws NullPointerException se la lista è nulla
	 */
	public MaxHeap(List<E> list) {
		//se è nulla lancio l'eccezione
		if (list == null)
			throw new NullPointerException("La lista non può essere nulla");
		//creo un heap con la lista
		this.heap = new ArrayList<E>(list);
		int lastNonLeaf = this.size()/2 -1;
		//riordino la lista facendo l'heapify dall'ultimo nodo padre nell'heap fino alla radice
		for(int i = lastNonLeaf; i>=0; i--) {
			this.heapify(i);
		}
	}

	/**
	 * Inserisce un elemento nello heap
	 * 
	 * @param el l'elemento da inserire
	 * @throws NullPointerException se l'elemento è null
	 * 
	 */
	public void insert(E el) {
		if (el == null)
			throw new NullPointerException("L'elemento non può essere nullo");
		//aggiungo un elemento alla fine dell'heap
		this.heap.add(el);
		//richiamo heapify sul padre dell'elemento appena aggiunto
		this.heapify(parentIndex(this.heap.size() - 1));
	}

	/*
	 * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
	 * posizione i. Si noti che la posizione 0 è significativa e contiene sempre la
	 * radice dello heap.
	 */
	private int leftIndex(int i) {
		// Se l'indice dato è fuori dall'ArrayList
		if (i >= this.heap.size() || i < 0)
			throw new IndexOutOfBoundsException("l'indice è fuori dall'arraylist");
		return 2 * i + 1;
	}

	/*
	 * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
	 * posizione i. Si noti che la posizione 0 è significativa e contiene sempre la
	 * radice dello heap.
	 */
	private int rightIndex(int i) {
		// Se l'indice dato è fuori dall'ArrayList
		if (i >= this.heap.size() || i < 0)
			throw new IndexOutOfBoundsException("l'indice è fuori dall'arraylist");
		return 2 * i + 2;
	}

	/*
	 * Funzione di comodo per calcolare l'indice del genitore del nodo in posizione
	 * i. Si noti che la posizione 0 è significativa e contiene sempre la radice
	 * dello heap.
	 */
	private int parentIndex(int i) {
		// Se l'indice dato è fuori dall'ArrayList
		if (i >= this.heap.size() || i < 0)
			throw new IndexOutOfBoundsException("l'indice è fuori dall'arraylist");
		// se l'indice è 0 restituisco di nuovo la posizione 0 essendo la radice
		if (i == 0)
			return 0;
		return (i - 1) / 2;
	}

	/**
	 * Ritorna l'elemento massimo senza toglierlo.
	 * 
	 * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
	 */
	public E getMax() {
		// se la lista è vuota restituisco null
		if (this.heap.isEmpty())
			return null;
		// essendo un MaxHeap restituisco la radice che sarà sempre l'elemento maggiore
		return this.heap.get(0);
	}

	/**
	 * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non è
	 * più presente nello heap.
	 * 
	 * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
	 */
	public E extractMax() {
		// se l'heap è vuoto riturno null
		if (this.heap.isEmpty())
			return null;
		// mi salvo il valore da restituire e lo rimuovo dall'heap
		E tmp = this.getMax();
		this.heap.remove(this.getMax());
		//una volta rimosso, riordino nuovamente la lista partendo dall'ultimo nodo padre fino alla radice
		int lastNonLeaf = this.size()/2 -1;
		for(int i = lastNonLeaf; i>=0; i--) {
			this.heapify(i);
		}
		// l'heap rimarrà in MaxHeap dato che viene tutto shiftato di 1 a sinistra
		return tmp;
	}

	/*
	 * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i suoi
	 * sottoalberi sinistro e destro (se esistono) siano heap.
	 */
	private void heapify(int i) {
		// tengo traccia dell'indice maggiore
		int biggest = i;
		// se il figlio sinistro è piu grande del padre allora imposto biggest = leftindex
		if (leftIndex(i) < this.heap.size() && this.heap.get(leftIndex(i)).compareTo(this.heap.get(i)) == 1)
			biggest = leftIndex(i);
		// se il figlio destro è piu grande dell'attuale biggest imposto biggest = rightindex
		if (rightIndex(i) < this.heap.size() && this.heap.get(rightIndex(i)).compareTo(this.heap.get(biggest)) == 1)
			biggest = rightIndex(i);
		// se il biggest è cambiato rispetto alla partenza allora faccio uno swap tra padre e figlio piu grande
		if (biggest != i) {
			E tmp = this.heap.get(i);
			this.heap.set(i, this.heap.get(biggest));
			this.heap.set(biggest, tmp);
			//richiamo heapify sul padre dell'indice di partenza
			heapify(parentIndex(i));
		}
	}

	/**
	 * Only for JUnit testing purposes.
	 * 
	 * @return the arraylist representing this max heap
	 */
	protected ArrayList<E> getHeap() {
		return this.heap;
	}
}
