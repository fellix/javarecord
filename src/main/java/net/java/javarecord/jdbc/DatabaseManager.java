/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.javarecord.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.javarecord.adapter.Adapter;
import net.java.javarecord.entities.Config;
import net.java.javarecord.registry.Registry;

/**
 * This class manage the JDBC connections, and provide means do access the database
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public class DatabaseManager {

    /**
     * A single DatabaseManager instance
     * @since 1.0
     */
    private static DatabaseManager instance;
    private DatabaseMetaData metaData;
    private Connection connection;
    private Registry registry = Registry.getInstance();

    /**
     * Default private Constructor
     * @since 1.0
     */
    private DatabaseManager() {
        stabilishConnection();
    }

    /**
     * Get the unique instance of the DatabaseManager
     * @return unique DatabaseManager's instance
     * @since 1.0
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    /**
     * Stabilish Connection to the database
     * @since 1.0
     * @see DriverManager#getConnection(java.lang.String, java.lang.String, java.lang.String)
     * @throws ExceptionInInitializerError
     */
    private void stabilishConnection() {
        Adapter adapter = registry.getAdapter();
        Config config = registry.getConfig();
        try {
            Class.forName(adapter.getDriverClass());
            connection = DriverManager.getConnection(adapter.getUrl(), config.getUser(), config.getPassword());
            metaData = connection.getMetaData();
        } catch (SQLException ex) {
            throw new ExceptionInInitializerError(ex);
        } catch (ClassNotFoundException ex) {
            throw new ExceptionInInitializerError(ex);
        }

    }
    /**
     * Get an prepared statment to use in application
     * @param statment the statment to prepare
     * @return the statment prepared
     * @since 1.0
     * @throws ExceptionInInitializerError
     */
    public PreparedStatement getPreparedStatement(String statment){
        try {
            return connection.prepareStatement(statment);
        } catch (SQLException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    /**
     * Get the Primary Key of an tableName
     * @param tableName
     * @return ResultSet containing the Primary Keys
     * @throws java.sql.SQLException
     * @since 1.0
     */
    public ResultSet getPrimaryKey(String tableName) throws SQLException{
        ResultSet rs = metaData.getPrimaryKeys(null, null, tableName);
        return rs;
    }

}
