package it.unicam.cs.asdl2223.mp1;

import java.util.Objects;

/**
 * Un oggetto di quest classe rappresenta un fattore primo di un numero naturale
 * con una certa molteplicità.
 * 
 * @author Luca Tesei (template)
 * cristian.marinozzi@studenti.unicam.it DELLO STUDENTE Cristian Marinozzi (implementazione)
 *
 */
public class Factor implements Comparable<Factor> {

	/*
	 * Numero primo corrispondente a questo fattore
	 */
	private final int primeValue;

	/*
	 * Molteplicità del numero primo di questo fattore, deve essere maggiore o
	 * uguale a 1.
	 */
	private final int multiplicity;

	/**
	 * Crea un fattore primo di un numero naturale, formato da un numero primo e
	 * dalla sua molteplicità.
	 * 
	 * @param primeValue,   numero primo
	 * @param multiplicity, valore della molteplicità, deve essere almeno 1
	 * @throws IllegalArgumentException se la molteplicità è minore di 1 oppure se
	 *                                  primeValue è minore o uguale di 0.
	 */

	/*
	 * Costruisco un oggetto Factor che contiene un numero primo e la sua moltiplicità
	 */
	public Factor(int primeValue, int multiplicity) {
		if (primeValue <= 0 || multiplicity < 1)
			throw new IllegalArgumentException(
					"Il numero deve essere >= di 0 e la molteplicità deve essere almeno pari a 1");
		this.primeValue = primeValue;
		this.multiplicity = multiplicity;
	}

	/**
	 * @return the primeValue
	 */
	public int getPrimeValue() {
		return primeValue;
	}

	/**
	 * @return the multiplicity
	 */
	public int getMultiplicity() {
		return multiplicity;
	}

	/*
	 * Calcola l'hashcode dell'oggetto in accordo ai valori usati per definire il
	 * metodo equals.
	 */

	/*
	 * Sovrascrivo l'hashcode per renderlo equivalente al metodo equals che ho elaborato.
	 */
	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + this.getPrimeValue();
		return hash = 31 * hash + this.getMultiplicity();
	}

	/*
	 * Due oggetti Factor sono uguali se e solo se hanno lo stesso numero primo e la
	 * stessa molteplicità
	 */

	/*
	 * Sovrascrivo il metodo equals in modo tale che se il riferimento dell'oggeto dato è nullo
	 * oppure non appartiene alla stessa classe di this restituisca false.
	 * Nel caso in cui il riferimento di this sia lo stesso dell'oggetto dato restituisco true,
	 * Nel caso in cui i riferimenti non combacino restituisco true solo se i due oggetti
	 * hanno lo stesso numero primo e la stessa moltiplicità.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		if (this == obj)
			return true;
		Factor other = (Factor) obj;
		return Objects.equals(this.getPrimeValue(), other.getPrimeValue())
				&& Objects.equals(this.getMultiplicity(), other.getMultiplicity());
	}

	/*
	 * Un Factor è minore di un altro se contiene il numero primo minore. Se due
	 * Factor hanno lo stesso numero primo allora il più piccolo dei due è quello ce
	 * ha minore molteplicità.
	 */

	/*
	 * Sovrascrivo il metodo compareTo in modo tale che sarà più grande il Factor con il numero primo maggiore.
	 * In caso siano uguali sarà piu grande il Factor con la moltiplicità maggiore.
	 * In caso siano uguali anch'esse i due Factor saranno identici.
	 */
	@Override
	public int compareTo(Factor o) {
		if (this.getPrimeValue() < o.getPrimeValue())
			return -1;
		else if (this.getPrimeValue() > o.getPrimeValue())
			return 1;
		else if (this.getMultiplicity() < o.getMultiplicity())
			return -1;
		else if (this.getMultiplicity() > o.getMultiplicity())
			return 1;
		else
			return 0;
	}

	/*
	 * Il fattore viene reso con la stringa primeValue^multiplicity
	 */

	public String toString() {
		return this.getPrimeValue() + "^" + this.getMultiplicity();
	}
}
