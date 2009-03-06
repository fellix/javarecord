/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.javarecord;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.javarecord.exceptions.NoParameterException;
import net.java.javarecord.jdbc.TableResolver;

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
        //TODO: BelongTo update the object and the belong to key (field_id)
        }
        else {
            attributes.put(key, value);
        }
    }

    /**
     * Generic getter for the object
     * @param key the field to get
     * @return value of the field - null if the key doesn't exist
     * @since 1.0
     */
    public Object getAttribute(String key) {
        if(resolver.isHasManyKey(key)){
            try {
                //For use lazy, fill the array
                resolver.loadCollection(key);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }else if(resolver.isBelongsToKey(key)){
            //LOAD THE OBJECT FOR REFERENCE
            resolver.loadObject(key);
        }
        return attributes.get(key);
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
    //String formats key => value

    public static <T> T find(Class<? extends JavaRecord> objClazz, Object... params) {
        if (params.length <= 0) {
            throw new NoParameterException("No Parameters for find");
        }
        String idstr = params[0] + "";
        try {
            int id = Integer.parseInt(idstr);
            return (T) findById(objClazz, id);
        } catch (NumberFormatException ex) {
            //TODO: Make the SQL for others
        }
        return null;
    }
    /**
     * Search an object by the id
     * @param clazz the object class
     * @param id the id to find
     * @return object found or null
     * @since 1.0
     */
    private static Object findById(Class<? extends JavaRecord> clazz, int id) {
        try {
            Object instance = clazz.newInstance();
            if (instance instanceof JavaRecord) {
                //Search the TableResolver of this class to find the tableName
                TableResolver table = ((JavaRecord) instance).getResolver();
                return table.selectById(id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaRecord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(JavaRecord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(JavaRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public TableResolver getResolver() {
        return resolver;
    }
}
