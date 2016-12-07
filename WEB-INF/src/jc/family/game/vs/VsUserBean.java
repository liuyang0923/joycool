package jc.family.game.vs;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.UserInfoUtil;

public class VsUserBean {

	int userId;
	int fmId;
	int side;// 游戏属于哪方 0 FmidA 1 FmIdB

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFmId() {
		return fmId;
	}

	public void setFmId(int fmId) {
		this.fmId = fmId;
	}

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public String getNickName() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(userId);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickName();
	}

	public String getNickNameWml() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(userId);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickNameWml();
	}

	public void init(VsGameBean game) {
		
	}
}
