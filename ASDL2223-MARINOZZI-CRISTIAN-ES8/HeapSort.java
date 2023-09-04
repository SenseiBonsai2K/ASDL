/**
 * 
 */
package it.unicam.cs.asdl2223.es8;

import java.util.List;

// TODO completare import 

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

	@Override
	public SortingAlgorithmResult<E> sort(List<E> l) {
		if (l == null)
			throw new NullPointerException();
		if (l.size() <= 1)
			return new SortingAlgorithmResult<E>(l, 0);
		int counter = 0;
		for (int i = l.size() - 1; i >= 0; i--) {
			for (int j = i; j >= 0; j--) {
				if (l.get(j).compareTo(l.get(i)) > 0) {
					E temp = l.get(i);
					l.set(i, l.get(j));
					l.set(j, temp);
					counter++;
				}
			}
		}
		return new SortingAlgorithmResult<E>(l, counter);
	}

	@Override
	public String getName() {
		return "HeapSort";
	}

}
