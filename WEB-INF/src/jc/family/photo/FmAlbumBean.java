package jc.family.photo;

public class FmAlbumBean {
	int id;
	int fid; // 相册归属于哪个家族
	int permission; // 权限0全部人可见,1本家族人可见
	int createUid; // 创建人id
	int photoNum; // 相片数量
	long createTime; // 创建时间
	String albumName; // 相册名字

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

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public int getCreateUid() {
		return createUid;
	}

	public void setCreateUid(int createUid) {
		this.createUid = createUid;
	}

	public int getPhotoNum() {
		return photoNum;
	}

	public void setPhotoNum(int photoNum) {
		this.photoNum = photoNum;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
}
