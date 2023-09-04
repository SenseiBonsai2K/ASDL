package it.unicam.cs.asdl2223.es4;

/**
 * Una facility generica è una caratteristica o delle dotazioni che una certa
 * aula può avere. La classe va specificata ulteriormente per definire i diversi
 * tipi di facilities.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public abstract class Facility {

	private final String codice;

	private final String descrizione;

	/**
	 * Costruisce una certa facility generica.
	 * 
	 * @param codice      identifica la facility univocamente
	 * @param descrizione descrizione della facility
	 * @throws NullPointerException se una qualsiasi delle informazioni richieste è
	 *                              nulla.
	 */
	public Facility(String codice, String descrizione) {
		// TODO implementare
		if (codice == null || descrizione == null) //se uno o piu dei parametri passati è nullo lancio l'eccezione
			throw new NullPointerException("il codice e la descrizone non devono essere nulli");
		this.codice = codice;
		this.descrizione = descrizione;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/*
	 * Poiché l'uguaglianza è basata sul codice, anche l'hashcode deve essere basato
	 * sul codice
	 */
	@Override
	public int hashCode() {
		// TODO implementare
		int hash = 17; //uso i numeri primi per facilitare il calcolo della macchina
		return hash = 31 * hash + this.getCodice().hashCode(); //cerco di rendere l'hashcode piu unico possibile
	}

	/*
	 * L'uguaglianza di due facilities è basata unicamente sul codice
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO implementare
		if (obj == null) //se obj è nullo
			return false;
		if (this.getClass() != obj.getClass()) //se l'oggetto chiamato è un'istanza della stessa classe di this
			return false;
		if (this == obj) 
			return true;
		Facility other = (Facility) obj; //casto obj come un Facility
		return this.getCodice().equals(other.getCodice()); //vedo se i loro codici si equivalgono
	}

	@Override
	public String toString() {
		return "Facility [codice=" + codice + ", descrizione=" + descrizione + "]";
	}

	/**
	 * Determina se questa facility soddisfa un'altra facility data. Ad esempio se
	 * questa facility indica che è presente un proiettore HDMI, allora essa
	 * soddisfa la facility "presenza di un proiettore HDMI". Un altro esempio: se
	 * questa facility indica un numero di posti a sedere pari a 30, allora essa
	 * soddisfa ogni altra facility che indica che ci sono un numero di posti minore
	 * o uguale a 30. Il metodo dipende dal tipo di facility, per questo è astratto
	 * e va definito nelle varie sottoclassi.
	 * 
	 * @param o l'altra facility con cui determinare la compatibilità
	 * @return true se questa facility soddisfa la facility passata, false
	 *         altrimenti
	 * @throws NullPointerException se la facility passata è nulla.
	 */
	public abstract boolean satisfies(Facility o);
	
}
