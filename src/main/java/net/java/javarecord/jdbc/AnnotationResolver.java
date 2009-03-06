/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.java.javarecord.annotations.BelongsTo;
import net.java.javarecord.annotations.HasMany;
import net.java.javarecord.annotations.HasOne;
import net.java.javarecord.annotations.Sequence;
import net.java.javarecord.annotations.TableName;
import net.java.javarecord.inflector.Inflector;
import net.java.javarecord.registry.Registry;

/**
 * Resolve JavaRecord's annotations, in the object class
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public class AnnotationResolver {
    private Class clazz;
    private Registry registry = Registry.getInstance();
    private Map<String, Class> hasManyClass = new HashMap<String, Class>();
    private Map<String, Class> belongToClass = new HashMap<String, Class>();
    /**
     * The Class for read the annotations
     * @param clazz JavaRecord implementation
     * @since 1.0
     */
    public AnnotationResolver(Class clazz){
        this.clazz = clazz;
    }
    /**
     * Read the clazz searching for the TableName annotation.</p>
     * If the annotation is not present the default table name is the ClassName in plural.
     * @return String containing the TableName.
     * @since 1.0
     * @see Inflector#inflect(java.lang.String)
     * @see TableName
     */
    public String readTableName(){
        if(clazz.isAnnotationPresent(TableName.class)){
            TableName tableName = (TableName) clazz.getAnnotation(TableName.class);
            return tableName.name();
        }
        Inflector inflector = registry.getInflector();
        String tableName = clazz.getSimpleName().toLowerCase();
        if(inflector != null){
            tableName = inflector.pluralize(tableName);
        }
        return tableName;
    }
    /**
     * Get the sequence name in the annotation for this class
     * @return the sequence name or null
     * @since 1.0
     * @see Sequence
     */
    public String getSequenceName(){
        if(clazz.isAnnotationPresent(Sequence.class)){
            Sequence sequence = (Sequence) clazz.getAnnotation(Sequence.class);
            return sequence.name();
        }
        return null;
    }
    /**
     * Search for the HasMany annotation, create an new Attribute in the Object, this attribute is an ArrayList object.</p>
     * The name is the Class name inflected.
     * @return The Map containing the Collection attribute
     * @since 1.0
     * @see HasMany
     * @see ArrayList
     * @see Inflector
     */
    public Map<String, Object> resolveHasMany(){
        if(clazz.isAnnotationPresent(HasMany.class)){
            Map<String, Object> map = new HashMap<String, Object>();
            HasMany hasMany = (HasMany) clazz.getAnnotation(HasMany.class);
            Inflector inflector = registry.getInflector();
            for(Class c : hasMany.classes()){
                String collection = c.getSimpleName().toLowerCase();
                //Collection name in plural
                collection = inflector.pluralize(collection);
                //Each Collection is an ArrayList instance
                map.put(collection, new ArrayList());
                hasManyClass.put(collection, c);
            }
            return map;
        }
        return null;
    }
    /**
     * Search for the HasOne annotation, and create the attribute wich contains the object
     * @return Map containing the has one objects
     * @since 1.0
     * @see HasOne
     */
    public Map<String, Object> resolveHasOne(){
        if(clazz.isAnnotationPresent(HasOne.class)){
            Map<String, Object> map = new HashMap<String, Object>();
            HasOne hasOne = (HasOne) clazz.getAnnotation(HasOne.class);
            for(Class c : hasOne.classes()){
                String key = c.getSimpleName().toLowerCase();
                map.put(key, null);
            }
            return map;
        }
        return null;
    }
    /**
     * Find the blongsTo annotation and make the key for the annotation</p>
     * WARNING: This Attribute is lazy
     * @return
     */
    public Map<String, Object> resolveBelongTo(){
        if(clazz.isAnnotationPresent(BelongsTo.class)){
            Map<String, Object> map = new HashMap<String, Object>();
            BelongsTo belong = (BelongsTo) clazz.getAnnotation(BelongsTo.class);
            int index = 0;
            for(Class c : belong.classes()){
                //Default Object key is the className
                String keyName = c.getSimpleName().toLowerCase();
                //Verify the name of relation
                //TODO: THIS IS FOR THE DATABASE. STORE THIS IN A LIST
                /*
                if(belong.names().length > 0 ){
                    if(belong.names()[index] != null){
                        keyName = belong.names()[index];
                    }
                }*/
                belongToClass.put(keyName, c);
                if(!map.containsKey(keyName)){
                    map.put(keyName, null);
                }
                index++;
            }
            return map;
        }
        return null;
    }

    /**
     * Return the class of the object with the key
     * @param key the key of the List
     * @return class of this object
     * @since 1.0
     */
    public Class getHasManyClass(String key){
        return hasManyClass.get(key);
    }

    public Class getBelongsToClass(String key){
        return belongToClass.get(key);
    }

}
