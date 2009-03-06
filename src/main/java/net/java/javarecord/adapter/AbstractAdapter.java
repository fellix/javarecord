/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.javarecord.adapter;

import net.java.javarecord.entities.Config;
import net.java.javarecord.registry.Registry;

/**
 * This is the base Adapter.
 * @author Rafael Felix
 * @version 1.0
 * @since 1.0-snapshot
 */
public abstract class AbstractAdapter implements Adapter{
    
    /**
     * By defaul the Generator Type is IDENTITY.</p>
     * Override this metodo to use another GeneratorType
     * @return GeneratorType.IDENTITY
     * @see Adapter#getGeneratorType()
     * @since 1.0
     */
    @Override
    public GeneratorType getGeneratorType() {
        return GeneratorType.IDENTITY;
    }
    /**
     * Gets an Url using the getBaseUrl, using config object
     * @return the application url for connection
     * @since 1.0
     * @see Registry#getConfig()
     * @see Config
     * @see Adapter#getBaseUrl() 
     */
    @Override
    public String getUrl(){
        Config config = Registry.getInstance().getConfig();
        if(config == null){
            return null;
        }
        String url = getBaseUrl().replace("{server}", config.getServer());
        url = url.replace("{database}", config.getDatabase());
        return url;
    }
    
}
