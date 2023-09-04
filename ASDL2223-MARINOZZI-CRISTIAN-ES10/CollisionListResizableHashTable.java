/**
 * 
 */
package it.unicam.cs.asdl2223.es10;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 * 
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 * 
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di defaut (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class CollisionListResizableHashTable<E> implements Set<E> {

	/*
	 * La capacità iniziale. E' una potenza di due e quindi la capacità sarà sempre
	 * una potenza di due, in quanto ogni resize raddoppia la tabella.
	 */
	private static final int INITIAL_CAPACITY = 16;

	/*
	 * Fattore di bilanciamento di default. Tipico valore.
	 */
	private static final double LOAD_FACTOR = 0.75;

	/*
	 * Numero di elementi effettivamente presenti nella hash table in questo
	 * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
	 * lunghezza attuale dell'array di Object che rappresenta la tabella.
	 */
	private int size;

	/*
	 * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
	 * contiene null oppure il puntatore al primo nodo di una lista concatenata di
	 * elementi. Si può riprendere e adattare il proprio codice della Esercitazione
	 * 6 che realizzava una lista concatenata di elementi generici. La classe
	 * interna Node<E> è ripresa proprio da lì.
	 * 
	 * ATTENZIONE: la tabella hash vera e propria può essere solo un generico array
	 * di Object e non di Node<E> per una impossibilità del compilatore di accettare
	 * di creare array a runtime con un tipo generics. Ciò infatti comporterebbe dei
	 * problemi nel sistema di check dei tipi Java che, a run-time, potrebbe
	 * eseguire degli assegnamenti in violazione del tipo effettivo della variabile.
	 * Quindi usiamo un array di Object che riempiremo sempre con null o con
	 * puntatori a oggetti di tipo Node<E>.
	 * 
	 * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
	 * Object:
	 * 
	 * this.table[i] = new Node<E>(item, next);
	 * 
	 * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a fare
	 * un cast esplicito:
	 * 
	 * Node<E> myNode = (Node<E>) this.table[i];
	 * 
	 * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo con
	 * un tag @SuppressWarning,
	 */
	private Object[] table;

	/*
	 * Funzion di hash primaria usata da questa hash table. Va inizializzata nel
	 * costruttore all'atto di creazione dell'oggetto.
	 */
	private final PrimaryHashFunction phf;

	/*
	 * Contatore del numero di modifiche. Serve per rendere l'iterator fail-fast.
	 */
	private int modCount;

	// I due metodi seguenti sono di comodo per gestire la capacity e la soglia
	// oltre la quale bisogna fare il resize.

	/* Numero di elementi della tabella corrente */
	private int getCurrentCapacity() {
		return this.table.length;
	};

	/*
	 * Valore corrente soglia oltre la quale si deve fare la resize,
	 * getCurrentCapacity * LOAD_FACTOR
	 */
	private int getCurrentThreshold() {
		return (int) (getCurrentCapacity() * LOAD_FACTOR);
	}

	/**
	 * Costruisce una Hash Table con capacità iniziale di default e fattore di
	 * caricamento di default.
	 */
	public CollisionListResizableHashTable(PrimaryHashFunction phf) {
		this.phf = phf;
		this.table = new Object[INITIAL_CAPACITY];
		this.size = 0;
		this.modCount = 0;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		if (o == null)
			throw new NullPointerException();
		//mi salvo la posizione nell'hashtable dove dovrebbe trovarsi o
		int itemPosition = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
		//se è vuota torno false
		if (this.table[itemPosition] == null)
			return false;
		//altrimenti prendo il primo nodo nella posizione e verifico se 
		//lui o i nodi successivi siano uguali ad o in accordo con equals
		Node<E> myNode = (Node<E>) this.table[itemPosition];
		while (myNode != null) {
			if (o.equals(myNode.item))
				return true;
			myNode = myNode.next;
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException("Operazione non supportata");
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("Operazione non supportata");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean add(E e) {
		if (e == null)
			throw new NullPointerException();
		//mi salvo la posizione nell'hashtable dove si dovrà trovare e
		int itemPosition = this.phf.hash(e.hashCode(), this.getCurrentCapacity());
		boolean added = false;
		//se la sua pos è vuota aggiungo il nodo
		if (this.table[itemPosition] == null) {
			this.table[itemPosition] = new Node<E>(e, null);
			added = true;
		} else {
			//se non è vuota verifico che non sia gia presente
			if (this.contains(e))
				return false;
			//poi lo inserisco in testa alla lista
			Node<E> head = (Node<E>) this.table[itemPosition];
			this.table[itemPosition] = new Node<E>(e, head);
			added = true;
		}
		if (added) {
			this.size++;
			this.modCount++;
		}
		//verifico la size ed in caso richiamo resize
		if (this.size > this.getCurrentThreshold())
			resize();
		return added;
	}

	/*
	 * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da chiamare
	 * quando this.size diventa maggiore di getCurrentThreshold()
	 */
	private void resize() {
		//creo una nuova tabella grande il doppio
		Object[] newTable = new Object[this.getCurrentCapacity() * 2];
		//creo un iteratore per scorrere la lista
		Iterator<E> iter = this.iterator();
		//finche cè un next lo copio nella nuova tabella
		while (iter.hasNext()) {
			E item = iter.next();
			int pos = this.phf.hash(item.hashCode(), newTable.length);
			newTable[pos] = item;
		}
		//faccio puntare il rif. della tabella alla tabella nuova
		this.table = newTable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		if (o == null)
			throw new NullPointerException();
		//posizione in cui si deve trovare l'oggetto
		int pos = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
		//se è vuoto ritorno false
		if (this.table[pos] == null)
			return false;
		Node<E> previous = null;
		Node<E> list = (Node<E>) this.table[pos];
		//quando trovo l'oggetto da rimuovere imposto il successivo del 
		//precedente uguale al successivo di quello da rimuovere.
		//se quello da rimuovere era il primo imposto solamente il secondo alla testa
		do {
			if (o.equals(list.item)) {
				if (previous == null) {
					this.table[pos] = list.next;
				} else
					previous.next = list.next;
				this.size--;
				this.modCount++;
				return true;
			}
			previous = list;
			list = list.next;
		} while (list != null);
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (c == null)
			throw new NullPointerException();
		//creo un iteratore per scorrere la lista
		Iterator<?> iter = c.iterator();
		//finchè ci sono elementi nella collezione verifico che non siano 
		//nulli e che siano contenuti nella tabella
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item == null)
				throw new NullPointerException();
			if (!this.contains(item))
				return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c == null)
			throw new NullPointerException();
		boolean changed = false;
		Iterator<? extends E> iter = c.iterator();
		while (iter.hasNext()) {
			E item = iter.next();
			if (item == null)
				throw new NullPointerException();
			changed = changed | this.add(item);
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Operazione non supportata");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (c == null)
			throw new NullPointerException();
		boolean changed = false;
		Iterator<?> iter = c.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item == null)
				throw new NullPointerException();
			changed = changed | this.remove(item);
		}
		return changed;
	}

	@Override
	public void clear() {
		// Ritorno alla situazione iniziale
		this.table = new Object[INITIAL_CAPACITY];
		this.size = 0;
		this.modCount = 0;
	}

	/*
	 * Classe per i nodi della lista concatenata. Lo specificatore è protected solo
	 * per permettere i test JUnit.
	 */
	protected static class Node<E> {
		protected E item;
		protected Node<E> next;

		/*
		 * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
		 */
		Node(E item, Node<E> next) {
			this.item = item;
			this.next = next;
		}
	}

	/*
	 * Classe che realizza un iteratore per questa hash table. L'ordine in cui
	 * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
	 * presente deve essere restituito dall'iteratore una e una sola volta.
	 * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
	 * ConcurrentModificationException se a una chiamata di next() si "accorge" che
	 * la tabella è stata cambiata rispetto a quando l'iteratore è stato creato.
	 */
	private class Itr implements Iterator<E> {
		private int lastPosition;
		private int lastNodeIdx;
		private int numeroModificheAtteso;

		private Itr() {
			this.numeroModificheAtteso = modCount;
			this.lastPosition = 0;
			this.lastNodeIdx = 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean hasNext() {
			for (int i = 0; i < CollisionListResizableHashTable.this.table.length; i++) {
				Node<E> head = (Node<E>) CollisionListResizableHashTable.this.table[i];
				if (head == null) {
					continue;
				}
				if (lastNodeIdx == 0 && head != null) {
					return true;
				}
				for (int j = 1; head.next != null;) {
					head = head.next;
					if (j == lastNodeIdx && head.next != null) {
						return true;
					}
					if (j == lastNodeIdx && head.next == null) {
						return false;
					}
				}
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			if (this.numeroModificheAtteso != CollisionListResizableHashTable.this.modCount) {
				throw new ConcurrentModificationException("Lista modificata durante l'iterazione");
			}
			if (!hasNext())
				throw new NoSuchElementException("Richiesta di next quando hasNext Ã¨ falso");
			Node<E> head = (Node<E>) CollisionListResizableHashTable.this.table[lastPosition];
			while (head == null) {
				lastPosition++;
				lastNodeIdx = 0;
				head = (Node<E>) CollisionListResizableHashTable.this.table[lastPosition];
			}
			if (lastNodeIdx == 0 && head != null) {
				lastNodeIdx++;
				return head.item;
			}
			for (int j = 1; head.next != null;) {
				head = head.next;
				if (j == lastNodeIdx && head.next != null) {
					lastNodeIdx++;
					return head.item;
				}
			}
			return null;
		}
	}

	/*
	 * Only for JUnit testing purposes.
	 */
	protected Object[] getTable() {
		return this.table;
	}

	/*
	 * Only for JUnit testing purposes.
	 */
	protected PrimaryHashFunction getPhf() {
		return this.phf;
	}

}
