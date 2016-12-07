package jc.download;

public class PringBean {

	int id;
    int catalogId;
	String name;
	String singer;
	int typeId;
	String file;
	String remoteUrl;
	int userId;
	long createDatetime;
	int downloadSum;
    String linkUrl;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getRemoteUrl() {
		return remoteUrl;
	}
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public long getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(long createDatetime) {
		this.createDatetime = createDatetime;
	}
	public int getDownloadSum() {
		return downloadSum;
	}
	public void setDownloadSum(int downloadSum) {
		this.downloadSum = downloadSum;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
}
