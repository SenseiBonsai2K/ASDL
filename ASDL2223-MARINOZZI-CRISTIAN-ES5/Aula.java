package it.unicam.cs.asdl2223.es5;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

// TODO importare eventuali classi o interfacce che servono

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 */
public class Aula implements Comparable<Aula> {
	// Identificativo unico di un'aula
	private final String nome;

	// Location dell'aula
	private final String location;

	// Insieme delle facilities di quest'aula
	private final Set<Facility> facilities;

	// Insieme delle prenotazioni per quest'aula, segue l'ordinamento naturale
	// delle prenotazioni
	private final SortedSet<Prenotazione> prenotazioni;

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
		if (nome == null)
			throw new NullPointerException("Tentativo di costruire un'Aula senza nome");
		if (location == null)
			throw new NullPointerException("Tentativo di costruire un'Aula senza location");
		this.nome = nome;
		this.location = location;
		this.facilities = new HashSet<Facility>();
		this.prenotazioni = new TreeSet<Prenotazione>();
	}

	/**
	 * Costruisce una certa aula con nome, location e insieme delle facilities.
	 * L'aula non ha inizialmente nessuna prenotazione.
	 * 
	 * @param nome       il nome dell'aula
	 * @param location   la location dell'aula
	 * @param facilities l'insieme delle facilities dell'aula
	 * @throws NullPointerException se una qualsiasi delle informazioni richieste è
	 *                              nulla
	 */
	public Aula(String nome, String location, Set<Facility> facilities) {
		// TODO implementare
		if (nome == null)
			throw new NullPointerException("Tentativo di costruire un'Aula senza nome");
		if (location == null)
			throw new NullPointerException("Tentativo di costruire un'Aula senza location");
		if (facilities == null)
			throw new NullPointerException("Tentativo di costruire un'Aula con facilities nulle");
		this.nome = nome;
		this.location = location;
		this.facilities = facilities;
		this.prenotazioni = new TreeSet<Prenotazione>();
	}

	/*
	 * Ridefinire in accordo con equals
	 */
	@Override
	public int hashCode() {
		// TODO implementare
		int hash = 17;
		return hash = 31 * hash + this.getNome().hashCode();
	}

	/* Due aule sono uguali se e solo se hanno lo stesso nome */
	@Override
	public boolean equals(Object obj) {
		// TODO implementare
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		if (this == obj)
			return true;
		Aula other = (Aula) obj;
		return this.getNome().equals(other.getNome());
	}

	/* L'ordinamento naturale si basa sul nome dell'aula */
	@Override
	public int compareTo(Aula o) {
		// TODO implementare
		return this.getNome().compareTo(o.getNome());
	}

	/**
	 * @return the facilities
	 */
	public Set<Facility> getFacilities() {
		return facilities;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return the prenotazioni
	 */
	public SortedSet<Prenotazione> getPrenotazioni() {
		return prenotazioni;
	}

	/**
	 * Aggiunge una faciltity a questa aula.
	 * 
	 * @param f la facility da aggiungere
	 * @return true se la facility non era già presente e quindi è stata aggiunta,
	 *         false altrimenti
	 * @throws NullPointerException se la facility passata è nulla
	 */
	public boolean addFacility(Facility f) {
		// TODO implementare
		if (f == null)
			throw new NullPointerException("Tentativo di aggiungere una facilities nulla");
		if (this.facilities.contains(f))
			return false;
		this.facilities.add(f);
		return true;
	}

	/**
	 * Determina se l'aula è libera in un certo time slot.
	 * 
	 * @param ts il time slot da controllare
	 * 
	 * @return true se l'aula risulta libera per tutto il periodo del time slot
	 *         specificato
	 * @throws NullPointerException se il time slot passato è nullo
	 */
	public boolean isFree(TimeSlot ts) {
		// TODO implementare
		/*
		 * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in maniera
		 * efficiente: poiché le prenotazioni sono in ordine crescente di time slot se
		 * arrivo a una prenotazione che segue il time slot specificato posso concludere
		 * che l'aula è libera nel time slot desiderato e posso interrompere la ricerca
		 */
		if (ts == null)
			throw new NullPointerException("Il TimeSlot non può essere nullo");
		for (Prenotazione a : this.prenotazioni) {
			if (a.getTimeSlot().getStart().after(ts.getStop()))
				break;
			if (a.getTimeSlot().overlapsWith(ts))
				return false;
		}
		return true;
	}

	/**
	 * Determina se questa aula soddisfa tutte le facilities richieste rappresentate
	 * da un certo insieme dato.
	 * 
	 * @param requestedFacilities l'insieme di facilities richieste da soddisfare
	 * @return true se e solo se tutte le facilities di {@code requestedFacilities}
	 *         sono soddisfatte da questa aula.
	 * @throws NullPointerException se il set di facility richieste è nullo
	 */
	public boolean satisfiesFacilities(Set<Facility> requestedFacilities) {
		// TODO implementare
		if (requestedFacilities == null)
			throw new NullPointerException("Le Facilities richieste sono nulle");
		int satisfiedFacilities = 0;
		for (Facility a : requestedFacilities) {
			for (Facility b : this.facilities) {
				if (a == null || b.satisfies(a)) {
					satisfiedFacilities++;
					break;
				}
			}
		}
		if (satisfiedFacilities == requestedFacilities.size())
			return true;
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
		if (ts == null)
			throw new NullPointerException("Tentativo di aggiungere una prenotazione con ts nullo");
		if (docente == null)
			throw new NullPointerException("Tentativo di aggiungere una prenotazione con docente nullo");
		if (motivo == null)
			throw new NullPointerException("Tentativo di aggiungere una prenotazione con motivo nullo");
		if (this.isFree(ts))
			this.prenotazioni.add(new Prenotazione(this, ts, docente, motivo));
		else
			throw new IllegalArgumentException("Il timeslot scelto è in sovrapposizione con un altra prenotazione");
	}

	/**
	 * Cancella una prenotazione di questa aula.
	 * 
	 * @param p la prenotazione da cancellare
	 * @return true se la prenotazione è stata cancellata, false se non era
	 *         presente.
	 * @throws NullPointerException se la prenotazione passata è null
	 */
	public boolean removePrenotazione(Prenotazione p) {
		// TODO implementare
		if (p == null)
			throw new NullPointerException("La prenotazione non deve essere nulla");
		return this.prenotazioni.remove(p);
	}

	/**
	 * Rimuove tutte le prenotazioni di questa aula che iniziano prima (o
	 * esattamente in) di un punto nel tempo specificato.
	 * 
	 * @param timePoint un certo punto nel tempo
	 * @return true se almeno una prenotazione è stata cancellata, false altrimenti.
	 * @throws NullPointerException se il punto nel tempo passato è nullo.
	 */
	public boolean removePrenotazioniBefore(GregorianCalendar timePoint) {
		// TODO implementare
		/*
		 * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in maniera
		 * efficiente: poiché le prenotazioni sono in ordine crescente di time slot se
		 * ho raggiunto una prenotazione con tempo di inizio maggiore del tempo indicato
		 * posso smettere la procedura
		 */
		if (timePoint == null)
			throw new NullPointerException("Il TimeSlot non può essere nullo");
		Set<Prenotazione> toBeRemovedPrenotations = new HashSet<Prenotazione>();
		for (Prenotazione a : this.prenotazioni) {
			if (a.getTimeSlot().getStart().compareTo(timePoint) == 1)
				break;
			toBeRemovedPrenotations.add(a);

		}
		return this.prenotazioni.removeAll(toBeRemovedPrenotations);
	}
}
