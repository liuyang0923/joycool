/*
 * Created on 2005-12-13
 *
 */
package net.joycool.wap.cache;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author lbj
 *  
 */
public class CacheUtil {
    public static Hashtable cache;
    public static Hashtable urlCache;
    
    public static int MAX_SIZE = 1000;
    
    public static void put(Object key, Object value){
        if(key == null){
            return;
        }
        if(cache == null){
            cache = new Hashtable();
        }      
        Object o = null;
        if(cache.size() >= MAX_SIZE){
            Enumeration enu = cache.keys();
            int i = 0;
            while(enu.hasMoreElements()){
                if(i > 50){
                    break;
                }
                o = enu.nextElement();
                cache.remove(o);
                urlCache.remove(o);
                i ++;
            }
        }
        cache.remove(key);
        cache.put(key, value);
    }
    
    public static void putUrl(Object key, Object value){
        if(key == null){
            return;
        }
        if(urlCache == null){
            urlCache = new Hashtable();
        }      
        
        urlCache.remove(key);
        urlCache.put(key, value);
    }
    
    public static Object get(Object key){
        if(cache == null){
            return null;
        }  
        if(key == null){
            return null;
        }
        return cache.get(key);
    }
}
