/**
 * 
 */
package it.unicam.cs.asdl2223.mp2;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.text.html.parser.Element;

/**
 * Implementation of the Java SE Double-ended Queue (Deque) interface
 * (<code>java.util.Deque</code>) based on a double linked list. This deque does
 * not have capacity restrictions, i.e., it is always possible to insert new
 * elements and the number of elements is unbound. Duplicated elements are
 * permitted while <code>null</code> elements are not permitted. Being
 * <code>Deque</code> a sub-interface of
 * <code>Queue<code>, this class can be used also as an implementaion of a <code>Queue</code>
 * and of a <code>Stack</code>.
 * 
 * The following operations are not supported:
 * <ul>
 * <li><code>public <T> T[] toArray(T[] a)</code></li>
 * <li><code>public boolean removeAll(Collection<?> c)</code></li>
 * <li><code>public boolean retainAll(Collection<?> c)</code></li>
 * <li><code>public boolean removeFirstOccurrence(Object o)</code></li>
 * <li><code>public boolean removeLastOccurrence(Object o)</code></li>
 * </ul>
 * 
 * @author Template: Luca Tesei, Implementation: Cristian Marinozzi -
 *         cristian.marinozzi@studenti.unicam.it
 *
 */
public class ASDL2223Deque<E> implements Deque<E> {

	/*
	 * Current number of elements in this deque
	 */
	private int size;

	/*
	 * Pointer to the first element of the double-linked list used to implement this
	 * deque
	 */
	private Node<E> first;

	/*
	 * Pointer to the last element of the double-linked list used to implement this
	 * deque
	 */
	private Node<E> last;

	/*
	 * Ho inserito questa variabile per rispettare il fail-fast dell'iterator.
	 * Questa variabile salva il numero di modifiche che vengono apportate alla
	 * lista per poi controllare che durante l'utilizzo dell'iteraore la lista non
	 * venga modificata ulteriormente dalla chiamata di uno dei metodi della calsse
	 * principale.
	 */
	private int numeroModifiche;

	/**
	 * Constructs an empty deque.
	 */

	/*
	 * imposto tutti i parametri a null o 0 per rappresentare una empty deque
	 */
	public ASDL2223Deque() {
		this.first = null;
		this.last = null;
		this.size = 0;
		this.numeroModifiche = 0;
	}

	/*
	 * per verificare che la lista sia vuota mi basta verificare tramite un boolean
	 * se la size è uguale a 0
	 */
	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public Object[] toArray() {
		// creo un array di dimensione uguale alla size della lista
		Object[] array = new Object[this.size];
		int index = 0;
		// scorro tutta la lista aggiungendo mano a mano gli elementi all'array
		for (E element : this)
			array[index++] = element;
		return array;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("This class does not implement this service.");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (c == null)
			throw new NullPointerException("la Collection non può essere nulla");
		// Verifico con un ciclo che tutti i valori della collection in input
		// siano diversi da null e siano contenuti in nella deque passata.
		for (Object e : c) {
			// il contains verifica se un oggetto di c è null
			if (!this.contains(e))
				return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c == null)
			throw new NullPointerException("la Collection non può essere nulla");
		// imposto un boolean a false per determinare se la deque è stata modificata
		boolean changed = false;
		// Verifico tramite un ciclo che tutti gli elementi siano diversi da null
		// ed in caso lo siano li aggiungo alla fine della lista per poi impostare
		// changed a true.
		for (E e : c) {
			// l'add verifica se un oggetto di c è null
			this.add(e);
			changed = true;
		}
		return changed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("This class does not implement this service.");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("This class does not implement this service.");
	}

	/*
	 * Imposto nuovamente tutti i valori a null o 0(come se la lista sia stata
	 * appena creata)
	 */
	@Override
	public void clear() {
		this.first = null;
		this.last = null;
		this.size = 0;
		this.numeroModifiche = 0;
	}

	@Override
	public void addFirst(E e) {
		if (e == null)
			throw new NullPointerException("e non può essere nullo");
		// se la lista è vuota imposto la testa e la coda come un nuovo Nodo con next e
		// prev nulli
		if (this.isEmpty()) {
			this.first = new Node<E>(null, e, null);
			this.last = this.first;
			// altrimenti imposto il prev di first come un nuovo nodo con
			// il prev null ed il next uguale alla vecchia testa della lista
		} else {
			this.first.prev = new Node<E>(null, e, this.first);
			this.first = this.first.prev;
		}
		// incremento la size ed il numero delle modifiche
		this.size++;
		this.numeroModifiche++;
	}

	@Override
	public void addLast(E e) {
		if (e == null)
			throw new NullPointerException("e non può essere nullo");
		// se la lista è vuota imposto la coda e la testa come un nuovo Nodo con next e
		// prev nulli
		if (this.isEmpty()) {
			this.last = new Node<E>(null, e, null);
			this.first = this.last;
			// altrimenti imposto il next di last come un nuovo nodo con
			// il next null ed il prev uguale alla vecchia coda della lista
		} else {
			this.last.next = new Node<E>(this.last, e, null);
			this.last = this.last.next;
		}
		// incremento la size ed il numero delle modifiche
		this.size++;
		this.numeroModifiche++;
	}

