package it.unicam.cs.asdl2223.mp1;

/**
 * Il crivello di Eratostene è un modo per determinare tutti i numeri primi da
 * {@code 1} a un certo intero {@code n} assegnato. Il crivello può essere
 * semplicemente rappresentato da un array di booleani in cui la posizione i è
 * true se e solo se il numero i è primo. Per costruirlo si segue un semplice
 * algoritmo che parte dalla posizione 2 e la mette a true. Dopodiché vengono
 * messe a false tutte le posizioni multiple di 2. Poi viene determinata la
 * prima posizione successiva a 2 che non è stato messa a false, in questo caso
 * 3. Si passa quindi a mettere a false tutte le posizioni multiple di 3. Si
 * ripete la procedura per la posizione non false successiva, che sarà 5. E così
 * via fino ad aver visitato tutte le caselle minori o uguali alla capacità del
 * crivello.
 * <p>
 * Un oggetto di questa classe permette di conoscere direttamente se un numero
 * tra 2 e la capacità del crivello è primo tramite il metodo isPrime(int).
 * <p>
 * Inoltre un oggetto di questa classe fornisce la funzionalità di elencare
 * tutti i numeri primi da 2 alla capacità del crivello tramite una serie di
 * chiamate al metodo nextPrime(). L'elenco può essere fatto ripartire in
 * qualsiasi momento chiamando il metodo restartPrimeIteration() e si interrompe
 * non appena il metodo hasNextPrime() restituisce false.
 *
 * @author Luca Tesei (template) cristian.marinozzi@studenti.unicam.it DELLO
 *         STUDENTE Cristian Marinozzi (implementazione)
 */

public class CrivelloDiEratostene {
	/*
	 * Array di booleani che rappresenta il crivello. La posizione i dell'arraya è
	 * true se e solo se il numero i è primo altrimenti la posizione i deve essere
	 * false. Le posizioni 0 e 1 dell'array non sono significative e non vengono
	 * usate. L'ultima posizione dell'array deve essere uguale alla capacità del
	 * crivello.
	 */
	private boolean[] crivello;

	/*
	 * Capacità del crivello, immutabile
	 */
	private final int capacity;

	/*
	 * Un intero che indica l'ultima posizione dell'array chaimata dal metodo
	 * nextPrime e che può essere reimpostata a 1 solamente dal metodo
	 * restartPrimeIteration.
	 */
	private int lastCalledPrimeNumber = 1;

	/**
	 * Costruisce e inizializza il crivello di Eratostene fino alla capacità data.
	 * La capacità deve essere almeno 2.
	 *
	 * @param capacity capacità del crivello, almeno 2
	 * @throws IllegalArgumentException se il numero {@code capacity} è minore di
	 *                                  {@code 2}
	 */

	/*
	 * Costruisce un crivello di dimensione pari a capacity+1 e poi imposta tutte le
	 * posizioni a true. viene richiamato il metodo buildCrivello che si occuperà di
	 * cambiare i valori da true a false dei numeri non primi.
	 * infine viene richiamato il metodo restartPrimeIteration che si occuperà di far 
	 * ripartire la verifica dei numeri primi dall'inizio poichè è stata utilizzata
	 * dal metodo buildCrivello.
	 */
	public CrivelloDiEratostene(int capacity) {
		if (capacity < 2)
			throw new IllegalArgumentException("la capacità del crivello deve essere almeno 2");
		this.capacity = capacity;
		this.crivello = new boolean[this.capacity + 1];
		for (int i = 2; i <= this.capacity; i++) {
			this.crivello[i] = true;
		}
		buildCrivello();
		restartPrimeIteration();
	}

	/*
	 * Finchè il crivello ha da controllare un numero primo minore della radice quadrata della
	 * capacità del crivello, lascia il valore true al numero che sta controllando e successivamente
	 * imposta il valore di tutti i suoi multipli a false. Ho utilizzato la radice quadrata perchè
	 * i numeri primi al di sotto della capacità del crivello sono tutti
	 * quelli che si "salvano" dai multipli dei numeri primi al di sotto della 
	 * radice quadrata della capacità del crivello. 
	 * Continua finchè il valore del multiplo è minore della capacità del crivello.
	 */
	private void buildCrivello() {
		while (hasNextPrime()) {
			int lastPrimeFound = nextPrime(); //numero primo attualmente sotto controllo
			if(lastPrimeFound > Math.sqrt(this.capacity)) break;
			int multipleOfLastPrimeFound = lastPrimeFound * 2; //multiplo del numero che verrà verificato
			while (multipleOfLastPrimeFound <= this.capacity) {
				this.crivello[multipleOfLastPrimeFound] = false; //imposto a falso il numero
				multipleOfLastPrimeFound += lastPrimeFound; //prendo il prossimo multiplo del numero sotto controllo
			}
		}
	}

