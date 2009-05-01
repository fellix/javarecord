/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.javarecord;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.java.javarecord.adapter.jdbc.TableResolver;

/**
 * JavaRecord Core class, resolve annotations, and load attributes from database
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public abstract class JavaRecord {

    protected Map<String, Object> attributes = new HashMap<String, Object>();
    private TableResolver resolver;

    /**
     * Default Constructor
     * @since 1.0
     */
    public JavaRecord() {
        resolver = new TableResolver(this);
    }

    /**
     * Save the row in the database
     * @since 1.0
     * @see TableResolver#save() 
     */
    public void save() {
        resolver.save();
    }

    /**
     * Generic Setter for the object
     * @param key the field to set
     * @param value the value to the field
     * @since 1.0
     */
    public void setAttribute(String key, Object value) {
        if (resolver.isHasManyKey(key)) {
            List l = (List) attributes.get(key);
            l.add(value);
            l = null;
        } else if (resolver.isBelongsToKey(key)) {
            String validKey = key + "_id";
            JavaRecord j = (JavaRecord) value;
            setAttribute(validKey, j.getAttribute("id"));
            j = null;
            attributes.put(key, value);
        } else {
            attributes.put(key, value);
        }
    }

    /**
     * Generic getter for the object
     * @param key the field to get
     * @return value of the field - null if the key doesn't exist
     * @since 1.0
     */
    public <T> T getAttribute(String key) {
        if (resolver.isHasManyKey(key)) {
            try {
                //For use lazy, fill the array
                resolver.loadCollection(key);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (resolver.isBelongsToKey(key)) {
            //LOAD THE OBJECT FOR REFERENCE
            resolver.loadObject(key);
        }
        return (T) attributes.get(key);
    }

    /**
     * Get Object attributes
     * @return attributes of this object
     * @since 1.0
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Define new attributes of the object
     * @param attributes
     * @since 1.0
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public static <T> T find(Class<? extends JavaRecord> objClazz, Properties params) {
        try {
            Object instance = objClazz.newInstance();
            TableResolver table;
            if (instance instanceof JavaRecord) {
                table = ((JavaRecord) instance).getResolver();
            } else {
                throw new JavaRecordException("Not found a valid JavaRecord class to find");
            }
            if (params == null) {
                return (T) table.find(null);
            }
            return (T) table.find(params);
        } catch (InstantiationException ex) {
            throw new JavaRecordException("Couldn't find Object of class " + objClazz, ex);
        } catch (IllegalAccessException ex) {
            throw new JavaRecordException("Couldn't find Object of class " + objClazz, ex);
        }
    }

    public TableResolver getResolver() {
        return resolver;
    }
}
