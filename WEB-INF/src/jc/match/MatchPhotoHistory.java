package jc.match;

/**
 * 图片历史
 */
public class MatchPhotoHistory {
	int id;
	int userId;
	String photo;
	long createTime;
	int photoFrom;
	String photo2;
	public String getPhoto2() {
		return photo2;
	}
	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}
	public int getPhotoFrom() {
		return photoFrom;
	}
	public void setPhotoFrom(int photoFrom) {
		this.photoFrom = photoFrom;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
