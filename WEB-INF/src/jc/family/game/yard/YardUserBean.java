package jc.family.game.yard;

/**
 * 用户信息
 * 数据库中的fm_yard_user_log是本周的，fm_yard_user_log2是上周的。
 * @author qiuranke
 * 
 */
public class YardUserBean {
	
	int id;
	int userid;
	int fmid;
	int seedCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getFmid() {
		return fmid;
	}

	public void setFmid(int fmid) {
		this.fmid = fmid;
	}

	public int getSeedCount() {
		return seedCount;
	}

	public void setSeedCount(int seedCount) {
		this.seedCount = seedCount;
	}

}
