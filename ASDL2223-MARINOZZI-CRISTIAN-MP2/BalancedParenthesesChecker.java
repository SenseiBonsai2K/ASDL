package it.unicam.cs.asdl2223.mp2;

/**
 * An object of this class is an actor that uses an ASDL2223Deque<Character> as
 * a Stack in order to check that a sequence containing the following
 * characters: '(', ')', '[', ']', '{', '}' in any order is a string of balanced
 * parentheses or not. The input is given as a String in which white spaces,
 * tabs and newlines are ignored.
 * 
 * Some examples:
 * 
 * - " (( [( {\t (\t) [ ] } ) \n ] ) ) " is a string o balanced parentheses - "
 * " is a string of balanced parentheses - "(([)])" is NOT a string of balanced
 * parentheses - "( { } " is NOT a string of balanced parentheses - "}(([]))" is
 * NOT a string of balanced parentheses - "( ( \n [(P)] \t ))" is NOT a string
 * of balanced parentheses
 * 
 * @author Template: Luca Tesei, Implementation: Cristian Marinozzi -
 *         cristian.marinozzi@studenti.unicam.it
 */
public class BalancedParenthesesChecker {

	// The stack is to be used to check the balanced parentheses
	private ASDL2223Deque<Character> stack;

	/**
	 * Create a new checker.
	 */
	public BalancedParenthesesChecker() {
		this.stack = new ASDL2223Deque<Character>();
	}

	/**
	 * Check if a given string contains a balanced parentheses sequence of
	 * characters '(', ')', '[', ']', '{', '}' by ignoring white spaces ' ', tabs
	 * '\t' and newlines '\n'.
	 * 
	 * @param s the string to check
	 * @return true if s contains a balanced parentheses sequence, false otherwise
	 * @throws IllegalArgumentException if s contains at least a character different
	 *                                  form:'(', ')', '[', ']', '{', '}', white
	 *                                  space ' ', tab '\t' and newline '\n'
	 */
	public boolean check(String s) {
		//pulisco lo stack come primo passaggio
		this.stack.clear();
		//creo un ciclo per scorrere i caratteri della stringa
		for (int i = 0; i < s.length(); i++) {
			char current = s.charAt(i);
			//verifico se il carattere all'indice corrente sia uguale ad una parentesi in apertura
			//se lo è aggiungo la parentesi alla testa dello stack e continuo con la computazione del ciclo.
			if (current == '{' || current == '[' || current == '(') {
				this.stack.push(current);
				continue;
			//se nell'evenienza trovo uno dei caratteri da ignorare semplicemente 
			//lo ignoro continuando la computazione del ciclo
			} else if (current == ' ' || current == '\t' || current == '\n')
				continue;
			//per tutti gli altri caratteri diversi dalle parentesi di chiusura e
			// dai caratteri nelle condizioni precedenti verrà lanciata l'eccezione
			else if (current != ')' && current != ']' && current != '}')
				throw new IllegalArgumentException("La stringa contiene dei caratteri che non sono permessi");
			//se lo stack a questo punto è vuoto possiamo tranquillamente 
			//ritornare false perche non sono mai state aperti parentesi
			if (this.stack.isEmpty())
				return false;
			//questa variabile di appoggio serve per salvare il primo elemento rimosso dallo stack
			//(sicuramente una parentesi di apertura) per poi verificarne la correttezza tramite lo switch.
			char poppedChar;
			switch (current) {
			//se il primo elemento è una parentesi in chiusura dello stesso tipo della parentesi poppata(poppedChar)
			//la computazione del ciclo continua altrimenti il for si interrompe lasciando lo stack con almeno 1 elemento al suo interno
			case ')':
				poppedChar = (char) this.stack.pop();
				if (poppedChar == '[' || poppedChar == '{')
					return false;
				break;
				//stesso funzionamento del caso con la parentesi tonda ma riapplicato alla quadra
			case ']':
				poppedChar = (char) this.stack.pop();
				if (poppedChar == '(' || poppedChar == '{')
					return false;
				break;
				//stesso funzionamento del caso con la parenesi tonda ma riapplicato alla graffa
			case '}':
				poppedChar = (char) this.stack.pop();
				if (poppedChar == '(' || poppedChar == '[')
					return false;
				break;
			}
		}
		//se il ciclo è stato interrotto lo stack avrà almeno 1 elemento all'interno e di conseguenza verrà restituito false.
		//se il ciclo non è stato interrotto lo stack sarà vuoto dato che tutte le parentesi hanno trovato la loro chiusura
		//e verrà restituito true 
		return this.stack.isEmpty();
	}
}
