package jc.family.game;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.UserInfoUtil;

public class MemberBean {
	int id;
	int mid; // 赛事表主键
	int fid;// 家族id
	int uid = 0;// 用户id
	int totalHit = 0;// 操作总数/击中次数
	int contribution = 0;// 贡献点数
	int PaySweep = 0;// 扫雪花费(雪仗用)
	int PayMake = 0;// 造雪球花费(雪仗用)
	int seat = -1; // 座位(龙舟用,默认未坐下)

	int ask_wrong;
	int ask_right;
	int ask_score;

	public int getAsk_wrong() {
		return ask_wrong;
	}

	public void setAsk_wrong(int askWrong) {
		ask_wrong = askWrong;
	}

	public int getAsk_right() {
		return ask_right;
	}

	public void setAsk_right(int askRight) {
		ask_right = askRight;
	}

	public int getAsk_score() {
		return ask_score;
	}

	public void setAsk_score(int askScore) {
		ask_score = askScore;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getTotalHit() {
		return totalHit;
	}

	public void setTotalHit(int totalHit) {
		this.totalHit = totalHit;
	}

	public int getContribution() {
		return contribution;
	}

	public void setContribution(int contribution) {
		this.contribution = contribution;
	}

	public int getPaySweep() {
		return PaySweep;
	}

	public void setPaySweep(int paySweep) {
		PaySweep = paySweep;
	}

	public int getPayMake() {
		return PayMake;
	}

	public void setPayMake(int payMake) {
		PayMake = payMake;
	}

	// 龙舟
	int hit = 0;// 队员操作,普通人(1表示前行,2表示方向),掌舵人(1表示向左,2表示向右)

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	// 雪仗
	int isChange = 0;// 是否兑换过雪币

	public int getIsChange() {
		return isChange;
	}

	public void setIsChange(int isChange) {
		this.isChange = isChange;
	}

	public String getNickName() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(uid);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickName();
	}

	public String getNickNameWml() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(uid);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickNameWml();
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}
}
