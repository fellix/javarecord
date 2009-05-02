/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.javarecord.adapter.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.java.javarecord.JavaRecord;
import net.java.javarecord.adapter.Adapter;
import net.java.javarecord.adapter.Generator;
import net.java.javarecord.exceptions.NoParameterException;

/**
 * Class for make a SQL command generator
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public class SQLGenerator implements Generator {

    private Adapter adapter;
    private String tableName;
    private DatabaseManager database = DatabaseManager.getInstance();
    private ResultSetMetaData metaData;
    private Map<String, Object> attributes;
    private String primaryKey;
    /**
     * Defaults construtor
     * @param adapter Adapter to use
     * @param tableName
     * @since 1.0
     */
    public SQLGenerator(Adapter adapter, String tableName) {
        this.adapter = adapter;
        this.tableName = adapter.isUpperCase() ? tableName.toUpperCase() : tableName;
        primaryKey = getPrimaryKeyColumn();
    }
    /**
     * Retrive the default column and make the attributes to the database
     * @since 1.0
     */
    @Override
    public void loadAttributes() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(tableName);
        if (adapter.getLimiter() != null) {
            sb.append(" ").append(adapter.getLimiter()).append(" 1");
        }
        PreparedStatement smt = null;
        ResultSet rs = null;
        try {
            smt = database.getPreparedStatement(sb.toString());
            rs = smt.executeQuery();
            metaData = rs.getMetaData();
        } catch (SQLException ex) {
            throw new SQLGeneratorException(ex.getMessage(), ex);
        } finally {
            try {
                if (smt != null) {
                    smt.close();
                    smt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException ex) {
                throw new SQLGeneratorException(ex.getMessage(), ex);
            }
        }
    }
    /**
     * Getts the attributes of the loaded table
     * @return Map<String, Object>
     * @since 1.0
     */
    @Override
    public Map<String, Object> getAttributes() {
        if (attributes != null) {
            return attributes;
        }
        if (metaData == null) {
            loadAttributes();
        }
        try {
            return reloadAttributes();
        } catch (SQLException ex) {
            throw new SQLGeneratorException(ex.getMessage(), ex);
        }
    }

    /**
     * Force the application to reload the object attributes
     * @return attributes of this object
     * @throws java.sql.SQLException
     * @see #getAttributes()
     * @since 1.0
     */
    public Map<String, Object> reloadAttributes() throws SQLException {
        loadAttributes();
        attributes = new HashMap<String, Object>();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String columnLabel = metaData.getColumnLabel(i + 1);
            attributes.put(columnLabel, null);
        }
        return attributes;
    }

    /**
     * Gets the Primary key of the table
     * @return columnName of the primary key
     * @since 1.0
     * @throws SQLException
     */
    private String getPrimaryKeyColumn() {
        try {
            ResultSet rs = database.getPrimaryKey(tableName);
            rs.next();
            return rs.getString(4);
        } catch (SQLException ex) {
            throw new SQLGeneratorException(ex.getMessage(), ex);
        }
    }
    /**
     * Generic method to call. Insert a registry if the PrimaryKey column is empty
     * @since 1.0
     * @see #insert()
     * @see #update()
     */
    @Override
    public void save() {
        if (attributes.get(primaryKey) == null) {
            insert();
        } else {
            update();
        }

    }
    /**
     * Update a registry in the database
     * @since 1.0
     */
    @Override
    public void update() {
        if (attributes.get(primaryKey) == null) {
            throw new NullPointerException("Can't update a non inserted object");
        }
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(tableName).append(" SET ");
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(getPrimaryKeyColumn()).append(" = ?");
        List<Object> values = new ArrayList<Object>();
        for (Map.Entry<String, Object> e : attributes.entrySet()) {
            String key = e.getKey();
            Object value = e.getValue();
            if (!(key.equals(primaryKey)) && !(value instanceof List) &&
                    !(value instanceof JavaRecord) && (value != null)) {
                sb.append(", ").append(key);
                sb.append(" = ");
                sb.append("?");
                values.add(value);
            }
        }
        String update = sb.toString().replaceFirst(",", "");
        update += where.toString();
        PreparedStatement smt = database.getPreparedStatement(update);
        int index = 0;
        try {
            for (index = 1; index <= values.size(); index++) {
                SQLUtil.addParameter(smt, index, values.get(index - 1));
            }
            SQLUtil.addParameter(smt, index, attributes.get(primaryKey));
            smt.execute();
            smt.close();
            smt = null;
        } catch (SQLException ex) {
            throw new SQLGeneratorException(ex.getMessage(), ex);
        }
    }
    /**
     * Insets a registry (new registry) in the database
     * @since 1.0
     */
    @Override
    public void insert() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        StringBuilder values = new StringBuilder(" VALUES(");
        sb.append(tableName);
        sb.append("(");
        for (Map.Entry<String, Object> e : attributes.entrySet()) {
            //Ignores the Primary Key
            if (!e.getKey().equals(primaryKey)) {
                Object value = e.getValue();
                //Ignores the Associantion at this moment
                if (!(value instanceof List) && !(value instanceof JavaRecord) && (value != null)) {
                    sb.append(", ").append(e.getKey());
                    values.append(",? ");
                }
            }
        }
        String insert = sb.toString().replaceFirst(",", "");
        String value = values.toString().replaceFirst(",", "");
        insert += ")" + value + ")";
        PreparedStatement smt = database.getPreparedStatement(insert);
        int index = 1;
        try {
            for (Map.Entry<String, Object> e : attributes.entrySet()) {
                Object val = e.getValue();
                if (!(val instanceof List) && !(val instanceof JavaRecord) &&
                        !(e.getKey().equals(primaryKey))) {
                    SQLUtil.addParameter(smt, index, val);
                    index++;
                }
            }
            smt.execute();
            smt.close();
            smt = null;
            smt = database.getPreparedStatement(adapter.getIdSelect());
            ResultSet rs = smt.executeQuery();
            rs.next();
            attributes.put(primaryKey, rs.getInt(1));
            rs.close();
            smt.close();
            rs = null;
            smt = null;
        } catch (SQLException ex) {
            throw new SQLGeneratorException(ex.getMessage(), ex);
        }
    }
    /**
     * Removes an registry from the database
     * @since 1.0
     */
    @Override
    public void remove() {
        if (attributes.get(primaryKey) == null) {
            throw new NullPointerException("Can't delete a non inserted object");
        }
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(tableName).append(" WHERE ").append(primaryKey).append(" = ?");
        PreparedStatement smt = database.getPreparedStatement(sb.toString());
        System.out.println(sb.toString());
        try {
            SQLUtil.addParameter(smt, 1, attributes.get(primaryKey));
            smt.execute();
            smt.close();
            smt = null;
        } catch (SQLException ex) {
            throw new SQLGeneratorException(ex.getMessage(), ex);
        }

    }
    /**
     * Sets the atributes
     * @param attributes to set
     * @since 1.0
     */
    @Override
    public void setAttributes(Map<String, Object> attributes) {
        if (attributes == null) {
            throw new NullPointerException("Cannot set NULL to attributes");
        }
        this.attributes = attributes;
    }
    /**
     * Overrides the generator find, and call the correct method to use find
     * @param entityClass Class of the entity to load
     * @param conditions Properties file to make the conditions, can be null
     * @return List
     * @since 1.0
     * @see #findAll(java.lang.Class)
     * @see #findByConditions(java.lang.Class, java.util.Properties) 
     */
    @Override
    public List<? extends JavaRecord> find(Class<? extends JavaRecord> entityClass, Properties conditions) {
        if (conditions == null) {
            return findAll(entityClass);
        }
        return findByConditions(entityClass, conditions);
    }

    /**
     * Load all objects of this entity class
     * @param entityClass Class of the entity to load all
     * @return List
     * @since 1.0
     * @see #find(java.lang.Class, java.util.Properties)
     * @see #findByConditions(java.lang.Class, java.util.Properties)
     */
    private List<? extends JavaRecord> findAll(Class<? extends JavaRecord> entityClass) {
        PreparedStatement smt = database.getPreparedStatement("SELECT * FROM " + tableName);
        try {
            ResultSet rs = smt.executeQuery();
            ResultSetMetaData data = rs.getMetaData();
            List<JavaRecord> returnList = new ArrayList<JavaRecord>();
            while (rs.next()) {
                Map<String, Object> att = new HashMap<String, Object>();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    att.put(data.getColumnName(i), rs.getObject(i));
                }
                JavaRecord entity = entityClass.newInstance();
                entity.setAttributes(att);
                returnList.add(entity);
                att = null;
                entity = null;
            }
            return returnList;
        } catch (InstantiationException ex) {
            throw new SQLGeneratorException("findAll throw Exception: " + ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new SQLGeneratorException("findAll throw Exception: " + ex.getMessage(), ex);
        } catch (SQLException ex) {
            throw new SQLGeneratorException("findAll throw Exception: " + ex.getMessage(), ex);
        }
    }
    /**
     * Generates an Select clause using the WHERE statement
     * @param entityClass Class of the entity to load
     * @param conditions Properties file to make the conditions, can be null
     * @return List
     * @since 1.0
     * @see #find(java.lang.Class, java.util.Properties)
     * @see #findAll(java.lang.Class) 
     */
    private List<? extends JavaRecord> findByConditions(Class<? extends JavaRecord> entityClass, Properties conditions) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(tableName).append(" WHERE ");
        int totalParam = 0;
        for (Map.Entry entry : conditions.entrySet()) {
            sb.append(entry.getKey()).append(" = ?");
            totalParam++;
        }
        if (totalParam <= 0) {
            throw new NoParameterException("Cann't find without parameters");
        }
        PreparedStatement smt = database.getPreparedStatement(sb.toString());
        try {
            int index = 1;
            for (Map.Entry entry : conditions.entrySet()) {
                SQLUtil.addParameter(smt, index, entry.getValue());
                index++;
            }
            ResultSet rs = smt.executeQuery();
            ResultSetMetaData data = rs.getMetaData();
            List<JavaRecord> returnList = new ArrayList<JavaRecord>();
            while (rs.next()) {
                Map<String, Object> att = new HashMap<String, Object>();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    att.put(data.getColumnName(i), rs.getObject(i));
                }
                JavaRecord entity = entityClass.newInstance();
                entity.setAttributes(att);
                returnList.add(entity);
                att = null;
                entity = null;
            }
            return returnList;
        } catch (InstantiationException ex) {
            throw new SQLGeneratorException("findByConditions throw Exception: " + ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new SQLGeneratorException("findByConditions throw Exception: " + ex.getMessage(), ex);
        } catch (SQLException ex) {
            throw new SQLGeneratorException("findByConditions throw Exception: " + ex.getMessage(), ex);
        }
    }
}
