package jc.download;

public class GameBean {
	int id;
	String name;
	String description;
	String fitMobile;
	int kb;
	String fileUrl;
	String remoteUrl;
	String picUrl;
	int hits;
	int catalogId;
	int createUserId;
	long createDatetime;
	int updateUserId;
	long updateDatetime;
	int providerId;
	int mark;		// 原GameBean里没有mark，但数据库里有
	String linkUrl; // 原GameBean里有，但数据库表里没有link_url
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFitMobile() {
		return fitMobile;
	}
	public void setFitMobile(String fitMobile) {
		this.fitMobile = fitMobile;
	}
	public int getKb() {
		return kb;
	}
	public void setKb(int kb) {
		this.kb = kb;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getRemoteUrl() {
		return remoteUrl;
	}
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public int getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}
	public long getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(long createDatetime) {
		this.createDatetime = createDatetime;
	}
	public int getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}
	public long getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(long updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
}
