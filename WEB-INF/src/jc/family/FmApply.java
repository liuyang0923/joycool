package jc.family;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 家族申请信息
 * 
 * @author qiuranke
 * 
 */
public class FmApply {

	private int id;
	private String fm_name;
	private int send_total;
	private int isok;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApply_name() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(id);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickName();
	}

	public String getApply_nameToWml() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(id);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickNameWml();
	}

	public String getFm_name() {
		return fm_name;
	}

	public String getFm_nameToWml() {
		return StringUtil.toWml(fm_name);
	}

	public void setFm_name(String fm_name) {
		this.fm_name = fm_name;
	}

	public int getSend_total() {
		return send_total;
	}

	public void setSend_total(int send_total) {
		this.send_total = send_total;
	}

	public int getIsok() {
		return isok;
	}

	public void setIsok(int isok) {
		this.isok = isok;
	}

}