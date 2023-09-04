/**
 * 
 */
package it.unicam.cs.asdl2223.mp1;

import java.util.Objects;

/**
 * Un oggetto di questa classe rappresenta una lampada che ha un appoggio
 * circolare. Implementa l'interfaccia ShelfItem, ma come lunghezza e larghezza
 * ha il diametro della base. Ridefinisce il metodo di default per calcolare la
 * superficie occupata restituiendo l'area del cerchio che corrisponde alla
 * base. Una lampada è identificata dal nome e dal nome del brand.
 * 
 * @author Luca Tesei (template)
 * cristian.marinozzi@studenti.unicam.it DELLO STUDENTE Cristian Marinozzi (implementazione)
 */
public class RoundLamp implements ShelfItem {

	private final double diameter;

	private final double weight;

	private final String name;

	private final String brandName;

	/**
	 * @param diameter  diametro della base in cm
	 * @param weight    peso in grammi
	 * @param name      nome del modello della lampada
	 * @param brandName nome del brand della lampada
	 */
	public RoundLamp(double diameter, double weight, String name, String brandName) {
		this.diameter = diameter;
		this.weight = weight;
		this.name = name;
		this.brandName = brandName;
	}

	/*
	 * Restituisce l'area del cerchio corrispondente alla base
	 */

	/*
	 * Sovrascrivo il metodo della superclasse dato che il calcolo per la superficie è diverso
	 */
	@Override
	public double getOccupiedSurface() {
		double raggio = this.getDiameter() / 2;
		return Math.PI * Math.pow(raggio, 2);
	}

	/*
	 * Restituisce il diametro della base
	 */

	@Override
	public double getLength() {
		return this.diameter;
	}

	/*
	 * Restituisce il diametro della base
	 */
	@Override
	public double getWidth() {
		return this.diameter;
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * @return the diameter
	 */
	public double getDiameter() {
		return diameter;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/*
     * Sovrascrivo il metodo equals in modo tale che se il riferimento dell'oggeto dato è nullo
     * oppure non appartiene alla stessa classe di this restituisca false.
     * Nel caso in cui il riferimento di this sia lo stesso dell'oggetto dato restituisco true,
     * Nel caso in cui i riferimenti non combacino restituisco true solo se i due oggetti hanno
     * lo stesso nome e lo stesso brand.
     */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		if (this == obj)
			return true;
		RoundLamp other = (RoundLamp) obj;
		return Objects.equals(this.getName(), other.getName())
				&& Objects.equals(this.getBrandName(), other.getBrandName());
	}

	/*
	 * L'hashcode viene calcolato usando gli stessi campi usati per definire
	 * l'uguaglianza
	 */

	/*
	 * Sovrascrivo l'hashcode per renderlo equivalente al metodo equals che ho elaborato.
	 */
	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + this.getName().hashCode();
		return 31 * hash + this.getBrandName().hashCode();
	}
}
