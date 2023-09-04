/**
 * 
 */
package it.unicam.cs.asdl2223.es4;

/**
 * Una Presence Facility è una facility che può essere presente oppure no. Ad
 * esempio la presenza di un proiettore HDMI oppure la presenza dell'aria
 * condizionata.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class PresenceFacility extends Facility {

	/**
	 * Costruisce una presence facility.
	 * 
	 * @param codice
	 * @param descrizione
	 * @throws NullPointerException se una qualsiasi delle informazioni richieste è
	 *                              nulla.
	 */
	public PresenceFacility(String codice, String descrizione) {
		super(codice, descrizione);  //richiamando il costruttore di FAcility non ho bisogno di nuovi controlli
		// TODO implementare

	}

	/*
	 * Una Presence Facility soddisfa una facility solo se la facility passata è una
	 * Presence Facility ed ha lo stesso codice.
	 * 
	 */
	@Override
	public boolean satisfies(Facility o) {
		// TODO implementare
		if(o==null) throw new NullPointerException("la Facility passata è nulla"); //se o è nullo lancio l'eccezione
		if (this.getClass() == o.getClass() && this.getCodice().equals(o.getCodice())) //se le classi dei due oggetti ed il loro codice si equivale
			return true;
		else
			return false;
	}

}
