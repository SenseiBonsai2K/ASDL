package it.unicam.cs.asdl2223.mp1;

/**
 * Un oggetto di questa classe rappresenta una mensola su cui possono essere
 * appoggiati degli oggetti. Tali oggetti possono essere di diverso tipo, ma
 * tutti implementano l'interface ShelfItem. Un oggetto non può essere
 * appoggiato sulla mensola se ha lunghezza o larghezza che eccedono quelle
 * della mensola stessa. La mensola può contenere un numero non precisato di
 * oggetti, ma ad un certo punto non si possono appoggiare oggetti la cui
 * superficie occupata o il cui peso fanno eccedere la massima superficie
 * occupabile o il massimo peso sostenibile definiti nel costruttore della
 * mensola.
 * 
 * @author Luca Tesei (template)
 * cristian.marinozzi@studenti.unicam.it DELLO STUDENTE Cristian Marinozzi (implementazione)
 */
public class Shelf {
	/*
	 * Dimensione iniziale dell'array items. Quando non è più sufficiente l'array
	 * deve essere raddoppiato, anche più volte se necessario.
	 */
	private final int INITIAL_SIZE = 5;

	/*
	 * massima lunghezza di un oggetto che può essere appoggiato sulla mensola in cm
	 */
	private final double maxLength;

	/*
	 * massima larghezza di un oggetto che può essere appoggiato sulla mensola in cm
	 */
	private final double maxWidth;

	/*
	 * massima superficie occupabile della mensola in cm^2
	 */
	private final double maxOccupableSurface;

	/*
	 * massimo peso sostenibile dalla mensola in grammi
	 */
	private final double maxTotalWeight;

	/*
	 * array contenente tutti gli oggetti attualmente poggiati sulla mensola. In
	 * caso di necessità viene raddoppiato nel momento che si poggia un nuovo
	 * oggetto che fa superare la capacità dell'array.
	 */
	private ShelfItem[] items;

	/*
	 * variabile che indica il numero corrente di caselle nell'array che sono
	 * occupate
	 */
	private int numberOfItems;

	/**
	 * Costruisce una mensola con le sue caratteristiche. All'inizio nessun oggetto
	 * è posato sulla mensola.
	 * 
	 * @param maxLength           lunghezza massima di un oggetto appoggiabile in cm
	 * @param maxWidth            larghezza massima di un oggetto appoggiabile in cm
	 * @param maxOccupableSurface massima superficie occupabile di questa mensola in
	 *                            cm^2
	 * @param maxTotalWeight      massimo peso sostenibile da questa mensola in
	 *                            grammi
	 */
	public Shelf(double maxLength, double maxWidth, double maxOccupableSurface, double maxTotalWeight) {
		this.maxLength = maxLength;
		this.maxWidth = maxWidth;
		this.maxOccupableSurface = maxOccupableSurface;
		this.maxTotalWeight = maxTotalWeight;
		this.items = new ShelfItem[INITIAL_SIZE];
		this.numberOfItems = 0;
	}

	/**
	 * Aggiunge un nuovo oggetto sulla mensola. Qualora non ci sia più spazio
	 * nell'array che contiene gli oggetti correnti, tale array viene raddoppiato
	 * per fare spazio al nuovo oggetto.
	 * 
	 * @param i l'oggetto da appoggiare
	 * @return true se l'oggetto è stato inserito, false se è già presente
	 * @throws IllegalArgumentException se il peso dell'oggetto farebbe superare il
	 *                                  massimo peso consentito oopure se la
	 *                                  superficie dell'oggetto farebbe superare la
	 *                                  massima superficie occupabile consentita,
	 *                                  oppure se la lunghezza o larghezza
	 *                                  dell'oggetto superano quelle massime
	 *                                  consentite
	 * @throws NullPointerException     se l'oggetto passato è null
	 */

