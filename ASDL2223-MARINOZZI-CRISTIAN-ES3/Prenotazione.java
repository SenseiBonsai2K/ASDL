package it.unicam.cs.asdl2223.es3;

import java.util.Objects;

/**
 * Una prenotazione riguarda una certa aula per un certo time slot.
 * 
 * @author Luca Tesei
 *
 */
public class Prenotazione implements Comparable<Prenotazione> {

	private final String aula;

	private final TimeSlot timeSlot;

	private String docente;

	private String motivo;

	/**
	 * Costruisce una prenotazione.
	 * 
	 * @param aula     l'aula a cui la prenotazione si riferisce
	 * @param timeSlot il time slot della prenotazione
	 * @param docente  il nome del docente che ha prenotato l'aula
	 * @param motivo   il motivo della prenotazione
	 * @throws NullPointerException se uno qualsiasi degli oggetti passati è null
	 */
	public Prenotazione(String aula, TimeSlot timeSlot, String docente, String motivo) {
		// TODO implementare
		if (aula == null || timeSlot == null || docente == null || motivo == null) //verifico se ci sono parametri nulli.
			throw new NullPointerException();
		this.aula = aula;
		this.timeSlot = timeSlot;
		this.docente = docente;
		this.motivo = motivo;
	}

	/**
	 * @return the aula
	 */
	public String getAula() {
		return aula;
	}

	/**
	 * @return the timeSlot
	 */
	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	/**
	 * @return the docente
	 */
	public String getDocente() {
		return docente;
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param docente the docente to set
	 */
	public void setDocente(String docente) {
		this.docente = docente;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/**
	 * Due prenotazioni sono uguali se hanno la stessa aula e lo stesso time 
	 * slot. Il docente e il motivo possono cambiare senza influire 
	 * sull'uguaglianza.
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO implementare
		if (this == obj) //se l'oggetto chiamato è uguale ad obj
			return true;
		if (obj == null) //se obj è nullo
			return false;
		if (getClass() != obj.getClass())  //se l'oggetto chiamato è 
			return false;                  //un'istanza della stesse classe di obj.
		Prenotazione other = (Prenotazione) obj;
		return Objects.equals(aula, other.aula) && Objects.equals(timeSlot, other.timeSlot);//ora verifico il contenuto degli 
	}                                                                                       //oggetti per determinare se sono uguali.
	
	/**
	 * L'hashcode di una prenotazione si calcola a partire dai due campi usati
	 * per equals.
	 */
	@Override
	public int hashCode() {
		// TODO implementare
		int hash = 17; //uso i numeri primi per facilitare il calcolo della macchina
		hash = 31 * hash + this.getAula().hashCode(); //cerco di rendere l'hashcode piu unico possibile
		hash = 31 * hash + this.getTimeSlot().getStart().hashCode();
		hash = 31 * hash + this.getTimeSlot().getStop().hashCode();
		return hash;
	}

	/*
	 * Una prenotazione precede un altra in base all'ordine dei time slot. Se due
	 * prenotazioni hanno lo stesso time slot allora una precede l'altra in base
	 * all'ordine tra le aule.
	 */
	@Override
	public int compareTo(Prenotazione o) {
		// TODO implementare
		if (o == null) //se il parametro del metodo è nullo.
			throw new NullPointerException("Non puoi confrontare un null");
		if (this.getTimeSlot().compareTo(o.getTimeSlot()) == 0) //comparo il TimeSlot della Prenotazione chiamata con o per vedere se sono uguali
			return this.getAula().compareTo(o.getAula());
		else
			return this.getTimeSlot().compareTo(o.getTimeSlot());
	}

	@Override
	public String toString() {
		return "Prenotazione [aula = " + aula + ", time slot =" + timeSlot + ", docente=" + docente + ", motivo="
				+ motivo + "]";
	}

}
