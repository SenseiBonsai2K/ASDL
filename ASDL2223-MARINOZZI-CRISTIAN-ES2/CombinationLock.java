package it.unicam.cs.asdl2223.es2;

/**
 * Un oggetto cassaforte con combinazione ha una manopola che può essere
 * impostata su certe posizioni contrassegnate da lettere maiuscole. La
 * serratura si apre solo se le ultime tre lettere impostate sono uguali alla
 * combinazione segreta.
 * 
 * @author Luca Tesei
 */
public class CombinationLock {

	// TODO inserire le variabili istanza che servono
	private String aCombination = "";
	private String bCombination = "";
	private String lastTry = "";
	private boolean isOpen = true;

	/**
	 * Costruisce una cassaforte <b>aperta</b> con una data combinazione
	 * 
	 * @param aCombination la combinazione che deve essere una stringa di 3 lettere
	 *                     maiuscole dell'alfabeto inglese
	 * @throw IllegalArgumentException se la combinazione fornita non è una stringa
	 *        di 3 lettere maiuscole dell'alfabeto inglese
	 * @throw NullPointerException se la combinazione fornita è nulla
	 */
	public CombinationLock(String aCombination) {
		// TODO implementare
		if (aCombination == null)
			throw new NullPointerException("la combinazione non deve essere nulla");
		if (aCombination.length() != 3)
			throw new IllegalArgumentException(
					"La combinazione deve contenere 3 lettere maiuscole dell'alfabeto inglese");
		for (int i = 0; i < 3; i++) {
			if (aCombination.codePointAt(i) < 65 || aCombination.codePointAt(i) > 90)
				throw new IllegalArgumentException(
						"La combinazione deve contenere 3 lettere maiuscole dell'alfabeto inglese");
		}
		this.aCombination = aCombination;
	}

	public String getaCombination() {
		return aCombination;
	}

	/**
	 * Imposta la manopola su una certaposizione.
	 * 
	 * @param aPosition un carattere lettera maiuscola su cui viene impostata la
	 *                  manopola
	 * @throws IllegalArgumentException se il carattere fornito non è una lettera
	 *                                  maiuscola dell'alfabeto inglese
	 */
	public void setPosition(char aPosition) {
		if ((int) aPosition >= 65 && (int) aPosition <= 90)
			this.bCombination += aPosition;
		else
			throw new IllegalArgumentException(
					"La manopola deve essere impostata su una lettera maiuscola dell'alfabeto inglese");
	}

	/**
	 * Tenta di aprire la serratura considerando come combinazione fornita le ultime
	 * tre posizioni impostate. Se l'apertura non va a buon fine le lettere
	 * impostate precedentemente non devono essere considerate per i prossimi
	 * tentativi di apertura.
	 */
	public void open() {
		// TODO implementare
		if (this.bCombination.length() < 3) {
			this.isOpen = false;
			this.bCombination = "";
		} else {
			if (!this.lastTry.equals(this.bCombination.substring(this.bCombination.length() - 3))) {
				this.lastTry = this.bCombination.substring(this.bCombination.length() - 3);
				if (this.lastTry.equals(this.aCombination))
					this.isOpen = true;
				else {
					this.isOpen = false;
					this.bCombination = "";
				}
			}
		}
	}

	/**
	 * Determina se la cassaforte è aperta.
	 * 
	 * @return true se la cassaforte è attualmente aperta, false altrimenti
	 */
	public boolean isOpen() {
		// TODO implementare
		return this.isOpen;
	}

	/**
	 * Chiude la cassaforte senza modificare la combinazione attuale. Fa in modo che
	 * se si prova a riaprire subito senza impostare nessuna nuova posizione della
	 * manopola la cassaforte non si apre. Si noti che se la cassaforte era stata
	 * aperta con la combinazione giusta le ultime posizioni impostate sono proprio
	 * la combinazione attuale.
	 */
	public void lock() {
		// TODO implementare
		this.isOpen = false;
	}

	/**
	 * Chiude la cassaforte e modifica la combinazione. Funziona solo se la
	 * cassaforte è attualmente aperta. Se la cassaforte è attualmente chiusa rimane
	 * chiusa e la combinazione non viene cambiata, ma in questo caso le le lettere
	 * impostate precedentemente non devono essere considerate per i prossimi
	 * tentativi di apertura.
	 * 
	 * @param aCombination la nuova combinazione che deve essere una stringa di 3
	 *                     lettere maiuscole dell'alfabeto inglese
	 * @throw IllegalArgumentException se la combinazione fornita non è una stringa
	 *        di 3 lettere maiuscole dell'alfabeto inglese
	 * @throw NullPointerException se la combinazione fornita è nulla
	 */
	public void lockAndChangeCombination(String aCombination) {
		// TODO implementare
		if (this.isOpen) {
			if (aCombination == null)
				throw new NullPointerException("la combinazione non deve essere nulla");
			if (aCombination.length() != 3)
				throw new IllegalArgumentException(
						"La combinazione deve contenere 3 lettere maiuscole dell'alfabeto inglese");
			for (int i = 0; i < 3; i++) {
				if (aCombination.codePointAt(i) < 65 || aCombination.codePointAt(i) > 90)
					throw new IllegalArgumentException(
							"La combinazione deve contenere 3 lettere maiuscole dell'alfabeto inglese");
			}
			this.aCombination = aCombination;
			this.isOpen = false;
		} else
			this.bCombination = "";
	}
}