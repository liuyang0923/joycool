package jc.family.photo;

public class FmPhotoBean {
	int id;
	int fid; // 相片所归属家族
	int albumId; // 相片所归属相册
	int readTime; // 浏览次数
	int commentTime; // 评论次数
	int uploadUid; // 上传人id
	long uploadTime; // 上传时间
	String photoName; // 相片名字
	String url; // 相片地址,后台查看,删除文件时用的地址
//	String useUrl; // 相片使用地址,给用户显示用的地址

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public int getReadTime() {
		return readTime;
	}

	public void setReadTime(int readTime) {
		this.readTime = readTime;
	}

	public int getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(int commentTime) {
		this.commentTime = commentTime;
	}

	public int getUploadUid() {
		return uploadUid;
	}

	public void setUploadUid(int uploadUid) {
		this.uploadUid = uploadUid;
	}

	public long getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if(url.length() != 0 && !url.startsWith("/"))
			this.url = "/family/photo/" + url;
		else
			this.url = url;
	}

//	public String getUseUrl() {
//		return useUrl;
//	}
//
//	public void setUseUrl(String useUrl) {
//		this.useUrl = useUrl;
//	}

}
