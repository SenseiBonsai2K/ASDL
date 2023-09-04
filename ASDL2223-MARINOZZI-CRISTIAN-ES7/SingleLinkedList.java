package it.unicam.cs.asdl2223.es7;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Lista concatenata singola che non accetta valori null, ma permette elementi
 * duplicati. Le seguenti operazioni non sono supportate:
 * 
 * <ul>
 * <li>ListIterator<E> listIterator()</li>
 * <li>ListIterator<E> listIterator(int index)</li>
 * <li>List<E> subList(int fromIndex, int toIndex)</li>
 * <li>T[] toArray(T[] a)</li>
 * <li>boolean containsAll(Collection<?> c)</li>
 * <li>addAll(Collection<? extends E> c)</li>
 * <li>boolean addAll(int index, Collection<? extends E> c)</li>
 * <li>boolean removeAll(Collection<?> c)</li>
 * <li>boolean retainAll(Collection<?> c)</li>
 * </ul>
 * 
 * L'iteratore restituito dal metodo {@code Iterator<E> iterator()} è fail-fast,
 * cioè se c'è una modifica strutturale alla lista durante l'uso dell'iteratore
 * allora lancia una {@code ConcurrentMopdificationException} appena possibile,
 * cioè alla prima chiamata del metodo {@code next()}.
 * 
 * @author Luca Tesei
 *
 * @param <E> il tipo degli elementi della lista
 */
public class SingleLinkedList<E> implements List<E> {

	private int size;

	private Node<E> head;

	private Node<E> tail;

	private int numeroModifiche;

	/**
	 * Crea una lista vuota.
	 */
	public SingleLinkedList() {
		this.size = 0;
		this.head = null;
		this.tail = null;
		this.numeroModifiche = 0;
	}

	/*
	 * Classe per i nodi della lista concatenata. E' dichiarata static perché gli
	 * oggetti della classe Node<E> non hanno bisogno di accedere ai campi della
	 * classe principale per funzionare.
	 */
	private static class Node<E> {
		private E item;

		private Node<E> next;

		/*
		 * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
		 */
		Node(E item, Node<E> next) {
			this.item = item;
			this.next = next;
		}

	}

	/*
	 * Classe che realizza un iteratore per SingleLinkedList. L'iteratore deve
	 * essere fail-fast, cioè deve lanciare una eccezione
	 * ConcurrentModificationException se a una chiamata di next() si "accorge" che
	 * la lista è stata cambiata rispetto a quando l'iteratore è stato creato.
	 * 
	 * La classe è non-static perché l'oggetto iteratore, per funzionare
	 * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
	 * principale presso cui è stato creato.
	 */
	private class Itr implements Iterator<E> {

		private Node<E> lastReturned;

		private int numeroModificheAtteso;

		private Itr() {
			// All'inizio non è stato fatto nessun next
			this.lastReturned = null;
			this.numeroModificheAtteso = SingleLinkedList.this.numeroModifiche;
		}

		@Override
		public boolean hasNext() {
			if (this.lastReturned == null)
				// sono all'inizio dell'iterazione
				return SingleLinkedList.this.head != null;
			else
				// almeno un next è stato fatto
				return lastReturned.next != null;

		}

		@Override
		public E next() {
			// controllo concorrenza
			if (this.numeroModificheAtteso != SingleLinkedList.this.numeroModifiche) {
				throw new ConcurrentModificationException("Lista modificata durante l'iterazione");
			}
			// controllo hasNext()
			if (!hasNext())
				throw new NoSuchElementException("Richiesta di next quando hasNext è falso");
			// c'è sicuramente un elemento di cui fare next
			// aggiorno lastReturned e restituisco l'elemento next
			if (this.lastReturned == null) {
				// sono all’inizio e la lista non è vuota
				this.lastReturned = SingleLinkedList.this.head;
				return SingleLinkedList.this.head.item;
			} else {
				// non sono all’inizio, ma c’è ancora qualcuno
				lastReturned = lastReturned.next;
				return lastReturned.item;
			}

		}

	}

