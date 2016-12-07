package jc.family;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 家族申请人
 * @author qiuranke
 *
 */
public class FmApplyUser {

	private int id;
	private int fm_apply_id;
	private int userid;
	private long apply_time;
	private int is_apply;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFm_apply_id() {
		return fm_apply_id;
	}

	public int getTongId() {
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(userid);
		if (userStatus != null) {
			return userStatus.getTong();
		}
		return 0;
	}

	public void setFm_apply_id(int fm_apply_id) {
		this.fm_apply_id = fm_apply_id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getNickNameWml() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(userid);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickNameWml();
	}

	public long getApply_time() {
		return apply_time;
	}

	public void setApply_time(long apply_time) {
		this.apply_time = apply_time;
	}

	public int getIs_apply() {
		return is_apply;
	}

	public String getIs_apply_String() {
		String state = "";
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(userid);
		if (userStatus != null && userStatus.getTong() != 0) {
			return "已拒绝";
		}
		switch (is_apply) {
		case 1:
			state = "已同意";
			break;
		case 2:
			state = "已拒绝";
			break;
		case 0:
			state = "未操作";
			break;
		default:
			state = "未操作";
		}
		return state;
	}

	/**
	 * 是否到一分钟
	 * 
	 * @param nowtime
	 * @return
	 */
	public boolean is_time(long nowtime) {
		return nowtime > apply_time + 1000 * 60;
	}

	public void setIs_apply(int is_apply) {
		this.is_apply = is_apply;
	}

	public String getNickName() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(userid);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickName();
	}

}