/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.javarecord.registry;

import java.util.ArrayList;
import java.util.List;
import net.java.javarecord.adapter.Adapter;
import net.java.javarecord.entities.Config;
import net.java.javarecord.inflector.BaseInflector;
import net.java.javarecord.inflector.Inflector;

/**
 * This class maintains the base unique configuration of the JavaRecord
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public class Registry {

    /**
     * The unique instance to the registry class
     * @since 1.0
     */
    private static final Registry instance = new Registry();
    /**
     * The varios known adapters
     * @since 1.0
     */
    private List<Adapter> adapters = new ArrayList<Adapter>();
    /**
     * Only one config to the application
     * @since 1.0
     */
    private Config config;
    /**
     * the inflector of the database
     * @since 1.0
     */
    private Inflector inflector;
    /**
     * the atual adapter used in this application
     * @since 1.0
     */
    private Adapter thisAdapter;

    /**
     * Default private constructor
     * @since 1.0
     */
    private Registry() {
        inflector = new BaseInflector();
    }

    /**
     * Get a instance of Registry
     * @return single Registry Object.
     * @since 1.0
     */
    public static Registry getInstance() {
        return instance;
    }

    /**
     * Add an new Adapter to the registry
     * @param adapter - the new adaoter to be addeded
     * @since 1.0
     */
    public void addAdapter(Adapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("Adapter can't be null");
        }
        if (!adapters.contains(adapter)) {
            adapters.add(adapter);
        }
    }

    /**
     * Get the config of the application
     * @return <code>Config</code> of the application
     * @since 1.0
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Sets a new config to the application</p>
     * WARNING: THIS IS AN DESTRUCTIVE METHOD.
     * @param config new Config to the application
     * @since 1.0
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * Get the adapter of this configuration
     * @return Adapter implementation used in this application
     * @since 1.0
     */
    public Adapter getAdapter() {
        if (config == null) {
            return null;
        }
        if (thisAdapter == null) {
            String adapterName = config.getAdapter();
            for (Adapter a : adapters) {
                if (a.getSimpleName().equalsIgnoreCase(adapterName)) {
                    thisAdapter = a;
                }
            }
        }
        return thisAdapter;
    }
    /**
     * Get the inflector of the registry
     * @return Inflector for usage
     * @since 1.0
     */
    public Inflector getInflector() {
        return inflector;
    }
    /**
     * Define an new Inflector.</p>
     * WARNING: THIS IS AN DESTRUCTIVE METHOD.
     * @param inflector the new Inflector.
     * @since 1.0
     */
    public void setInflector(Inflector inflector) {
        this.inflector = inflector;
    }

}
