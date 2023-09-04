package it.unicam.cs.asdl2223.mp3;

import java.util.ArrayList;

/**
 * Class that solves an instance of the the El Mamun's Caravan problem using
 * dynamic programming.
 * <p>
 * Template: Daniele Marchei and Luca Tesei,
 * Implementation: Marinozzi Cristian - cristian.marinozzi@studenti.unicam.it
 */
public class ElMamunCaravanSolver {

    // the expression to analyse
    private final Expression expression;

    // table to collect the optimal solution for each sub-problem,
    // protected just for Junit Testing purposes
    protected Integer[][] table;

    // table to record the chosen optimal solution among the optimal solution of
    // the sub-problems, protected just for JUnit Testing purposes
    protected Integer[][] tracebackTable;

    // flag indicating that the problem has been solved at least once
    private boolean solved;

    private ArrayList<Integer> valuesCandidates;
    private ArrayList<Integer> indexCandidates;

    /**
     * Create a solver for a specific expression.
     *
     * @param expression The expression to work on
     * @throws NullPointerException if the expression is null
     */
    public ElMamunCaravanSolver(Expression expression) {
        if (expression == null)
            throw new NullPointerException("Creazione di solver con expression null");
        this.expression = expression;
        //Inizializzo entrambe le tabelle con colonne e righe pari alla dimenione dell'espressione
        this.table = new Integer[this.expression.size()][this.expression.size()];
        this.tracebackTable = new Integer[this.expression.size()][this.expression.size()];
        //Creo due arraylist per salvare rispettivamente i possibili candidati con i loro risopettivi indici k
        this.valuesCandidates = new ArrayList<Integer>();
        this.indexCandidates = new ArrayList<Integer>();
        //imposto il flag del solved a false
        this.solved = false;
    }

    /**
     * Returns the expression that this solver analyse.
     *
     * @return the expression of this solver
     */
    public Expression getExpression() {
        return this.expression;
    }

    /**
     * Solve the problem on the expression of this solver by using a given objective
     * function.
     *
     * @param function The objective function to be used when deciding which
     *                 candidate to choose
     * @throws NullPointerException if the objective function is null
     */
    public void solve(ObjectiveFunction function) {
        // Se la funzione data in input è nulla throwo
        if (function == null) throw new NullPointerException("La funzione non deve essere nulla");
        //per comodità trasformo la mia espressione in una stringa e vado a lavorarci tramiti il charAt
        String exp = this.expression.toString();
        //utilizzo un indice l per muovermi in diagonale ed un indice i per scorrere ogni volta
        for (int l = 1; l <= this.expression.size(); l = l + 2) {
            for (int i = 0; i <= this.expression.size() - l; i = i + 2) {
                int j = i + l - 1;
                //questa condizione sarà vera solo quando l==1 ed imposterà l'espressione lungo la diagonale maggiore della table
                if (i == j) {
                    this.table[i][j] = ((int) exp.charAt(i)) - 48;
                    continue;
                }
                //richiamo un metodo creato per pulizia del codice che, come dice il nome, si occuperà
                //solamente di aggiungere i candidati alle liste.
                candidatesAdder(exp, i, j);
                //imposto alla posizione i,j il migliore risultato(in base alla funzione in input) tra i candidati trovati
                this.table[i][j] = function.getBest(valuesCandidates);
                //imposto successivamente nella posizione i,j dell'altra tabella il k corrispondente al candidato scelto
                this.tracebackTable[i][j] = this.indexCandidates.get(function.getBestIndex(valuesCandidates));
                //pulisco entrambe le liste per l'utilizzo futuro
                this.valuesCandidates.clear();
                this.indexCandidates.clear();
            }
        }
        // arrivati qui il problema sarà sempre risolto
        this.solved = true;
    }

    private void candidatesAdder(String exp, int i, int j) {
        for (int k = 0; i + k + 2 <= j; k = k + 2) {
            //verifico che il carattere sia un segno +
            if (exp.charAt(i + k + 1) == '+')
                //se lo è sommo gli elementi della tabella per poi aggiungere il risultato alla lista dei candidati
                this.valuesCandidates.add(this.table[i][i + k] + this.table[i + k + 2][j]);
            else
                //altrimenti gli elementi della tabella verranno moltiplicati e poi il risultato verra aggiunto alla lista
                this.valuesCandidates.add(this.table[i][i + k] * this.table[i + k + 2][j]);
            //infine viene aggiunto il k ad ogni candidato
            this.indexCandidates.add(k + i);
        }
    }

    /**
     * Returns the current optimal value for the expression of this solver. The
     * value corresponds to the one obtained after the last solving (which used a
     * particular objective function).
     *
     * @return the current optimal value
     * @throws IllegalStateException if the problem has never been solved
     */
    public int getOptimalSolution() {
        // Verifica se il problema è stato risolto
        if (this.isSolved())
            //se si, restituisce il miglior risultato del problema
            return this.table[0][this.expression.size() - 1];
        //altrimenti throwa
        throw new IllegalStateException("Il problema non è stato ancora risolto");
    }

    /**
     * Returns an optimal parenthesization corresponding to an optimal solution of
     * the expression of this solver. The parenthesization corresponds to the
     * optimal value obtained after the last solving (which used a particular
     * objective function).
     * <p>
     * If the expression is just a digit then the parenthesization is the expression
     * itself. If the expression is not just a digit then the parethesization is of
     * the form "(<parenthesization>)". Examples: "1", "(1+2)", "(1*(2+(3*4)))"
     *
     * @return the current optimal parenthesization for the expression of this
     * solver
     * @throws IllegalStateException if the problem has never been solved
     */
    public String getOptimalParenthesization() {
        // Verifica se il problema è stato risolto
        if (this.isSolved()) {
            //se l'espressione ha solo una cifra restituisco quella cifra
            if (this.expression.size() == 1) return this.expression.toString();
            //altrimenti restituisco la sua parentesizzazione
            return traceBack(0, this.expression.size() - 1);
        }
        //altrimenti throwa
        throw new IllegalStateException("Il problema non è stato ancora risolto");
    }

    private String traceBack(int i, int j) {
        //Qui ho semplicemente seguito le istruzioni date nella consegna del progetto
        //ho quindi construito una ricorsione sulla sottoespressione sinistra collegata
        // dall'operatore ottimale alla sottoespressione destra e cosi via
        if (i == j) return this.table[i][j].toString();
        else return "(" + traceBack(i, this.tracebackTable[i][j]) + "" +
                this.expression.get(this.tracebackTable[i][j] + 1).getValue().toString() + "" +
                traceBack((this.tracebackTable[i][j] + 2), j) + ")";
    }

    /**
     * Determines if the problem has been solved at least once.
     *
     * @return true if the problem has been solved at least once, false otherwise.
     */
    public boolean isSolved() {
        return this.solved;
    }

    @Override
    public String toString() {
        return "ElMamunCaravanSolver for " + expression;
    }
}
