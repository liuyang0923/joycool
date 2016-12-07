/*
 * Created on 2005-5-23
 *
 */
package net.joycool.wap.bean;

import java.util.Vector;

import net.joycool.wap.framework.BaseAction;


/**
 * @author lbj
 * 
 * 栏目分类javabean。
 */
public class CatalogBean{
	int id;
	String name;
	String description;
	int order;
	int level;
	int visible;
	int parentId;
	String type;
	Vector subCatas;
	String linkURL;
	int child_num;
	public String getLinkURL(){
	    if(type == null){
	        return BaseAction.INDEX_URL;
	    }
	    else if(type.equals("wapnews")){
	        return "http://wap.joycool.net/news/NewsCataList.do?id=" + id;
	    }
	    else if(type.equals("wapgame")){
	        return "http://wap.joycool.net/game/GameCataList.do?id=" + id;
	    }
	    else if(type.equals("image")){
	        return "http://wap.joycool.net/image/ImageCataList.do?id=" + id;
	    }
	    else if(type.equals("ebook")){
	        return "http://wap.joycool.net/ebook/EBookCataList.do?id=" + id;
	    }
	    return BaseAction.INDEX_URL;
	}
	
    /**
     * @return Returns the subCatas.
     */
    public Vector getSubCatas() {
        return subCatas;
    }
    /**
     * @param subCatas The subCatas to set.
     */
    public void setSubCatas(Vector subCatas) {
        this.subCatas = subCatas;
    }

	/**
	 * @return Returns the depth.
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param depth The depth to set.
	 */
	public void setLevel(int depth) {
		this.level = depth;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
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
	/**
	 * @return Returns the order.
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * @param order The order to set.
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	/**
	 * @return Returns the parentID.
	 */
	public int getParentId() {
		return parentId;
	}
	/**
	 * @param parentID The parentID to set.
	 */
	public void setParentId(int parentID) {
		this.parentId = parentID;
	}
	/**
	 * @return Returns the visible.
	 */
	public int getVisible() {
		return visible;
	}
	/**
	 * @param visible The visible to set.
	 */
	public void setVisible(int visible) {
		this.visible = visible;
	}
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    
    public void setChild_num(int child_num){
    	this.child_num=child_num;
    }
    
    public int getChild_num(){
    	return child_num;
    }
}