	/**
	 * Restituisce la capacità di questo crivello, cioè il numero massimo di
	 * entrate.
	 *
	 * @return la capacità di questo crivello
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * Controlla se un numero è primo. Può rispondere solo se il numero passato come
	 * parametro è minore o uguale alla capacità di questo crivello.
	 *
	 * @param n il numero da controllare
	 * @return true se il numero passato è primo, false altrimenti
	 * @throws IllegalArgumentException se il numero passato {@code n} eccede la
	 *                                  capacità di questo crivello o se è un numero
	 *                                  minore di 2.
	 */

	/*
	 * Restituisce semplicemente il valore alla posizione n ovvero true se è un
	 * numero primo e false altrimenti.
	 */
	public boolean isPrime(int n) {
		if (n > this.capacity)
			throw new IllegalArgumentException(
					"Il numero è superiore alla capacità di questo crivello oppure è minore di 2");
		if (n < 2)
			throw new IllegalArgumentException("Il numero deve essere almeno 2");
		return this.crivello[n];
	}

	/**
	 * Indica se l'elenco corrente dei numeri primi di questo crivello ha ancora un
	 * numero disponibile da elencare o se l'elenco è giunto al termine perché sono
	 * già stati elencati tutti i numeri primi minori uguali alla capacità. Se il
	 * metodo restituisce true, può essere fatta una ulteriore chiamata al metodo
	 * nextPrime() per ottenere il numero successivo nell'elenco. Se il metodo
	 * restituisce false non si potrà più chiamare il metodo nextPrime() fino a
	 * quando l'elenco non viene fatto ripartire tramite il metodo
	 * restartPrimeIteration().
	 *
	 * @return true se c'è ancora un numero primo nell'elenco dei numeri primi di
	 *         questo livello, false se sono già stati elencati tutti i numeri primi
	 *         di questo crivello.
	 */

	/*
	 * Crea un ciclo a partire dall'ultimo valore di lastCalledPrimeNumber+1 fino
	 * alla capacità del crivello compresa se in questo ciclo viene trovato unnumero
	 * primo il ciclo si interrompe restituendo true altrimenti verrà restituito
	 * false.
	 */
	public boolean hasNextPrime() {
		for (int i = this.lastCalledPrimeNumber + 1; i < this.crivello.length; i++) {
			if (isPrime(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Restituisce il prossimo numero primo in questo crivello nell'elenco corrente.
	 * L'elenco parte sempre dal numero 2 e si interrompe non appena il metodo
	 * hasNextPrime() diventa false. Il metodo lancia l'eccezione
	 * IllegalStateException se si prova a chiedere il prossimo numero primo quando
	 * l'elenco corrente è terminato. L'elenco può essere fatto ripartire in
	 * qualsiasi momento chiamando il metodo restartPrimeIteration().
	 *
	 * @return il prossimo numero primo nell'elenco corrente
	 * @throws IllegalStateException se l'elenco è terminato e non è stato ancora
	 *                               fatto ripartire.
	 */

	/*
	 * Se il metodo hasNextPrime restituisce true significa che è presente almeno un
	 * altro numero primo di conseguenza questo metodo lo va a cercare ed imposta
	 * lastCalledPrimeNumber al valore di questo numero primo e poi ritorna
	 * lastCalledPrimeNumber. se il metodo hasNextPrime restituisce false allora
	 * verrà lanciata un'eccezione.
	 */
	public int nextPrime() {
		if (this.hasNextPrime()) {
			for (int i = this.lastCalledPrimeNumber + 1; i < this.crivello.length; i++) {
				if (isPrime(i)) {
					this.lastCalledPrimeNumber = i;
					break;
				}
			}
		} else
			throw new IllegalStateException("L'elenco è giunto al termine, fallo ripartire.");
		return this.lastCalledPrimeNumber;
	}

	/**
	 * Fa ripartire da 2 l'elenco corrente dei numeri primi fino alla capacità di
	 * questo crivello. Questo metodo può essere chiamato in qualsiasi momento,
	 * anche se l'elenco corrente non è ancora terminato. L'effetto è comunque di
	 * ricominciare da 2.
	 */

	/*
	 * Reimposta la variabile globale lastCalledPrimeNumber al suo valore
	 * iniziale(1) sia che l'elenco sia finito sia che non lo sia. E' impostata ad 1
	 * ma ad ogni chiamata viene incrementata di 1 quindi partirà sempre da 2.
	 */
	public void restartPrimeIteration() {
		this.lastCalledPrimeNumber = 1;
	}
}
