/**
 * 
 */
package it.unicam.cs.asdl2223.es1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
class EquazioneSecondoGradoModificabileConRisolutoreTest {
	/*
	 * Costante piccola per il confronto di due numeri double
	 */
	static final double EPSILON = 1.0E-15;

	@Test
	final void testEquazioneSecondoGradoModificabileConRisolutore() {
		// controllo che il valore 0 su a lanci l'eccezione
		assertThrows(IllegalArgumentException.class, () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1, 1));
		// devo controllare che comunque nel caso normale il costruttore
		// funziona
		EquazioneSecondoGradoModificabileConRisolutore eq = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
		// Controllo che all'inizio l'equazione non sia risolta
		assertFalse(eq.isSolved());
	}

	@Test
	final void testGetA() {
		double x = 10;
		EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(x, 1, 1);
		// controllo che il valore restituito sia quello che ho messo
		// all'interno
		// dell'oggetto
		assertTrue(x == e1.getA());
		// in generale si dovrebbe usare assertTrue(Math.abs(x -
		// e1.getA())<EPSILON) ma in
		// questo caso il valore che testiamo non ha subito manipolazioni quindi
		// la sua rappresentazione sarà la stessa di quella inserita nel
		// costruttore senza errori di approssimazione
	}

	@Test
	final void testSetA() {
		double x = 0;
		EquazioneSecondoGradoModificabileConRisolutore a = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
		// verifico che inserire 0 su a lanci l'eccezione.
		assertThrows(IllegalArgumentException.class, () ->a.setA(x)); 
		double y = 2;
		//inserisco y in a
	    a.setA(y);
		// verifico che il valore y sia stato inserito correttamente.
		assertTrue(a.getA() == y); 
	}

	@Test
	final void testGetB() {
		double x = 2;
		EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(1, x, 1);
		// controllo che il valore restituito sia quello che ho messo.
		assertTrue(x == e1.getB()); 
	}

	@Test
	final void testSetB() {
		double x = 2;
		EquazioneSecondoGradoModificabileConRisolutore a = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
		// inserisco il valore x nel secondo parametro.
		a.setB(x); 
		// verifico che il valore x sia stato inserito correttamente.
		assertTrue(a.getB() == x); 
	}

	@Test
	final void testGetC() {
		double x = 2;
		EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, x);
		// controllo che il valore restituito sia quello che ho messo.
		assertTrue(x == e1.getC()); 
	}

	@Test
	final void testSetC() {
		double x = 2;
		EquazioneSecondoGradoModificabileConRisolutore a = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
		// inserisco il valore x nel terzo parametro.
		a.setC(x); 
		// verifico che il valore x sia stato inserito correttamente.
		assertTrue(a.getC() == x); 
	}

	@Test
	final void testIsSolved() {
		EquazioneSecondoGradoModificabileConRisolutore a = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
		// verifico che l'equazione non abbia soluzione.
		assertFalse(a.isSolved());
		// trovo la/e soluzione/i.
		a.solve();
		// verifico che l'equazione abbia soluzione.
		assertTrue(a.isSolved());
	}

	@Test
	final void testSolve() {
		EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 3);
		// controllo semplicemente che la chiamata a solve() non generi errori
		e3.solve();
		// i test con i valori delle soluzioni vanno fatti nel test del metodo
		// getSolution()
	}

	@Test
	final void testGetSolution() {
		EquazioneSecondoGradoModificabileConRisolutore a = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1); // delta minore di 0.
		//verifico che venga lanciate l'eccezione quando a non ha una soluzione
		assertThrows(IllegalStateException.class, () -> a.getSolution());
		// risolvo a.	
		a.solve();
		// verifico che a abbia una soluzione
		a.getSolution();
		EquazioneSecondoGradoModificabileConRisolutore b = new EquazioneSecondoGradoModificabileConRisolutore(3, 5, 1); // delta maggiore di 0.
		//verifico che venga lanciate l'eccezione quando b non ha una soluzione
		assertThrows(IllegalStateException.class, () -> b.getSolution());
		// risolvo b.	
		b.solve();
		// verifico che b abbia una soluzione
		b.getSolution();
		EquazioneSecondoGradoModificabileConRisolutore c = new EquazioneSecondoGradoModificabileConRisolutore(4, 4, 1); // delta uguale a 0
		//verifico che venga lanciate l'eccezione quando c non ha una soluzione
		assertThrows(IllegalStateException.class, () -> c.getSolution());
		// risolvo c.	
		c.solve();
		// verifico che c abbia una soluzione
		c.getSolution();
	}
}
