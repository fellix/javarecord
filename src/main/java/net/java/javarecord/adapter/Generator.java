/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.adapter;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.java.javarecord.JavaRecord;

/**
 * This class is used for generates commands to communicates with any database server.</p>
 * With JDBC use de SQLGenerator, for CouchDB CouchGenerator.</p>
 * This especification is in the adapter configuration.
 * @author Rafael Felix da Silva
 * @since 1.0-snapshot
 * @version 1.0
 */
public interface Generator {
    /**
     * Load all object attributes from the database
     * @since 1.0
     */
    void loadAttributes();
    /**
     * Retrieve the loaded attributes from the database
     * @return Map containing the attributes
     * @since 1.0
     */
    Map<String, Object> getAttributes();
    /**
     * Defines the new Attributes to the Generator
     * @param attributes to set
     * @since 1.0
     */
    void setAttributes(Map<String, Object> attributes);
    /**
     * Save execute insert or update
     * @since 1.0
     * @see #insert()
     * @see #update()
     */
    void save();
    /**
     * Execute an update in the database
     * @since 1.0
     */
    void update();
    /**
     * Execute an insert in the database
     * @since 1.0
     */
    void insert();
    /**
     * Remove the registry from the database
     * @since 1.0
     */
    void remove();
    /**
     * Execute an query in the database filtering by the conditions
     * @param conditions to find an registry
     * @return List of found registers
     * @since 1.0
     */
    List<? extends JavaRecord> find(Class<? extends JavaRecord> entityClass, Properties conditions);
}
