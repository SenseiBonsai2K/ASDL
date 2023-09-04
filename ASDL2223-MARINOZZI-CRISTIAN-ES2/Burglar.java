package it.unicam.cs.asdl2223.es2;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei
 *
 */
public class Burglar {
	// TODO inserire le variabili istanza che servono
	private int attemps = 0;
	private CombinationLock second;
	private String burglarCombination = "";
	private int index = 0;

	/**
	 * Costruisce uno scassinatore per una certa cassaforte.
	 * 
	 * @param aCombinationLock
	 * @throw NullPointerException se la cassaforte passata è nulla
	 */
	public Burglar(CombinationLock aCombinationLock) {
		// TODO implementare
		if (aCombinationLock == null)
			throw new NullPointerException("La cassaforte non può essere nulla");
		this.second = aCombinationLock;
	}

	/**
	 * Forza la cassaforte e restituisce la combinazione.
	 * 
	 * @return la combinazione della cassaforte forzata.
	 */
	public String findCombination() {
			for (int i = 65; i <= 90; i++) {
				if(this.burglarCombination.equals(second.getaCombination())) break;
				if (i == second.getaCombination().codePointAt(this.index)) {
					this.burglarCombination += (char) i;
					this.index++;
					this.attemps++;
					findCombination();
				} else
					this.attemps++;
			}
		return this.burglarCombination;
	}

	/**
	 * Restituisce il numero di tentativi che ci sono voluti per trovare la
	 * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
	 * 
	 * @return il numero di tentativi che ci sono voluti per trovare la
	 *         combinazione, oppure -1 se la cassaforte non è stata ancora forzata.
	 */
	public long getAttempts() {
		// TODO implementare
		if (this.burglarCombination == "")
			return -1;
		else
			return this.attemps;
	}
}