	/*
	 * Una lista concatenata è uguale a un'altra lista se questa è una lista
	 * concatenata e contiene gli stessi elementi nello stesso ordine.
	 * 
	 * Si noti che si poteva anche ridefinire il metodo equals in modo da accettare
	 * qualsiasi oggetto che implementi List<E> senza richiedere che sia un oggetto
	 * di questa classe:
	 * 
	 * obj instanceof List
	 * 
	 * In quel caso si può fare il cast a List<?>:
	 * 
	 * List<?> other = (List<?>) obj;
	 * 
	 * e usando l'iteratore si possono tranquillamente controllare tutti gli
	 * elementi (come è stato fatto anche qui):
	 * 
	 * Iterator<E> thisIterator = this.iterator();
	 * 
	 * Iterator<?> otherIterator = other.iterator();
	 * 
	 * ...
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!(obj instanceof SingleLinkedList))
			return false;
		SingleLinkedList<?> other = (SingleLinkedList<?>) obj;
		// Controllo se entrambe liste vuote
		if (head == null) {
			if (other.head != null)
				return false;
			else
				return true;
		}
		// Liste non vuote, scorro gli elementi di entrambe
		Iterator<E> thisIterator = this.iterator();
		Iterator<?> otherIterator = other.iterator();
		while (thisIterator.hasNext() && otherIterator.hasNext()) {
			E o1 = thisIterator.next();
			// uso il polimorfismo di Object perché non conosco il tipo ?
			Object o2 = otherIterator.next();
			// il metodo equals che si usa è quello della classe E
			if (!o1.equals(o2))
				return false;
		}
		// Controllo che entrambe le liste siano terminate
		return !(thisIterator.hasNext() || otherIterator.hasNext());
	}

	/*
	 * L'hashcode è calcolato usando gli hashcode di tutti gli elementi della lista.
	 */
	@Override
	public int hashCode() {
		int hashCode = 1;
		// implicitamente, col for-each, uso l'iterator di questa classe
		for (E e : this)
			hashCode = 31 * hashCode + e.hashCode();
		return hashCode;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public boolean contains(Object o) {
		if (o == null)
			throw new NullPointerException("o non può essere nullo");
		for (E actualNode : this) {
			if (actualNode.equals(o))
				return true;
		}
		return false;
	}

	@Override
	public boolean add(E e) {
		if (e == null)
			throw new NullPointerException("e non può essere nullo");
		if (this.head == null) {
			this.head = new Node<E>(e, null);
			this.tail = this.head;
		} else {
			this.tail.next = new Node<E>(e, null);
			this.tail = this.tail.next;
		}
		this.numeroModifiche++;
		this.size++;
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (o == null)
			throw new NullPointerException("o non pùò essere nullo");
		if (this.head == null)
			return false;
		// Se o è la testa della lista allora faccio diventare l'elemetno dopo la testa
		if (this.head.item.equals(o)) {
			this.head = this.head.next;
			this.numeroModifiche++;
			size--;
			return true;
		}
		Itr iterator = new Itr();
		// faccio un next perchè ho gia verificato head ma controllo prima che la lista
		// non sia formata solo dalla testa
		if (iterator.hasNext())
			iterator.next();
		else
			return false;
		// previousNode inzia da head
		Node<E> previousNode = iterator.lastReturned;
		E element;
		while (iterator.hasNext()) {
			// inizio dal nodo dopo head (se esiste)
			element = iterator.next();
			if (element.equals(o)) {
				// Se l'elemento che stiamo controllando è uguale ad o allora imposto il
				// puntatore
				// del nodo precedente uguale al puntatore dell'elemento.
				previousNode.next = iterator.lastReturned.next;
				// se il puntatore è nullo significa che l'elemento rimosso era la coda quindi
				// imposto la nuova coda
				if (previousNode.next == null)
					this.tail = previousNode;
				this.size--;
				this.numeroModifiche++;
				return true;
			} else
				// imposto previousNode uguale al nodo appena verificato
				previousNode = iterator.lastReturned;
		}
		return false;
	}

	@Override
	public void clear() {
		// imposto a null e a 0 tutti i parametri della lista
		this.head = null;
		this.tail = null;
		this.numeroModifiche = 0;
		this.size = 0;
	}

	@Override
	public E get(int index) {
		// creo un counter in parallelo con il for each per ottenere E dell'indice
		// corretto
		int indexOfList = 0;
		for (E actualNode : this) {
			if (indexOfList == index) {
				return actualNode;
			} else
				indexOfList++;
		}
		throw new IndexOutOfBoundsException("L'indice è fuori dalla della lista");
	}

	@Override
	public E set(int index, E element) {
		if (element == null)
			throw new NullPointerException("L'elemento non può essere nullo");
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("L'indice è fuori dalla della lista");
		// se la testa corrisponde all'elemento cercato allora creo un nuovo nodo che
		// punta alla testa
		// ed imposto la testa della lista uguale al nuovo nodo
		if (index == 0 && this.head.item.equals(element)) {
			E result = this.head.item;
			this.head.item = element;
			this.numeroModifiche++;
			return result;
		}
		Itr iterator = new Itr();
		// faccio partire l'iteratore dalla testa perche ho gia fatto il controllo.
		// Non serve inserire il controllo di hasNext perchè sappiamo che l'indice in
		// input deve essere sempre <= della size dell lista
		iterator.next();
		E result = null;
		// faccio partire l'indice da 1 perche la testa l'ho gia controllata
		int indexOfList = 1;
		while (iterator.hasNext()) {
			iterator.next();
			// se gli indici corrispondono allora ho trovato l'elemento da sostituire
			if (indexOfList == index) {
				// mi salvo il vecchio item e lo cambio con il nuovo item. Non mi serve cambiare
				// i puntatori.
				result = iterator.lastReturned.item;
				iterator.lastReturned.item = element;
				this.numeroModifiche++;
				break;
			} else {
				// se gli indici non corrispondono incremento l'indice di 1.
				indexOfList++;
			}
		}
		return result;
	}

	@Override
	public void add(int index, E element) {
		// Questo non va non sono riuscito a capire il perche
		if (element == null)
			throw new NullPointerException("L'elemento non può essere nullo");
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException("L'indice è fuori dalla della lista");
		// se l'indice è zero creo un nuovo nodo che punta alla testa della lista 
		// e poi imposto la testa della lista su questo nodo
		if (index == 0) {
			Node<E> newNode = new Node<E>(element, this.head);
			this.head = newNode;
			this.size++;
			this.numeroModifiche++;
		} else {
			Itr iterator = new Itr();
			// faccio partire l'iteratore dalla testa perche ho gia fatto il controllo.
			// Non serve inserire il controllo di hasNext perchè sappiamo che l'indice in
			// input deve essere sempre <= della size dell lista
			iterator.next();
			// creo un nodo per salvare il nodo precedente a quello che si sta verificando 
			Node<E> previousNode = iterator.lastReturned;
			// faccio partire l'indice da 1 perche la testa l'ho gia controllata
			int indexOfList = 1;
			while (iterator.hasNext()) {
				// prendo l'elemento dopo la testa
				iterator.next();
				// se gli indici corrispondono allora creo un nuovo nodo con l'elemento in input 
				// ed il puntatore verso lo stesso nodo che era puntato dal nodo precedente.
				// poi faccio puntare il nodo precedente al nuovo nodo.
				if (indexOfList == index) {
					Node<E> newNode = new Node<E>(element, previousNode.next);
					previousNode.next = newNode;
					this.size++;
					this.numeroModifiche++;
				} else {
					previousNode = iterator.lastReturned;
					indexOfList++;
				}
			}
		}
	}

	@Override
	public E remove(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("L'indice è fuori dalla della lista");
		// se l'indice è uguale a 0 vuol dire che si vuole rimuovere la testa.
		// quindi salvo l'item da rimuovere, faccio diventare la testa della lista
		// uguale al prossimo Nodo
		// e diminuisco la size della lista aumentando le modifiche.
		if (index == 0) {
			E result = this.head.item;
			this.head = this.head.next;
			this.numeroModifiche++;
			this.size--;
			return result;
		}
		Itr iterator = new Itr();
		// faccio partire l'iteratore dalla testa perche ho gia fatto il controllo.
		// Non serve inserire il controllo di hasNext perchè sappiamo che l'indice in
		// input deve essere sempre <= della size dell lista
		iterator.next();
		Node<E> previousNode = iterator.lastReturned;
		E result = null;
		// faccio partire l'indice da 1 perche la testa l'ho gia controllata
		int indexOfList = 1;
		while (iterator.hasNext()) {
			// prendo l'elemento dopo la testa
			iterator.next();
			// se gli indici corrispondono allora salvo l'item che andrà rimosso e poi
			// faccio puntare il Nodo
			// precedente al nodo successivo a quello dell'indice.
			// il garbage recupererà gli oggetti senza puntatori
			if (indexOfList == index) {
				result = iterator.lastReturned.item;
				previousNode.next = iterator.lastReturned.next;
				this.size--;
				this.numeroModifiche++;
				break;
			} else {
				// se non corrispondo imposto il nodoprecedente uguale a
				// quello appena verificato e incremento il counter di 1
				previousNode = iterator.lastReturned;
				indexOfList++;
			}
		}
		return result;
	}

	@Override
	public int indexOf(Object o) {
		if (o == null)
			throw new NullPointerException("L'elemento non può essere nullo");
		// creo una variabile per salvarela posizione del primo elemento uguale ad o
		int firstIndexFound = 0;
		for (E actualNode : this) {
			if (actualNode.equals(o)) {
				// se corrisponde restituisco la posizione
				return firstIndexFound;
			} else
				// se non corrisponde incremento l'indice
				firstIndexFound++;
		}
		// se arriviamo fino a qui significa che l'elemento non è stato trovato quindi
		// restituisco -1.
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		if (o == null)
			throw new NullPointerException("L'elemento non può essere nullo");
		// creo una variabile per salvare l'ultimo elemento uguale ad o ed una per
		// scorrere la lista
		int lastIndexFound = -1;
		int indexOfList = 0;
		for (E actualNode : this) {
			if (actualNode.equals(o)) {
				// se corrisponde salvo questa posizione ed incremento la lista
				lastIndexFound = indexOfList;
				indexOfList++;
			} else
				// se non corrisponde incremento solo la lista
				indexOfList++;
		}
		// restituisco l'ultima occorrenza dell'elemento cercato
		return lastIndexFound;
	}

	@Override
	public Object[] toArray() {
		// creo un array di dimensione uguale alla size della lista
		Object[] array = new Object[this.size];
		int index = 0;
		// scorro tutta la lista aggiungendo mano a mano gli elementi all'array
		for (E element : this) {
			array[index] = element;
			index++;
		}
		return array;
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException("Operazione non supportata.");
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException("Operazione non supportata.");
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("Operazione non supportata.");
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("Operazione non supportata.");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("Operazione non supportata.");
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException("Operazione non supportata.");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("Operazione non supportata.");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Operazione non supportata.");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Operazione non supportata.");
	}
}
