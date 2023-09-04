package it.unicam.cs.asdl2223.es4;

import java.util.Objects;

/**
 * Una prenotazione riguarda una certa aula per un certo time slot.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class Prenotazione implements Comparable<Prenotazione> {

	private final Aula aula;

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
	public Prenotazione(Aula aula, TimeSlot timeSlot, String docente, String motivo) {
		// TODO implementare
		if (aula == null || timeSlot == null || docente == null || motivo == null) //se uno dei parametri è nullo lancio l'eccezione
			throw new NullPointerException("Uno o piu parametri passati è null");
		this.aula = aula;
		this.timeSlot = timeSlot;
		this.docente = docente;
		this.motivo = motivo;
	}

	/**
	 * @return the aula
	 */
	public Aula getAula() {
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

	@Override
	public int hashCode() {
		// TODO implementare
		int hash = 17; //utilizzo i numeri primi per rendere piu veloce il calcolo alla macchina
		return hash = 31 * hash + this.getAula().hashCode() + this.getTimeSlot().hashCode(); //cerco di rendere l'hashcode piu unico possibile
	}

	/*
	 * L'uguaglianza è data solo da stessa aula e stesso time slot. Non sono ammesse
	 * prenotazioni diverse con stessa aula e stesso time slot.
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO implementare
		if (obj == null) //se obj è nullo
			return false;
		if (this.getClass() != obj.getClass()) //se this ed obj appartengono alla stessa classee
			return false;
		if (this == obj)
			return true;
		Prenotazione other = (Prenotazione) obj; //casto obj come un oggetto Prenotazione e lo inizializzo come other
		return Objects.equals(this.getAula(), other.getAula()) //ritorno vero se le aule ed i timeSlot si equivalgono
				&& Objects.equals(this.getTimeSlot(), other.getTimeSlot()); //falso altrimenti
	}

	/*
	 * Una prenotazione precede un altra in base all'ordine dei time slot. Se due
	 * prenotazioni hanno lo stesso time slot allora una precede l'altra in base
	 * all'ordine tra le aule.
	 */
	@Override
	public int compareTo(Prenotazione o) {
		// TODO implementare
		if (this.getTimeSlot().compareTo(o.getTimeSlot()) == 0) //utilizzo il compareTo di timeSlot e se è uguale a 0 significa che si equivalgono
			return this.getAula().compareTo(o.getAula()); //utilizzo il compareTo di Aula
		else
			return this.getTimeSlot().compareTo(o.getTimeSlot()); //se arriviamo qui ritornerà sicuramente falso
	}

	@Override
	public String toString() {
		return "Prenotazione [aula = " + aula + ", time slot =" + timeSlot + ", docente=" + docente + ", motivo="
				+ motivo + "]";
	}

}
