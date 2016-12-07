/*
 * Created on 2005-12-19
 *
 */
package net.joycool.wap.bean;


/**
 * @author lbj
 *
 */
public class DiyBean {
    int id;
    int userId;
    int catalogId;
    CatalogBean catalog;
    int displayOrder;
            
    /**
     * @return Returns the displayOrder.
     */
    public int getDisplayOrder() {
        return displayOrder;
    }
    /**
     * @param displayOrder The displayOrder to set.
     */
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
    /**
     * @return Returns the catalog.
     */
    public CatalogBean getCatalog() {
        return catalog;
    }
    /**
     * @param catalog The catalog to set.
     */
    public void setCatalog(CatalogBean catalog) {
        this.catalog = catalog;
    }
    /**
     * @return Returns the catalogId.
     */
    public int getCatalogId() {
        return catalogId;
    }
    /**
     * @param catalogId The catalogId to set.
     */
    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
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
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
