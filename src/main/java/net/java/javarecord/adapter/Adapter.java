/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.adapter;

/**
 * This is an Adapter to connect to an Database
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public interface Adapter {
    /**
     * Get the Driver Class for use the adapter connection
     * @return <code>String</code> containing the DriverClass for make connections
     * @since 1.0
     */
    String getDriverClass();
    /**
     * Get the base url for JDBC connection to an database</p>
     * For example using the odbc4j:
     * <pre>
     *  <code>return "jdbc:mysql://{server}/{database}";</code></p>
     *  Where server is the server url, and database is the database name</p>
     *  This atributes is replaced by the JavaRecord
     * </pre>
     * @return <code>String</code> with the base url
     * @since 1.0
     */
    String getBaseUrl();
    /**
     * Get the GeneratorType of this class, is used for inject the surrogate key
     * @return Type of ID Generator
     * @since 1.0
     */
    GeneratorType getGeneratorType();
    /**
     * Gets an simple adapter name to use in the configuration file</p>
     * For Example:
     * <pre>
     *   in the config entity have an adapter attribute, this attribute contains this name
     * </pre>
     * @return String containing the name of the adapter
     * @since 1.0
     */
    String getSimpleName();
    /**
     * Return the correct URL of the database
     * @return return the correct url
     * @since 1.0
     */
    String getUrl();
    /**
     * Return if the database use uppercase for the objects. e.g. Oracle
     * @return true case the database use uppercase, false otherwise
     * @since 1.0
     */
    Boolean isUpperCase();
    /**
     * Database rows limiter. Limiter the loader delay.</p>
     * For example:
     * <pre>
     * MySQL limiter = "limit"</p>
     * Oracle limiter = "rownum <"
     * </pre>
     * JavaRecord don't know '<' or '>', the adapter implementation must inform this characters.
     * @return database row limiter
     * @since 1.0
     */
    String getLimiter();
    /**
     * Get the SQL Code for the adapter with use for GeneratorType Sequence</p>
     * Use {sequence} for auto replacement.
     * @return SQL code
     * @since 1.0
     * @see #getGeneratorType()
     * @see GeneratorType
     */
    String getSequenceStatment();
    /**
     * The command generator to comunicates with the database
     * @return Generator
     * @since 1.0
     */
    Generator getCommandGenerator(String tableName);
    /**
     * The ID Selecion String
     * @return SQL String
     * @since 1.0
     */
    String getIdSelect();
}
