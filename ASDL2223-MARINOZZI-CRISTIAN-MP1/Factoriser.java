package it.unicam.cs.asdl2223.mp1;

/**
 * Un fattorizzatore è un agente che fattorizza un qualsiasi numero naturale nei
 * sui fattori primi.
 *
 * @author Luca Tesei (template)
 * cristian.marinozzi@studenti.unicam.it DELLO STUDENTE Cristian Marinozzi (implementazione)
 */
public class Factoriser {
    private CrivelloDiEratostene crivello;

    /**
     * Fattorizza un numero restituendo la sequenza crescente dei suoi fattori
     * primi. La molteplicità di ogni fattore primo esprime quante volte il fattore
     * stesso divide il numero fattorizzato. Per convenzione non viene mai
     * restituito il fattore 1. Il minimo numero fattorizzabile è 1. In questo caso
     * viene restituito un array vuoto.
     *
     * @param n un numero intero da fattorizzare
     * @return un array contenente i fattori primi di n
     * @throws IllegalArgumentException se si chiede di fattorizzare un numero
     *                                  minore di 1.
     */

    /*
     * Verifica prima di tutto n lanciando le Exception dovute.
     * Se n è uguale a 1 restituisce un insieme Vuoto di Fattori primi.
     * Viene creato poi il crivello di n che trova tutti i numeri primi fino ad n.
     * Se n è un numero primo allora restituisce un insieme contenente solamente n e la sua moltiplicità(1).
     * In tutti gli altri casi viene chiamato il metodo fillFactors che come dice il nome riempirà l'insieme
     * dei fattori primi di n con la loro moltiplicità. Questo metodo si occuperà anche di restituire l'insieme finale.
     */
    public Factor[] getFactors(int n) {
        if (n < 1)
            throw new IllegalArgumentException("Il numero da fattorizzare deve essere >= ad 1");
        if (n == 1) return new Factor[0];
        this.crivello = new CrivelloDiEratostene(n);
        Factor[] factors = new Factor[1];
        if (crivello.isPrime(n)) {
            factors[0] = new Factor(n, 1);
            return factors;
        } else return fillFactors(factors, n);
    }

    /*
     * Finché il crivello ha un numero primo successivo a quello verificato precedentemente(inizialmente successivo a 1)
     * vedo se il numero che mi è rimasto da fattorizzare (toFactorise) è divisibile per quel numero primo.
     * Se è divisibile calcolo anche la sua moltiplicità continuando a dividere toFactorise per
     * il numero primo ed incrementando di 1 la moltiplicità ad ogni passo.
     * Quando il toFactorise non è piu divisibile per il numero primo in questione
     * creo un nuovo Factor con il numero primo e la sua moltiplicità e richiamo il metodo incrementSizeAndAdd il quale
     * si occuperà di creare un altro Array di Factor lungo quello precedente+1 per poi inserire in questa posizione aggiuntiva
     * il Fattore appena creato. Infine si passa al prossimo numero primo e si ripete il procedimento.
     * Quando finiscono i numeri primi da verificare il metodo restituisce la fattorizzazione del numero richiesto.
     */
    private Factor[] fillFactors(Factor[] factors, int toFactorise) {
        while (this.crivello.hasNextPrime()) {
            int prime = crivello.nextPrime();
            int multiplicity = 0;
            if (toFactorise % prime == 0) {
                while (toFactorise % prime == 0) {
                    toFactorise = toFactorise / prime;
                    multiplicity++;
                }
                factors = incrementSizeAndAdd(factors, new Factor(prime, multiplicity));
            }
        }
        return factors;
    }

    /*
     * Aggiunge il newFactor dato in input all'Array di Factor sempre dato in input.
     * Se l'Array è vuoto allora lo inserirà senza problemi altrimenti crea una copia dell'Array esistente ma più lungo di 1
     * ed infine aggiunge il new Factor all'ultima posizione restituendo poi il nuovo Array.
     * Ho deciso di gestire cosi la lunghezza dell'Array di Factor perche mi sembrava un modo originale
     * rispetto alle mie passate esperienze. Ovviamente poteva essere fatto in maniera più semplice e forse più veloce ma non
     * essendo l'efficenza di Factoriser importante per l'esito del mini progetto ho deciso di affrontare così il problema
     * per sperimentare qualcosa di nuovo.
     */
    private Factor[] incrementSizeAndAdd(Factor[] factors, Factor newFactor) {
        if (factors[0] == null) {
            factors[0] = newFactor;
            return factors;
        }
        Factor[] result = new Factor[factors.length + 1];
        for (int i = 0; i < factors.length; i++) {
            result[i] = factors[i];
        }
        result[result.length - 1] = newFactor;
        return result;
    }
}
