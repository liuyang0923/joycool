/*
 * Created on 2006-1-23
 *
 */
package net.joycool.wap.bean.wgame;

/**
 * @author lbj
 *
 */
public class GirlBean {
    int id;
    String name;    
    String[] picList;
    
    /**
     * @return Returns the picList.
     */
    public String[] getPicList() {
        return picList;
    }
    /**
     * @param picList The picList to set.
     */
    public void setPicList(String[] picList) {
        this.picList = picList;
    }
    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }    
}
