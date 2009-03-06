/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class for relationship with belongs to (N-1) to an object, used in too in the inverse has one association (the class if contains the FK).
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BelongsTo {
    /**
     * The classes with the object know.
     * <pre>
     * Pass an array containing the Class of the objects.
     * </pre>
     * @return Array containing the class if this object has one reference
     * @since 1.0
     */
    Class[] classes();
    /**
     * The name of each FK column in the database.</p>
     * By default it's empty, and the FK name is 'tableName_id'
     * @return the name of each FK
     * @since 1.0
     */
    String[] names() default {};
}
