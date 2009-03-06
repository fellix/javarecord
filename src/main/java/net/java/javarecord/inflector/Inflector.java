/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.inflector;

/**
 * Class for pluralize inflectors
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public interface Inflector {
    /**
     * Make the changes to an word, searching for the mathcers and replacing for the plural
     * @param word the word for replacement
     * @return the work pluralized
     * @since 1.0
     */
    String pluralize(String word);
    /**
     * Converts an pluralized String to the singular mode
     * @param pluralWord the word pluralized
     * @return the word singularized
     * @since 1.0
     */
    String singularize(String pluralWord);
}
