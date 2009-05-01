/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.javarecord.adapter.jdbc;

import net.java.javarecord.jdbc.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.java.javarecord.JavaRecord;
import net.java.javarecord.adapter.Generator;
import net.java.javarecord.registry.Registry;

/**
 * Resolve the table atributes.
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public class TableResolver {

    private JavaRecord javaRecord;
    private String idColumn;
    private AnnotationResolver resolver;
    private String tableName;
    private Generator generator;
    private Map<String, Object> hasMany;
    private Map<String, Object> hasOne;
    private Map<String, Object> belongsTo;

    /**
     * Default contructor
     * @param javaRecord the object to be read
     * @since 1.0
     * @see #load()
     */
    public TableResolver(JavaRecord javaRecord) {
        this.javaRecord = javaRecord;
        load();
    }

    /**
     * Load the basic configurations
     * @since 1.0
     */
    private void load() {
        resolver = new AnnotationResolver(javaRecord.getClass());
        tableName = resolver.readTableName();
        generator = Registry.getInstance().getAdapter().getCommandGenerator(tableName);
        javaRecord.setAttributes(generator.getAttributes());
        hasMany = resolver.resolveHasMany();
        if (hasMany != null) {
            javaRecord.getAttributes().putAll(hasMany);
        }
        hasOne = resolver.resolveHasOne();
        if (hasOne != null) {
            javaRecord.getAttributes().putAll(hasOne);
        }
        belongsTo = resolver.resolveBelongTo();
        if (belongsTo != null) {
            javaRecord.getAttributes().putAll(belongsTo);
        }
    }

    /**
     * Verify in the hasMany Map if the Key is an object
     * @param key the key to find
     * @return true if the key is an hasMany
     * @since 1.0
     */
    public Boolean isHasManyKey(String key) {
        return hasMany == null ? false : hasMany.containsKey(key);
    }

    /**
     * Verify in the hasOne Map if the Key is an object
     * @param key the key to find
     * @return true if the key is an hasMany
     * @since 1.0
     */
    public Boolean isHasOneKey(String key) {
        return hasOne == null ? false : hasOne.containsKey(key);
    }

    /**
     * Verify if the key is an object key
     * @param key the key to verify
     * @return true case the key is found, false otherwise
     * @since 1.0
     */
    public Boolean isBelongsToKey(String key) {
        return belongsTo == null ? false : belongsTo.containsKey(key);
    }

    /**
     * Save the atribute to the database
     * @since 1.0
     * @see #insert()
     */
    public void save() {
        //TODO: Save the relationship object, hasOne e hasMany
        generator.setAttributes(javaRecord.getAttributes());
        generator.save();
    //saveHasMany();
    }

    /**
     * Search for each hasMany key, and save each JavaRecord object of the list
     * @since 1.0
     * @see JavaRecord#save() 
     */
    private void saveHasMany() {
        for (Map.Entry<String, Object> e : javaRecord.getAttributes().entrySet()) {
            if (isHasManyKey(e.getKey())) {
                List<JavaRecord> objects = (List<JavaRecord>) e.getValue();
                for (JavaRecord j : objects) {
                    //Update the BelongTo key
                    String jrTable = javaRecord.getClass().getSimpleName().toLowerCase();
                    j.setAttribute(jrTable + "_" + idColumn, javaRecord.getAttribute(idColumn));
                    j.setAttribute(jrTable, javaRecord);
                    //Save all objects.
                    j.save();
                }
                objects = null;
            }
        }
    }

    public <T> List<T> find(Properties params) {
        return (List<T>) generator.find(javaRecord.getClass(), params);
    }

    /**
     * Fill the object with the Select result
     * @param id object id
     * @return the JavaRecord object
     * @throws java.sql.SQLException
     * @since 1.0
     */
    public JavaRecord selectById(int id) throws SQLException {
        /*ResultSet rs = sql.selectById(id);
        ResultSetMetaData data = rs.getMetaData();
        while (rs.next()) {
        for (int i = 0; i < data.getColumnCount(); i++) {
        javaRecord.setAttribute(data.getColumnLabel(i + 1), rs.getObject(i + 1));
        }
        }*/
        return javaRecord;
    }

    /**
     * Loads an Hasmany Collection
     * @param key collection key
     * @throws java.sql.SQLException
     */
    public void loadCollection(String key) throws SQLException {
        /*ResultSet rs = sql.selectCollection(key, (Integer) javaRecord.getAttribute(getIdColumn()),
        javaRecord.getClass().getSimpleName());
        if (rs != null) {
        List collection = new ArrayList();
        while (rs.next()) {
        Class cl = resolver.getHasManyClass(key);
        try {
        Object obj = cl.newInstance();
        collection.add(fillObject(rs, obj));
        } catch (InstantiationException ex) {
        throw new SQLException(ex);
        } catch (IllegalAccessException ex) {
        throw new SQLException(ex);
        }
        }
        javaRecord.setAttribute(key, collection);
        }*/
    }

    /**
     * Load an object, used for belong to Relationship
     * @param belongKey the key to load an object
     * @since 1.0
     */
    public void loadObject(String belongKey) {
        /*
        //TODO: Find the personal key.
        Integer id = (Integer) javaRecord.getAttribute(belongKey + "_id");
        Inflector inf = Registry.getInstance().getInflector();
        String belongTable = belongKey;
        if (inf != null) {
        belongTable = inf.pluralize(belongKey);
        }
        try {
        ResultSet rs = sql.selectObject(belongTable, id);
        while (rs.next()) {
        Class cl = resolver.getBelongsToClass(belongKey);
        try {
        Object instance = cl.newInstance();
        javaRecord.setAttribute(belongKey, fillObject(rs, instance));
        } catch (InstantiationException ex) {
        Logger.getLogger(TableResolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
        Logger.getLogger(TableResolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        } catch (SQLException ex) {
        throw new RuntimeException(ex);
        }*/
    }

    /**
     * Fill an object with the resultset
     * @param rs the ResultSet
     * @param instance the object instance
     * @return an object fileld
     * @throws java.sql.SQLException
     * @since 1.0
     */
    private JavaRecord fillObject(ResultSet rs, Object instance) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        if (instance instanceof JavaRecord) {
            JavaRecord j = (JavaRecord) instance;
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                j.setAttribute(metaData.getColumnLabel(i + 1), rs.getObject(i + 1));
            }
            //UPDATE THE OBJECT
            return j;
        } else {
            throw new RuntimeException("Invalid relationship class");
        }
    }
}
