package it.unicam.cs.asdl2223.es4;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class Aula implements Comparable<Aula> {

	/*
	 * numero iniziale delle posizioni dell'array facilities. Se viene richiesto di
	 * inserire una facility e l'array è pieno questo viene raddoppiato. La costante
	 * è protected solo per consentirne l'accesso ai test JUnit
	 */
	protected static final int INIT_NUM_FACILITIES = 5;

	/*
	 * numero iniziale delle posizioni dell'array prenotazioni. Se viene richiesto
	 * di inserire una prenotazione e l'array è pieno questo viene raddoppiato. La
	 * costante è protected solo per consentirne l'accesso ai test JUnit.
	 */
	protected static final int INIT_NUM_PRENOTAZIONI = 100;

	// Identificativo unico di un'aula
	private final String nome;

	// Location dell'aula
	private final String location;

	/*
	 * Insieme delle facilities di quest'aula. L'array viene creato all'inizio della
	 * dimensione specificata nella costante INIT_NUM_FACILITIES. Il metodo
	 * addFacility(Facility) raddoppia l'array qualora non ci sia più spazio per
	 * inserire la facility.
	 */
	private Facility[] facilities;

	// numero corrente di facilities inserite
	private int numFacilities;

	/*
	 * Insieme delle prenotazioni per quest'aula. L'array viene creato all'inizio
	 * della dimensione specificata nella costante INIT_NUM_PRENOTAZIONI. Il metodo
	 * addPrenotazione(TimeSlot, String, String) raddoppia l'array qualora non ci
	 * sia più spazio per inserire la prenotazione.
	 */
	private Prenotazione[] prenotazioni;

	// numero corrente di prenotazioni inserite
	private int numPrenotazioni;

	/**
	 * Costruisce una certa aula con nome e location. Il set delle facilities è
	 * vuoto. L'aula non ha inizialmente nessuna prenotazione.
	 * 
	 * @param nome     il nome dell'aula
	 * @param location la location dell'aula
	 * 
	 * @throws NullPointerException se una qualsiasi delle informazioni richieste è
	 *                              nulla
	 */
	public Aula(String nome, String location) {
		// TODO implementare
		if (nome == null || location == null)
			throw new NullPointerException("la Facility passata è nulla"); //se almeno un paramentro è nullo lancio l'eccezione
		this.nome = nome;
		this.location = location;
		this.facilities = new Facility[INIT_NUM_FACILITIES];
		this.prenotazioni = new Prenotazione[INIT_NUM_PRENOTAZIONI];
	}

	/*
	 * Ridefinire in accordo con equals
	 */
	@Override
	public int hashCode() {
		// TODO implementare
		int hash = 17; //utilizzo dei numeri primi per facilitare il calcolo alla machcina
		return hash = 31 * hash + this.getNome().hashCode(); //cerco di rendere l'hashcode piu unico possibile
	}

	/* Due aule sono uguali se e solo se hanno lo stesso nome */
	@Override
	public boolean equals(Object obj) {
		// TODO implementare
		if (obj == null) //se obj è nullo
			return false;
		if (this.getClass() != obj.getClass()) //se obj non appartiene alla stessa classe di this
			return false;
		if (this == obj) 
			return true;
		Aula other = (Aula) obj; //casto obj come un oggetto Aula
		return this.getNome().equals(other.getNome()); //verifico che i nomi delle aulee siano equivalenti
	}

	/* L'ordinamento naturale si basa sul nome dell'aula */
	@Override
	public int compareTo(Aula o) {
		// TODO implementare
		return this.getNome().compareTo(o.getNome()); //utilizzo il compareTo delle stringhe
	}

	/**
	 * @return the facilities
	 */
	public Facility[] getFacilities() {
		return this.facilities;
	}

	/**
	 * @return il numero corrente di facilities
	 */
	public int getNumeroFacilities() {
		return this.numFacilities;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * @return the prenotazioni
	 */
	public Prenotazione[] getPrenotazioni() {
		return this.prenotazioni;
	}

	/**
	 * @return il numero corrente di prenotazioni
	 */
	public int getNumeroPrenotazioni() {
		return this.numPrenotazioni;
	}

	/**
	 * Aggiunge una faciltity a questa aula. Controlla se la facility è già
	 * presente, nel qual caso non la inserisce.
	 * 
	 * @param f la facility da aggiungere
	 * @return true se la facility non era già presente e quindi è stata aggiunta,
	 *         false altrimenti
	 * @throws NullPointerException se la facility passata è nulla
	 */
	public boolean addFacility(Facility f) {
		/*
		 * Nota: attenzione! Per controllare se una facility è già presente bisogna
		 * usare il metodo equals della classe Facility.
		 * 
		 * Nota: attenzione bis! Si noti che per le sottoclassi di Facility non è
		 * richiesto di ridefinire ulteriormente il metodo equals...
		 */
		// TODO implementare
		if (f == null) //se f è nullo lancio l'eccezione
			throw new NullPointerException("La Facility non deve essere nulla");
		if (this.numFacilities == 0) { //se l'array è vuoto aggiungo f
			this.facilities[this.numFacilities++] = f;
			return true;
		}
		for (int i = 0; i < this.numFacilities; i++) { //verifico se f è gia presente nell'insieme delle facilities dell'aula
			if (this.facilities[i].equals(f)) //se è gia presente non lo aggiungo di nuovo
				return false;
		}
		this.facilities[this.numFacilities++] = f; //arrivati a questo punto sappiamo che f non è gia presente nell'insieme quindi lo aggiungiamo
		if (this.numFacilities == this.facilities.length) //una volta aggiunto se siamo arrivati al massimo numero di elementi nell'insieme ne creo uno nuovo lungo il doppio
			newFacilities(); //richiamo il metodo private che ho creato per questa procedura
		return true;
	}

	/**
	 * Determina se l'aula è libera in un certo time slot.
	 * 
	 * 
	 * @param ts il time slot da controllare
	 * 
	 * @return true se l'aula risulta libera per tutto il periodo del time slot
	 *         specificato
	 * @throws NullPointerException se il time slot passato è nullo
	 */
	public boolean isFree(TimeSlot ts) {
		// TODO implementare
		if (ts == null) //se ts è nullo lancio l'eccezione
			throw new NullPointerException("Il TimeSlot non può essere nullo");
		for (int i = 0; i < this.numPrenotazioni; i++) { //verifico che tutti i timeslot delle prenotazioni dell'aula non si sovrappongano con ts
			if (this.prenotazioni[i].getTimeSlot().getMinutesOfOverlappingWith(ts) != -1)
				return false;
		}
		return true;
	}

	/**
	 * Determina se questa aula soddisfa tutte le facilities richieste rappresentate
	 * da un certo insieme dato.
	 * 
	 * @param requestedFacilities l'insieme di facilities richieste da soddisfare,
	 *                            sono da considerare solo le posizioni diverse da
	 *                            null
	 * @return true se e solo se tutte le facilities di {@code requestedFacilities}
	 *         sono soddisfatte da questa aula.
	 * @throws NullPointerException se il set di facility richieste è nullo
	 */
	public boolean satisfiesFacilities(Facility[] requestedFacilities) {
		// TODO implementare
		if (requestedFacilities == null) //se requestedfacilities è nullo lancio l'ecezione
			throw new NullPointerException("Le Facilities richieste sono nulle");
		int satisfiedFacilities = 0; //satisfiedFacilities++ quando una facility di requestedFacilities è soddisfatta
		for (int i = 0; i < requestedFacilities.length; i++) {
			if (requestedFacilities[i] == null) { //se la facility è nulla sarà sempre soddisfatta poiche non richiede nulla
				satisfiedFacilities++;
				continue; //interrompo questo ciclo e passo al ciclo dopo di i
			}
			for (int j = 0; j < this.numFacilities; j++) { //verifico se una Facility di facilities soddisfa una requestedFacilities
				if (this.facilities[j].satisfies(requestedFacilities[i])) {
					satisfiedFacilities++; 
					break; //se soddisfa interrompo il ciclo in corso e passo al ciclo dopo di i
				}
			}
		}
		if (satisfiedFacilities == requestedFacilities.length) //se satisfiedFacilities è uguale alla lunghezza di requestedFacilities
			return true;                                       //significa che sono tutte soddisfatte e quindi ritorno true
		else
			return false;
	}

	/**
	 * Prenota l'aula controllando eventuali sovrapposizioni.
	 * 
	 * @param ts
	 * @param docente
	 * @param motivo
	 * @throws IllegalArgumentException se la prenotazione comporta una
	 *                                  sovrapposizione con un'altra prenotazione
	 *                                  nella stessa aula.
	 * @throws NullPointerException     se una qualsiasi delle informazioni
	 *                                  richieste è nulla.
	 */
	public void addPrenotazione(TimeSlot ts, String docente, String motivo) {
		// TODO implementare
		if (ts == null || docente == null || motivo == null) //se uno o piu parametri è nullo lancio l'eccezione
			throw new NullPointerException("Uno o più valori inseriti sono nulli");
		if (!this.isFree(ts)) //se il ts richiesto va in sovrapposizione con un altro timeSlot lancio l'eccezione
			throw new IllegalArgumentException("Il TimeSlot scelto entra in sovrapposizione con un altra prenotazione");
		Prenotazione a = new Prenotazione(this, ts, docente, motivo); //se non sono state lanciate eccezioni la prenotazione viene aggiunta
		this.prenotazioni[this.numPrenotazioni++] = a;
		if (this.numPrenotazioni == this.prenotazioni.length) //una volta aggiunta se siamo arrivati al massimo numero di elementi nell'insieme ne creo uno nuovo lungo il doppio
			newPrenotazioni(); //richiamo il metodo private che ho creato per questa procedura
	}

	// TODO inserire eventuali metodi privati per questioni di organizzazione
	private void newPrenotazioni() {
		Prenotazione[] prenotazioni = new Prenotazione[this.numPrenotazioni * 2]; //creo un array lungo il doppio del precedente
		for (int i = 0; i < this.numPrenotazioni; i++) { //copio sul nuovo array quello vecchio
			prenotazioni[i] = this.prenotazioni[i];
		}
		this.prenotazioni = prenotazioni; //faccio puntare il riferimento del vecchio array sul contenuto del nuovo array
	}

	private void newFacilities() {
		Facility[] facilities = new Facility[this.numFacilities * 2]; //creo un array lungo il doppio del precedente
		for (int i = 0; i < this.numFacilities; i++) { //copio sul nuovo array quello vecchio
			facilities[i] = this.facilities[i];
		}
		this.facilities = facilities; //faccio puntare il riferimento del vecchio array sul contenuto del nuovo array
	}
}
