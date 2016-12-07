/*
 * Created on 2005-12-24
 *
 */
package net.joycool.wap.bean.forum;

/**
 * @author lbj
 *
 */
public class ForumBean {
    int id;
    String name;
    String backTo;
    
    /**
     * @return Returns the backTo.
     */
    public String getBackTo() {
        return backTo;
    }
    /**
     * @param backTo The backTo to set.
     */
    public void setBackTo(String backTo) {
        this.backTo = backTo;
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