	/*
	 * Lancia delle eccezioni personalizzate in base al tipo di Exception che viene a verificarsi.
	 * Una volta appurato che i sia un Item valido allora procede con l'inserimento se è il
	 * primo Item della mensola oppure procede a verificare se l'Item è gia presente in questa.
	 * In caso l'Item non sia già presente verrà aggiunto nell'ultima posizione
	 * libera(numberOfItems) che verrà successivamente incrementata di 1.
	 * In caso l'Item sia già presente non verrà aggiunto.
	 * Infine si verificherà se l'Array ha raggiunto la sua massima dimensione.
	 * Se ha raggiunto la sua massima dimensione verrà richiamato il metodo newItems()
	 */
	public boolean addItem(ShelfItem i) {
		// TODO implementare
		if (i == null)
			throw new NullPointerException("l'oggetto da appoggiare non deve essere nullo");
		if (this.getCurrentTotalWeight() + i.getWeight() > this.maxTotalWeight)
			throw new IllegalArgumentException("L'oggetto pesa troppo");
		if (this.getCurrentTotalOccupiedSurface() + i.getOccupiedSurface() > this.maxOccupableSurface)
			throw new IllegalArgumentException("La mensola è piena");
		if (i.getLength() > this.maxLength || i.getWidth() > this.maxWidth)
			throw new IllegalArgumentException(
					"Le misure dell'oggetto superano le misure previste per gli oggeti di questa mensola");
		if (this.getNumberOfItems() == 0) {
			this.items[this.numberOfItems++] = i;
			return true;
		}
		for (int j = 0; j < this.getNumberOfItems(); j++) {
			if (this.items[j].equals(i))
				return false;
		}
		if (this.getNumberOfItems() == this.items.length)
			this.newItems();
		this.items[this.numberOfItems++] = i;
		return true;
	}

	/**
	 * Cerca se è presente un oggetto sulla mensola. La ricerca utilizza il metodo
	 * equals della classe dell'oggetto.
	 * 
	 * @param i un oggetto per cercare sulla mensola un oggetto uguale a i
	 * @return null se sulla mensola non c'è nessun oggetto uguale a i, altrimenti
	 *         l'oggetto x che si trova sulla mensola tale che i.equals(x) == true
	 * @throws NullPointerException se l'oggetto passato è null
	 */

	/*
	 * Ricerca un elemento della mensola diverso da null.
	 * Verifica quindi ogni elemento della mensola fino all'ultimo aggiunto finchè non lo trova.
	 * Quando lo trova ritornerà l'elemento trovato mentre se non viene trovato ritornerà null.
	 */
	public ShelfItem search(ShelfItem i) {
		if (i == null)
			throw new NullPointerException("L'oggetto ricercato non deve essere null");
		for (int j = 0; j < this.getNumberOfItems(); j++) {
			if (this.items[j].equals(i))
				return this.items[j];
		}
		return null;
	}

	/**
	 * @return il numero attuale di oggetti appoggiati sulla mensola
	 */
	public int getNumberOfItems() {
		return this.numberOfItems;
	}

	/*
	 * protected, per solo scopo di JUnit testing
	 */
	protected ShelfItem[] getItems() {
		return this.items;
	}

	/**
	 * @return the currentTotalWeight
	 */

	/*
	 * Prende il peso di ogni elemento presente nella mensola fino all'ultimo aggiunto
	 * e li somma al fine di restituire il peso totale degli elementi sopra la mensola.
	 */
	public double getCurrentTotalWeight() {
		double totalWeight = 0;
		for (int i = 0; i < this.getNumberOfItems(); i++) {
			totalWeight += this.items[i].getWeight();
		}
		return totalWeight;
	}

	/**
	 * @return the currentTotalOccupiedSurface
	 */

	/*
	 * Prende la superficie occupata da ogni elemento presente nell mensola fino all'ultimo aggiunto
	 * e li somma al fine di restituire la superficie totale occupata dagli elementi sopra la mensola.
	 */
	public double getCurrentTotalOccupiedSurface() {
		// TODO implementare
		double occupiedSurface = 0;
		for (int i = 0; i < this.getNumberOfItems(); i++) {
			occupiedSurface += this.items[i].getOccupiedSurface();
		}
		return occupiedSurface;
	}

	/**
	 * @return the maxLength
	 */
	public double getMaxLength() {
		return maxLength;
	}

	/**
	 * @return the maxWidth
	 */
	public double getMaxWidth() {
		return maxWidth;
	}

	/**
	 * @return the maxOccupableSurface
	 */
	public double getMaxOccupableSurface() {
		return maxOccupableSurface;
	}

	/**
	 * @return the maxTotalWeight
	 */
	public double getMaxTotalWeight() {
		return maxTotalWeight;
	}

	/*
	 * Questo metodo serve solamente per raddoppiare la dimensione dell'Array che contiene gli elementi della mensola.
	 * Crea un nuovo Array di dimensione doppia al precedente e poi ci copia tutti gli elementi che erano presenti
	 * nel vecchio Array nello stesso ordine. Infine rimpiazza il riferimento del nuovo Array con quello vecchio.
	 */
	private void newItems() {
		ShelfItem[] items = new ShelfItem[this.getNumberOfItems() * 2];
		for (int i = 0; i < this.getNumberOfItems(); i++) {
			items[i] = this.items[i];
		}
		this.items = items;
	}

}
