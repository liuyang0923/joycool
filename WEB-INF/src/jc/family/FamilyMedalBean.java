package jc.family;

/**
 * 家族勋章
 * @author zhouj
 *
 */
public class FamilyMedalBean {

	int id;
	String img;	// 图片文件名
	int fmId;
	int seq;	// 排序（暂时没用）
	String name;	// 勋章名称
	String info;	// 勋章详细介绍
	long createTime;	// 获得时间
	long endTime;		// 可能的期限（暂时没有用）

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getFmId() {
		return fmId;
	}

	public void setFmId(int fmId) {
		this.fmId = fmId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}


}