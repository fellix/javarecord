/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.exceptions;

/**
 * An Generic exception for Querys if no Parameters
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snaphsot
 */
public class NoParameterException extends RuntimeException{
    /**
     * Default Construtor
     * @param message the exception message
     * @since 1.0
     */
    public NoParameterException(String message) {
        super(message);
    }

}
