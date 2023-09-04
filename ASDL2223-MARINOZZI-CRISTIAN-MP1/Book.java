/**
 *
 */
package it.unicam.cs.asdl2223.mp1;

import java.util.Objects;

/**
 * Un oggetto di questa classe rappresenta un libro. Oltre le informazioni
 * tipiche ci sono la lunghezza, la larghezza e il peso. Queste servono per
 * implementare i metodi dell'interfaccia ShelfItem. Un libro è identificato dal
 * suo codice ISBN.
 *
 * @author Luca Tesei (template)
 * cristian.marinozzi@studenti.unicam.it DELLO STUDENTE Cristian Marinozzi (implementazione)
 */
public class Book implements ShelfItem {
    private final String author;

    private final String title;

    private final int year;

    private final String publisher;

    private final String iSBN;

    private final double length;

    private final double width;

    private final double weight;

    /**
     * @param author    stringa contenente l'autore
     * @param title     stringa contente il titolo
     * @param year      anno di pubblicazione
     * @param publisher stringa con il nome dell'editore
     * @param iSBN      codice univoco del libro
     * @param length    lunghezza in cm
     * @param width     larghezza in cm
     * @param weight    peso in grammi
     */
    public Book(String author, String title, int year, String publisher, String iSBN, double length, double width,
                double weight) {
        this.author = author;
        this.title = title;
        this.year = year;
        this.publisher = publisher;
        this.iSBN = iSBN;
        this.length = length;
        this.width = width;
        this.weight = weight;
    }

    @Override
    public double getLength() {
        return this.length;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @return the iSBN
     */
    public String getiSBN() {
        return iSBN;
    }

    /*
     * Due libri sono uguali se e solo se hanno lo stesso codice ISBN.
     */

    /*
     * Sovrascrivo il metodo equals in modo tale che se il riferimento dell'oggeto dato è nullo
     * oppure non appartiene alla stessa classe di this restituisca false.
     * Nel caso in cui il riferimento di this sia lo stesso dell'oggetto dato restituisco true,
     * Nel caso in cui i riferimenti non combacino restituisco true solo se i due oggetti hanno lo stesso ISBN.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        if (this == obj)
            return true;
        Book other = (Book) obj;
        return Objects.equals(this.getiSBN(), other.getiSBN());
    }

    /*
     * Hashcode definito in base all'ISBN, per compatibilità con equals.
     */

    /*
     * Sovrascrivo l'hashcode per renderlo equivalente al metodo equals che ho elaborato.
     */
    @Override
    public int hashCode() {
        // TODO implementare
        int hash = 17;
        return 31 * hash + this.getiSBN().hashCode();
    }
}
