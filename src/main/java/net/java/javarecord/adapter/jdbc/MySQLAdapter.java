/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.adapter.jdbc;

import net.java.javarecord.adapter.*;

/**
 * This is an MySQL Adapter implementation
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 * @see Adapter
 */
public class MySQLAdapter extends AbstractAdapter {

    /**
     * The MySQL driver
     * @return MySQL driver class
     * @since 1.0
     * @see Adapter#getDriverClass()
     */
    @Override
    public String getDriverClass() {
        return "org.gjt.mm.mysql.Driver";
    }
    /**
     * Return the base mysql url to connect to an database
     * @return mysql database url default
     * @since 1.0
     * @see Adapter#getBaseUrl()
     * @see AbstractAdapter#getUrl()
     */
    @Override
    public String getBaseUrl() {
        return "jdbc:mysql://{server}/{database}";
    }
    /**
     * Return the MySQL simple adapter name
     * @return "mysql"
     * @since 1.0
     * @see Adapter#getSimpleName()
     */
    @Override
    public String getSimpleName() {
        return "mysql";
    }
    /**
     * Return false, MySQL database isn't uppercase
     * @return false
     * @since 1.0
     * @see Adapter#isUpperCase() 
     */
    @Override
    public Boolean isUpperCase() {
        return false;
    }
    /**
     * MySQL row limiter
     * @return 'limit'
     * @since 1.0
     */
    @Override
    public String getLimiter() {
        return "limit";
    }

    /**
     * MySQL doen't use Sequences
     * @return null
     * @since 1.0
     */
    @Override
    public String getSequenceStatment() {
        return null;
    }
    /**
     * Retrieve the MySQL Command Generator.
     * @param tableName the table name
     * @return Generator
     * @since 1.0
     */
    @Override
    public Generator getCommandGenerator(String tableName) {
        return new SQLGenerator(this, tableName);
    }

    @Override
    public String getIdSelect() {
        return "select last_insert_id()";
    }

}
