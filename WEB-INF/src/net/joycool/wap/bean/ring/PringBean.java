/*
 * Created on 2006-2-16
 *
 */
package net.joycool.wap.bean.ring;
/**
 * @author mcq
 *
 */
public class PringBean {

	int id;

    int catalog_id;
	
	String name;

	String singer;
	
	int type_id;
	
	String file;
	
	String remote_url;
	
	int user_id;
	
	String create_datetime;
	
	int download_sum;
	
    String linkUrl;
	
	/**
     * @return Returns the linkUrl.
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * @param linkUrl
     *            The linkUrl to set.
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
	
	 /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     * The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return Returns the catalog_id.
     */
    public int getCatalog_id() {
        return catalog_id;
    }

    /**
     * @param catalog_id
     * The catalog_id to set.
     */
    public void setCatalog_id(int catalog_id) {
        this.catalog_id = catalog_id;
    }
    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     * The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return Returns the Singer.
     */
    public String getSinger() {
        return singer;
    }

    /**
     * @param Singer
     * The Singer to set.
     */
    public void setSinger(String singer) {
        this.singer = singer;
    }
    
    /**
     * @return Returns the type_id.
     */
    public int getType_id() {
        return type_id;
    }

    /**
     * @param type_id
     * The type_id to set.
     */
    public void setType_id(int type_id) {
        this.type_id = type_id;
    }
    
    /**
     * @return Returns the file.
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file
     * The file to set.
     */
    public void setFile(String file) {
        this.file = file;
    }

	/**
     * @return Returns the remote_url.
     */
    public String getRemote_url() {
        return remote_url;
    }

    /**
     * @param remote_url
     * The remote_url to set.
     */
    public void setRemote_url(String remote_url) {
        this.remote_url = remote_url;
    }
    /**
     * @return Returns the user_id.
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * @param user_id
     * The user_id to set.
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    
	/**
     * @return Returns the create_datetime.
     */
    public String getCreate_datetime() {
        return create_datetime;
    }

    /**
     * @param create_datetime
     * The create_datetime to set.
     */
    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

	 /**
     * @return Returns the download_sum.
     */
    public int getDownload_sum() {
        return download_sum;
    }

    /**
     * @param download_sum
     * The download_sum to set.
     */
    public void setDownload_sum(int download_sum) {
        this.download_sum = download_sum;
    }
}
