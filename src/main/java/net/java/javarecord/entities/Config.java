/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.entities;

/**
 * This is an Config entity, this entity is for load default aplication configuration
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public class Config {
    private String adapter;
    private String server;
    private String database;
    private String user;
    private String password;
    /**
     * Default Blank constructor
     * @since 1.0
     */
    public Config(){

    }
    /**
     * Full Constructor
     * @param adapter - the adapter name
     * @param server - the server name
     * @param database - the database name
     * @param user - the user name
     * @since 1.0
     */
    public Config(String adapter, String server, String database, String user) {
        this.adapter = adapter;
        this.server = server;
        this.database = database;
        this.user = user;
    }
    /**
     * Get the adapter simple name
     * @return adapter name
     * @since 1.0
     */
    public String getAdapter() {
        return adapter;
    }
    /**
     * Define an new Adapter
     * @param adapter - the new Adapter
     * @since 1.0
     */
    public void setAdapter(String adapter) {
        this.adapter = adapter;
    }
    /**
     * Get the database name
     * @return database name
     * @since 1.0
     */
    public String getDatabase() {
        return database;
    }
    /**
     * Define an new Database name
     * @param database new database name
     * @since 1.0
     */
    public void setDatabase(String database) {
        this.database = database;
    }
    /**
     * Get the database user password
     * @return the password
     * @since 1.0
     */
    public String getPassword() {
        return password;
    }
    /**
     * Define an new Database password for connection
     * @param password the new database password
     * @since 1.0
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Get the server address for connect
     * @return the server address
     * @since 1.0
     */
    public String getServer() {
        return server;
    }
    /**
     * Define an new server to connect
     * @param server new server address
     * @since 1.0
     */
    public void setServer(String server) {
        this.server = server;
    }
    /**
     * Get the user name used in connection
     * @return username of the database
     * @since 1.0
     */
    public String getUser() {
        return user;
    }
    /**
     * Define a new user to connect
     * @param user - the new username
     * @since 1.0
     */
    public void setUser(String user) {
        this.user = user;
    }

}
