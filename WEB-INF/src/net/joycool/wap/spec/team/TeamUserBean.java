package net.joycool.wap.spec.team;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 圈子成员
 *
 */
public class TeamUserBean {
	public static int FLAG_NO_NOTICE = (1 << 0);		// 不提醒
	int id;
	int userId;
	int teamId;
	long createTime;		// 加入时间
	
	int duty;			// 职位
	String name;		// 群名片
	int flag;			// 一些群内设置
	int readId;		// 阅读过的最后一条记录id

	public int getReadId() {
		return readId;
	}
	public void setReadId(int readId) {
		this.readId = readId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getDuty() {
		return duty;
	}
	public void setDuty(int duty) {
		this.duty = duty;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlagNoNotice() {
		return (flag & FLAG_NO_NOTICE) != 0;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public void toggleFlag(int flag) {
		this.flag ^= (1 << flag);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
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
}
