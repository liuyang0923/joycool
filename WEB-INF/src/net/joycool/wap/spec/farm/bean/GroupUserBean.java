package net.joycool.wap.spec.farm.bean;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 
 *
 */
public class GroupUserBean {
	int userId;
	int readTotal;		// 阅读过的聊天记录数量
	int groupId;		// 在的组或者被邀请的组

	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getReadTotal() {
		return readTotal;
	}
	public void setReadTotal(int readTotal) {
		this.readTotal = readTotal;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
}
