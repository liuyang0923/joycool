package net.joycool.wap.spec.team;

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
public class TeamChatBean {
	public static int FLAG_ACT = (1 << 0);		// 是动作
	
	int id;
	int fromId;		// 发言人
	int teamId;		// 发言组
	String fromName;
	String content;
	long time;		// 发言事件
	int flag;		// 各种标志位
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public void setFlagAct(boolean is) {
		if(is)
			flag |= FLAG_ACT;
		else
			flag &= ~FLAG_ACT;
	}
	public boolean isFlagAct() {
		return (flag & FLAG_ACT) != 0;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getFromId() {
		return fromId;
	}
	public void setFromId(int fromId) {
		this.fromId = fromId;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
