package jc.imglib;

public class ImgPoolBean {

	int id;
	long md5Short;
	String md5;
	int userId;
	long createTime;
	int fileSize;
	String fileName;
	int catalog;
	int flag;	// 0 表示没有审核通过，1通过 

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getMd5Short() {
		return md5Short;
	}

	public void setMd5Short(long md5Short) {
		this.md5Short = md5Short;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}
	
	public String getUseFileName() {
		if(flag == 0)
			return "o.gif";
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}
}