	@Override
	public boolean offerFirst(E e) {
		// esegue gli stessi passi del metodo addFirst solamente
		// che se viene aggiunto con successo restituisce true
		int actualSize = this.size();
		this.addFirst(e);
		return this.size() != actualSize;
	}

	@Override
	public boolean offerLast(E e) {
		// esegue gli stessi passi del metodo addLast solamente
		// che se viene aggiunto con successo restituisce true
		int actualSize = this.size();
		this.addLast(e);
		return this.size() != actualSize;
	}

	@Override
	public E removeFirst() {
		if (this.isEmpty())
			throw new NoSuchElementException("Non ci sono elementi nella lista");
		// assegno ad una variabile di appoggio il primo elemento della lista
		E firstElement = this.getFirst();
		// rimuovo l'elemento dalla lista
		this.remove(firstElement);
		// ritorno la variabile di appoggio
		return firstElement;
	}

	@Override
	public E removeLast() {
		if (this.isEmpty())
			throw new NoSuchElementException("Non ci sono elementi nella lista");
		// assegno ad una variabile di appoggio il primo elemento della lista
		E lastElement = this.getLast();
		// rimuovo l'elemento dalla lista
		this.remove(lastElement);
		// ritorno la variabile di appoggio
		return lastElement;
	}

	@Override
	public E pollFirst() {
		// esegue gli stessi passi del removeFirst con al differenza
		// che in caso di lista vuota ritorna null invece di throware un eccezione
		if (this.isEmpty())
			return null;
		return this.removeFirst();
	}

	@Override
	public E pollLast() {
		// esegue gli stessi passi del removeLast con al differenza
		// che in caso di lista vuota ritorna null invece di throware un eccezione
		if (this.isEmpty())
			return null;
		return this.removeLast();
	}

	@Override
	public E getFirst() {
		// restituisce l'elemento in testa alla lista oppure throwa se la lista è vuota.
		if (this.isEmpty())
			throw new NoSuchElementException("Non ci sono elementi nella lista");
		return this.first.item;
	}

	@Override
	public E getLast() {
		// restituisce l'elemento in coda alla lista oppure throwa se la lista è vuota.
		if (this.isEmpty())
			throw new NoSuchElementException("Non ci sono elementi nella lista");
		return this.last.item;
	}

	@Override
	public E peekFirst() {
		// restituisce l'elemento in testa alla lista oppure null se la lista è vuota.
		if (this.isEmpty())
			return null;
		return this.first.item;
	}

	@Override
	public E peekLast() {
		// restituisce l'elemento in coda alla lista oppure null se la lista è vuota.
		if (this.isEmpty())
			return null;
		return this.last.item;
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		throw new UnsupportedOperationException("This class does not implement this service.");
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		throw new UnsupportedOperationException("This class does not implement this service.");
	}

	// nei metodi che seguono ho richiamato altri metodi creati
	// in precedenza dato che non abbiamo limitazione di size
	@Override
	public boolean add(E e) {
		return this.offerLast(e);
	}

	@Override
	public boolean offer(E e) {
		return this.offerLast(e);
	}

	@Override
	public E remove() {
		return this.removeFirst();
	}

	@Override
	public E poll() {
		return this.pollFirst();
	}

	@Override
	public E element() {
		return this.getFirst();
	}

	@Override
	public E peek() {
		return this.peekFirst();
	}

	@Override
	public void push(E e) {
		this.addFirst(e);
	}

	@Override
	public E pop() {
		return this.removeFirst();
	}

	public boolean remove(Object o) {
		if (o == null)
			throw new NullPointerException("o non pùò essere nullo");
		// se la lista è vuota non posso ovviamente rimuovere l'oggetto
		if (this.isEmpty())
			return false;
		// se l'oggetto è alla prima posizione imposto la testa della lista come
		// il secondo elemento (null se la size è 1) e decremento size aumentando il
		// numero delle modifiche
		if (this.first.item.equals(o)) {
			// se la size è 1 bisogna anche impostare il last come null
			if (this.size == 1)
				this.last = null;
			this.first = this.first.next;
			this.size--;
			this.numeroModifiche++;
			return true;
		} else {
			// creo un iterator per scorrere gli elementi della lista
			Itr iterator = new Itr();
			// avendo gia verificato la testa della coda effettuo un next
			E current = iterator.next();
			// pongo il nodo precedente attualmente uguale a first
			Node<E> previousNode = iterator.lastReturned;
			while (iterator.hasNext()) {
				// inizio dal nodo dopo head (se esiste)
				current = iterator.next();
				if (current.equals(o)) {
					// Se l'elemento che stiamo controllando è uguale ad o allora imposto il
					// puntatore del nodo precedente uguale al puntatore dell'elemento.
					previousNode.next = iterator.lastReturned.next;
					// se il puntatore è nullo significa che l'elemento rimosso era la coda quindi
					// imposto la nuova coda e decremento la size ed incremento il numeroModifiche
					if (previousNode.next == null)
						this.last = previousNode;
					// altrimenti rimpiazzo il il nodo da eliminare con il previousNode
					else
						previousNode.next.prev = previousNode;
					// decremento size ed incremento il numero delle modifiche per poi ritornare
					// true
					this.size--;
					this.numeroModifiche++;
					return true;
					// se l'elemento che cerchiamo non è quello corrente imposto il previousNode
					// uguale all'ulitmo elemento verificato dell'iterator
				} else
					// imposto previousNode uguale al nodo appena verificato
					previousNode = iterator.lastReturned;
			}
			// se arriviamo qui significa che l'elemento non è in lista e ritorno false
			return false;
		}
	}

