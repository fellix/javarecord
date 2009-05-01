/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord;

/**
 * Generic JavaRecord Exception
 * @author Rafael Felix
 * @version 1.0
 */
public class JavaRecordException extends RuntimeException{

    public JavaRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public JavaRecordException(String message) {
        super(message);
    }
    
}
