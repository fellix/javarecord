/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.entities;

/**
 * This an single key/object entity
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public class Hash<T, K> {
    private T key;
    private K value;
    /**
     * Default blank Constructor
     * @since 1.0
     */
    public Hash() {
    }
    /**
     * Full Constructor
     * @param key the key value
     * @param value the value of this key
     * @since 1.0
     */
    public Hash(T key, K value) {
        this.key = key;
        this.value = value;
    }
    /**
     * Gets the key
     * @return key of this object
     * @since 1.0
     */
    public T getKey() {
        return key;
    }
    /**
     * Define a new Key
     * @param key a new key
     * @since 1.0
     */
    public void setKey(T key) {
        this.key = key;
    }
    /**
     * Gets the value of this key
     * @return value of this key
     * @since 1.0
     */
    public K getValue() {
        return value;
    }
    /**
     * Define a new value to this key
     * @param value
     * @since 1.0
     */
    public void setValue(K value) {
        this.value = value;
    }



}
