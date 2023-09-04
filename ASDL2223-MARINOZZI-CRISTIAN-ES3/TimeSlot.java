/**
 * 
 */
package it.unicam.cs.asdl2223.es3;

import java.util.Calendar;

// TODO completare gli import se necessario

import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 * 
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

	/**
	 * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione di
	 * due Time Slot. Se si sovrappongono per un numero di minuti minore o uguale a
	 * questa soglia allora NON vengono considerati sovrapposti.
	 */
	public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

	private final GregorianCalendar start;

	private final GregorianCalendar stop;

	/**
	 * Crea un time slot tra due istanti di inizio e fine
	 * 
	 * @param start inizio del time slot
	 * @param stop  fine del time slot
	 * @throws NullPointerException     se uno dei due istanti, start o stop, è null
	 * @throws IllegalArgumentException se start è uguale o successivo a stop
	 */
	public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {
		// TODO implementare
		if (start == null || stop == null) //verifico che lo start e lo stop non siano nulli
			throw new NullPointerException("Start o/e Stop è/sono null");
		if (start.compareTo(stop) == -1) { //verifico tramite il compareTo (scritto da me) 
			this.start = start;            //per vedere se lo start è sempre prima dello stop.
			this.stop = stop;
		} else
			throw new IllegalArgumentException("Start non può essere uguale o successivo a stop");
	}

	/**
	 * @return the start
	 */
	public GregorianCalendar getStart() {
		return start;
	}

	/**
	 * @return the stop
	 */
	public GregorianCalendar getStop() {
		return stop;
	}

	/*
	 * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
	 * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
	 * stesso istante.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) //se l'oggetto chiamato è uguale ad obj
			return true; 
		if (obj == null) //se obj è nullo
			return false;
		if (this.getClass() != obj.getClass()) //se l'oggetto chiamato è 
			return false;                      //un'istanza della stesse classe di obj.
		TimeSlot other = (TimeSlot) obj;
		return Objects.equals(start, other.start) && Objects.equals(stop, other.stop); //ora verifico il contenuto degli 
	}                                                                                  //oggetti per determinare se sono uguali.

	/*
	 * Il codice hash associato a un timeslot viene calcolato a partire dei due
	 * istanti di inizio e fine, in accordo con i campi usati per il metodo equals.
	 */
	@Override
	public int hashCode() {
		int hash = 17; //uso i numeri primi per facilitare il calcolo della macchina
		hash = 31 * hash + this.getStart().hashCode(); //cerco di rendere l'hashcode piu unico possibile
		hash = 31 * hash + this.getStop().hashCode();
		return hash;
	}

	/*
	 * Un time slot precede un altro se inizia prima. Se due time slot iniziano
	 * nello stesso momento quello che finisce prima precede l'altro. Se hanno
	 * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
	 */
	@Override
	public int compareTo(TimeSlot o) {
		// TODO implementare
		if (o == null) //se il parametro del metodo è nullo.
			throw new NullPointerException("Il TimeSLot è nullo");
		if (this.start.before(o.getStart())) //se l'oggetto TimeSlot chiamato inizia
			return -1;                       //prima del TimeSlot dato come parametro.
		else if (this.start.equals(o.getStart()) && this.stop.before(o.getStop())) //se i due TimeSlot iniziano insieme
			return -1;                                                             //ma quello chiamato finisce prima.
		else if (this.start.equals(o.getStart()) && this.stop.equals(o.getStop())) //se i due TimeSlot iniziano e finiscono insieme.
			return 0;
		return 1;
	}

	/**
	 * Determina il numero di minuti di sovrapposizione tra questo timeslot e quello
	 * passato.
	 * 
	 * @param o il time slot da confrontare con questo
	 * @return il numero di minuti di sovrapposizione tra questo time slot e quello
	 *         passato, oppure -1 se non c'è sovrapposizione. Se questo time slot
	 *         finisce esattamente al millisecondo dove inizia il time slot
	 *         <code>o</code> non c'è sovrapposizione, così come se questo time slot
	 *         inizia esattamente al millisecondo in cui finisce il time slot
	 *         <code>o</code>. In questi ultimi due casi il risultato deve essere -1
	 *         e non 0. Nel caso in cui la sovrapposizione non è di un numero esatto
	 *         di minuti, cioè ci sono secondi e millisecondi che avanzano, il
	 *         numero dei minuti di sovrapposizione da restituire deve essere
	 *         arrotondato per difetto
	 * @throws NullPointerException     se il time slot passato è nullo
	 * @throws IllegalArgumentException se i minuti di sovrapposizione superano
	 *                                  Integer.MAX_VALUE
	 */
	public int getMinutesOfOverlappingWith(TimeSlot o) {
		// TODO implementare
		if (o == null)
			throw new NullPointerException("Il TimeSLot è nullo");
		double minutes = 0;
		if ((this.getStart().before(o.getStart()) && this.getStop().after(o.getStart()) //caso in cui il TimeSlot chiamato 
				&& this.getStop().before(o.getStop())))                                 //finisce dopo l'inizio del parametro.
			minutes = (this.getStop().getTimeInMillis() - o.getStart().getTimeInMillis()) / 60000; //trasformo in millisecondi per poi ricavare i minuti.
		else if ((this.getStart().after(o.getStart()) && this.getStart().before(o.getStop()) //caso in cui il TimeSlot chiamato 
				&& this.getStop().after(o.getStop())))                                       //inizia prima della fine del parametro.
			minutes = (o.getStop().getTimeInMillis() - this.getStart().getTimeInMillis()) / 60000; //trasformo in millisecondi per poi ricavare i minuti.
		else if ((this.getStart().after(o.getStart()) && this.getStop().before(o.getStop()))) //caso in cui il TimeSlot inizia e finisce mentro il parametro è in corso.
			minutes = (this.getStop().getTimeInMillis() - this.getStart().getTimeInMillis()) / 60000; //trasformo in millisecondi per poi ricavare i minuti.
		else if ((this.getStart().before(o.getStart()) && this.getStop().after(o.getStop()))) //caso in cui il TimeSlot inizia prima e finisce dopo il paramentro.
			minutes = (o.getStop().getTimeInMillis() - o.getStart().getTimeInMillis()) / 60000; //trasformo in millisecondi per poi ricavare i minuti.
		else
			return -1;
		if ((int) minutes > Integer.MAX_VALUE) //casto i minuti in int per vedere se superano il massimo valore di un intero.
			throw new IllegalArgumentException("i minuti di sovrapposizione superano Integer.MAX_VALUE");
		else
			return (int) minutes; //ritorno i minuti senza secondi arrotondandoli.
	}

	/**
	 * Determina se questo time slot si sovrappone a un altro time slot dato,
	 * considerando la soglia di tolleranza.
	 * 
	 * @param o il time slot che viene passato per il controllo di sovrapposizione
	 * @return true se questo time slot si sovrappone per più (strettamente) di
	 *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
	 * @throws NullPointerException se il time slot passato è nullo
	 */
	public boolean overlapsWith(TimeSlot o) {
		// TODO implementare
		if (o == null) //verifico che il paramentro non sia nullo.
			throw new NullPointerException("Il TimeSLot è nullo");
		if (this.getMinutesOfOverlappingWith(o) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) //richiamo il metodo fatto in precedenza per ottenere i minuti 
			return true;                                                                //di overlap e verifico che siano maggiori di 5m.
		else
			return false;
	}

	/*
	 * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
	 * 
	 * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
	 * 
	 * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
	 * 
	 * I secondi e i millisecondi eventuali non vengono scritti.
	 */
	@Override
	public String toString() {
		// TODO implementare
		return "[" + this.getStart().get(Calendar.DAY_OF_MONTH) + "/" + this.getStart().get(Calendar.MONTH) + "/" //concateno le stringhe per ottenere 
				+ this.getStart().get(Calendar.YEAR) + " " + this.getStart().get(Calendar.HOUR_OF_DAY) + "."      //inizio del TimeSlot ed il suo stop.
				+ this.getStart().get(Calendar.MINUTE) + " - " + this.getStop().get(Calendar.DAY_OF_MONTH) + "/"
				+ this.getStop().get(Calendar.MONTH) + "/" + this.getStop().get(Calendar.YEAR) + " "
				+ this.getStop().get(Calendar.HOUR_OF_DAY) + "." + this.getStop().get(Calendar.MINUTE) + "]";
	}

}