	@Override
	public boolean contains(Object o) {
		if (o == null)
			throw new NullPointerException("o non può essere nullo");
		// tramite un foreach scorro la lista e verifico se
		// è presente almeno un elemento uguale ad o in accordo con equals
		for (E actualNode : this) {
			if (actualNode.equals(o))
				return true;
		}
		return false;
	}

	@Override
	public int size() {
		return this.size;
	}

	/*
	 * Class for representing the nodes of the double-linked list used to implement
	 * this deque. The class and its members/methods are protected instead of
	 * private only for JUnit testing purposes.
	 */
	protected static class Node<E> {
		protected E item;

		protected Node<E> next;

		protected Node<E> prev;

		protected Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.next = next;
			this.prev = prev;
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	/*
	 * Class for implementing an iterator for this deque. The iterator is fail-fast:
	 * it detects if during the iteration a modification to the original deque was
	 * done and, if so, it launches a <code>ConcurrentModificationException</code>
	 * as soon as a call to the method <code>next()</code> is done.
	 */
	private class Itr implements Iterator<E> {
		private Node<E> lastReturned;
		private int numeroModificheAtteso;

		Itr() {
			// All'inizio non è stato fatto nessun next
			this.lastReturned = null;
			this.numeroModificheAtteso = ASDL2223Deque.this.numeroModifiche;
		}

		public boolean hasNext() {
			if (this.lastReturned == null)
				// sono all'inizio dell'iterazione
				return ASDL2223Deque.this.first != null;
			else
				// almeno un next è stato fatto
				return lastReturned.next != null;
		}

		public E next() {
			// controllo per il fail-fast
			if (this.numeroModificheAtteso != ASDL2223Deque.this.numeroModifiche) {
				throw new ConcurrentModificationException("Lista modificata durante l'iterazione");
			}
			// controllo hasNext()
			if (!hasNext())
				throw new NoSuchElementException("Richiesta di next quando hasNext è falso");
			// c'è sicuramente un elemento di cui fare next
			// aggiorno lastReturned e restituisco l'elemento next
			if (this.lastReturned == null) {
				// sono all’inizio e la lista non è vuota
				this.lastReturned = ASDL2223Deque.this.first;
				return lastReturned.item;
			} else {
				// non sono all’inizio, ma c’è ancora qualcuno
				lastReturned = lastReturned.next;
				return lastReturned.item;
			}
		}
	}

	@Override
	public Iterator<E> descendingIterator() {
		return new DescItr();
	}

	/*
	 * Class for implementing a descendign iterator for this deque. The iterator is
	 * fail-fast: it detects if during the iteration a modification to the original
	 * deque was done and, if so, it launches a
	 * <code>ConcurrentModificationException</code> as soon as a call to the method
	 * <code>next()</code> is done.
	 */
	private class DescItr implements Iterator<E> {
		private Node<E> lastReturned;
		private int numeroModificheAtteso;

		DescItr() {
			// All'inizio non è stato fatto nessun next
			this.lastReturned = null;
			this.numeroModificheAtteso = ASDL2223Deque.this.numeroModifiche;
		}

		public boolean hasNext() {
			if (this.lastReturned == null)
				// sono all'inizio dell'iterazione
				return ASDL2223Deque.this.last != null;
			else
				// almeno un next è stato fatto
				return lastReturned.prev != null;
		}

		public E next() {
			// controllo per il fail-fast
			if (this.numeroModificheAtteso != ASDL2223Deque.this.numeroModifiche) {
				throw new ConcurrentModificationException("Lista modificata durante l'iterazione");
			}
			// controllo hasNext()
			if (!hasNext())
				throw new NoSuchElementException("Richiesta di next quando hasNext è falso");
			// c'è sicuramente un elemento di cui fare next
			// aggiorno lastReturned e restituisco l'elemento last
			if (this.lastReturned == null) {
				// sono alla fine della lista e la lista non è vuota
				this.lastReturned = ASDL2223Deque.this.last;
				return lastReturned.item;
			} else {
				// non sono all'inizio ma c’è ancora qualche elemento nella lista
				lastReturned = lastReturned.prev;
				return lastReturned.item;
			}
		}
	}

	/*
	 * This method is only for JUnit testing purposes.
	 */
	protected Node<E> getFirstNode() {
		return this.first;
	}

	/*
	 * This method is only for JUnit testing purposes.
	 */
	protected Node<E> getLastNode() {
		return this.last;
	}

}
